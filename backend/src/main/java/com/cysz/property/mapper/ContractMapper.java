package com.cysz.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.property.entity.Contract;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 合同Mapper接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */

public interface ContractMapper extends BaseMapper<Contract> {

    /**
     * 分页查询合同列表
     *
     * @param page 分页参数
     * @param contractNumber 合同编号
     * @param contractName 合同名称
     * @param contractType 合同类型
     * @param status 合同状态
     * @param tenantName 租户名称
     * @param projectId 项目ID
     * @return 合同列表
     */
    Page<Map<String, Object>> selectContractPage(Page<Map<String, Object>> page,
                                                 @Param("contractNumber") String contractNumber,
                                                 @Param("contractName") String contractName,
                                                 @Param("contractType") Integer contractType,
                                                 @Param("status") Integer status,
                                                 @Param("tenantName") String tenantName,
                                                 @Param("projectId") Long projectId);

    /**
     * 获取合同统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT COUNT(*) as totalContracts, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as activeContracts, " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) as expiredContracts, " +
            "SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END) as terminatedContracts " +
            "FROM contracts WHERE deleted = 0")
    Map<String, Object> getContractStatistics();

    /**
     * 获取即将到期的合同
     *
     * @param days 天数
     * @return 即将到期的合同列表
     */
    List<Map<String, Object>> getExpiringContracts(@Param("days") Integer days);

    /**
     * 获取合同收入统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 收入统计
     */
    List<Map<String, Object>> getContractRevenueStatistics(@Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);

    /**
     * 根据状态统计合同数量
     *
     * @return 状态统计
     */
    @Select("SELECT status, COUNT(*) as count " +
            "FROM contracts WHERE deleted = 0 " +
            "GROUP BY status")
    List<Map<String, Object>> getContractCountByStatus();

    /**
     * 根据类型统计合同数量
     *
     * @return 类型统计
     */
    @Select("SELECT contract_type, COUNT(*) as count " +
            "FROM contracts WHERE deleted = 0 " +
            "GROUP BY contract_type")
    List<Map<String, Object>> getContractCountByType();

    /**
     * 获取合同详情（包含关联信息）
     *
     * @param contractId 合同ID
     * @return 合同详情
     */
    Map<String, Object> getContractDetail(@Param("contractId") Long contractId);

    /**
     * 获取合同关联的单元信息
     *
     * @param contractId 合同ID
     * @return 单元信息列表
     */
    List<Map<String, Object>> getContractUnits(@Param("contractId") Long contractId);

    /**
     * 检查合同编号是否存在
     *
     * @param contractNumber 合同编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM contracts " +
            "WHERE contract_number = #{contractNumber} " +
            "AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    int checkContractNumberExists(@Param("contractNumber") String contractNumber,
                                 @Param("excludeId") Long excludeId);

    /**
     * 获取合同生成进度
     *
     * @param batchId 批次ID
     * @return 生成进度
     */
    Map<String, Object> getContractGenerationProgress(@Param("batchId") String batchId);

    /**
     * 获取合同生成历史
     *
     * @param page 分页参数
     * @return 生成历史
     */
    Page<Map<String, Object>> getContractGenerationHistory(Page<Map<String, Object>> page);

    /**
     * 验证合同数据
     *
     * @param contractId 合同ID
     * @return 验证结果
     */
    Map<String, Object> validateContractData(@Param("contractId") Long contractId);

}