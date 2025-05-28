package xin.eason.domain.model.aggregate;

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
public class AnalyseHeatPointAggregate {
    /**
     * 热搜置顶列表
     */
    private List<WbHotSearchEntity> hotSearchEntityTopList;
    /**
     * 热搜板块数据列表
     */
    private List<WbHotSearchEntity> hotSearchEntityList;
    /**
     * 文娱板块数据列表
     */
    private List<WbEntertainmentEntity> entertainmentEntityList;
    /**
     * 要闻板块数据列表
     */
    private List<WbNewsEntity> newsEntityList;
}
