package xin.eason.infrastructure.gateway;

import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.*;
import xin.eason.infrastructure.dto.WbEntertainmentResponseDTO;
import xin.eason.infrastructure.dto.WbHotSearchResponseDTO;
import xin.eason.infrastructure.dto.WbNewsResponseDTO;
import xin.eason.infrastructure.dto.WbStandardResult;

/**
 * 热点查询网络处理器, 用于实际发送网络请求
 */
@Component
public interface IHeatPointWebHandler {

    /**
     * 根据 Cookie 查询 微博 平台的所有热搜信息
     *
     * @param cookieString Cookie 字符串形式
     * @return 微博热搜响应 DTO
     */
    @Headers({
            "accept: application/json, text/plain, */*",
            "accept-language: zh-CN,zh;q=0.9",
            "cache-control: no-cache",
            "client-version: v2.47.67",
            "pragma: no-cache",
            "priority: u=1, i",
            "referer: https://weibo.com/hot/search",
            "sec-ch-ua: \"Chromium\";v=\"9\", \"Not?A_Brand\";v=\"8\"",
            "sec-ch-ua-mobile: ?0",
            "sec-ch-ua-platform: \"Windows\"",
            "sec-fetch-dest: empty",
            "sec-fetch-mode: cors",
            "sec-fetch-site: same-origin",
            "server-version: v2025.05.26.1",
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 SLBrowser/9.0.6.2081 SLBChan/10 SLBVPV/64-bit",
            "x-requested-with: XMLHttpRequest",
            "x-xsrf-token: O8FlBz255V1awMvjtHQh2ZOd"
    })
    @GET("/ajax/side/hotSearch")
    Call<WbStandardResult<WbHotSearchResponseDTO>> wbQueryHotSearch(@Header("Cookie") String cookieString);

    /**
     * 根据 Cookie 查询 微博 平台的所有文娱热搜信息
     *
     * @param cookieString Cookie 字符串形式
     * @return 微博文娱响应 DTO
     */
    @Headers({
            "accept: application/json, text/plain, */*",
            "accept-language: zh-CN,zh;q=0.9",
            "cache-control: no-cache",
            "client-version: v2.47.67",
            "pragma: no-cache",
            "priority: u=1, i",
            "referer: https://weibo.com/hot/entertainment",
            "sec-ch-ua: \"Chromium\";v=\"9\", \"Not?A_Brand\";v=\"8\"",
            "sec-ch-ua-mobile: ?0",
            "sec-ch-ua-platform: \"Windows\"",
            "sec-fetch-dest: empty",
            "sec-fetch-mode: cors",
            "sec-fetch-site: same-origin",
            "server-version: v2025.05.26.1",
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 SLBrowser/9.0.6.2081 SLBChan/10 SLBVPV/64-bit",
            "x-requested-with: XMLHttpRequest",
            "x-xsrf-token: O8FlBz255V1awMvjtHQh2ZOd"
    })
    @GET("/ajax/statuses/entertainment")
    Call<WbStandardResult<WbEntertainmentResponseDTO>> wbQueryEntertainment(@Header("Cookie") String cookieString);

    /**
     * 根据 Cookie 查询 微博 平台的所有要闻热搜信息
     *
     * @param cookieString Cookie 字符串形式
     * @return 微博要闻响应 DTO
     */
    @Headers({
            "accept: application/json, text/plain, */*",
            "accept-language: zh-CN,zh;q=0.9",
            "cache-control: no-cache",
            "client-version: v2.47.67",
            "pragma: no-cache",
            "priority: u=1, i",
            "referer: https://weibo.com/hot/news",
            "sec-ch-ua: \"Chromium\";v=\"9\", \"Not?A_Brand\";v=\"8\"",
            "sec-ch-ua-mobile: ?0",
            "sec-ch-ua-platform: \"Windows\"",
            "sec-fetch-dest: empty",
            "sec-fetch-mode: cors",
            "sec-fetch-site: same-origin",
            "server-version: v2025.05.26.1",
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 SLBrowser/9.0.6.2081 SLBChan/10 SLBVPV/64-bit",
            "x-requested-with: XMLHttpRequest",
            "x-xsrf-token: O8FlBz255V1awMvjtHQh2ZOd"
    })
    @GET("/ajax/statuses/news")
    Call<WbStandardResult<WbNewsResponseDTO>> wbQueryNews(@Header("Cookie") String cookieString);

    /**
     * 根据 URL 获取热搜词条详细信息
     *
     * @param url          热搜词条 URL
     * @param cookieString Cookie 字符串形式
     * @return HTML 页面
     */
    @Headers({
            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
            "accept-language: zh-CN,zh;q=0.9",
            "cache-control: no-cache",
            "pragma: no-cache",
            "priority: u=0, i",
            "referer: https://s.weibo.com/weibo?q=%23%E5%9F%B9%E5%85%BB%E4%B8%AD%E5%9B%BD%E7%89%B9%E8%89%B2%E7%A4%BE%E4%BC%9A%E4%B8%BB%E4%B9%89%E4%BA%8B%E4%B8%9A%E5%90%88%E6%A0%BC%E5%BB%BA%E8%AE%BE%E8%80%85%23&t=31&page=2",
            "sec-ch-ua: \"Chromium\";v=\"9\", \"Not?A_Brand\";v=\"8\"",
            "sec-ch-ua-mobile: ?0",
            "sec-ch-ua-platform: \"Windows\"",
            "sec-fetch-dest: document",
            "sec-fetch-mode: navigate",
            "sec-fetch-site: same-origin",
            "sec-fetch-user: ?1",
            "upgrade-insecure-requests: 1",
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 SLBrowser/9.0.6.2081 SLBChan/10 SLBVPV/64-bit"
    })
    @GET
    Call<String> wbQueryHotSearchDetail(@Url String url, @Header("Cookie") String cookieString);
}
