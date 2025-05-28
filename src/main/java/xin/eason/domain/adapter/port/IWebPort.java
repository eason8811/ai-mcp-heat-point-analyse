package xin.eason.domain.adapter.port;

import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
import xin.eason.domain.model.enums.HeatPointCategory;

import java.io.IOException;

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
}
