package com.cysz.minimal;

import com.cysz.minimal.common.Result;
import com.cysz.minimal.entity.ContractTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contract-template")
@CrossOrigin(origins = "*")
public class ContractTemplateController {

    // 将具体路径放在前面，避免与 /{id} 冲突
    @GetMapping("/page")
    public Result<Map<String, Object>> getTemplatePage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String templateType,
            @RequestParam(required = false) String status) {
        
        // 获取所有模板数据
        List<ContractTemplate> allTemplates = getAllTemplates();
        
        // 过滤数据
        List<ContractTemplate> filteredTemplates = allTemplates.stream()
            .filter(template -> status == null || status.isEmpty() || template.getStatus().equals(status))
            .filter(template -> templateType == null || templateType.isEmpty() || template.getTemplateType().equals(templateType))
            .filter(template -> keyword == null || keyword.isEmpty() || 
                template.getTemplateName().contains(keyword) || 
                (template.getDescription() != null && template.getDescription().contains(keyword)))
            .toList();
        
        // 分页处理
        int total = filteredTemplates.size();
        int startIndex = (current - 1) * size;
        int endIndex = Math.min(startIndex + size, total);
        
        List<ContractTemplate> pageData = startIndex < total ? 
            filteredTemplates.subList(startIndex, endIndex) : new ArrayList<>();
        
        // 构建分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData);
        result.put("total", total);
        result.put("current", current);
        result.put("size", size);
        result.put("pages", (int) Math.ceil((double) total / size));
        
        return Result.success(result);
    }

    @GetMapping("/list")
    public Result<List<ContractTemplate>> getTemplateList(@RequestParam(required = false) String status) {
        List<ContractTemplate> templates = getAllTemplates();
        
        if (status != null && !status.isEmpty()) {
            templates = templates.stream()
                .filter(template -> template.getStatus().equals(status))
                .toList();
        }
        
        return Result.success(templates);
    }
    
    // 将 /{id} 路径放在最后，避免匹配冲突
    @GetMapping("/{id}")
    public Result<ContractTemplate> getTemplateById(@PathVariable Long id) {
        // 从所有模板中查找指定ID的模板
        List<ContractTemplate> templates = getAllTemplates();
        ContractTemplate template = templates.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElse(null);
            
        if (template == null) {
            return Result.error("模板不存在");
        }
        
        return Result.success(template);
    }
    
    private List<ContractTemplate> getAllTemplates() {
        // 模拟数据
        List<ContractTemplate> templates = new ArrayList<>();
        
        ContractTemplate template1 = new ContractTemplate();
        template1.setId(1L);
        template1.setTemplateName("标准租赁合同");
        template1.setTemplateType("租赁合同");
        template1.setVersion("v1.0");
        template1.setStatus("ACTIVE");
        template1.setDescription("适用于商业地产租赁的标准合同模板");
        template1.setTemplateContent("房屋租赁合同\n\n合同编号：{{contractNo}}\n合同名称：{{contractName}}\n签约日期：{{signDate}}\n生效日期：{{effectiveDate}}\n\n甲方（出租方）：{{partyA}}\n联系电话：{{partyAPhone}}\n身份证号：{{partyAIdCard}}\n地址：{{partyAAddress}}\n\n乙方（承租方）：{{partyB}}\n联系电话：{{partyBPhone}}\n身份证号：{{partyBIdCard}}\n地址：{{partyBAddress}}\n\n根据《中华人民共和国民法典》、《中华人民共和国城市房地产管理法》等相关法律法规的规定，甲乙双方在平等、自愿、协商一致的基础上，就房屋租赁事宜达成如下协议：\n\n第一条 租赁房屋基本情况\n房屋坐落：{{houseLocation}}\n房屋面积：{{area}}平方米\n房屋用途：{{houseUsage}}\n房屋结构：{{houseStructure}}\n\n第二条 租赁期限\n租赁期限自{{startDate}}起至{{endDate}}止，共计{{rentPeriod}}个月。\n\n第三条 租金及支付方式\n1. 月租金为人民币{{monthlyRent}}元（大写：{{monthlyRentChinese}}）\n2. 租金支付方式：{{paymentMethod}}\n3. 租金应于每月{{paymentDate}}日前支付\n\n第四条 押金\n乙方应向甲方支付押金人民币{{deposit}}元（大写：{{depositChinese}}），用于担保合同的履行。合同终止时，甲方应在扣除相关费用后将剩余押金退还乙方。\n\n第五条 房屋交付及验收\n甲方应于{{deliveryDate}}前将房屋交付乙方使用。房屋交付时，双方应共同验收，并签署房屋交接清单。\n\n第六条 房屋维修责任\n1. 甲方负责房屋主体结构的维修\n2. 乙方负责日常使用中的维护保养\n3. 因乙方使用不当造成的损坏，由乙方承担维修费用\n\n第七条 合同变更和解除\n1. 合同变更需经双方书面同意\n2. 任何一方违约，守约方有权解除合同并要求赔偿损失\n\n第八条 违约责任\n1. 乙方逾期支付租金的，每日按欠款金额的0.3%支付违约金\n2. 甲方无正当理由收回房屋的，应双倍返还押金\n3. 其他违约行为按相关法律法规处理\n\n第九条 争议解决\n因本合同引起的争议，双方应协商解决；协商不成的，可向房屋所在地人民法院起诉。\n\n第十条 其他约定\n{{otherTerms}}\n\n第十一条 合同生效\n本合同一式两份，甲乙双方各执一份，自双方签字盖章之日起生效。\n\n甲方（出租方）：________________    乙方（承租方）：________________\n签字日期：{{signDate}}              签字日期：{{signDate}}\n\n附件：\n1. 甲方身份证复印件\n2. 房屋产权证复印件\n3. 乙方身份证复印件\n4. 房屋交接清单\n\n本合同受中华人民共和国法律保护，任何违反本合同的行为都将承担相应的法律责任。");
        template1.setUsageCount(25);
        template1.setCreateTime(LocalDateTime.now());
        templates.add(template1);
        
        ContractTemplate template2 = new ContractTemplate();
        template2.setId(2L);
        template2.setTemplateName("物业服务合同");
        template2.setTemplateType("服务合同");
        template2.setVersion("v1.2");
        template2.setStatus("ACTIVE");
        template2.setDescription("物业管理服务合同模板");
        template2.setTemplateContent("合同编号：{{contractNo}}\n合同名称：{{contractName}}\n签约日期：{{signDate}}\n生效日期：{{effectiveDate}}\n\n委托方：{{client}}\n服务方：{{serviceProvider}}\n服务内容：{{serviceContent}}\n服务费用：{{serviceFee}}元/月\n\n本合同自{{effectiveDate}}起生效。");
        template2.setUsageCount(18);
        template2.setCreateTime(LocalDateTime.now());
        templates.add(template2);
        
        ContractTemplate template3 = new ContractTemplate();
        template3.setId(3L);
        template3.setTemplateName("设备采购合同");
        template3.setTemplateType("采购合同");
        template3.setVersion("v2.0");
        template3.setStatus("ACTIVE");
        template3.setDescription("设备采购合同标准模板");
        template3.setTemplateContent("合同编号：{{contractNo}}\n合同名称：{{contractName}}\n签约日期：{{signDate}}\n生效日期：{{effectiveDate}}\n\n采购方：{{buyer}}\n供应方：{{supplier}}\n设备名称：{{equipmentName}}\n采购数量：{{quantity}}\n单价：{{unitPrice}}元\n总价：{{totalPrice}}元\n\n本合同自{{effectiveDate}}起生效。");
        template3.setUsageCount(12);
        template3.setCreateTime(LocalDateTime.now());
        templates.add(template3);
        
        return templates;
    }
}