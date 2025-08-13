package com.cysz.property.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;

    public PageResult() {
    }

    public PageResult(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size;
    }

    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        return new PageResult<>(records, total, current, size);
    }

    /**
     * 创建空分页结果
     */
    public static <T> PageResult<T> empty(Long current, Long size) {
        return new PageResult<>(List.of(), 0L, current, size);
    }

    /**
     * 判断是否有数据
     */
    public boolean hasData() {
        return records != null && !records.isEmpty();
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return !hasData();
    }

    /**
     * 判断是否有下一页
     */
    public boolean hasNext() {
        return current < pages;
    }

    /**
     * 判断是否有上一页
     */
    public boolean hasPrevious() {
        return current > 1;
    }
}