package xin.eason.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 微博文娱板块数据实体, 表示一行数据
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WbEntertainmentEntity extends AbstractWbHeatPointEntity{
    /**
     * 文娱类别
     */
    @JsonProperty
    @JsonPropertyDescription("热点的类别")
    private String category;
    /**
     * 频道类型列表 ( 源数据使用 | 进行分割 )
     */
    @JsonProperty
    @JsonPropertyDescription("热点的频道类型列表")
    private List<String> channelTypeList;
    /**
     * 实时搜索人数
     */
    @JsonProperty
    @JsonPropertyDescription("热点的实时搜索人数")
    private int searchNum;
    /**
     * 合作
     */
    @JsonProperty
    @JsonPropertyDescription("这个热点是否有合作 (无合作为0)")
    private int cooperate;
    /**
     * 情绪
     */
    @JsonProperty
    @JsonPropertyDescription("热点的情绪 (无特殊情况为空)")
    private String emotion;
    /**
     * 标签名称
     */
    @JsonProperty
    @JsonPropertyDescription("热点的标签名, 表示热点当前相较于其他热点的状态")
    private String labelName;
    /**
     * 词条真实排行位置
     */
    @JsonProperty
    @JsonPropertyDescription("热点在排行榜上的名次, 名次越高热点关注度越高")
    private int rank;
    /**
     * 上榜主热搜的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty
    @JsonPropertyDescription("热点上榜主热点排行榜的时间")
    private LocalDateTime onHotSearchBoardTime;
    /**
     * 上榜文娱热搜的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty
    @JsonPropertyDescription("热点上榜本板块排行榜的时间")
    private LocalDateTime onBoardTime;
    /**
     * 关联的明星列表
     */
    @JsonProperty
    @JsonPropertyDescription("本热点提到的明星名字的列表")
    private List<String> starNameList;
    /**
     * 主题信息 Map 形式
     */
    @JsonProperty
    @JsonPropertyDescription("热点关联的主题, 以明星名字列表中的明星名字为键, 主题细节信息为值")
    private Map<String, SubjectDetail> subjectDetailMap;

    /**
     * 主题详细信息类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SubjectDetail {
        /**
         * 主题对应的 name 列表
         */
        @JsonProperty
        @JsonPropertyDescription("主题对应的 name 列表, 作用未知")
        private String nameList;
        /**
         * 主题来源
         */
        @JsonProperty
        @JsonPropertyDescription("热点的情绪 (无特殊情况为空)")
        private String source;
        /**
         * 主题的类别
         */
        private String type;
    }

    /**
     * 将 {@link WbEntertainmentEntity#starNameList} 由 Object 转换回 {@link List<String>}
     *
     * @param starNameList 明星名字列表
     */
    public void setStarNameList(JsonNode starNameList) {
        if (starNameList.isObject()) {
            this.starNameList = List.of();
            return;
        }
        if (!starNameList.isArray()) {
            this.starNameList = null;
            return;
        }
        List<String> tempList = new ArrayList<>();
        starNameList.forEach(starName -> tempList.add(starName.asText()));
        this.starNameList = tempList;
    }

    /**
     * 将传入的上榜热搜榜的时间戳转换为 {@link LocalDateTime}
     * @param onHotSearchBoardTime 传入的上榜热搜榜时间戳
     */
    public void setOnHotSearchBoardTime(long onHotSearchBoardTime) {
        if (onHotSearchBoardTime == 0)
            return;
        this.onHotSearchBoardTime = Instant.ofEpochSecond(onHotSearchBoardTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 将传入的上榜文娱热搜榜的时间戳转换为 {@link LocalDateTime}
     * @param onBoardTime 传入的上榜文娱热搜榜的时间戳
     */
    public void setOnBoardTime(long onBoardTime) {
        if (onBoardTime == 0)
            return;
        this.onBoardTime = Instant.ofEpochSecond(onBoardTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 普通 Setter
     * @param onHotSearchBoardTime 上榜主热搜的时间
     */
    public void setOnHotSearchBoardTime(LocalDateTime onHotSearchBoardTime) {
        this.onHotSearchBoardTime = onHotSearchBoardTime;
    }

    /**
     * 普通 Setter
     * @param onBoardTime 上榜文娱热搜时间
     */
    public void setOnBoardTime(LocalDateTime onBoardTime) {
        this.onBoardTime = onBoardTime;
    }

    /**
     * 普通 Setter
     * @param starNameList 明星名字列表
     */
    public void setStarNameList(List<String> starNameList) {
        this.starNameList = starNameList;
    }
}
