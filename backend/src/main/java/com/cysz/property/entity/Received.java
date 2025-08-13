package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 已收款实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("received")
public class Received {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 银行流水号
     */
    @TableField("bank_serial_number")
    private String bankSerialNumber;

    /**
     * 收款金额（元）
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 收款方式：1-银行转账，2-现金，3-支票，4-支付宝，5-微信，6-其他
     */
    @TableField("payment_method")
    private Integer paymentMethod;

    /**
     * 收款日期
     */
    @TableField("payment_date")
    private LocalDate paymentDate;

    /**
     * 付款人
     */
    @TableField("payer_name")
    private String payerName;

    /**
     * 付款账户
     */
    @TableField("payer_account")
    private String payerAccount;

    /**
     * 收款账户
     */
    @TableField("receiver_account")
    private String receiverAccount;

    /**
     * 账单类型：1-租金，2-物业费，3-水费，4-电费，5-其他
     */
    @TableField("bill_type")
    private Integer billType;

    /**
     * 匹配状态：1-未匹配，2-已匹配，3-部分匹配
     */
    @TableField("match_status")
    private Integer matchStatus;

    /**
     * 已匹配金额（元）
     */
    @TableField("matched_amount")
    private BigDecimal matchedAmount;

    /**
     * 未匹配金额（元）
     */
    @TableField("unmatched_amount")
    private BigDecimal unmatchedAmount;

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

    // 收款方式枚举
    public enum PaymentMethod {
        BANK_TRANSFER(1, "银行转账"),
        CASH(2, "现金"),
        CHECK(3, "支票"),
        ALIPAY(4, "支付宝"),
        WECHAT(5, "微信"),
        OTHER(6, "其他");

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

    // 匹配状态枚举
    public enum MatchStatus {
        UNMATCHED(1, "未匹配"),
        MATCHED(2, "已匹配"),
        PARTIAL_MATCHED(3, "部分匹配");

        private final Integer code;
        private final String name;

        MatchStatus(Integer code, String name) {
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