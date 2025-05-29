package xin.eason.infrastructure.adapter.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import xin.eason.domain.adapter.port.IWebPort;
import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
import xin.eason.domain.model.entity.HeatPointDetailEntity;
import xin.eason.domain.model.entity.WbEntertainmentEntity;
import xin.eason.domain.model.entity.WbHotSearchEntity;
import xin.eason.domain.model.entity.WbNewsEntity;
import xin.eason.domain.model.enums.HeatPointCategory;
import xin.eason.infrastructure.dto.WbEntertainmentResponseDTO;
import xin.eason.infrastructure.dto.WbHotSearchResponseDTO;
import xin.eason.infrastructure.dto.WbNewsResponseDTO;
import xin.eason.infrastructure.dto.WbStandardResult;
import xin.eason.infrastructure.gateway.IHeatPointWebHandler;
import xin.eason.types.config.HeatPointConfigProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 适配器网络接口的 微博 实现, 用于向 微博 发送请求, 获取热点信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WbWebPort implements IWebPort {

    /**
     * 热点查询网络处理器
     */
    private final IHeatPointWebHandler webHandler;
    /**
     * 热点分析配置属性
     */
    private final HeatPointConfigProperties properties;

    /**
     * 查询所有热点信息
     *
     * @return 热点分析聚合
     */
    @Override
    public AnalyseHeatPointAggregate queryHeatPoints() throws IOException {
        // 新建一个空的聚合对象
        AnalyseHeatPointAggregate aggregate = new AnalyseHeatPointAggregate();
        // 依次获取 热搜, 文娱, 要闻 的数据
        queryHotSearchData(aggregate);
        queryEntertainmentData(aggregate);
        queryNewsData(aggregate);
        return aggregate;
    }

    /**
     * 根据 category 板块类型查询所有热点信息
     *
     * @param category 热点板块类型
     * @return 热点分析聚合
     */
    @Override
    public AnalyseHeatPointAggregate queryHeatPoints(HeatPointCategory category) throws IOException {
        // 新建一个空的聚合对象
        AnalyseHeatPointAggregate aggregate = new AnalyseHeatPointAggregate();
        // 匹配查询类别, 将对应的数据放入聚合对象后返回
        if (category.getCode() == HeatPointCategory.HOT_SEARCH.getCode())
            return queryHotSearchData(aggregate);
        if (category.getCode() == HeatPointCategory.ENTERTAINMENT.getCode())
            return queryEntertainmentData(aggregate);
        if (category.getCode() == HeatPointCategory.NEWS.getCode())
            return queryNewsData(aggregate);
        return null;
    }

    /**
     * 根据热点的 URL 查询热点的详细信息
     *
     * @param url 热点 URL
     * @return 热点详细信息对象列表
     */
    @Override
    public List<HeatPointDetailEntity> queryHeatPointsDetail(String url) throws IOException {
        return queryHeatPointsDetail(url, 10);
    }

    /**
     * 根据热点的 URL 查询热点帖子的前 N 条详细信息
     *
     * @param url   热点 URL
     * @param limit 返回前 N 条详细内容
     * @return 热点详细信息对象列表
     */
    @Override
    public List<HeatPointDetailEntity> queryHeatPointsDetail(String url, int limit) throws IOException {
        // 获取 Web 请求响应
        Response<String> response = webHandler.wbQueryHotSearchDetail(url, properties.getCookieString()).execute();
        if (!response.isSuccessful()) {
            log.error("请求出错! 响应代码: {}, 响应体: {}", response.code(), response.errorBody().string());
            return null;
        }
        // 以字符串形式读取 HTML 代码
        String responseBody = response.body();
        if (responseBody == null) {
            log.error("响应体为空! 响应代码: {},", response.code());
            return null;
        }
        // 将字符串形式将 HTML 字符串解析为 Document 文档
        Document document = Jsoup.parse(responseBody);
        // 提取所有 class = txt 且 node-type 属性为 feed_list_content 的节点, 形成节点列表
        Elements elements = document.body().select("div.content[node-type='like'] > p.txt[node-type='feed_list_content']");

        List<Element> elementList = new ArrayList<>();
        // 遍历所有节点, 找出下一个兄弟节点存在属性 node-type 且属性值为 feed_list_content_full 的节点
        // 这样的节点就是这条帖子的全文节点, 将其添加到 elementList 列表中
        for (Element element : elements) {
            Element next = element.nextElementSibling();
            if (next != null && next.hasAttr("node-type") && "feed_list_content_full".equals(next.attr("node-type"))) {
                elementList.add(next);
                continue;
            }
            elementList.add(element);
        }

        // 获取每个帖子的 转发, 评论, 点赞 节点
        Elements elementsSocialInfo = document.body().select("div.card-act > ul > li > a");
        // 装入列表之中
        List<Map<String, Element>> socialInfoList = new ArrayList<>();
        for (int i = 0; i < elementsSocialInfo.size(); i += 3) {
            HashMap<String, Element> hashMap = new HashMap<>();
            hashMap.put("send", elementsSocialInfo.get(i));
            hashMap.put("comment", elementsSocialInfo.get(i + 1));
            hashMap.put("like", elementsSocialInfo.get(i + 2));
            socialInfoList.add(hashMap);
        }

        if (elementList.size() != socialInfoList.size()) {
            log.error("解析 HTML 过程错误! 帖子数量与转评赞数量不符! 帖子数量: {}, 转评赞数量: {}", elementList.size(), socialInfoList.size());
            return null;
        }

        // 构建热点细节实体对象列表
        List<HeatPointDetailEntity> heatPointDetailEntityList = new ArrayList<>();
        for (int i = 0; i < elementList.size() && i < limit; i++) {
            Element contentElement = elementList.get(i);
            Map<String, Element> socialInfoMap = socialInfoList.get(i);
            // 判断有没有 用户名 这个属性, 没有的话抛出警告
            if (!contentElement.hasAttr("nick-name"))
                log.warn("内容索引: {}, 用户名缺失! 热点 URL: {}", i, url);

            // 解析 转评赞 数量
            int sendNum = convertDetailDataToInteger(url, contentElement, socialInfoMap.get("send").text(), "send");
            int commentNum = convertDetailDataToInteger(url, contentElement, socialInfoMap.get("comment").text(), "comment");
            int likeNum = convertDetailDataToInteger(url, contentElement, socialInfoMap.get("like").text(), "like");

            HeatPointDetailEntity entity = HeatPointDetailEntity.builder()
                    .userName(contentElement.attr("nick-name"))
                    .content(contentElement.text())
                    .send(sendNum)
                    .comment(commentNum)
                    .like(likeNum)
                    .build();
            heatPointDetailEntityList.add(entity);
        }
        return heatPointDetailEntityList;
    }

    /**
     * 查询热搜数据
     *
     * @param aggregate 热点分析聚合对象
     * @return 热点分析聚合对象
     * @throws IOException IO 异常
     */
    private AnalyseHeatPointAggregate queryHotSearchData(AnalyseHeatPointAggregate aggregate) throws IOException {
        Response<WbStandardResult<WbHotSearchResponseDTO>> hotSearchResponse = webHandler.wbQueryHotSearch(properties.getCookieString()).execute();
        // 检查响应是否出现错误
        WbStandardResult<WbHotSearchResponseDTO> hotSearchBody = filterResponse(hotSearchResponse);
        // 如果出现错误则返回 null
        if (hotSearchBody == null)
            return null;
        // 将数据装载入聚合中
        List<WbHotSearchEntity> hotSearchEntityTopList = hotSearchBody.getData().getHotgovList().stream().map(WbWebPort::convertDataRowToEntity).toList();
        List<WbHotSearchEntity> hotSearchEntityList = hotSearchBody.getData().getHotSearchDataRowList().stream().map(WbWebPort::convertDataRowToEntity).toList();
        aggregate.setHotSearchEntityTopList(hotSearchEntityTopList);
        aggregate.setHotSearchEntityList(hotSearchEntityList);
        return aggregate;
    }

    /**
     * 查询文娱热搜数据
     *
     * @param aggregate 热点分析聚合对象
     * @return 热点分析聚合对象
     * @throws IOException IO 异常
     */
    private AnalyseHeatPointAggregate queryEntertainmentData(AnalyseHeatPointAggregate aggregate) throws IOException {
        Response<WbStandardResult<WbEntertainmentResponseDTO>> entertainmentResponse = webHandler.wbQueryEntertainment(properties.getCookieString()).execute();
        // 检查响应是否出现错误
        WbStandardResult<WbEntertainmentResponseDTO> entertainmentBody = filterResponse(entertainmentResponse);
        // 如果出现错误则返回 null
        if (entertainmentBody == null)
            return null;
        // 将数据装载入聚合中
        List<WbEntertainmentEntity> entertainmentEntityList = entertainmentBody.getData().getEntertainmentDataRowList().stream().map(WbWebPort::convertDataRowToEntity).toList();
        aggregate.setEntertainmentEntityList(entertainmentEntityList);
        return aggregate;
    }

    /**
     * 查询要闻热搜数据
     *
     * @param aggregate 热点分析聚合对象
     * @return 热点分析聚合对象
     * @throws IOException IO 异常
     */
    private AnalyseHeatPointAggregate queryNewsData(AnalyseHeatPointAggregate aggregate) throws IOException {
        Response<WbStandardResult<WbNewsResponseDTO>> newsResponse = webHandler.wbQueryNews(properties.getCookieString()).execute();
        // 检查响应是否出现错误
        WbStandardResult<WbNewsResponseDTO> newsBody = filterResponse(newsResponse);
        // 如果出现错误则返回 null
        if (newsBody == null)
            return null;
        // 将数据装载入聚合中
        List<WbNewsEntity> newsEntityList = newsBody.getData().getNewsDataRowList().stream().map(WbWebPort::convertDataRowToEntity).toList();
        aggregate.setNewsEntityList(newsEntityList);
        return aggregate;
    }

    /**
     * 过滤响应结果, 如果响应有错误则输出警告或错误信息
     *
     * @param rawResponse 调用 Retrofit 的响应对象
     * @param <T>         响应泛型
     * @return 若响应成功, 则返回响应体, 否则返回 NULL
     * @throws IOException IO 异常
     */
    private static <T> WbStandardResult<T> filterResponse(Response<WbStandardResult<T>> rawResponse) throws IOException {
        // 判断响应是否失败, 如果失败则直接返回 null
        if (!rawResponse.isSuccessful()) {
            log.error("请求出错! 响应代码: {}, 响应体: {}", rawResponse.code(), rawResponse.errorBody().string());
            return null;
        }
        WbStandardResult<T> responseBody = rawResponse.body();
        if (responseBody == null) {
            log.warn("响应体为空! 响应代码: {}", rawResponse.code());
            return null;
        }
        if (responseBody.getOk() != 1) {
            log.warn("响应错误! ok 代码: {}, 响应数据体: {}", responseBody.getOk(), responseBody.getData());
            return null;
        }
        return responseBody;
    }

    /**
     * 将数据行 DTO 对象转换为实体对象
     *
     * @param row 数据行对象
     * @return 对应板块的实体对象
     */
    private static WbHotSearchEntity convertDataRowToEntity(WbHotSearchResponseDTO.HotSearchDataRow row) {
        // 将热搜响应 DTO 转换为热搜数据实体
        WbHotSearchEntity entity = WbHotSearchEntity.builder()
                .emotion(row.getEmotion())
                .labelName(row.getLabelName())
                .topic(null)
                .searchNum(row.getSearchNum())
                .rank(row.getRealPosition())
                .build();
        entity.setTopic(row.getWord());
        return entity;
    }

    /**
     * 将数据行 DTO 对象转换为实体对象
     *
     * @param row 数据行对象
     * @return 对应板块的实体对象
     */
    private static WbEntertainmentEntity convertDataRowToEntity(WbEntertainmentResponseDTO.EntertainmentDataRow row) {
        // 将文娱热搜响应 DTO 转换为文娱数据实体
        WbEntertainmentEntity entity = WbEntertainmentEntity.builder()
                .category(row.getCategory())
                .channelTypeList(List.of(row.getChannelType().split("\\|")))
                .searchNum(row.getSearchNum())
                .cooperate(row.getCooperate())
                .emotion(row.getEmotion())
                .labelName(row.getLabelName())
                .rank(row.getRealPosition())
                .onHotSearchBoardTime(null)
                .onBoardTime(null)
                .starNameList(null)
                .subjectDetailMap(null)
                .topic(null)
                .build();
        entity.setOnHotSearchBoardTime(row.getOnMainBoardTime());
        entity.setOnBoardTime(row.getOnBoardTime());
        entity.setStarNameList(row.getStarNameList());
        entity.setTopic(row.getWord());
        HashMap<String, WbEntertainmentEntity.SubjectDetail> hashMap = new HashMap<>();
        for (Map.Entry<String, WbEntertainmentResponseDTO.SubjectDetail> entry : row.getSubjectDetailMap().entrySet()) {
            WbEntertainmentEntity.SubjectDetail subjectDetail = WbEntertainmentEntity.SubjectDetail.builder()
                    .type(entry.getValue().getType())
                    .source(entry.getValue().getSource())
                    .nameList(entry.getValue().getNameList())
                    .build();
            hashMap.put(entry.getKey(), subjectDetail);
        }
        entity.setSubjectDetailMap(hashMap);
        return entity;
    }

    /**
     * 将数据行 DTO 对象转换为实体对象
     *
     * @param row 数据行对象
     * @return 对应板块的实体对象
     */
    private static WbNewsEntity convertDataRowToEntity(WbNewsResponseDTO.NewsDataRow row) {
        // 将要闻热搜 DTO 转换为要闻数据实体
        return WbNewsEntity.builder()
                .topic(row.getWord())
                .build();
    }

    /**
     * 将 转评赞 的数据转换为整数, 无法转换的数据置零
     *
     * @param url            当前访问的热点 URL
     * @param contentElement 当前热点内容的 HTML 元素
     * @param contentText    转评赞内容文本
     * @param key            转评赞 -> send, comment, like
     * @return 转换后的 转评赞 数量的整数形式
     */
    private static int convertDetailDataToInteger(String url, Element contentElement, String contentText, String key) {
        try {
            return Integer.parseInt(contentText);
        } catch (NumberFormatException e) {
            log.debug("在 URL: {} 的热点中, 内容为: {} 的热点详细信息的 {} 数据无法转换为整数, 原数据为: {}", url, contentElement.text(), key, contentText);
            return 0;
        }
    }
}
