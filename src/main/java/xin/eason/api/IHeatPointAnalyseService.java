package xin.eason.api;

import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
import xin.eason.domain.model.aggregate.HeatPointAggregateRequest;
import xin.eason.domain.model.entity.HeatPointDetailEntity;
import xin.eason.domain.model.enums.HeatPointCategory;

import java.util.List;

/**
 * 热点分析 API 接口
 */
public interface IHeatPointAnalyseService {

    /**
     * 查询所有热搜数据
     *
     * @return 热搜数据响应对象
     */
    AnalyseHeatPointAggregate queryAllHeatPoints();

    /**
     * 根据热搜种类, 查询一个热搜种类的所有数据
     *
     * @param category 热搜种类  ( 热搜, 文娱, 要闻 )
     * @return 热搜数据响应对象
     */
    AnalyseHeatPointAggregate queryHeatPointsByCategory(HeatPointCategory category);

    /**
     * 根据热搜种类, 查询一个热搜种类的前 N 条数据
     *
     * @param category 热搜种类  ( 热搜, 文娱, 要闻 )
     * @param limit    前 N 条数据
     * @return 热搜数据响应对象
     */
    AnalyseHeatPointAggregate queryHeatPointByCategory(HeatPointCategory category, Integer limit);

    /**
     * 根据热点的 URL 查询热点帖子的详细信息 ( 默认前 10 条 )
     *
     * @param url 热点 URL
     * @return 热点详细信息对象列表
     */
    List<HeatPointDetailEntity> queryHeatPointDetailByUrl(String url);

    /**
     * 根据热点的 URL 查询热点帖子的前 N 条详细信息
     *
     * @param url 热点 URL
     * @param limit 返回前 N 条详细内容
     * @return 热点详细信息对象列表
     */
    List<HeatPointDetailEntity> queryHeatPointDetailByUrl(String url, int limit);

    /**
     * 根据查询热点数据聚合对象, 查询其中的所有类别的热点帖子详细信息 ( 默认前 10 条 )
     *
     * @param aggregate 热点数据聚合对象
     * @return 热点详细信息对象列表
     */
    List<HeatPointDetailEntity> queryHeatPointDetail(HeatPointAggregateRequest requestAggregate);

    /**
     * 根据查询热点数据聚合对象, 查询其中的置顶类别的热点帖子详细信息 ( 默认前 10 条 )
     *
     * @param aggregate 热点数据聚合对象
     * @param category  热点板块类别
     * @return 热点详细信息对象列表
     */
    List<HeatPointDetailEntity> queryHeatPointDetailByCategory(HeatPointAggregateRequest requestAggregate, HeatPointCategory category);

    /**
     * 根据查询热点数据聚合对象, 查询其中的置顶类别的前 N 条热点帖子详细信息
     *
     * @param aggregate 热点数据聚合对象
     * @param category  热点板块类别
     * @param limit     返回前 N 条详细内容
     * @return 热点详细信息对象列表
     */
    List<HeatPointDetailEntity> queryHeatPointDetailByCategory(HeatPointAggregateRequest requestAggregate, HeatPointCategory category, int limit);
}
