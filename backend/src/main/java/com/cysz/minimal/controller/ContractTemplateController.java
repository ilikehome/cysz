package com.cysz.minimal.controller;

import com.cysz.minimal.entity.ContractTemplate;
import com.cysz.minimal.common.Result;
import com.cysz.minimal.common.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/contract-template")
@CrossOrigin(origins = "*")
public class ContractTemplateController {

    // 模拟数据存储
    private static final Map<Long, ContractTemplate> templateStore = new HashMap<>();
    private static Long idCounter = 1L;

    static {
        // 初始化模拟数据
        ContractTemplate template1 = new ContractTemplate();
        template1.setId(idCounter++);
        template1.setTemplateName("标准租赁合同模板");
        template1.setTemplateType("LEASE");
        template1.setVersion("v1.0");
        template1.setStatus("ACTIVE");
        template1.setDescription("适用于商业地产租赁的标准合同模板");
        template1.setUsageCount(25);
        template1.setTemplateContent("租赁合同\n\n甲方：{{甲方名称}}\n乙方：{{乙方名称}}\n\n根据《中华人民共和国合同法》等相关法律法规，甲乙双方在平等、自愿、协商一致的基础上，就租赁事宜达成如下协议：\n\n一、租赁物业\n租赁物业地址：{{物业地址}}\n租赁面积：{{租赁面积}}平方米\n\n二、租赁期限\n租赁期限自{{生效日期}}起至{{到期日期}}止。\n\n三、租金及支付方式\n月租金：人民币{{合同金额}}元\n支付方式：{{支付方式}}\n\n四、其他条款\n本合同一式两份，甲乙双方各执一份，具有同等法律效力。\n\n甲方（签字）：_____________    日期：{{签订日期}}\n乙方（签字）：_____________    日期：{{签订日期}}");
        template1.setCreateTime(LocalDateTime.of(2024, 1, 10, 10, 30, 0));
        template1.setUpdateTime(LocalDateTime.of(2024, 1, 15, 14, 20, 0));
        
        List<ContractTemplate.ContractField> fields1 = new ArrayList<>();
        ContractTemplate.ContractField field1 = new ContractTemplate.ContractField();
        field1.setFieldName("partyA");
        field1.setFieldLabel("甲方名称");
        field1.setFieldType("text");
        field1.setRequired(true);
        field1.setDefaultValue("");
        fields1.add(field1);
        
        ContractTemplate.ContractField field2 = new ContractTemplate.ContractField();
        field2.setFieldName("partyB");
        field2.setFieldLabel("乙方名称");
        field2.setFieldType("text");
        field2.setRequired(true);
        field2.setDefaultValue("");
        fields1.add(field2);
        
        template1.setFields(fields1);
        templateStore.put(template1.getId(), template1);

        ContractTemplate template2 = new ContractTemplate();
        template2.setId(idCounter++);
        template2.setTemplateName("服务外包合同模板");
        template2.setTemplateType("SERVICE");
        template2.setVersion("v2.1");
        template2.setStatus("ACTIVE");
        template2.setDescription("适用于IT服务外包的合同模板");
        template2.setUsageCount(12);
        template2.setTemplateContent("服务合同\n\n委托方：{{甲方名称}}\n承接方：{{乙方名称}}\n\n根据相关法律法规，双方就服务外包事宜达成如下协议：\n\n一、服务内容\n服务项目：{{服务项目}}\n服务期限：{{服务期限}}\n\n二、服务费用\n服务费用：人民币{{合同金额}}元\n\n三、双方权利义务\n...\n\n委托方（签字）：_____________    日期：{{签订日期}}\n承接方（签字）：_____________    日期：{{签订日期}}");
        template2.setCreateTime(LocalDateTime.of(2024, 1, 8, 9, 15, 0));
        template2.setUpdateTime(LocalDateTime.of(2024, 1, 12, 16, 45, 0));
        template2.setFields(new ArrayList<>());
        templateStore.put(template2.getId(), template2);

        ContractTemplate template3 = new ContractTemplate();
        template3.setId(idCounter++);
        template3.setTemplateName("销售合同模板（已禁用）");
        template3.setTemplateType("SALES");
        template3.setVersion("v1.5");
        template3.setStatus("INACTIVE");
        template3.setDescription("产品销售合同模板，已停用");
        template3.setUsageCount(8);
        template3.setTemplateContent("销售合同\n\n卖方：{{甲方名称}}\n买方：{{乙方名称}}\n\n商品详情：{{商品详情}}\n合同金额：{{合同金额}}\n\n卖方（签字）：_____________    日期：{{签订日期}}\n买方（签字）：_____________    日期：{{签订日期}}");
        template3.setCreateTime(LocalDateTime.of(2023, 12, 20, 11, 20, 0));
        template3.setUpdateTime(LocalDateTime.of(2024, 1, 5, 13, 30, 0));
        template3.setFields(new ArrayList<>());
        templateStore.put(template3.getId(), template3);
    }

    /**
     * 分页查询合同模板
     */
    @GetMapping("/page")
    public Result<PageResult<ContractTemplate>> getTemplatePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String templateType,
            @RequestParam(required = false) String status) {
        
        log.info("合同模板分页查询 - current: {} size: {} keyword: {} templateType: {} status: {}", 
                current, size, keyword, templateType, status);

        List<ContractTemplate> allTemplates = new ArrayList<>(templateStore.values());
        
        // 过滤
        List<ContractTemplate> filteredTemplates = allTemplates.stream()
                .filter(template -> keyword == null || template.getTemplateName().contains(keyword))
                .filter(template -> templateType == null || template.getTemplateType().equals(templateType))
                .filter(template -> status == null || template.getStatus().equals(status))
                .sorted((a, b) -> b.getUpdateTime().compareTo(a.getUpdateTime()))
                .collect(Collectors.toList());

        // 分页
        int total = filteredTemplates.size();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);
        
        List<ContractTemplate> pageData = start < total ? 
                filteredTemplates.subList(start, end) : new ArrayList<>();

        PageResult<ContractTemplate> pageResult = new PageResult<>();
        pageResult.setRecords(pageData);
        pageResult.setTotal((long) total);
        pageResult.setCurrent((long) current);
        pageResult.setSize((long) size);

        return Result.success(pageResult);
    }

    /**
     * 根据ID查询合同模板
     */
    @GetMapping("/{id}")
    public Result<ContractTemplate> getTemplateById(@PathVariable Long id) {
        log.info("查询合同模板详情 - id: {}", id);
        
        ContractTemplate template = templateStore.get(id);
        if (template == null) {
            return Result.error("合同模板不存在");
        }
        
        return Result.success(template);
    }

    /**
     * 创建合同模板
     */
    @PostMapping
    public Result<ContractTemplate> createTemplate(@RequestBody ContractTemplate template) {
        log.info("创建合同模板 - templateName: {}", template.getTemplateName());
        
        template.setId(idCounter++);
        template.setUsageCount(0);
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        
        if (template.getFields() == null) {
            template.setFields(new ArrayList<>());
        }
        
        templateStore.put(template.getId(), template);
        
        return Result.success(template);
    }

    /**
     * 更新合同模板
     */
    @PutMapping("/{id}")
    public Result<ContractTemplate> updateTemplate(@PathVariable Long id, @RequestBody ContractTemplate template) {
        log.info("更新合同模板 - id: {} templateName: {}", id, template.getTemplateName());
        
        ContractTemplate existingTemplate = templateStore.get(id);
        if (existingTemplate == null) {
            return Result.error("合同模板不存在");
        }
        
        template.setId(id);
        template.setUsageCount(existingTemplate.getUsageCount());
        template.setCreateTime(existingTemplate.getCreateTime());
        template.setUpdateTime(LocalDateTime.now());
        
        if (template.getFields() == null) {
            template.setFields(new ArrayList<>());
        }
        
        templateStore.put(id, template);
        
        return Result.success(template);
    }

    /**
     * 删除合同模板
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        log.info("删除合同模板 - id: {}", id);
        
        ContractTemplate template = templateStore.get(id);
        if (template == null) {
            return Result.error("合同模板不存在");
        }
        
        templateStore.remove(id);
        
        return Result.success();
    }

    /**
     * 切换模板状态
     */
    @PutMapping("/{id}/status")
    public Result<ContractTemplate> toggleTemplateStatus(@PathVariable Long id) {
        log.info("切换合同模板状态 - id: {}", id);
        
        ContractTemplate template = templateStore.get(id);
        if (template == null) {
            return Result.error("合同模板不存在");
        }
        
        template.setStatus("ACTIVE".equals(template.getStatus()) ? "INACTIVE" : "ACTIVE");
        template.setUpdateTime(LocalDateTime.now());
        
        return Result.success(template);
    }

    /**
     * 复制合同模板
     */
    @PostMapping("/{id}/copy")
    public Result<ContractTemplate> copyTemplate(@PathVariable Long id) {
        log.info("复制合同模板 - id: {}", id);
        
        ContractTemplate originalTemplate = templateStore.get(id);
        if (originalTemplate == null) {
            return Result.error("合同模板不存在");
        }
        
        ContractTemplate newTemplate = new ContractTemplate();
        newTemplate.setId(idCounter++);
        newTemplate.setTemplateName(originalTemplate.getTemplateName() + "_副本");
        newTemplate.setTemplateType(originalTemplate.getTemplateType());
        newTemplate.setVersion("v1.0");
        newTemplate.setStatus("INACTIVE");
        newTemplate.setDescription(originalTemplate.getDescription());
        newTemplate.setTemplateContent(originalTemplate.getTemplateContent());
        newTemplate.setUsageCount(0);
        newTemplate.setFields(originalTemplate.getFields() != null ? 
                new ArrayList<>(originalTemplate.getFields()) : new ArrayList<>());
        newTemplate.setCreateTime(LocalDateTime.now());
        newTemplate.setUpdateTime(LocalDateTime.now());
        
        templateStore.put(newTemplate.getId(), newTemplate);
        
        return Result.success(newTemplate);
    }

    /**
     * 获取模板列表（不分页）
     */
    @GetMapping("/list")
    public Result<List<ContractTemplate>> getTemplateList(@RequestParam(required = false) String status) {
        log.info("获取合同模板列表 - status: {}", status);
        
        List<ContractTemplate> templates = templateStore.values().stream()
                .filter(template -> status == null || template.getStatus().equals(status))
                .sorted((a, b) -> b.getUpdateTime().compareTo(a.getUpdateTime()))
                .collect(Collectors.toList());
        
        return Result.success(templates);
    }

    /**
     * 预览模板内容
     */
    @PostMapping("/preview")
    public Result<String> previewTemplate(@RequestBody Map<String, Object> request) {
        log.info("预览合同模板");
        
        String templateContent = (String) request.get("templateContent");
        @SuppressWarnings("unchecked")
        Map<String, String> variables = (Map<String, String>) request.get("variables");
        
        if (templateContent == null) {
            return Result.error("模板内容不能为空");
        }
        
        // 使用默认变量值进行预览
        Map<String, String> defaultVariables = new HashMap<>();
        defaultVariables.put("{{甲方名称}}", "北京科技有限公司");
        defaultVariables.put("{{乙方名称}}", "上海贸易有限公司");
        defaultVariables.put("{{合同金额}}", "100,000.00");
        defaultVariables.put("{{签订日期}}", "2024年1月15日");
        defaultVariables.put("{{生效日期}}", "2024年2月1日");
        defaultVariables.put("{{到期日期}}", "2025年1月31日");
        defaultVariables.put("{{物业地址}}", "北京市朝阳区xxx大厦");
        defaultVariables.put("{{租赁面积}}", "200");
        defaultVariables.put("{{支付方式}}", "按月支付");
        
        // 如果提供了自定义变量，使用自定义变量
        if (variables != null) {
            defaultVariables.putAll(variables);
        }
        
        String previewContent = templateContent;
        for (Map.Entry<String, String> entry : defaultVariables.entrySet()) {
            previewContent = previewContent.replace(entry.getKey(), entry.getValue());
        }
        
        return Result.success(previewContent);
    }
}