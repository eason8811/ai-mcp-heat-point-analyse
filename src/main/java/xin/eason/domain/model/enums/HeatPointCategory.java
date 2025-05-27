package xin.eason.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 热点种类枚举类
 */
@Getter
public enum HeatPointCategory {
    HOT(0, "热搜"),
    ENTERTAIN(1, "文娱"),
    NEWS(2, "要闻");

    /**
     * 代码
     */
    private final int code;
    /**
     * 描述
     */
    @JsonValue
    private final String desc;

    HeatPointCategory(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
