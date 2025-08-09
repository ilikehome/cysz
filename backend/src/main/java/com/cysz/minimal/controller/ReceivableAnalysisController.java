package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 账款分析控制器
 * 提供账款数据的统计分析功能
 */
@RestController
@RequestMapping("/receivable-analysis")
@CrossOrigin(origins = "*")
public class ReceivableAnalysisController {

    /**
     * 获取账款概览数据
     */
    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();
        
        // 模拟数据 - 实际应从数据库查询
        Map<String, Object> data = new HashMap<>();
        data.put("totalReceivable", 2850000.00);
        data.put("totalReceived", 2100000.00);
        data.put("totalPending", 750000.00);
        data.put("collectionRate", 73.7);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 获取收款情况分布数据（饼图）
     */
    @GetMapping("/pie-chart")
    public Map<String, Object> getPieChartData(@RequestParam(required = false) String startMonth,
                                               @RequestParam(required = false) String endMonth) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        Map<String, Object> received = new HashMap<>();
        received.put("name", "已收金额");
        received.put("value", 2100000);
        received.put("itemStyle", Map.of("color", "#67C23A"));
        
        Map<String, Object> pending = new HashMap<>();
        pending.put("name", "未收金额");
        pending.put("value", 750000);
        pending.put("itemStyle", Map.of("color", "#E6A23C"));
        
        data.add(received);
        data.add(pending);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 获取收款趋势数据（折线图）
     */
    @GetMapping("/trend-chart")
    public Map<String, Object> getTrendChartData() {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> data = new HashMap<>();
        data.put("months", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月"));
        data.put("receivable", Arrays.asList(450000, 520000, 480000, 600000, 550000, 650000));
        data.put("received", Arrays.asList(420000, 480000, 450000, 580000, 520000, 600000));
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 获取项目收款对比数据（柱状图）
     */
    @GetMapping("/project-chart")
    public Map<String, Object> getProjectChartData(@RequestParam(required = false) List<Integer> projectIds) {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> data = new HashMap<>();
        data.put("projects", Arrays.asList("万达广场", "中关村写字楼", "阳光公寓"));
        data.put("receivable", Arrays.asList(1200000, 800000, 350000));
        data.put("received", Arrays.asList(900000, 650000, 280000));
        data.put("pending", Arrays.asList(300000, 150000, 70000));
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 获取逾期账款预警数据
     */
    @GetMapping("/overdue-warning")
    public Map<String, Object> getOverdueWarning() {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("tenantName", "广州设计工作室");
        item1.put("contractNo", "HT003");
        item1.put("amount", 8000.00);
        item1.put("overdueDays", 15);
        
        data.add(item1);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 获取即将到期提醒数据
     */
    @GetMapping("/upcoming-warning")
    public Map<String, Object> getUpcomingWarning() {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("tenantName", "北京科技有限公司");
        item1.put("contractNo", "HT001");
        item1.put("amount", 50000.00);
        item1.put("daysUntilDue", 5);
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 2);
        item2.put("tenantName", "上海贸易公司");
        item2.put("contractNo", "HT002");
        item2.put("amount", 35000.00);
        item2.put("daysUntilDue", 12);
        
        data.add(item1);
        data.add(item2);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 获取项目列表
     */
    @GetMapping("/projects")
    public Map<String, Object> getProjects() {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        Map<String, Object> project1 = new HashMap<>();
        project1.put("id", 1);
        project1.put("name", "万达广场");
        
        Map<String, Object> project2 = new HashMap<>();
        project2.put("id", 2);
        project2.put("name", "中关村写字楼");
        
        Map<String, Object> project3 = new HashMap<>();
        project3.put("id", 3);
        project3.put("name", "阳光公寓");
        
        data.add(project1);
        data.add(project2);
        data.add(project3);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }
}