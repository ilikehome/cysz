package com.cysz.property.common;

/**
 * 响应码枚举
 *
 * @author CYSZ
 * @since 2024-01-01
 */
public enum ResultCode {

    // 通用响应码
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),

    // 认证授权相关
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    TOKEN_INVALID(4001, "Token无效"),
    TOKEN_EXPIRED(4002, "Token已过期"),
    LOGIN_FAILED(4003, "登录失败"),
    ACCOUNT_DISABLED(4004, "账户已禁用"),
    ACCOUNT_LOCKED(4005, "账户已锁定"),
    PASSWORD_ERROR(4006, "密码错误"),

    // 业务相关
    DATA_NOT_FOUND(5001, "数据不存在"),
    DATA_ALREADY_EXISTS(5002, "数据已存在"),
    DATA_IN_USE(5003, "数据正在使用中"),
    OPERATION_NOT_ALLOWED(5004, "操作不允许"),
    STATUS_ERROR(5005, "状态错误"),

    // 项目管理相关
    PROJECT_NOT_FOUND(6001, "项目不存在"),
    PROJECT_CODE_EXISTS(6002, "项目编码已存在"),
    PROJECT_NAME_EXISTS(6003, "项目名称已存在"),
    PROJECT_CREATE_FAILED(6004, "项目创建失败"),
    PROJECT_UPDATE_FAILED(6005, "项目更新失败"),
    PROJECT_DELETE_FAILED(6006, "项目删除失败"),
    PROJECT_HAS_BUILDINGS(6007, "项目下存在楼栋，无法删除"),

    // 楼栋管理相关
    BUILDING_NOT_FOUND(6101, "楼栋不存在"),
    BUILDING_NUMBER_EXISTS(6102, "楼栋编号已存在"),
    BUILDING_HAS_FLOORS(6103, "楼栋下存在楼层，无法删除"),

    // 楼层管理相关
    FLOOR_NOT_FOUND(6201, "楼层不存在"),
    FLOOR_NUMBER_EXISTS(6202, "楼层编号已存在"),
    FLOOR_SEQUENCE_EXISTS(6203, "楼层序号已存在"),
    FLOOR_HAS_UNITS(6204, "楼层下存在单元，无法删除"),

    // 单元管理相关
    UNIT_NOT_FOUND(6301, "单元不存在"),
    UNIT_NUMBER_EXISTS(6302, "单元编号已存在"),
    UNIT_HAS_CONTRACTS(6303, "单元存在合同，无法删除"),
    UNIT_STATUS_ERROR(6304, "单元状态错误"),

    // 租户管理相关
    TENANT_NOT_FOUND(6401, "租户不存在"),
    TENANT_CODE_EXISTS(6402, "租户编号已存在"),
    TENANT_NAME_EXISTS(6403, "租户名称已存在"),
    TENANT_HAS_CONTRACTS(6404, "租户存在合同，无法删除"),

    // 合同管理相关
    CONTRACT_NOT_FOUND(6501, "合同不存在"),
    CONTRACT_NUMBER_EXISTS(6502, "合同编号已存在"),
    CONTRACT_STATUS_ERROR(6503, "合同状态错误"),
    CONTRACT_DATE_ERROR(6504, "合同日期错误"),
    CONTRACT_HAS_RECEIVABLES(6505, "合同存在应收账款，无法删除"),

    // 应收账款相关
    RECEIVABLE_NOT_FOUND(6601, "应收账款不存在"),
    RECEIVABLE_BILL_NUMBER_EXISTS(6602, "账单编号已存在"),
    RECEIVABLE_STATUS_ERROR(6603, "应收账款状态错误"),
    RECEIVABLE_AMOUNT_ERROR(6604, "应收金额错误"),

    // 已收款相关
    RECEIVED_NOT_FOUND(6701, "已收款不存在"),
    RECEIVED_SERIAL_NUMBER_EXISTS(6702, "银行流水号已存在"),
    RECEIVED_MATCH_ERROR(6703, "收款匹配错误"),
    RECEIVED_AMOUNT_ERROR(6704, "收款金额错误"),

    // 用户管理相关
    USER_NOT_FOUND(7001, "用户不存在"),
    USERNAME_EXISTS(7002, "用户名已存在"),
    EMAIL_EXISTS(7003, "邮箱已存在"),
    PHONE_EXISTS(7004, "手机号已存在"),
    USER_STATUS_ERROR(7005, "用户状态错误"),

    // 角色管理相关
    ROLE_NOT_FOUND(7101, "角色不存在"),
    ROLE_CODE_EXISTS(7102, "角色编码已存在"),
    ROLE_NAME_EXISTS(7103, "角色名称已存在"),
    ROLE_HAS_USERS(7104, "角色下存在用户，无法删除"),

    // 权限管理相关
    PERMISSION_NOT_FOUND(7201, "权限不存在"),
    PERMISSION_CODE_EXISTS(7202, "权限编码已存在"),
    PERMISSION_HAS_CHILDREN(7203, "权限下存在子权限，无法删除"),

    // 部门管理相关
    DEPARTMENT_NOT_FOUND(7301, "部门不存在"),
    DEPARTMENT_CODE_EXISTS(7302, "部门编码已存在"),
    DEPARTMENT_HAS_CHILDREN(7303, "部门下存在子部门，无法删除"),
    DEPARTMENT_HAS_USERS(7304, "部门下存在用户，无法删除");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}