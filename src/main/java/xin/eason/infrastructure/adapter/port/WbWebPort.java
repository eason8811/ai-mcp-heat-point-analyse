package xin.eason.infrastructure.adapter.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import xin.eason.domain.adapter.port.IWebPort;
import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
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
}
