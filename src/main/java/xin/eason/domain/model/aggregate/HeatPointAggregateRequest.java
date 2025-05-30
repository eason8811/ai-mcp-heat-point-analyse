package xin.eason.domain.model.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分析热点信息聚合请求类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeatPointAggregateRequest {
    /**
     * 热搜板块热点标题列表
     */
    @JsonProperty
    @JsonPropertyDescription("热搜板块热点标题列表")
    private List<String> hotSearchTopicList;
    /**
     * 文娱板块热点标题列表
     */
    @JsonProperty
    @JsonPropertyDescription("文娱板块热点标题列表")
    private List<String> entertainmentTopicList;
    /**
     * 要闻板块热点标题列表
     */
    @JsonProperty
    @JsonPropertyDescription("要闻板块热点标题列表")
    private List<String> newsTopicList;
}
