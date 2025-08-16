package com.cysz.minimal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cysz.minimal.entity.ContractCommissionRules;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 合同佣金规则Mapper接口
 */
@Mapper
public interface ContractCommissionRulesMapper extends BaseMapper<ContractCommissionRules> {
    
    /**
     * 根据合同ID查询佣金规则列表
     * @param contractId 合同ID
     * @return 佣金规则列表
     */
    @Select("SELECT * FROM contract_commission_rules WHERE contract_id = #{contractId}")
    List<ContractCommissionRules> selectByContractId(Long contractId);
}