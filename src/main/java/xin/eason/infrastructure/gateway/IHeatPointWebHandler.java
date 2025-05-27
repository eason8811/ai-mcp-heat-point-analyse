package xin.eason.infrastructure.gateway;

import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import xin.eason.infrastructure.dto.WbEntertainmentResponseDTO;
import xin.eason.infrastructure.dto.WbHotSearchResponseDTO;
import xin.eason.infrastructure.dto.WbNewsResponseDTO;
import xin.eason.infrastructure.dto.WbStandardResult;

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
     * 根据 Cookie 查询 微博 平台的所有热搜信息
     * @param cookisString Cookie 字符串形式
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
    Call<WbStandardResult<WbEntertainmentResponseDTO>> wbQueryEntertainment(@Header("Cookie") String cookisString);

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
}
