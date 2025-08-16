package com.cysz.minimal.controller;

import com.cysz.minimal.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 地址搜索控制器
 */
@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController {
    
    @Autowired
    private AddressService addressService;
    
    /**
     * 根据城市和关键字搜索地址
     */
    @GetMapping("/search")
    public Map<String, Object> searchAddress(
            @RequestParam String city,
            @RequestParam String keyword) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> suggestions = addressService.searchAddress(city, keyword);
            
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
}