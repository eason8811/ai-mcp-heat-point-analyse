package xin.eason;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Response;
import xin.eason.api.IHeatPointAnalyseService;
import xin.eason.domain.adapter.port.IWebPort;
import xin.eason.domain.model.aggregate.AnalyseHeatPointAggregate;
import xin.eason.domain.model.aggregate.HeatPointAggregateRequest;
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

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest
public class ApiTest {

    @Autowired
    private IHeatPointWebHandler heatPointWebHandler;

    @Autowired
    private IHeatPointAnalyseService heatPointAnalyseService;

    @Autowired
    private IWebPort webPort;

    /**
     * 测试 Retrofit 发送网络请求的接口
     */
    @Test
    public void testGetWbHotSearch() {
        try {
            Response<WbStandardResult<WbHotSearchResponseDTO>> response = heatPointWebHandler.wbQueryHotSearch("").execute();
            if (response.isSuccessful()) {
                log.info("请求成功! HTTP 响应代码: {}", response.code());
                WbStandardResult standardResult = response.body();
                int okCode = standardResult.getOk();
                if (okCode != 1) {
                    log.warn("响应错误!");
                    return;
                }
                WbHotSearchResponseDTO data = (WbHotSearchResponseDTO) standardResult.getData();
                log.info("热搜置顶第一条: {}", data.getHotgov());
                log.info("热搜置顶列表: {}", data.getHotgovList());
                log.info("热搜数据前 10 项: {}", data.getHotSearchDataRowList().subList(0, 10));
            } else {
                log.warn("请求失败! HTTP 响应代码: {}", response.code());
                log.warn("请求失败响应体: {}", response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetWbEntertainment() {
        try {
            Response<WbStandardResult<WbEntertainmentResponseDTO>> response = heatPointWebHandler.wbQueryEntertainment("XSRF-TOKEN=O8FlBz255V1awMvjtHQh2ZOd; _s_tentry=www.baidu.com; UOR=www.baidu.com,open.weibo.com,www.baidu.com; Apache=3600856565491.366.1748323006418; SINAGLOBAL=3600856565491.366.1748323006418; ULV=1748323006423:1:1:1:3600856565491.366.1748323006418:; SCF=At_uHy0mJO7o4Y4rARVkp1NYl6BZQkDIdynz9m2DUD9zyhN9JyPInPrriDtiwXvMk-ujnBPsiEBnx7B67n5631c.; SUB=_2A25FMTxWDeRhGeFL7FMX9izPyzuIHXVmTzGerDV8PUNbmtAbLWL_kW9NfdRmYDvFpmURq9cSNLYNLqiHVeyEHtpo; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFuh-A6BWLKAHX9hMMwGTBY5JpX5KzhUgL.FoMfS02cSoz0ehM2dJLoIpeLxK.L1KnL12-LxKML1hzL12H4; ALF=02_1750915334; WBPSESS=Dt2hbAUaXfkVprjyrAZT_P2eVZnXPh0MY6e1nlS9e2RV185KZSYgO1SKLTco16N_nM4xVOACT1A8CJ568vqh-lipmknun117qG0t7q3nnkjkekPwaskSCm-9IOwrFjVYvBtp6ZW2Cg-SmHbCiO0m42yEjPZ0E6FZ_b1NjTYN5BEl6Cq3oTBDc0vRg_a4YVEPxbFS_b_2ikHfzrKQkpXihQ==").execute();
            if (response.isSuccessful()) {
                log.info("请求成功! HTTP 响应代码: {}", response.code());
                WbStandardResult standardResult = response.body();
                int okCode = standardResult.getOk();
                if (okCode != 1) {
                    log.warn("响应错误! 响应错误响应体: {}", standardResult);
                    return;
                }
                WbEntertainmentResponseDTO data = (WbEntertainmentResponseDTO) standardResult.getData();
                log.info("文娱数据前 10 项: {}", data.getEntertainmentDataRowList().subList(0, 10));
            } else {
                log.warn("请求失败! HTTP 响应代码: {}", response.code());
                log.warn("请求失败响应体: {}", response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetWbNews() {
        try {
            Response<WbStandardResult<WbNewsResponseDTO>> response = heatPointWebHandler.wbQueryNews("XSRF-TOKEN=O8FlBz255V1awMvjtHQh2ZOd; _s_tentry=www.baidu.com; UOR=www.baidu.com,open.weibo.com,www.baidu.com; Apache=3600856565491.366.1748323006418; SINAGLOBAL=3600856565491.366.1748323006418; ULV=1748323006423:1:1:1:3600856565491.366.1748323006418:; SCF=At_uHy0mJO7o4Y4rARVkp1NYl6BZQkDIdynz9m2DUD9zyhN9JyPInPrriDtiwXvMk-ujnBPsiEBnx7B67n5631c.; SUB=_2A25FMTxWDeRhGeFL7FMX9izPyzuIHXVmTzGerDV8PUNbmtAbLWL_kW9NfdRmYDvFpmURq9cSNLYNLqiHVeyEHtpo; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFuh-A6BWLKAHX9hMMwGTBY5JpX5KzhUgL.FoMfS02cSoz0ehM2dJLoIpeLxK.L1KnL12-LxKML1hzL12H4; ALF=02_1750915334; WBPSESS=Dt2hbAUaXfkVprjyrAZT_P2eVZnXPh0MY6e1nlS9e2RV185KZSYgO1SKLTco16N_nM4xVOACT1A8CJ568vqh-lipmknun117qG0t7q3nnkjkekPwaskSCm-9IOwrFjVYvBtp6ZW2Cg-SmHbCiO0m42yEjPZ0E6FZ_b1NjTYN5BEl6Cq3oTBDc0vRg_a4YVEPxbFS_b_2ikHfzrKQkpXihQ==").execute();
            if (response.isSuccessful()) {
                log.info("请求成功! HTTP 响应代码: {}", response.code());
                WbStandardResult standardResult = response.body();
                int okCode = standardResult.getOk();
                if (okCode != 1) {
                    log.warn("响应错误! 响应错误响应体: {}", standardResult);
                    return;
                }
                WbNewsResponseDTO data = (WbNewsResponseDTO) standardResult.getData();
                log.info("文娱数据前 10 项: {}", data.getNewsDataRowList().subList(0, 10));
            } else {
                log.warn("请求失败! HTTP 响应代码: {}", response.code());
                log.warn("请求失败响应体: {}", response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testQueryAllDataApi() {
        AnalyseHeatPointAggregate aggregate = heatPointAnalyseService.queryAllHeatPoints();
        log.info("获取热点信息成功!");
        log.info("结果: \n {}", JSON.toJSONString(aggregate));
    }

    @Test
    public void testQueryByCategory() {
        AnalyseHeatPointAggregate aggregate = heatPointAnalyseService.queryHeatPointsByCategory(HeatPointCategory.NEWS);
        log.info("获取热点信息成功!");
        log.info("结果: \n {}", JSON.toJSONString(aggregate));
    }

    @Test
    public void testQueryByCategoryLimit() {
        AnalyseHeatPointAggregate aggregate = heatPointAnalyseService.queryHeatPointByCategory(HeatPointCategory.HOT_SEARCH, 5);
        log.info("获取热点信息成功!");
        log.info("结果: \n {}", JSON.toJSONString(aggregate));
    }

    @Test
    public void testQueryHeatPointDetailByUrl() {
        String url = "https://s.weibo.com/weibo?q=%23%E9%9F%A9%E5%9B%BD%E5%8F%91%E7%94%9F%E9%A3%9E%E6%9C%BA%E5%9D%A0%E6%AF%81%E4%BA%8B%E4%BB%B6%23&t=31";
        List<HeatPointDetailEntity> heatPointDetailEntityList = heatPointAnalyseService.queryHeatPointDetailByUrl(url, 2);
        log.info("查询热点细节信息成功!");
        log.info("结果: \n {}", JSON.toJSONString(heatPointDetailEntityList));
    }

    @Test
    public void testQueryHeatPointDetailByAggregate() {
        AnalyseHeatPointAggregate aggregate = heatPointAnalyseService.queryHeatPointByCategory(HeatPointCategory.ENTERTAINMENT, 1);
        log.info("获取热点信息成功!");
        log.info("结果: \n {}", JSON.toJSONString(aggregate));

        List<WbHotSearchEntity> hotSearchEntityList = aggregate.getHotSearchEntityList();
        List<WbEntertainmentEntity> entertainmentEntityList = aggregate.getEntertainmentEntityList();
        List<WbNewsEntity> newsEntityList = aggregate.getNewsEntityList();
        HeatPointAggregateRequest aggregateRequest = new HeatPointAggregateRequest();

        if (hotSearchEntityList != null)
            aggregateRequest.setHotSearchTopicList(hotSearchEntityList.stream().map(WbHotSearchEntity::getTopic).toList());
        if (entertainmentEntityList != null)
            aggregateRequest.setEntertainmentTopicList(entertainmentEntityList.stream().map(WbEntertainmentEntity::getTopic).toList());
        if (newsEntityList != null)
            aggregateRequest.setNewsTopicList(newsEntityList.stream().map(WbNewsEntity::getTopic).toList());

        List<HeatPointDetailEntity> heatPointDetailEntityList = heatPointAnalyseService.queryHeatPointDetailByCategory(aggregateRequest, HeatPointCategory.ENTERTAINMENT);
        log.info("查询热点细节信息成功!");
        log.info("结果: \n {}", JSON.toJSONString(heatPointDetailEntityList));
    }
}
