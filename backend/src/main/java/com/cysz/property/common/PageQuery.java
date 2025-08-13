package com.cysz.property.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 分页查询基类
 *
 * @author CYSZ
 * @since 2025-01-14
 */
@Data
public class PageQuery {

    /**
     * 当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer current = 1;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页大小最小为1")
    @Max(value = 1000, message = "每页大小最大为1000")
    private Integer size = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方式：asc/desc
     */
    private String order = "desc";

    /**
     * 转换为MyBatis-Plus的Page对象
     */
    public <T> Page<T> toPage() {
        return new Page<>(current, size);
    }

    /**
     * 转换为MyBatis-Plus的Page对象（带排序）
     */
    public <T> Page<T> toPage(String defaultOrderBy) {
        Page<T> page = new Page<>(current, size);
        
        String actualOrderBy = orderBy != null ? orderBy : defaultOrderBy;
        if (actualOrderBy != null && !actualOrderBy.trim().isEmpty()) {
            if ("asc".equalsIgnoreCase(order)) {
                page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.asc(actualOrderBy));
            } else {
                page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.desc(actualOrderBy));
            }
        }
        
        return page;
    }

}