package xin.eason.types.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import xin.eason.infrastructure.gateway.IHeatPointWebHandler;

/**
 * Retrofit 配置类
 */
@Configuration
@EnableConfigurationProperties(RetrofitConfigProperties.class)
public class RetrofitConfig {

    /**
     * 注入 {@link Retrofit} 的 Bean 对象
     *
     * @param properties {@link Retrofit} 配置属性
     * @return {@link Retrofit} 的 Bean 对象
     */
    @Bean
    public Retrofit retrofit(RetrofitConfigProperties properties) {
        return new Retrofit.Builder()
                .baseUrl(properties.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)))
                .build();
    }

    /**
     * 注册 {@link IHeatPointWebHandler} 发送请求的接口
     *
     * @param retrofit {@link Retrofit} 的 Bean 对象
     * @return {@link IHeatPointWebHandler} 实现类
     */
    @Bean
    public IHeatPointWebHandler heatPointWebHandler(Retrofit retrofit) {
        return retrofit.create(IHeatPointWebHandler.class);
    }
}
