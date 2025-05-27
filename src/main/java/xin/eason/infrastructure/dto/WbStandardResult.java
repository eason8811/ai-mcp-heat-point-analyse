package xin.eason.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 微博标准返回结果类
 * @param <T> 数据类型的泛型
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WbStandardResult<T> {
    /**
     * 相应代码
     */
    private int ok;
    /**
     * 数据本身
     */
    private T data;
}
