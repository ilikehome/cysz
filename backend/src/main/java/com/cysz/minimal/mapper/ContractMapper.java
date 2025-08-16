package com.cysz.minimal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cysz.minimal.entity.Contract;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同Mapper接口
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
    
}