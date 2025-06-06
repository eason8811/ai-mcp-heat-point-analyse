package xin.eason.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 抽象热点实体对象, 提取了热点实体对象的共性信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractWbHeatPointEntity {
    /**
     * 热搜标题
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("热点的标题")
    private String topic;

    /**
     * {@link AbstractWbHeatPointEntity#topic} 字段的 Setter 函数, 删除标题中的 # 符号
     *
     * @param topic 热搜标题
     */
    public void setTopic(String topic) {
        topic = topic.replaceAll("#", "");
        this.topic = topic;
    }
}
