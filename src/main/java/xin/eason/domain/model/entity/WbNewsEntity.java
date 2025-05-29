package xin.eason.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 微博要闻板块热搜数据实体, 表示一行数据
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WbNewsEntity extends AbstractWbHeatPointEntity{
}
