package com.cysz.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cysz.property.common.PageQuery;
import com.cysz.property.common.PageResult;
import com.cysz.property.entity.Contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 合同Service接口
 *
 * @author CYSZ
 * @since 2025-01-14
 */
public interface ContractService extends IService<Contract> {

    /**
     * 分页查询合同列表
     *
     * @param pageQuery 分页查询参数
     * @param number 合同编号
     * @param name 合同名称
     * @param type 合同类型
     * @param status 合同状态
     * @param tenantName 租户名称
     * @param projectId 项目ID
     * @return 合同列表
     */
    PageResult<Map<String, Object>> getContractPage(PageQuery pageQuery,
                                                    String number,
                                                    String name,
                                                    Integer type,
                                                    Integer status,
                                                    String tenantName,
                                                    Long projectId);

    /**
     * 获取合同详细信息
     *
     * @param contractId 合同ID
     * @return 合同详细信息
     */
    Map<String, Object> getContractDetail(Long contractId);

    /**
     * 创建合同
     *
     * @param contract 合同信息
     * @return 创建结果
     */
    boolean createContract(Contract contract);

    /**
     * 更新合同
     *
     * @param contract 合同信息
     * @return 更新结果
     */
    boolean updateContract(Contract contract);

    /**
     * 删除合同
     *
     * @param contractId 合同ID
     * @return 删除结果
     */
    boolean deleteContract(Long contractId);

    /**
     * 批量删除合同
     *
     * @param contractIds 合同ID列表
     * @return 删除结果
     */
    boolean batchDeleteContracts(List<Long> contractIds);

    /**
     * 签署合同
     *
     * @param contractId 合同ID
     * @param signDate 签署日期
     * @return 签署结果
     */
    boolean signContract(Long contractId, LocalDate signDate);

    /**
     * 生效合同
     *
     * @param contractId 合同ID
     * @param effectiveDate 生效日期
     * @return 生效结果
     */
    boolean activateContract(Long contractId, LocalDate effectiveDate);

    /**
     * 终止合同
     *
     * @param contractId 合同ID
     * @param terminateDate 终止日期
     * @param reason 终止原因
     * @return 终止结果
     */
    boolean terminateContract(Long contractId, LocalDate terminateDate, String reason);

    /**
     * 续签合同
     *
     * @param contractId 合同ID
     * @param newEndDate 新的到期日期
     * @param newRent 新租金
     * @return 续签结果
     */
    boolean renewContract(Long contractId, LocalDate newEndDate, java.math.BigDecimal newRent);

    /**
     * 获取合同统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getContractStatistics();

    /**
     * 根据状态统计合同数量
     *
     * @return 状态统计
     */
    List<Map<String, Object>> getContractCountByStatus();

    /**
     * 根据类型统计合同数量
     *
     * @return 类型统计
     */
    List<Map<String, Object>> getContractCountByType();

    /**
     * 获取即将到期的合同
     *
     * @param days 天数
     * @return 即将到期的合同列表
     */
    List<Map<String, Object>> getExpiringContracts(Integer days);

    /**
     * 获取合同收益统计
     *
     * @param year 年份
     * @return 收益统计
     */
    Map<String, Object> getContractRevenueStatistics(Integer year);

    /**
     * 验证合同编号是否存在
     *
     * @param number 合同编号
     * @param excludeId 排除的合同ID
     * @return 是否存在
     */
    boolean checkNumberExists(String number, Long excludeId);

    /**
     * 获取合同关联的单元信息
     *
     * @param contractId 合同ID
     * @return 单元信息列表
     */
    List<Map<String, Object>> getContractUnits(Long contractId);

    /**
     * 获取合同应收账款列表
     *
     * @param contractId 合同ID
     * @return 应收账款列表
     */
    List<Map<String, Object>> getContractReceivables(Long contractId);

    /**
     * 获取合同已收款列表
     *
     * @param contractId 合同ID
     * @return 已收款列表
     */
    List<Map<String, Object>> getContractReceived(Long contractId);

    /**
     * 生成合同应收账款
     *
     * @param contractId 合同ID
     * @param generateType 生成类型
     * @param periods 期数
     * @return 生成结果
     */
    Map<String, Object> generateContractReceivables(Long contractId, Integer generateType, Integer periods);

    /**
     * 获取合同生成进度
     *
     * @param contractId 合同ID
     * @return 生成进度
     */
    Map<String, Object> getContractGenerationProgress(Long contractId);

    /**
     * 获取合同历史记录
     *
     * @param contractId 合同ID
     * @return 历史记录
     */
    List<Map<String, Object>> getContractHistory(Long contractId);

    /**
     * 验证合同数据
     *
     * @param contract 合同信息
     * @return 验证结果
     */
    Map<String, Object> validateContractData(Contract contract);

    /**
     * 导出合同数据
     *
     * @param number 合同编号
     * @param name 合同名称
     * @param type 合同类型
     * @param status 合同状态
     * @param tenantName 租户名称
     * @param projectId 项目ID
     * @return 导出数据
     */
    List<Map<String, Object>> exportContractData(String number,
                                                 String name,
                                                 Integer type,
                                                 Integer status,
                                                 String tenantName,
                                                 Long projectId);

    /**
     * 生成合同报表
     *
     * @param reportType 报表类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param projectId 项目ID
     * @return 报表数据
     */
    Map<String, Object> generateContractReport(String reportType,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               Long projectId);

}