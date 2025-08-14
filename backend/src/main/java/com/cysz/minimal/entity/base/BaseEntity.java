package com.cysz.minimal.entity.base;

import java.time.LocalDateTime;

/**
 * 基础实体类
 * 包含通用字段：id、创建时间、更新时间
 */
public abstract class BaseEntity {
    
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}