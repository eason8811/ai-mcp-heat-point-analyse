package xin.eason.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 微博文娱板块数据实体, 表示一行数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WbEntertainmentEntity {
    /**
     * 文娱类别
     */
    private String category;
    /**
     * 频道类型列表 ( 源数据使用 | 进行分割 )
     */
    private List<String> channelTypeList;
    /**
     * 实时搜索人数
     */
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
     * 标签名称
     */
    private String labelName;
    /**
     * 词条真实排行位置
     */
    private int rank;
    /**
     * 上榜主热搜的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onHotSearchBoardTime;
    /**
     * 上榜文娱热搜的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onBoardTime;
    /**
     * 关联的明星列表
     */
    private List<String> starNameList;
    /**
     * 主题信息 Map 形式
     */
    private Map<String, SubjectDetail> subjectDetailMap;
    /**
     * 热搜标题
     */
    private String topic;

    /**
     * 主题详细信息类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectDetail {
        /**
         * 主题对应的 name 列表
         */
        private String nameList;
        /**
         * 主题来源
         */
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
     * {@link WbEntertainmentEntity#topic} 字段的 Setter 函数, 删除标题中的 # 符号
     *
     * @param topic 热搜标题
     */
    public void setTopic(String topic) {
        topic = topic.replaceAll("#", "");
        this.topic = topic;
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
