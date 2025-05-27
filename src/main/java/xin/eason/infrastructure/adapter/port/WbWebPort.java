package xin.eason.infrastructure.adapter.port;

import org.springframework.stereotype.Component;
import xin.eason.domain.adapter.port.IWebPort;

/**
 * 适配器网络接口的 微博 实现, 用于向 微博 发送请求, 获取热点信息
 */
@Component
public class WbWebPort implements IWebPort {
}
