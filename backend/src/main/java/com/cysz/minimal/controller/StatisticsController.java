package com.cysz.minimal.controller;

import com.cysz.minimal.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取仪表盘概览数据
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardData() {
        return statisticsService.getDashboardData();
    }

    /**
     * 获取项目统计数据
     */
    @GetMapping("/project")
    public Map<String, Object> getProjectStatistics() {
        return statisticsService.getProjectStatistics();
    }

    /**
     * 获取租户统计数据
     */
    @GetMapping("/tenant")
    public Map<String, Object> getTenantStatistics() {
        return statisticsService.getTenantStatistics();
    }

    /**
     * 获取单元统计数据
     */
    @GetMapping("/unit")
    public Map<String, Object> getUnitStatistics() {
        return statisticsService.getUnitStatistics();
    }

    /**
     * 获取合同统计数据
     */
    @GetMapping("/contract")
    public Map<String, Object> getContractStatistics() {
        return statisticsService.getContractStatistics();
    }

    /**
     * 获取财务统计数据
     */
    @GetMapping("/financial")
    public Map<String, Object> getFinancialStatistics() {
        return statisticsService.getFinancialStatistics();
    }

    /**
     * 获取运营分析数据
     */
    @GetMapping("/operation")
    public Map<String, Object> getOperationAnalysis() {
        return statisticsService.getOperationAnalysis();
    }
}