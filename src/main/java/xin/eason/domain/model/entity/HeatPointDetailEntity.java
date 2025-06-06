package xin.eason.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.eason.domain.model.enums.HeatPointCategory;

/**
 * 热点详细信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HeatPointDetailEntity {
    /**
     * 热点细节所属板块
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("本热点帖子详细信息所属的板块类别")
    private HeatPointCategory category;
    /**
     * 发表这个帖子的账号名
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("发表本热点帖子详细信息的用户的用户名")
    private String userName;
    /**
     * 热点细节内容
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("本热点帖子详细信息的内容")
    private String content;
    /**
     * 热点细节转发数量
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("本热点帖子的被转发数量")
    private int send;
    /**
     * 热点细节评论数量
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("本热点帖子的被评论数量")
    private int comment;
    /**
     * 热点细节点赞数量
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("本热点帖子的被点赞数量")
    private int like;
}
