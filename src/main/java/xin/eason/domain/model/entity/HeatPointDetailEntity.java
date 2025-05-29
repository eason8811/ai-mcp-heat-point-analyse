package xin.eason.domain.model.entity;

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
public class HeatPointDetailEntity {
    /**
     * 热点细节所属板块
     */
    private HeatPointCategory category;
    /**
     * 发表这个帖子的账号名
     */
    private String userName;
    /**
     * 热点细节内容
     */
    private String content;
    /**
     * 热点细节转发数量
     */
    private int send;
    /**
     * 热点细节评论数量
     */
    private int comment;
    /**
     * 热点细节点赞数量
     */
    private int like;
}
