package com.cysz.minimal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cysz.minimal.entity.ReceivableAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应收账款Mapper接口
 */
@Mapper
public interface ReceivableAccountMapper extends BaseMapper<ReceivableAccount> {
    
}