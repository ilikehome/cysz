package com.cysz.minimal.service.impl;

import com.cysz.minimal.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 地址服务实现类
 */
@Service
public class AddressServiceImpl implements AddressService {
    
    @Override
    public List<Map<String, Object>> searchAddress(String city, String keyword) {
        System.out.println("地址搜索 - 城市: " + city + ", 关键字: " + keyword);
        
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        // 根据关键字生成地址建议
        String[] suffixes = {"路", "街", "大道", "广场", "中心", "大厦", "商场", "公园", "小区"};
        String[] numbers = {"1号", "2号", "3号", "88号", "168号", "888号"};
        
        for (int i = 0; i < Math.min(5, suffixes.length); i++) {
            Map<String, Object> suggestion = new HashMap<>();
            String address = city + keyword + suffixes[i] + numbers[i % numbers.length];
            suggestion.put("address", address);
            suggestion.put("district", getDistrictByCity(city));
            suggestion.put("location", getLocationByAddress(address));
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }
    
    @Override
    public String getDistrictByCity(String city) {
        Map<String, String> cityDistrictMap = new HashMap<>();
        cityDistrictMap.put("北京市", "朝阳区");
        cityDistrictMap.put("上海市", "浦东新区");
        cityDistrictMap.put("广州市", "天河区");
        cityDistrictMap.put("深圳市", "南山区");
        cityDistrictMap.put("杭州市", "西湖区");
        cityDistrictMap.put("成都市", "锦江区");
        
        return cityDistrictMap.getOrDefault(city, "市中心区");
    }
    
    @Override
    public Map<String, Double> getLocationByAddress(String address) {
        Map<String, Double> location = new HashMap<>();
        // 模拟坐标，实际应该调用百度地图API
        location.put("lng", 116.404 + Math.random() * 0.1);
        location.put("lat", 39.915 + Math.random() * 0.1);
        return location;
    }
}