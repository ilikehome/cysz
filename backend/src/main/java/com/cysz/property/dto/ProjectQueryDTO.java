package com.cysz.property.dto;

import com.cysz.property.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 项目查询数据传输对象
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectQueryDTO extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目名称（模糊查询）
     */
    private String name;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目状态
     */
    private Integer status;

    /**
     * 城市（模糊查询）
     */
    private String city;

    /**
     * 项目地址（模糊查询）
     */
    private String address;

    /**
     * 公司名称（模糊查询）
     */
    private String companyName;

    /**
     * 项目经理（模糊查询）
     */
    private String projectManager;

    /**
     * 关键字搜索（模糊查询项目名称、城市、地址、公司名称、项目经理）
     */
    private String keyword;

    // 兼容前端字段名的getter/setter方法
    public String getProjectName() {
        return name;
    }

    public void setProjectName(String projectName) {
        this.name = projectName;
    }

    public String getProjectType() {
        return type;
    }

    public void setProjectType(String projectType) {
        this.type = projectType;
    }
}