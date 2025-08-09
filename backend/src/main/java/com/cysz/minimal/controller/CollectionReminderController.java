package com.cysz.minimal.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 自动催缴控制器
 * 提供催缴设置、记录查询、手动催缴等功能
 */
@RestController
@RequestMapping("/collection-reminder")
@CrossOrigin(origins = "*")
public class CollectionReminderController {

    /**
     * 获取催缴设置
     */
    @GetMapping("/settings")
    public Map<String, Object> getSettings() {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> settings = new HashMap<>();
        settings.put("enabled", true);
        settings.put("triggerDays", Arrays.asList(7, 15, 30)); // 逾期天数触发
        settings.put("maxReminders", 3); // 最大催缴次数
        settings.put("reminderInterval", 7); // 催缴间隔天数
        settings.put("workingHours", Map.of("start", "09:00", "end", "18:00"));
        settings.put("workingDays", Arrays.asList(1, 2, 3, 4, 5)); // 工作日
        
        // 短信模板
        List<Map<String, Object>> templates = new ArrayList<>();
        Map<String, Object> template1 = new HashMap<>();
        template1.put("id", 1);
        template1.put("name", "首次催缴模板");
        template1.put("content", "尊敬的{tenantName}，您的租金{amount}元已逾期{days}天，请及时缴纳。联系电话：{phone}");
        template1.put("type", "first");
        templates.add(template1);
        
        Map<String, Object> template2 = new HashMap<>();
        template2.put("id", 2);
        template2.put("name", "二次催缴模板");
        template2.put("content", "尊敬的{tenantName}，您的租金{amount}元已逾期{days}天，这是第二次提醒，请尽快缴纳避免影响信用。");
        template2.put("type", "second");
        templates.add(template2);
        
        Map<String, Object> template3 = new HashMap<>();
        template3.put("id", 3);
        template3.put("name", "最终催缴模板");
        template3.put("content", "尊敬的{tenantName}，您的租金{amount}元已严重逾期{days}天，请立即缴纳，否则将采取法律措施。");
        template3.put("type", "final");
        templates.add(template3);
        
        settings.put("smsTemplates", templates);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", settings);
        
        return result;
    }

    /**
     * 更新催缴设置
     */
    @PostMapping("/settings")
    public Map<String, Object> updateSettings(@RequestBody Map<String, Object> settings) {
        Map<String, Object> result = new HashMap<>();
        
        // 这里应该保存到数据库
        // settingsService.updateSettings(settings);
        
        result.put("code", 200);
        result.put("message", "设置保存成功");
        
        return result;
    }

    /**
     * 获取催缴记录列表
     */
    @GetMapping("/records")
    public Map<String, Object> getRecords(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 模拟数据
        List<Map<String, Object>> records = new ArrayList<>();
        
        for (int i = 1; i <= 15; i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("id", i);
            record.put("tenantName", "租户" + i);
            record.put("contractNo", "HT" + String.format("%03d", i));
            record.put("phone", "138****" + String.format("%04d", 1000 + i));
            record.put("amount", 5000.00 + i * 100);
            record.put("overdueDays", 10 + i);
            record.put("reminderCount", (i % 3) + 1);
            record.put("lastReminderTime", LocalDateTime.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            record.put("status", i % 4 == 0 ? "已缴费" : (i % 3 == 0 ? "已回复" : "已发送"));
            record.put("smsContent", "尊敬的租户" + i + "，您的租金已逾期，请及时缴纳。");
            record.put("response", i % 3 == 0 ? "已收到，明天缴费" : null);
            record.put("createTime", LocalDateTime.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            records.add(record);
        }
        
        // 分页处理
        int start = (current - 1) * size;
        int end = Math.min(start + size, records.size());
        List<Map<String, Object>> pageRecords = records.subList(start, end);
        
        Map<String, Object> data = new HashMap<>();
        data.put("records", pageRecords);
        data.put("total", records.size());
        data.put("current", current);
        data.put("size", size);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 手动发送催缴短信
     */
    @PostMapping("/send-manual")
    public Map<String, Object> sendManualReminder(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        List<Integer> receivableIds = (List<Integer>) request.get("receivableIds");
        Integer templateId = (Integer) request.get("templateId");
        
        // 这里应该调用短信服务发送催缴短信
        // smsService.sendReminder(receivableIds, templateId);
        
        result.put("code", 200);
        result.put("message", "催缴短信发送成功，共发送 " + receivableIds.size() + " 条");
        
        return result;
    }

    /**
     * 获取催缴统计数据
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReminders", 156); // 总催缴次数
        stats.put("successfulReminders", 89); // 成功催缴次数
        stats.put("successRate", 57.1); // 成功率
        stats.put("totalAmount", 450000.00); // 催缴总金额
        stats.put("collectedAmount", 256000.00); // 已收回金额
        stats.put("collectionRate", 56.9); // 回收率
        
        // 月度统计
        List<Map<String, Object>> monthlyStats = new ArrayList<>();
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月"};
        int[] reminderCounts = {25, 32, 28, 35, 30, 26};
        int[] successCounts = {15, 20, 16, 22, 18, 15};
        
        for (int i = 0; i < months.length; i++) {
            Map<String, Object> monthlyStat = new HashMap<>();
            monthlyStat.put("month", months[i]);
            monthlyStat.put("reminderCount", reminderCounts[i]);
            monthlyStat.put("successCount", successCounts[i]);
            monthlyStat.put("successRate", Math.round((double) successCounts[i] / reminderCounts[i] * 100 * 10) / 10.0);
            monthlyStats.add(monthlyStat);
        }
        stats.put("monthlyStats", monthlyStats);
        
        // 催缴类型统计
        List<Map<String, Object>> typeStats = new ArrayList<>();
        typeStats.add(Map.of("type", "首次催缴", "count", 65, "successRate", 45.2));
        typeStats.add(Map.of("type", "二次催缴", "count", 48, "successRate", 62.5));
        typeStats.add(Map.of("type", "最终催缴", "count", 43, "successRate", 74.4));
        stats.put("typeStats", typeStats);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", stats);
        
        return result;
    }

    /**
     * 获取待催缴的应收款列表
     */
    @GetMapping("/pending-receivables")
    public Map<String, Object> getPendingReceivables(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 模拟待催缴数据
        List<Map<String, Object>> receivables = new ArrayList<>();
        
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> receivable = new HashMap<>();
            receivable.put("id", i);
            receivable.put("tenantName", "租户" + i);
            receivable.put("contractNo", "HT" + String.format("%03d", i));
            receivable.put("phone", "138****" + String.format("%04d", 1000 + i));
            receivable.put("amount", 5000.00 + i * 100);
            receivable.put("dueDate", LocalDateTime.now().minusDays(i + 5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            receivable.put("overdueDays", i + 5);
            receivable.put("reminderCount", i % 3);
            receivable.put("lastReminderTime", i % 3 > 0 ? 
                LocalDateTime.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
            receivable.put("priority", i <= 5 ? "高" : (i <= 15 ? "中" : "低"));
            receivables.add(receivable);
        }
        
        // 分页处理
        int start = (current - 1) * size;
        int end = Math.min(start + size, receivables.size());
        List<Map<String, Object>> pageReceivables = receivables.subList(start, end);
        
        Map<String, Object> data = new HashMap<>();
        data.put("records", pageReceivables);
        data.put("total", receivables.size());
        data.put("current", current);
        data.put("size", size);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        
        return result;
    }

    /**
     * 批量设置催缴优先级
     */
    @PostMapping("/set-priority")
    public Map<String, Object> setPriority(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        List<Integer> receivableIds = (List<Integer>) request.get("receivableIds");
        String priority = (String) request.get("priority");
        
        // 这里应该更新数据库中的优先级
        // receivableService.updatePriority(receivableIds, priority);
        
        result.put("code", 200);
        result.put("message", "优先级设置成功");
        
        return result;
    }

    /**
     * 获取短信模板列表
     */
    @GetMapping("/sms-templates")
    public Map<String, Object> getSmsTemplates() {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> templates = new ArrayList<>();
        
        Map<String, Object> template1 = new HashMap<>();
        template1.put("id", 1);
        template1.put("name", "首次催缴模板");
        template1.put("content", "尊敬的{tenantName}，您的租金{amount}元已逾期{days}天，请及时缴纳。联系电话：{phone}");
        template1.put("type", "first");
        template1.put("enabled", true);
        templates.add(template1);
        
        Map<String, Object> template2 = new HashMap<>();
        template2.put("id", 2);
        template2.put("name", "二次催缴模板");
        template2.put("content", "尊敬的{tenantName}，您的租金{amount}元已逾期{days}天，这是第二次提醒，请尽快缴纳避免影响信用。");
        template2.put("type", "second");
        template2.put("enabled", true);
        templates.add(template2);
        
        Map<String, Object> template3 = new HashMap<>();
        template3.put("id", 3);
        template3.put("name", "最终催缴模板");
        template3.put("content", "尊敬的{tenantName}，您的租金{amount}元已严重逾期{days}天，请立即缴纳，否则将采取法律措施。");
        template3.put("type", "final");
        template3.put("enabled", true);
        templates.add(template3);
        
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", templates);
        
        return result;
    }

    /**
     * 保存短信模板
     */
    @PostMapping("/sms-templates")
    public Map<String, Object> saveSmsTemplate(@RequestBody Map<String, Object> template) {
        Map<String, Object> result = new HashMap<>();
        
        // 这里应该保存到数据库
        // templateService.saveTemplate(template);
        
        result.put("code", 200);
        result.put("message", "模板保存成功");
        
        return result;
    }
}