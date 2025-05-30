package xin.eason.domain.model.aggregate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.eason.domain.model.entity.WbEntertainmentEntity;
import xin.eason.domain.model.entity.WbHotSearchEntity;
import xin.eason.domain.model.entity.WbNewsEntity;

import java.util.List;

/**
 * 分析热点信息聚合类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AnalyseHeatPointAggregate {
    /**
     * 热搜置顶列表
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("热搜板块置顶热点列表")
    private List<WbHotSearchEntity> hotSearchEntityTopList;
    /**
     * 热搜板块数据列表
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("热搜板块普通热点列表")
    private List<WbHotSearchEntity> hotSearchEntityList;
    /**
     * 文娱板块数据列表
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("文娱板块热点列表")
    private List<WbEntertainmentEntity> entertainmentEntityList;
    /**
     * 要闻板块数据列表
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("要闻板块热点列表")
    private List<WbNewsEntity> newsEntityList;
}
