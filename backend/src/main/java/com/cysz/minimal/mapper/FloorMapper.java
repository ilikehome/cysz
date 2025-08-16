package com.cysz.minimal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cysz.minimal.entity.Floor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 楼层Mapper接口
 */
@Mapper
public interface FloorMapper extends BaseMapper<Floor> {
}