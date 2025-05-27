package xin.eason.types.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Retrofit 配置属性类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("heat-point")
public class HeatPointConfigProperties {
    /**
     * 根 URL
     */
    private String baseUrl;
    /**
     * Cookie 信息 ( String 格式 )
     */
    private String cookieString;
}
