package com.cysz.property.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询请求类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码（从1开始）
     */
    private Long current = 1L;

    /**
     * 每页大小
     */
    private Long size = 10L;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向：asc-升序，desc-降序
     */
    private String sortOrder = "desc";

    /**
     * 搜索关键字
     */
    private String keyword;

    public PageRequest() {
    }

    public PageRequest(Long current, Long size) {
        this.current = current;
        this.size = size;
    }

    public PageRequest(Long current, Long size, String sortField, String sortOrder) {
        this.current = current;
        this.size = size;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    /**
     * 获取偏移量
     */
    public Long getOffset() {
        return (current - 1) * size;
    }

    /**
     * 验证分页参数
     */
    public void validate() {
        if (current == null || current < 1) {
            current = 1L;
        }
        if (size == null || size < 1) {
            size = 10L;
        }
        if (size > 100) {
            size = 100L;
        }
        if (sortOrder == null || (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder))) {
            sortOrder = "desc";
        }
    }

    /**
     * 判断是否有排序
     */
    public boolean hasSort() {
        return sortField != null && !sortField.trim().isEmpty();
    }

    /**
     * 判断是否有搜索关键字
     */
    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    /**
     * 获取处理后的关键字（去除首尾空格）
     */
    public String getTrimmedKeyword() {
        return keyword != null ? keyword.trim() : null;
    }
}