package com.cysz.minimal.common;

import java.util.List;

/**
 * 分页结果封装类
 */
public class PageResult<T> {
    
    private List<T> records;    // 数据列表
    private long total;         // 总记录数
    private int current;        // 当前页
    private int size;           // 每页大小
    
    public PageResult() {}
    
    public PageResult(List<T> records, long total, int current, int size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
    }
    
    public List<T> getRecords() {
        return records;
    }
    
    public void setRecords(List<T> records) {
        this.records = records;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
}