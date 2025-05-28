package xin.eason.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微博热搜板块数据实体, 表示一行数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WbHotSearchEntity {
    /**
     * 情绪
     */
    private String emotion;
    /**
     * 标签名称 ( 新, 热, 爆 等 )
     */
    private String labelName;
    /**
     * 热搜标题
     */
    private String topic;
    /**
     * 实时搜索人数
     */
    private int searchNum;
    /**
     * 词条真实排行榜位置
     */
    private int rank;

    /**
     * {@link WbHotSearchEntity#topic} 字段的 Setter 函数, 删除标题中的 # 符号
     *
     * @param topic 热搜标题
     */
    public void setTopic(String topic) {
        topic = topic.replaceAll("#", "");
        this.topic = topic;
    }
}
