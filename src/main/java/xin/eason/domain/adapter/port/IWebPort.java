package xin.eason.domain.adapter.port;

import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
import xin.eason.domain.model.entity.HeatPointDetailEntity;
import xin.eason.domain.model.enums.HeatPointCategory;

import java.io.IOException;
import java.util.List;

/**
 * 适配器网络接口, 用于向外部网络发送请求
 */
public interface IWebPort {
    /**
     * 查询所有热点信息
     *
     * @return 热点分析聚合
     */
    AnalyseHeatPointAggregate queryHeatPoints() throws IOException;

    /**
     * 根据 category 板块类型查询所有热点信息
     *
     * @param category 热点板块类型
     * @return 热点分析聚合
     */
    AnalyseHeatPointAggregate queryHeatPoints(HeatPointCategory category) throws IOException;

    /**
     * 根据热点的 URL 查询热点的详细信息
     * @param url 热点 URL
     * @return 热点详细信息对象列表
     */
    List<HeatPointDetailEntity> queryHeatPointsDetail(String url) throws IOException;

    /**
     * 根据热点的 URL 查询热点帖子的前 N 条详细信息
     * @param url 热点 URL
     * @param limit 返回前 N 条详细内容
     * @return 热点详细信息对象列表
     */
    List<HeatPointDetailEntity> queryHeatPointsDetail(String url, int limit) throws IOException;
}
