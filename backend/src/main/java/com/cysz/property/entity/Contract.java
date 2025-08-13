package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("contracts")
public class Contract {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同编号
     */
    @TableField("number")
    private String number;

    /**
     * 合同名称
     */
    @TableField("name")
    private String name;

    /**
     * 合同类型：1-租赁合同，2-服务合同，3-其他
     */
    @TableField("type")
    private Integer type;

    /**
     * 合同状态：1-草稿，2-待签署，3-已签署，4-生效中，5-已到期，6-已终止
     */
    @TableField("status")
    private Integer status;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 合同开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 租金（元/月）
     */
    @TableField("rent")
    private BigDecimal rent;

    /**
     * 押金（元）
     */
    @TableField("deposit")
    private BigDecimal deposit;

    /**
     * 付款周期：1-月付，2-季付，3-半年付，4-年付
     */
    @TableField("payment_cycle")
    private Integer paymentCycle;

    /**
     * 付款方式：1-银行转账，2-现金，3-支票，4-其他
     */
    @TableField("payment_method")
    private Integer paymentMethod;

    /**
     * 签署日期
     */
    @TableField("sign_date")
    private LocalDate signDate;

    /**
     * 生效日期
     */
    @TableField("effective_date")
    private LocalDate effectiveDate;

    /**
     * 终止日期
     */
    @TableField("terminate_date")
    private LocalDate terminateDate;

    /**
     * 终止原因
     */
    @TableField("terminate_reason")
    private String terminateReason;

    /**
     * 自动续约：0-否，1-是
     */
    @TableField("auto_renew")
    private Integer autoRenew;

    /**
     * 续约期限（月）
     */
    @TableField("renew_period")
    private Integer renewPeriod;

    /**
     * 合同内容
     */
    @TableField("content")
    private String content;

    /**
     * 附件
     */
    @TableField("attachment")
    private String attachment;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // 合同类型枚举
    public enum Type {
        LEASE(1, "租赁合同"),
        SERVICE(2, "服务合同"),
        OTHER(3, "其他");

        private final Integer code;
        private final String name;

        Type(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 合同状态枚举
    public enum Status {
        DRAFT(1, "草稿"),
        PENDING_SIGN(2, "待签署"),
        SIGNED(3, "已签署"),
        EFFECTIVE(4, "生效中"),
        EXPIRED(5, "已到期"),
        TERMINATED(6, "已终止");

        private final Integer code;
        private final String name;

        Status(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 付款周期枚举
    public enum PaymentCycle {
        MONTHLY(1, "月付"),
        QUARTERLY(2, "季付"),
        SEMI_ANNUALLY(3, "半年付"),
        ANNUALLY(4, "年付");

        private final Integer code;
        private final String name;

        PaymentCycle(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 付款方式枚举
    public enum PaymentMethod {
        BANK_TRANSFER(1, "银行转账"),
        CASH(2, "现金"),
        CHECK(3, "支票"),
        OTHER(4, "其他");

        private final Integer code;
        private final String name;

        PaymentMethod(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}