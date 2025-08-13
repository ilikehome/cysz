package com.cysz.property;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 物业管理系统启动类
 *
 * @author CYSZ
 * @since 2024-01-01
 */
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class
})
@MapperScan("com.cysz.property.mapper")
public class PropertyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyApplication.class, args);
        System.out.println("\n===========================================\n" +
                "    物业管理系统启动成功！\n" +
                "    接口文档地址: http://localhost:8080/doc.html\n" +
                "    Swagger地址: http://localhost:8080/swagger-ui.html\n" +
                "===========================================\n");
    }
}