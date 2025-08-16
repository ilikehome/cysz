package com.cysz.minimal.service;

import java.util.List;
import java.util.Map;

/**
 * 地址服务接口
 */
public interface AddressService {
    
    /**
     * 根据城市和关键字搜索地址
     * @param city 城市名称
     * @param keyword 搜索关键字
     * @return 地址搜索结果列表
     */
    List<Map<String, Object>> searchAddress(String city, String keyword);
    
    /**
     * 根据城市获取区域信息
     * @param city 城市名称
     * @return 区域名称
     */
    String getDistrictByCity(String city);
    
    /**
     * 根据地址获取坐标信息
     * @param address 地址
     * @return 坐标信息（经纬度）
     */
    Map<String, Double> getLocationByAddress(String address);
}