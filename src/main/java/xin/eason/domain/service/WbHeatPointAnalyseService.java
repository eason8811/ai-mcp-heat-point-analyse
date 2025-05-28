package xin.eason.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xin.eason.api.IHeatPointAnalyseService;
import xin.eason.domain.adapter.port.IWebPort;
import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
import xin.eason.domain.model.entity.HeatPointDetailEntity;
import xin.eason.domain.model.entity.WbEntertainmentEntity;
import xin.eason.domain.model.entity.WbHotSearchEntity;
import xin.eason.domain.model.entity.WbNewsEntity;
import xin.eason.domain.model.enums.HeatPointCategory;

import java.io.IOException;
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
    public AnalyseHeatPointAggregate queryAllHeatPoints() {
        try {
            log.info("正在获取所有热点数据...");
            return webPort.queryHeatPoints();
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
    public AnalyseHeatPointAggregate queryHeatPointsByCategory(HeatPointCategory category) {
        try {
            log.info("正在根据类别获取所有热点数据... 当前类别为: [{}]", category.getDesc());
            return webPort.queryHeatPoints(category);
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
    public AnalyseHeatPointAggregate queryHeatPointByCategory(HeatPointCategory category, Integer limit) {
        AnalyseHeatPointAggregate aggregate = queryHeatPointsByCategory(category);
        log.info("正在进行列表切片, 获取 [{}] 板块前 {} 条数据...", category.getDesc(), limit);
        // 查询到数据之后切取子列表
        List<WbHotSearchEntity> hotSearchEntityList = aggregate.getHotSearchEntityList();
        if (category.getCode() == HeatPointCategory.HOT_SEARCH.getCode() && hotSearchEntityList != null) {
            aggregate.setHotSearchEntityList(hotSearchEntityList.subList(0, limit));
            return aggregate;
        }
        List<WbEntertainmentEntity> entertainmentEntityList = aggregate.getEntertainmentEntityList();
        if (category.getCode() == HeatPointCategory.ENTERTAINMENT.getCode() && entertainmentEntityList != null) {
            aggregate.setEntertainmentEntityList(entertainmentEntityList.subList(0, limit));
            return aggregate;
        }
        List<WbNewsEntity> newsEntityList = aggregate.getNewsEntityList();
        if (category.getCode() == HeatPointCategory.NEWS.getCode() && newsEntityList != null) {
            aggregate.setNewsEntityList(newsEntityList.subList(0, limit));
            return aggregate;
        }
        log.warn("category 类别不合法");
        return null;
    }

    /**
     * 根据热点的 URL 查询热点的详细信息
     *
     * @param url 热点 URL
     * @return 热点详细信息对象
     */
    @Override
    public HeatPointDetailEntity queryHeatPointDetailByUrl(String url) {
        return null;
    }

    /**
     * 根据查询热点数据得到的响应对象, 提取其中的热点 URL 并查询热点详细信息
     *
     * @param response 热搜数据响应对象
     * @return 热点详细信息对象
     */
    @Override
    public HeatPointDetailEntity queryHeatPointDetailByResponse(AnalyseHeatPointAggregate response) {
        return null;
    }

    /**
     * 批量的根据查询热点数据得到的响应对象, 提取其中的热点 URL 并查询热点详细信息
     *
     * @param responseList 热搜数据响应对象的 List
     * @return 热点详细信息对象的 List
     */
    @Override
    public List<HeatPointDetailEntity> queryHeatPointDetailByResponseBatch(List<AnalyseHeatPointAggregate> responseList) {
        return List.of();
    }
}
