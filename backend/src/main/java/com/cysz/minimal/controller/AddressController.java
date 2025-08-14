package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 地址搜索控制器
 */
@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController {
    
    /**
     * 根据城市和关键字搜索地址
     */
    @GetMapping("/search")
    public Map<String, Object> searchAddress(
            @RequestParam String city,
            @RequestParam String keyword) {
        
        System.out.println("地址搜索 - 城市: " + city + ", 关键字: " + keyword);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 模拟地址搜索结果
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
            
            response.put("code", 200);
            response.put("message", "搜索成功");
            response.put("data", suggestions);
            
        } catch (Exception e) {
            System.err.println("地址搜索失败: " + e.getMessage());
            response.put("code", 500);
            response.put("message", "搜索失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        
        return response;
    }
    
    /**
     * 根据城市获取区域信息
     */
    private String getDistrictByCity(String city) {
        Map<String, String> cityDistrictMap = new HashMap<>();
        cityDistrictMap.put("北京市", "朝阳区");
        cityDistrictMap.put("上海市", "浦东新区");
        cityDistrictMap.put("广州市", "天河区");
        cityDistrictMap.put("深圳市", "南山区");
        cityDistrictMap.put("杭州市", "西湖区");
        cityDistrictMap.put("成都市", "锦江区");
        
        return cityDistrictMap.getOrDefault(city, "市中心区");
    }
    
    /**
     * 根据地址获取坐标信息（模拟）
     */
    private Map<String, Double> getLocationByAddress(String address) {
        Map<String, Double> location = new HashMap<>();
        // 模拟坐标，实际应该调用百度地图API
        location.put("lng", 116.404 + Math.random() * 0.1);
        location.put("lat", 39.915 + Math.random() * 0.1);
        return location;
    }
}