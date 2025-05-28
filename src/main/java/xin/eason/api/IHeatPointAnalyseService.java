package xin.eason.api;

import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
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
     * 根据热点的 URL 查询热点的详细信息
     *
     * @param url 热点 URL
     * @return 热点详细信息对象
     */
    HeatPointDetailEntity queryHeatPointDetailByUrl(String url);

    /**
     * 根据查询热点数据得到的响应对象, 提取其中的热点 URL 并查询热点详细信息
     *
     * @param response 热搜数据响应对象
     * @return 热点详细信息对象
     */
    HeatPointDetailEntity queryHeatPointDetailByResponse(AnalyseHeatPointAggregate response);

    /**
     * 批量的根据查询热点数据得到的响应对象, 提取其中的热点 URL 并查询热点详细信息
     *
     * @param responseList 热搜数据响应对象的 List
     * @return 热点详细信息对象的 List
     */
    List<HeatPointDetailEntity> queryHeatPointDetailByResponseBatch(List<AnalyseHeatPointAggregate> responseList);
}
