package xin.eason.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 文娱热搜响应 DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WbEntertainmentResponseDTO {
    /**
     * 文娱热搜数据记录列表
     */
    @JsonProperty("band_list")
    private List<EntertainmentDataRow> entertainmentDataRowList;

    /**
     * 文娱热搜数据记录
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EntertainmentDataRow {
        /**
         * 文娱热搜类别
         */
        private String category;
        /**
         * 频道类型 ( 使用 | 进行分割 )
         */
        private String channelType;
        /**
         * 实时搜索人数
         */
        @JsonProperty("num")
        private int searchNum;
        /**
         * 合作
         */
        private int cooperate;
        /**
         * 情绪
         */
        private String emotion;
        /**
         * 花费 ? ( 未知字段 )
         */
        private int expand;
        /**
         * 标记
         */
        private int flag;
        /**
         * 标签名称
         */
        private String labelName;
        /**
         * 热搜排位
         */
        @JsonProperty("hot_rank_position")
        private int rank;
        /**
         * 记录
         */
        private String note;
        /**
         * 上榜主热搜的时间戳
         */
        private long onMainBoardTime;
        /**
         * 上榜文娱热搜的时间戳
         */
        private long onBoardTime;
        /**
         * 文娱热搜中的实际位置
         */
        @JsonProperty("realpos")
        private int realPosition;
        /**
         * 关联的明星列表
         */
        @JsonProperty("star_name")
        JsonNode starNameList;
        /**
         * 主题信息 Map 形式
         */
        @JsonProperty("subject_querys")
        private Map<String, SubjectDetail> subjectDetailMap;
        /**
         * 词条
         */
        private String word;
    }

    /**
     * 主题详细信息类
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubjectDetail {
        /**
         * 主题对应的 name 列表
         */
        @JsonProperty("subject_namelist")
        private String nameList;
        /**
         * 主题来源
         */
        @JsonProperty("subject_source")
        private String source;
        /**
         * 主题的类别
         */
        @JsonProperty("subject_type")
        private String type;
    }
}
