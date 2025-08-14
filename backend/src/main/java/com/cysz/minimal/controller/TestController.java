package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
    
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Hello from CYSZ Backend!");
        result.put("data", "系统运行正常");
        return result;
    }
}