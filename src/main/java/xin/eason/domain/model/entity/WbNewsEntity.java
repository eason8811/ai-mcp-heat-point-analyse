package xin.eason.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微博要闻板块热搜数据实体, 表示一行数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WbNewsEntity {
    /**
     * 热搜标题
     */
    private String topic;

    /**
     * {@link WbNewsEntity#topic} 字段的 Setter 函数, 删除标题中的 # 符号
     *
     * @param topic 热搜标题
     */
    public void setTopic(String topic) {
        topic = topic.replaceAll("#", "");
        this.topic = topic;
    }
}
