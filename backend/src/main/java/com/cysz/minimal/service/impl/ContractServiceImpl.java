package com.cysz.minimal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.entity.Contract;
import com.cysz.minimal.entity.ContractCommissionRules;
import com.cysz.minimal.exception.BusinessException;
import com.cysz.minimal.mapper.ContractMapper;
import com.cysz.minimal.mapper.ContractCommissionRulesMapper;
import com.cysz.minimal.service.ContractService;
import com.cysz.minimal.dto.contract.ContractCreateDTO;
import com.cysz.minimal.dto.contract.ContractUpdateDTO;
import com.cysz.minimal.vo.contract.ContractVO;
import com.cysz.minimal.vo.contract.CommissionRuleVO;
import com.cysz.minimal.dto.contract.CommissionRuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同服务实现类
 */
@Service
public class ContractServiceImpl implements ContractService {
    
    @Autowired
    private ContractMapper contractMapper;
    
    @Autowired
    private ContractCommissionRulesMapper contractCommissionRulesMapper;
    
    @Override
    public PageResult<ContractVO> getContractPage(int current, int size, String keyword, String contractType, String contractStatus) {
        Page<Contract> page = new Page<>(current, size);
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("contract_number", keyword)
                       .or().like("contract_name", keyword);
        }
        
        if (StringUtils.hasText(contractType)) {
            queryWrapper.eq("contract_type", contractType);
        }
        
        if (StringUtils.hasText(contractStatus)) {
            queryWrapper.eq("contract_status", contractStatus);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        IPage<Contract> contractPage = contractMapper.selectPage(page, queryWrapper);
        
        List<ContractVO> contractVOList = contractPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageResult<ContractVO>(contractVOList, contractPage.getTotal(), (int)contractPage.getCurrent(), (int)contractPage.getSize());
    }
    
    @Override
    public ContractVO getContractById(Long id) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }
        return convertToVO(contract);
    }
    
    @Override
    @Transactional
    public ContractVO createContract(ContractCreateDTO contractCreateDTO) {
        // 验证必填字段
        if (!StringUtils.hasText(contractCreateDTO.getContractNo())) {
            throw new BusinessException("合同编号不能为空");
        }
        if (!StringUtils.hasText(contractCreateDTO.getContractName())) {
            throw new BusinessException("合同名称不能为空");
        }
        
        // 检查合同编号是否重复
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_number", contractCreateDTO.getContractNo());
        if (contractMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("合同编号已存在");
        }
        
        // 创建合同
        Contract contract = new Contract();
        contract.setContractNo(contractCreateDTO.getContractNo());
        contract.setContractName(contractCreateDTO.getContractName());
        contract.setContractType(contractCreateDTO.getContractType());
        contract.setContractStatus(contractCreateDTO.getContractStatus());
        contract.setTenantId(contractCreateDTO.getTenantId());
        // unitIds字段已移除，不再处理单元ID
        contract.setStartDate(contractCreateDTO.getStartDate());
        contract.setEndDate(contractCreateDTO.getEndDate());
        contract.setRentAmount(contractCreateDTO.getPeriodRent());
        contract.setDepositAmount(contractCreateDTO.getDepositAmount());
        contract.setPaymentCycle(contractCreateDTO.getRentPeriodSetting());
        contract.setPaymentMethod(contractCreateDTO.getPaymentAccount());
        contract.setRemark(contractCreateDTO.getRemark());
        contract.setCreateTime(LocalDateTime.now());
        contract.setUpdateTime(LocalDateTime.now());
        
        contractMapper.insert(contract);
        
        // 保存佣金规则
        if (contractCreateDTO.getCommissionRules() != null && !contractCreateDTO.getCommissionRules().isEmpty()) {
            saveCommissionRulesFromDTO(contract.getId(), contractCreateDTO.getCommissionRules());
        }
        
        return convertToVO(contract);
    }
    
    @Override
    @Transactional
    public ContractVO updateContract(Long id, ContractUpdateDTO contractUpdateDTO) {
        Contract existingContract = contractMapper.selectById(id);
        if (existingContract == null) {
            throw new BusinessException("合同不存在");
        }
        
        // 检查合同编号是否重复（排除当前合同）
        if (StringUtils.hasText(contractUpdateDTO.getContractNo()) && 
            !contractUpdateDTO.getContractNo().equals(existingContract.getContractNo())) {
            QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("contract_number", contractUpdateDTO.getContractNo())
                       .ne("id", id);
            if (contractMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("合同编号已存在");
            }
        }
        
        // 更新合同信息
        if (StringUtils.hasText(contractUpdateDTO.getContractNo())) {
            existingContract.setContractNo(contractUpdateDTO.getContractNo());
        }
        if (StringUtils.hasText(contractUpdateDTO.getContractName())) {
            existingContract.setContractName(contractUpdateDTO.getContractName());
        }
        if (StringUtils.hasText(contractUpdateDTO.getContractType())) {
            existingContract.setContractType(contractUpdateDTO.getContractType());
        }
        if (StringUtils.hasText(contractUpdateDTO.getContractStatus())) {
            existingContract.setContractStatus(contractUpdateDTO.getContractStatus());
        }
        if (contractUpdateDTO.getTenantId() != null) {
            existingContract.setTenantId(contractUpdateDTO.getTenantId());
        }
        // unitIds字段已移除，不再处理单元ID
        if (contractUpdateDTO.getStartDate() != null) {
            existingContract.setStartDate(contractUpdateDTO.getStartDate());
        }
        if (contractUpdateDTO.getEndDate() != null) {
            existingContract.setEndDate(contractUpdateDTO.getEndDate());
        }
        if (contractUpdateDTO.getPeriodRent() != null) {
            existingContract.setRentAmount(contractUpdateDTO.getPeriodRent());
        }
        if (contractUpdateDTO.getDepositAmount() != null) {
            existingContract.setDepositAmount(contractUpdateDTO.getDepositAmount());
        }
        if (StringUtils.hasText(contractUpdateDTO.getRentPeriodSetting())) {
            existingContract.setPaymentCycle(contractUpdateDTO.getRentPeriodSetting());
        }
        if (StringUtils.hasText(contractUpdateDTO.getPaymentAccount())) {
            existingContract.setPaymentMethod(contractUpdateDTO.getPaymentAccount());
        }
        if (contractUpdateDTO.getRemark() != null) {
            existingContract.setRemark(contractUpdateDTO.getRemark());
        }
        existingContract.setUpdateTime(LocalDateTime.now());
        
        contractMapper.updateById(existingContract);
        
        return convertToVO(existingContract);
    }
    
    @Override
    @Transactional
    public void deleteContract(Long id) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException("合同不存在");
        }
        
        // 删除合同关联的佣金规则
        QueryWrapper<ContractCommissionRules> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_id", id);
        contractCommissionRulesMapper.delete(queryWrapper);
        
        // 删除合同
        contractMapper.deleteById(id);
    }
    
    @Override
    public List<ContractCommissionRules> getCommissionRulesByContractId(Long contractId) {
        return contractCommissionRulesMapper.selectByContractId(contractId);
    }
    
    @Override
    @Transactional
    public void saveCommissionRules(Long contractId, List<ContractCommissionRules> commissionRules) {
        // 先删除原有的佣金规则
        QueryWrapper<ContractCommissionRules> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_id", contractId);
        contractCommissionRulesMapper.delete(queryWrapper);
        
        // 保存新的佣金规则
        if (commissionRules != null && !commissionRules.isEmpty()) {
            for (ContractCommissionRules rule : commissionRules) {
                rule.setContractId(contractId);
                rule.setCreateTime(LocalDateTime.now());
                rule.setUpdateTime(LocalDateTime.now());
                contractCommissionRulesMapper.insert(rule);
            }
        }
    }
    
    /**
     * 从DTO保存佣金规则
     */
    private void saveCommissionRulesFromDTO(Long contractId, List<CommissionRuleDTO> commissionRuleDTOs) {
        List<ContractCommissionRules> commissionRules = commissionRuleDTOs.stream()
                .map(dto -> {
                    ContractCommissionRules rule = new ContractCommissionRules();
                    rule.setContractId(contractId);
                    rule.setMinAmount(dto.getMinAmount());
                    rule.setMaxAmount(dto.getMaxAmount());
                    rule.setCommissionRate(dto.getCommissionRate());
                    rule.setCreateTime(LocalDateTime.now());
                    rule.setUpdateTime(LocalDateTime.now());
                    return rule;
                })
                .collect(Collectors.toList());
        
        saveCommissionRules(contractId, commissionRules);
    }
    
    /**
     * 将Contract实体转换为ContractVO
     */
    private ContractVO convertToVO(Contract contract) {
        ContractVO vo = new ContractVO();
        vo.setId(contract.getId());
        vo.setContractNumber(contract.getContractNo());
        vo.setContractName(contract.getContractName());
        vo.setContractType(contract.getContractType());
        vo.setContractStatus(contract.getContractStatus());
        vo.setTenantId(contract.getTenantId());
        vo.setStartDate(contract.getStartDate());
        vo.setEndDate(contract.getEndDate());
        vo.setRentAmount(contract.getRentAmount());
        vo.setDepositAmount(contract.getDepositAmount());
        vo.setPaymentCycle(contract.getPaymentCycle());
        vo.setPaymentMethod(contract.getPaymentMethod());
        vo.setRemark(contract.getRemark());
        vo.setCreateTime(contract.getCreateTime());
        vo.setUpdateTime(contract.getUpdateTime());
        
        // 加载佣金规则
        List<ContractCommissionRules> commissionRules = getCommissionRulesByContractId(contract.getId());
        List<CommissionRuleVO> commissionRuleVOs = commissionRules.stream()
                .map(rule -> {
                    CommissionRuleVO ruleVO = new CommissionRuleVO();
                    ruleVO.setId(rule.getId());
                    ruleVO.setContractId(rule.getContractId());
                    ruleVO.setMinAmount(rule.getMinAmount());
                    ruleVO.setMaxAmount(rule.getMaxAmount());
                    ruleVO.setCommissionRate(rule.getCommissionRate());
                    return ruleVO;
                })
                .collect(Collectors.toList());
        vo.setCommissionRules(commissionRuleVOs);
        
        return vo;
    }
}