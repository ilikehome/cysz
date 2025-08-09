package com.cysz.minimal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContractTemplate {
    private Long id;
    private String templateName;
    private String templateType; // LEASE, SERVICE, SALES, OTHER
    private String version;
    private String status; // ACTIVE, INACTIVE
    private String description;
    private String templateContent;
    private Integer usageCount;
    private List<ContractField> fields;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    @Data
    public static class ContractField {
        private String fieldName;
        private String fieldLabel;
        private String fieldType; // text, number, date, select
        private Boolean required;
        private String defaultValue;
        private String options; // 用于select类型的选项，JSON格式
    }
}