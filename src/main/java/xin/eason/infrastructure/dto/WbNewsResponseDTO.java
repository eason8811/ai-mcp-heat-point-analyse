package xin.eason.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 微博要闻热搜响应信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WbNewsResponseDTO {
    /**
     * 要闻热搜记录列表
     */
    @JsonProperty("band_list")
    private List<NewsDataRow> newsDataRowList;

    /**
     * 要闻热搜记录
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsDataRow {
        /**
         * 是否有点状图标
         */
        private int dotIcon;
        /**
         * 词条
         */
        @JsonProperty("topic")
        private String word;
    }

}
