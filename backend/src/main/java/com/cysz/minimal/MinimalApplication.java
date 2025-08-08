package com.cysz.minimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 最小化应用启动类
 */
@SpringBootApplication
public class MinimalApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
        
        System.out.println("========================================");
        System.out.println("最小化后端服务启动成功！");
        System.out.println("访问地址: http://localhost:8080/api");
        System.out.println("测试接口: http://localhost:8080/api/test/hello");
        System.out.println("认证接口: http://localhost:8080/api/auth/login");
        System.out.println("========================================");
    }
}