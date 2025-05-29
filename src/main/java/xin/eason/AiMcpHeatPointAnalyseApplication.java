package xin.eason;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import xin.eason.api.IHeatPointAnalyseService;
import xin.eason.types.config.HeatPointConfigProperties;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class AiMcpHeatPointAnalyseApplication implements CommandLineRunner {

    private final HeatPointConfigProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(AiMcpHeatPointAnalyseApplication.class, args);
    }

    /**
     * 为 MCP Client 提供工具函数
     * @param heatPointAnalyseService 工具函数提供对象
     * @return {@link ToolCallbackProvider}
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(IHeatPointAnalyseService heatPointAnalyseService) {
        return MethodToolCallbackProvider.builder().toolObjects(heatPointAnalyseService).build();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("热点分析 MCP Server 启动成功!");
        log.info("正在获取 Cookie 信息...");
        if (properties.getCookieString() == null || properties.getCookieString().isBlank()) {
            log.warn("Cookie 为空, 请提供 Cookie, 否则可能无法获取正常数据!!!");
        } else {
            log.info("微博 Cookie 信息: {}", properties.getCookieString());
        }
    }
}
