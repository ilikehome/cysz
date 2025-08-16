package com.cysz.minimal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cysz.minimal.entity.Building;
import org.apache.ibatis.annotations.Mapper;

/**
 * 楼栋Mapper接口
 */
@Mapper
public interface BuildingMapper extends BaseMapper<Building> {
}