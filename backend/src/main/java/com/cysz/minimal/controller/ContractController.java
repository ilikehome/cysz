package com.cysz.minimal.controller;


import com.cysz.minimal.entity.Contract;
import com.cysz.minimal.entity.ContractCommissionRules;
import com.cysz.minimal.service.ContractService;
import com.cysz.minimal.common.Result;
import com.cysz.minimal.common.PageResult;
import com.cysz.minimal.vo.contract.ContractVO;
import com.cysz.minimal.vo.contract.CommissionRuleVO;
import com.cysz.minimal.dto.contract.ContractCreateDTO;
import com.cysz.minimal.dto.contract.ContractUpdateDTO;
import com.cysz.minimal.dto.contract.CommissionRuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * 分页查询合同列表
     */
    @GetMapping("/page")
    public Result<PageResult<ContractVO>> getContracts(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String tenantName,
            @RequestParam(required = false) String contractStatus) {
        
        // 构建搜索关键词
        String keyword = null;
        if (contractNo != null && !contractNo.trim().isEmpty()) {
            keyword = contractNo;
        } else if (tenantName != null && !tenantName.trim().isEmpty()) {
            keyword = tenantName;
        }
        
        PageResult<ContractVO> pageResult = contractService.getContractPage(current, size, keyword, null, contractStatus);
        return Result.success(pageResult);
    }

    /**
     * 根据ID获取合同详情
     */
    @GetMapping("/{id}")
    public Result<ContractVO> getContract(@PathVariable Long id) {
        ContractVO contractVO = contractService.getContractById(id);
        if (contractVO == null) {
            return Result.error("合同不存在");
        }
        return Result.success(contractVO);
    }

    /**
     * 创建合同
     */
    @PostMapping
    public Result<ContractVO> createContract(@RequestBody ContractCreateDTO contractCreateDTO) {
        ContractVO contractVO = contractService.createContract(contractCreateDTO);
        return Result.success(contractVO);
    }

    /**
     * 更新合同
     */
    @PutMapping("/{id}")
    public Result<ContractVO> updateContract(@PathVariable Long id, @RequestBody ContractUpdateDTO contractUpdateDTO) {
        ContractVO contractVO = contractService.updateContract(id, contractUpdateDTO);
        return Result.success(contractVO);
    }

    /**
     * 删除合同
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteContract(@PathVariable Long id) {
        try {
            contractService.deleteContract(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error("删除失败");
        }
    }




}