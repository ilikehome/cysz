package com.cysz.minimal.service;

import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.entity.Contract;
import com.cysz.minimal.entity.ContractCommissionRules;
import com.cysz.minimal.controller.ContractController.ContractCreateDTO;
import com.cysz.minimal.controller.ContractController.ContractUpdateDTO;
import com.cysz.minimal.controller.ContractController.ContractVO;

import java.util.List;

/**
 * 合同服务接口
 */
public interface ContractService {
    
    /**
     * 分页查询合同信息
     * @param current 当前页
     * @param size 每页大小
     * @param keyword 关键词搜索
     * @param contractType 合同类型
     * @param contractStatus 合同状态
     * @return 分页结果
     */
    PageResult<ContractVO> getContractPage(int current, int size, String keyword, String contractType, String contractStatus);
    
    /**
     * 根据ID查询合同详情
     * @param id 合同ID
     * @return 合同详情
     */
    ContractVO getContractById(Long id);
    
    /**
     * 创建合同
     * @param contractCreateDTO 合同创建DTO
     * @return 创建的合同
     */
    ContractVO createContract(ContractCreateDTO contractCreateDTO);
    
    /**
     * 更新合同
     * @param id 合同ID
     * @param contractUpdateDTO 合同更新DTO
     * @return 更新后的合同
     */
    ContractVO updateContract(Long id, ContractUpdateDTO contractUpdateDTO);
    
    /**
     * 删除合同
     * @param id 合同ID
     */
    void deleteContract(Long id);
    
    /**
     * 根据合同ID查询佣金规则列表
     * @param contractId 合同ID
     * @return 佣金规则列表
     */
    List<ContractCommissionRules> getCommissionRulesByContractId(Long contractId);
    
    /**
     * 保存合同佣金规则
     * @param contractId 合同ID
     * @param commissionRules 佣金规则列表
     */
    void saveCommissionRules(Long contractId, List<ContractCommissionRules> commissionRules);
}