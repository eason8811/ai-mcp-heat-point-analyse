package xin.eason.domain.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 微博要闻板块热搜数据实体, 表示一行数据
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WbNewsEntity extends AbstractWbHeatPointEntity{
}
