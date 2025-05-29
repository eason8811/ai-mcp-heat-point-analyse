package xin.eason.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 微博热搜板块数据实体, 表示一行数据
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WbHotSearchEntity extends AbstractWbHeatPointEntity{
    /**
     * 情绪
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("热点的情绪")
    private String emotion;
    /**
     * 标签名称 ( 新, 热, 爆 等 )
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("热点的标签名, 表示热点当前相较于其他热点的状态")
    private String labelName;
    /**
     * 实时搜索人数
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("热点实时搜索人数")
    private int searchNum;
    /**
     * 词条真实排行榜位置
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("热点在排行榜上的名次, 名次越高热点关注度越高")
    private int rank;
}
