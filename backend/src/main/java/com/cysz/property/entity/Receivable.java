package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收账款实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("receivables")
public class Receivable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账单编号
     */
    @TableField("bill_number")
    private String billNumber;

    /**
     * 合同ID
     */
    @TableField("contract_id")
    private Long contractId;

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
     * 账单类型：1-租金，2-物业费，3-水费，4-电费，5-其他
     */
    @TableField("bill_type")
    private Integer billType;

    /**
     * 应收金额（元）
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 已收金额（元）
     */
    @TableField("received_amount")
    private BigDecimal receivedAmount;

    /**
     * 未收金额（元）
     */
    @TableField("outstanding_amount")
    private BigDecimal outstandingAmount;

    /**
     * 账单日期
     */
    @TableField("bill_date")
    private LocalDate billDate;

    /**
     * 到期日期
     */
    @TableField("due_date")
    private LocalDate dueDate;

    /**
     * 状态：1-未收，2-部分收款，3-已收款，4-逾期
     */
    @TableField("status")
    private Integer status;

    /**
     * 逾期天数
     */
    @TableField("overdue_days")
    private Integer overdueDays;

    /**
     * 滞纳金（元）
     */
    @TableField("late_fee")
    private BigDecimal lateFee;

    /**
     * 备注
     */
    @TableField("description")
    private String description;

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

    // 账单类型枚举
    public enum BillType {
        RENT(1, "租金"),
        PROPERTY_FEE(2, "物业费"),
        WATER_FEE(3, "水费"),
        ELECTRICITY_FEE(4, "电费"),
        OTHER(5, "其他");

        private final Integer code;
        private final String name;

        BillType(Integer code, String name) {
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

    // 状态枚举
    public enum Status {
        UNPAID(1, "未收"),
        PARTIAL_PAID(2, "部分收款"),
        PAID(3, "已收款"),
        OVERDUE(4, "逾期");

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
}