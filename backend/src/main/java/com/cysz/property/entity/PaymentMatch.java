package com.cysz.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收款匹配实体类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("payment_matches")
public class PaymentMatch {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应收账款ID
     */
    @TableField("receivable_id")
    private Long receivableId;

    /**
     * 已收款ID
     */
    @TableField("received_id")
    private Long receivedId;

    /**
     * 匹配金额（元）
     */
    @TableField("matched_amount")
    private BigDecimal matchedAmount;

    /**
     * 匹配类型：1-自动匹配，2-手动匹配
     */
    @TableField("match_type")
    private Integer matchType;

    /**
     * 匹配时间
     */
    @TableField("match_date")
    private LocalDateTime matchDate;

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

    // 匹配类型枚举
    public enum MatchType {
        AUTO(1, "自动匹配"),
        MANUAL(2, "手动匹配");

        private final Integer code;
        private final String name;

        MatchType(Integer code, String name) {
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