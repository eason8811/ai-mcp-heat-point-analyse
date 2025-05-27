package xin.eason.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 微博热点信息响应 DTO, 用于装载热点响应信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WbHotSearchResponseDTO {
    /**
     * 热搜置顶
     */
    private HotSearchDataRow hotgov;
    /**
     * 热搜置顶列表
     */
    @JsonProperty("hotgovs")
    private List<HotSearchDataRow> hotgovList;
    /**
     * 非置顶热搜数据
     */
    @JsonProperty("realtime")
    private List<HotSearchDataRow> hotSearchDataRowList;

    /**
     * 热搜数据行类
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HotSearchDataRow {
        /**
         * 情绪
         */
        private String emotion;
        /**
         * 标记
         */
        private int flag;
        /**
         * 标签名称
         */
        private String labelName;
        /**
         * 记录
         */
        private String note;
        /**
         * 实时搜索人数
         */
        private int searchNum;
        /**
         * 词条排位
         */
        private int rank;
        /**
         * 词条实际位置 ( 如果有置顶, 商单等就要下顺 )
         */
        @JsonProperty("realpos")
        private int realPosition;
        /**
         * 标题标记 ? ( 未知字段 )
         */
        private int topicFlag;
        /**
         * 词条
         */
        private String word;
    }
}
