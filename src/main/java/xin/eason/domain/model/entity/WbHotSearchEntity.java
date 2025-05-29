package xin.eason.domain.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 微博热搜板块数据实体, 表示一行数据
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WbHotSearchEntity extends AbstractWbHeatPointEntity{
    /**
     * 情绪
     */
    private String emotion;
    /**
     * 标签名称 ( 新, 热, 爆 等 )
     */
    private String labelName;
    /**
     * 实时搜索人数
     */
    private int searchNum;
    /**
     * 词条真实排行榜位置
     */
    private int rank;
}
