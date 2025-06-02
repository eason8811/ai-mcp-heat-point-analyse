package xin.eason.domain.service;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import xin.eason.api.IHeatPointAnalyseService;
import xin.eason.domain.adapter.port.IWebPort;
import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
import xin.eason.domain.model.aggregate.HeatPointAggregateRequest;
import xin.eason.domain.model.entity.*;
import xin.eason.domain.model.enums.HeatPointCategory;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 热点分析接口 {@link xin.eason.api.IHeatPointAnalyseService} 的 微博热点 实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WbHeatPointAnalyseService implements IHeatPointAnalyseService {

    /**
     * 适配器网络接口
     */
    private final IWebPort webPort;

    /**
     * 查询所有热搜数据
     *
     * @return 热搜数据响应对象
     */
    @Override
    @Tool(name = "queryAllHeatPoints", description = "查询所有板块的热点数据, 并返回热点分析聚合, 其中包含了热搜, 文娱, 要闻三个板块的热点列表, 以及一个热搜板块的置顶热点列表, 可以用于初步获取当前的热点信息 (用户没有特别要求的情况下不适用该方法)")
    public AnalyseHeatPointAggregate queryAllHeatPoints() {
        try {
            log.info("正在获取所有热点数据...");
            AnalyseHeatPointAggregate aggregate = webPort.queryHeatPoints();
            log.info("获取完成, 热点分析聚合信息: {}", JSON.toJSONString(aggregate));
            return aggregate;
        } catch (IOException e) {
            log.error("获取所有热点数据出现异常! 异常信息: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据热搜种类, 查询一个热搜种类的所有数据
     *
     * @param category 热搜种类  ( 热搜, 文娱, 要闻 )
     * @return 热搜数据响应对象
     */
    @Override
    @Tool(name = "queryHeatPointsByCategory", description = "根据提供的热点板块类别查询对应板块的热点数据, 返回热点分析聚合, 其中含有热搜板块的置顶热点列表和普通热点列表, 或文娱热点列表, 或要闻热点列表其中之一, 可以用于初步获取当前的热点信息")
    public AnalyseHeatPointAggregate queryHeatPointsByCategory(HeatPointCategory category) {
        try {
            log.info("正在根据类别获取所有热点数据... 当前类别为: [{}]", category.getDesc());
            AnalyseHeatPointAggregate aggregate = webPort.queryHeatPoints(category);
            log.info("获取完成, 热点分析聚合信息: {}", JSON.toJSONString(aggregate));
            return aggregate;
        } catch (IOException e) {
            log.error("获取所有热点数据出现异常! 异常信息: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据热搜种类, 查询一个热搜种类的前 N 条数据
     *
     * @param category 热搜种类  ( 热搜, 文娱, 要闻 )
     * @param limit    前 N 条数据
     * @return 热搜数据响应对象
     */
    @Override
    @Tool(name = "queryHeatPointsByCategoryLimit", description = "根据提供的热点板块类别查询对应板块的热点数据, 返回热点分析聚合, 其中含有热搜板块的置顶热点列表和普通热点列表, 或文娱热点列表, 或要闻热点列表其中之一, 并且只返回热点列表的前 limit 条数据, 可以用于初步获取当前的热点信息")
    public AnalyseHeatPointAggregate queryHeatPointByCategory(HeatPointCategory category, Integer limit) {
        AnalyseHeatPointAggregate aggregate = queryHeatPointsByCategory(category);
        log.info("正在进行列表切片, 获取 [{}] 板块前 {} 条数据...", category.getDesc(), limit);
        // 查询到数据之后切取子列表
        List<WbHotSearchEntity> hotSearchEntityList = aggregate.getHotSearchEntityList();
        if (category.getCode() == HeatPointCategory.HOT_SEARCH.getCode() && hotSearchEntityList != null) {
            aggregate.setHotSearchEntityList(hotSearchEntityList.subList(0, limit));
            log.info("获取完成, 热点分析聚合信息: {}", JSON.toJSONString(aggregate));
            return aggregate;
        }
        List<WbEntertainmentEntity> entertainmentEntityList = aggregate.getEntertainmentEntityList();
        if (category.getCode() == HeatPointCategory.ENTERTAINMENT.getCode() && entertainmentEntityList != null) {
            aggregate.setEntertainmentEntityList(entertainmentEntityList.subList(0, limit));
            log.info("获取完成, 热点分析聚合信息: {}", JSON.toJSONString(aggregate));
            return aggregate;
        }
        List<WbNewsEntity> newsEntityList = aggregate.getNewsEntityList();
        if (category.getCode() == HeatPointCategory.NEWS.getCode() && newsEntityList != null) {
            aggregate.setNewsEntityList(newsEntityList.subList(0, limit));
            log.info("获取完成, 热点分析聚合信息: {}", JSON.toJSONString(aggregate));
            return aggregate;
        }
        log.warn("category 类别不合法");
        return null;
    }

    /**
     * 根据热点的 URL 查询热点帖子的详细信息 ( 默认前 10 条 )
     *
     * @param url 热点 URL
     * @return 热点详细信息对象列表
     */
    @Override
    @Tool(name = "queryHeatPointDetailByUrl", description = "根据提供的 URL 查询一个热点所属的帖子的详细信息, 返回热点详细信息列表, 列表中的一个元素代表一个帖子, 可以用于进一步获取对于这个热点社会各界发表的帖子和观点的详细信息")
    public List<HeatPointDetailEntity> queryHeatPointDetailByUrl(String url) {
        try {
            log.info("正在获取 URL: {} 的热点细节信息...", URLDecoder.decode(url, StandardCharsets.UTF_8));
            List<HeatPointDetailEntity> heatPointDetailEntityList = webPort.queryHeatPointsDetail(url);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        } catch (IOException e) {
            log.error("获取热点细节信息异常! 异常信息: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据热点的 URL 查询热点帖子的前 N 条详细信息
     *
     * @param url   热点 URL
     * @param limit 返回前 N 条详细内容
     * @return 热点详细信息对象列表
     */
    @Override
    @Tool(name = "queryHeatPointDetailByUrlLimit", description = "根据提供的 URL 查询一个热点所属的帖子的详细信息, 返回热点详细信息列表的前 limit 条, 列表中的一个元素代表一个帖子, 可以用于进一步获取对于这个热点社会各界发表的帖子和观点的详细信息")
    public List<HeatPointDetailEntity> queryHeatPointDetailByUrl(String url, int limit) {
        try {
            log.info("正在获取 URL: {} 的前 {} 热点细节信息...", URLDecoder.decode(url, StandardCharsets.UTF_8), limit);
            List<HeatPointDetailEntity> heatPointDetailEntityList = webPort.queryHeatPointsDetail(url, limit);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        } catch (IOException e) {
            log.error("获取热点细节信息异常! 异常信息: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据查询热点数据聚合对象, 查询其中的所有类别的热点帖子详细信息 ( 默认前 10 条 )
     *
     * @param requestAggregate 热点数据聚合请求对象
     * @return 热点详细信息对象列表
     */
    @Override
    @Tool(name = "queryHeatPointDetail", description = "根据提供的热点分析聚合对象, 提取其中各个不同板块的所有热点信息, 并自动查询这些热点所属的前 10 条帖子详细信息, 可以用于进一步获取对于这个热点社会各界发表的帖子和观点的详细信息")
    public List<HeatPointDetailEntity> queryHeatPointDetail(HeatPointAggregateRequest requestAggregate) {
        List<String> hotSearchTopicList = requestAggregate.getHotSearchTopicList();
        List<String> entertainmentTopicList = requestAggregate.getEntertainmentTopicList();
        List<String> newsTopicList = requestAggregate.getNewsTopicList();

        // 热点帖子详细信息列表
        List<HeatPointDetailEntity> heatPointDetailEntityList = new ArrayList<>();
        // 查询所有类别板块的热点细节信息然后装入 heatPointDetailEntityList 列表中
        createUrlAndQuery(hotSearchTopicList, heatPointDetailEntityList, HeatPointCategory.HOT_SEARCH);
        createUrlAndQuery(entertainmentTopicList, heatPointDetailEntityList, HeatPointCategory.ENTERTAINMENT);
        createUrlAndQuery(newsTopicList, heatPointDetailEntityList, HeatPointCategory.NEWS);
        log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
        return heatPointDetailEntityList;
    }


    /**
     * 根据查询热点数据聚合对象, 查询其中的置顶类别的热点帖子详细信息 ( 默认前 10 条 )
     *
     * @param requestAggregate 热点数据聚合请求对象
     * @param category  热点板块类别
     * @return 热点详细信息对象列表
     */
    @Override
    @Tool(name = "queryHeatPointDetailByCategory", description = "根据提供的热点分析聚合对象和热点板块类别, 提取其中指定板块的所有热点信息, 并自动查询这些热点所属的前 10 条帖子详细信息, 可以用于进一步获取对于这个热点社会各界发表的帖子和观点的详细信息")
    public List<HeatPointDetailEntity> queryHeatPointDetailByCategory(HeatPointAggregateRequest requestAggregate, HeatPointCategory category) {
        if (requestAggregate == null)
            return null;
        List<String> hotSearchTopicList = requestAggregate.getHotSearchTopicList();
        List<String> entertainmentTopicList = requestAggregate.getEntertainmentTopicList();
        List<String> newsTopicList = requestAggregate.getNewsTopicList();

        // 热点帖子详细信息列表
        List<HeatPointDetailEntity> heatPointDetailEntityList = new ArrayList<>();
        if (category.getCode() == HeatPointCategory.HOT_SEARCH.getCode() && hotSearchTopicList != null) {
            // 查询热搜板块的热点细节信息然后装入 heatPointDetailEntityList 列表中
            createUrlAndQuery(hotSearchTopicList, heatPointDetailEntityList, category);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        }
        if (category.getCode() == HeatPointCategory.ENTERTAINMENT.getCode() && entertainmentTopicList != null) {
            // 查询文娱板块的热点细节信息然后装入 heatPointDetailEntityList 列表中
            createUrlAndQuery(entertainmentTopicList, heatPointDetailEntityList, category);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        }
        if (category.getCode() == HeatPointCategory.NEWS.getCode() && newsTopicList != null) {
            // 查询要闻板块的热点细节信息然后装入 heatPointDetailEntityList 列表中
            createUrlAndQuery(newsTopicList, heatPointDetailEntityList, category);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        }
        // 无法匹配类别, 则返回 null
        return null;
    }

    /**
     * 根据查询热点数据聚合对象, 查询其中的置顶类别的前 N 条热点帖子详细信息
     *
     * @param requestAggregate 热点数据聚合请求对象
     * @param category  热点板块类别
     * @param limit     返回前 N 条详细内容
     * @return 热点详细信息对象列表
     */
    @Override
    @Tool(name = "queryHeatPointDetailByCategoryLimit", description = "根据提供的热点分析聚合对象, 提取其中各个不同板块的所有热点信息, 并自动查询这些热点所属的前 limit 条帖子详细信息, 可以用于进一步获取对于这个热点社会各界发表的帖子和观点的详细信息")
    public List<HeatPointDetailEntity> queryHeatPointDetailByCategory(HeatPointAggregateRequest requestAggregate, HeatPointCategory category, int limit) {
        List<String> hotSearchTopicList = requestAggregate.getHotSearchTopicList();
        List<String> entertainmentTopicList = requestAggregate.getEntertainmentTopicList();
        List<String> newsTopicList = requestAggregate.getNewsTopicList();

        // 热点帖子详细信息列表
        List<HeatPointDetailEntity> heatPointDetailEntityList = new ArrayList<>();
        if (category.getCode() == HeatPointCategory.HOT_SEARCH.getCode() && hotSearchTopicList != null) {
            // 查询热搜板块的热点细节信息然后装入 heatPointDetailEntityList 列表中
            createUrlAndQuery(hotSearchTopicList, heatPointDetailEntityList, category, limit);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        }
        if (category.getCode() == HeatPointCategory.ENTERTAINMENT.getCode() && entertainmentTopicList != null) {
            // 查询文娱板块的热点细节信息然后装入 heatPointDetailEntityList 列表中
            createUrlAndQuery(entertainmentTopicList, heatPointDetailEntityList, category, limit);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        }
        if (category.getCode() == HeatPointCategory.NEWS.getCode() && newsTopicList != null) {
            // 查询要闻板块的热点细节信息然后装入 heatPointDetailEntityList 列表中
            createUrlAndQuery(newsTopicList, heatPointDetailEntityList, category, limit);
            log.info("获取完成, 热点详细信息列表: {}", JSON.toJSONString(heatPointDetailEntityList));
            return heatPointDetailEntityList;
        }
        // 无法匹配类别, 则返回 null
        return null;
    }

    /**
     * 构造 URL 然后查询热点细节信息列表, 设置好热点信息列表中所有元素的 category 后装入传入热点细节的列表
     *
     * @param heatPointEntityList       热点实体列表
     * @param heatPointDetailEntityList 热点细节实体列表
     * @param <T>                       传入的热点类型的泛型 ( {@link AbstractWbHeatPointEntity} 的子类 )
     */
    private void createUrlAndQuery(List<String> heatPointTopicList, List<HeatPointDetailEntity> heatPointDetailEntityList, HeatPointCategory category) {
        // 若为 null 则直接返回
        if (heatPointTopicList == null || heatPointDetailEntityList == null)
            return;
        // 组装 URL
        for (String heatPointTopic : heatPointTopicList) {
            String baseUrl = "https://s.weibo.com/weibo";
            String query = URLEncoder.encode(heatPointTopic, StandardCharsets.UTF_8);
            String categoryId = null;
            switch (category) {
                case HOT_SEARCH -> categoryId = "31";
                case ENTERTAINMENT -> categoryId = "152";
                case NEWS -> categoryId = "153";
            }
            String page = "1";
            String url = baseUrl + "?" + "q=" + query + "&t=" + categoryId + "&page=" + page;
            // 获取热点细节信息列表
            List<HeatPointDetailEntity> resultList = queryHeatPointDetailByUrl(url);
            // 设置好类别之后放入列表之中
            resultList.forEach(heatPointDetailEntity -> heatPointDetailEntity.setCategory(category));
            heatPointDetailEntityList.addAll(resultList);
        }
    }

    /**
     * 构造 URL 然后查询热点细节信息列表, 设置好热点信息列表中所有元素的 category 后装入, 将前 N 条数据传入热点细节的列表
     *
     * @param heatPointTopicList 热点实体列表
     * @param detailEntityList    热点细节实体列表
     * @param category            热点板块类别
     * @param limit               限定前 N 条数据
     * @param <T>                 传入的热点类型的泛型 ( {@link AbstractWbHeatPointEntity} 的子类 )
     */
    private void createUrlAndQuery(List<String> heatPointTopicList, List<HeatPointDetailEntity> detailEntityList, HeatPointCategory category, int limit) {
        // 若为 null 则直接返回
        if (heatPointTopicList == null || detailEntityList == null)
            return;

        for (String heatPointTopic : heatPointTopicList) {
            int page = 1;
            int total = 0;
            while (true) {
                String baseUrl = "https://s.weibo.com/weibo";
                String query = URLEncoder.encode(heatPointTopic, StandardCharsets.UTF_8);
                String categoryId = null;
                switch (category) {
                    case HOT_SEARCH -> categoryId = "31";
                    case ENTERTAINMENT -> categoryId = "152";
                    case NEWS -> categoryId = "153";
                }
                String url = baseUrl + "?" + "q=" + query + "&t=" + categoryId + "&page=" + page;
                // 获取热点细节信息列表
                List<HeatPointDetailEntity> resultList = queryHeatPointDetailByUrl(url);
                // 设置好类别
                resultList.forEach(heatPointDetailEntity -> heatPointDetailEntity.setCategory(category));
                if (total + resultList.size() >= limit) {
                    detailEntityList.addAll(resultList.subList(0, limit - total));
                    break;
                }
                detailEntityList.addAll(resultList);
                total += resultList.size();
                page++;
            }
        }
    }
}
