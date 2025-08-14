package com.cysz.minimal;

import com.cysz.minimal.common.Result;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 合同生成控制器 - 使用专业PDF生成库iText7
 */
@RestController
@RequestMapping("/contract-generate")
@CrossOrigin(origins = "*")
public class ContractGenerateController {

    /**
     * 存储生成的合同数据
     */
    private static final Map<String, Map<String, Object>> contractDataStore = new HashMap<>();
    
    /**
     * 生成合同PDF
     */
    @PostMapping("/generate-pdf")
    public Result<Map<String, Object>> generatePDF(@RequestBody Map<String, Object> request) {
        try {
            // 安全获取请求参数
            String templateId = getStringValue(request, "templateId", "");
            String contractNo = getStringValue(request, "contractNo", "");
            String contractName = getStringValue(request, "contractName", "");
            String signDate = getStringValue(request, "signDate", "");
            String effectiveDate = getStringValue(request, "effectiveDate", "");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> dynamicFields = (Map<String, Object>) request.get("dynamicFields");
            if (dynamicFields == null) {
                dynamicFields = new HashMap<>();
            }
            
            // 存储合同数据供下载时使用
            Map<String, Object> contractData = new HashMap<>();
            contractData.put("contractNo", contractNo);
            contractData.put("contractName", contractName);
            contractData.put("signDate", signDate);
            contractData.put("effectiveDate", effectiveDate);
            contractData.put("dynamicFields", dynamicFields);
            contractData.put("templateId", templateId);
            contractDataStore.put(contractNo, contractData);
            
            // 返回生成结果
            Map<String, Object> result = new HashMap<>();
            result.put("contractId", UUID.randomUUID().toString());
            result.put("contractNo", contractNo);
            result.put("fileName", contractNo + "_contract.pdf");
            result.put("fileSize", "1.2MB");
            result.put("generateTime", LocalDateTime.now());
            result.put("downloadUrl", "/api/contract-generate/download/" + contractNo);
            result.put("previewUrl", "/api/contract-generate/preview/" + contractNo);
            
            return Result.success("合同生成成功", result);
        } catch (Exception e) {
            e.printStackTrace(); // 添加详细错误日志
            return Result.error("合同生成失败: " + e.getMessage());
        }
    }

    /**
     * 下载合同PDF
     */
    @GetMapping("/download/{contractNo}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable String contractNo) {
        try {
            // 检查合同数据是否存在
            Map<String, Object> contractData = contractDataStore.get(contractNo);
            if (contractData == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 使用iText7生成专业PDF
            byte[] pdfContent = generateProfessionalPDF(contractNo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", contractNo + "_contract.pdf");
            headers.setContentLength(pdfContent.length);
            
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 预览合同PDF
     */
    @GetMapping("/preview/{contractNo}")
    public ResponseEntity<byte[]> previewPDF(@PathVariable String contractNo) {
        try {
            byte[] pdfContent = generateProfessionalPDF(contractNo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", contractNo + "_contract.pdf");
            
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取生成进度
     */
    @GetMapping("/progress/{taskId}")
    public Result<Map<String, Object>> getProgress(@PathVariable String taskId) {
        // 模拟进度查询
        Map<String, Object> progress = new HashMap<>();
        progress.put("taskId", taskId);
        progress.put("progress", 100);
        progress.put("status", "completed");
        progress.put("message", "生成完成");
        
        return Result.success(progress);
    }

    /**
     * 批量生成合同
     */
    @PostMapping("/batch-generate")
    public Result<Map<String, Object>> batchGenerate(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> contracts = (List<Map<String, Object>>) request.get("contracts");
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", UUID.randomUUID().toString());
            result.put("totalCount", contracts.size());
            result.put("status", "processing");
            result.put("message", "批量生成任务已启动");
            
            return Result.success("批量生成任务创建成功", result);
        } catch (Exception e) {
            return Result.error("批量生成失败: " + e.getMessage());
        }
    }

    /**
     * 获取合同生成历史
     */
    @GetMapping("/history")
    public Result<List<Map<String, Object>>> getGenerateHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<Map<String, Object>> history = new ArrayList<>();
        
        // 模拟历史记录
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("id", i);
            record.put("contractNo", "HT2024" + String.format("%04d", i));
            record.put("contractName", "租赁合同" + i);
            record.put("templateName", "标准租赁合同模板");
            record.put("generateTime", LocalDateTime.now().minusDays(i));
            record.put("status", "completed");
            record.put("fileSize", "1." + i + "MB");
            record.put("downloadCount", i * 2);
            history.add(record);
        }
        
        return Result.success(history);
    }

    /**
     * 删除生成的合同文件
     */
    @DeleteMapping("/delete/{contractId}")
    public Result<String> deleteContract(@PathVariable String contractId) {
        try {
            // 这里应该删除实际的文件
            return Result.success("合同文件删除成功");
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 验证合同数据
     */
    @PostMapping("/validate")
    public Result<Map<String, Object>> validateContractData(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> validation = new HashMap<>();
            List<String> errors = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            
            // 模拟验证逻辑
            String contractNo = (String) request.get("contractNo");
            if (contractNo == null || contractNo.trim().isEmpty()) {
                errors.add("合同编号不能为空");
            }
            
            String contractName = (String) request.get("contractName");
            if (contractName == null || contractName.trim().isEmpty()) {
                errors.add("合同名称不能为空");
            }
            
            // 检查合同编号是否重复
            if (contractNo != null && contractNo.startsWith("HT2024")) {
                warnings.add("合同编号可能重复，请确认");
            }
            
            validation.put("valid", errors.isEmpty());
            validation.put("errors", errors);
            validation.put("warnings", warnings);
            
            return Result.success(validation);
        } catch (Exception e) {
            return Result.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 使用iText7生成专业的PDF合同
     */
    private byte[] generateProfessionalPDF(String contractNo) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        
        try {
            // 获取存储的合同数据
            Map<String, Object> contractData = contractDataStore.get(contractNo);
            
            // 尝试设置中文字体
            PdfFont chineseFont = null;
            try {
                chineseFont = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
            } catch (Exception e) {
                // 如果中文字体不可用，使用默认字体
                chineseFont = PdfFontFactory.createFont();
            }
            
            // 添加合同标题
            String contractTitle = contractData != null ? 
                getStringValue(contractData, "contractName", "房屋租赁合同") : "房屋租赁合同";
            document.add(new Paragraph(contractTitle)
                    .setFont(chineseFont)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold()
                    .setMarginBottom(20));
            
            if (contractData != null) {
                // 添加合同基本信息
                addContractBasicInfo(document, contractData, chineseFont);
                
                // 添加甲乙双方信息表格
                addPartyInfoTable(document, contractData, chineseFont);
                
                // 添加合同条款
                addContractTerms(document, contractData, chineseFont);
                
                // 添加签名区域
                addSignatureArea(document, contractData, chineseFont);
            } else {
                // 如果没有数据，添加基本内容
                document.add(new Paragraph("合同编号：" + contractNo)
                        .setFont(chineseFont)
                        .setFontSize(12));
                document.add(new Paragraph("生成时间：" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss")))
                        .setFont(chineseFont)
                        .setFontSize(12));
                document.add(new Paragraph("\n这是一个使用iText7专业PDF生成库创建的合同文档。")
                        .setFont(chineseFont)
                        .setFontSize(12));
            }
            
        } catch (Exception e) {
            // 如果出现异常，添加错误信息
            document.add(new Paragraph("Contract Number: " + contractNo)
                    .setFontSize(12));
            document.add(new Paragraph("Generated at: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setFontSize(12));
            document.add(new Paragraph("\nThis is a professional PDF contract generated using iText7 library.")
                    .setFontSize(12));
        }
        
        document.close();
        return baos.toByteArray();
    }
    
    /**
     * 添加合同基本信息
     */
    private void addContractBasicInfo(Document document, Map<String, Object> contractData, PdfFont font) {
        String contractNo = getStringValue(contractData, "contractNo", "");
        String signDate = getStringValue(contractData, "signDate", "");
        String effectiveDate = getStringValue(contractData, "effectiveDate", "");
        
        if (!contractNo.isEmpty()) {
            document.add(new Paragraph("合同编号：" + contractNo)
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginBottom(5));
        }
        
        if (!signDate.isEmpty()) {
            document.add(new Paragraph("签约日期：" + signDate)
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginBottom(5));
        }
        
        if (!effectiveDate.isEmpty()) {
            document.add(new Paragraph("生效日期：" + effectiveDate)
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginBottom(20));
        }
    }
    
    /**
     * 添加甲乙双方信息表格
     */
    private void addPartyInfoTable(Document document, Map<String, Object> contractData, PdfFont font) {
        @SuppressWarnings("unchecked")
        Map<String, Object> dynamicFields = (Map<String, Object>) contractData.get("dynamicFields");
        
        if (dynamicFields != null && !dynamicFields.isEmpty()) {
            // 创建双方信息表格
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setMarginBottom(20);
            
            // 甲方信息
            String partyA = getStringValue(dynamicFields, "甲方", getStringValue(dynamicFields, "出租方", ""));
            if (!partyA.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph("甲方（出租方）").setFont(font).setBold()));
                table.addCell(new Cell().add(new Paragraph(partyA).setFont(font)));
            }
            
            String partyAPhone = getStringValue(dynamicFields, "甲方联系电话", "");
            if (!partyAPhone.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph("联系电话").setFont(font)));
                table.addCell(new Cell().add(new Paragraph(partyAPhone).setFont(font)));
            }
            
            // 乙方信息
            String partyB = getStringValue(dynamicFields, "乙方", getStringValue(dynamicFields, "承租方", ""));
            if (!partyB.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph("乙方（承租方）").setFont(font).setBold()));
                table.addCell(new Cell().add(new Paragraph(partyB).setFont(font)));
            }
            
            String partyBPhone = getStringValue(dynamicFields, "乙方联系电话", "");
            if (!partyBPhone.isEmpty()) {
                table.addCell(new Cell().add(new Paragraph("联系电话").setFont(font)));
                table.addCell(new Cell().add(new Paragraph(partyBPhone).setFont(font)));
            }
            
            if (table.getNumberOfRows() > 0) {
                document.add(table);
            }
        }
    }
    
    /**
     * 添加合同条款 - 完全基于用户填写的真实数据
     */
    private void addContractTerms(Document document, Map<String, Object> contractData, PdfFont font) {
        @SuppressWarnings("unchecked")
        Map<String, Object> dynamicFields = (Map<String, Object>) contractData.get("dynamicFields");
        
        if (dynamicFields != null && !dynamicFields.isEmpty()) {
            // 根据用户填写的字段动态生成合同内容
            int sectionNumber = 1;
            
            // 房屋/物业相关信息
            if (hasAnyField(dynamicFields, "房屋地址", "物业地址", "租赁地址", "房屋面积", "物业面积", "房屋用途", "物业用途")) {
                document.add(new Paragraph("第" + sectionNumber + "条 租赁物业").setFont(font).setBold().setFontSize(14));
                
                String address = getFirstAvailableValue(dynamicFields, "房屋地址", "物业地址", "租赁地址");
                if (!address.isEmpty()) {
                    document.add(new Paragraph("租赁物业地址：" + address)
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
                
                String area = getFirstAvailableValue(dynamicFields, "房屋面积", "物业面积", "租赁面积");
                if (!area.isEmpty()) {
                    document.add(new Paragraph("租赁面积：" + area + "平方米")
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
                
                String usage = getFirstAvailableValue(dynamicFields, "房屋用途", "物业用途", "使用用途");
                if (!usage.isEmpty()) {
                    document.add(new Paragraph("使用用途：" + usage)
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
                
                document.add(new Paragraph("\n"));
                sectionNumber++;
            }
            
            // 租赁期限相关
            if (hasAnyField(dynamicFields, "租赁开始日期", "租赁结束日期", "租赁期限", "合同期限")) {
                document.add(new Paragraph("第" + sectionNumber + "条 租赁期限").setFont(font).setBold().setFontSize(14));
                
                String startDate = getFirstAvailableValue(dynamicFields, "租赁开始日期", "开始日期");
                String endDate = getFirstAvailableValue(dynamicFields, "租赁结束日期", "结束日期");
                String period = getFirstAvailableValue(dynamicFields, "租赁期限", "合同期限");
                
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    document.add(new Paragraph("租赁期限自 " + startDate + " 起至 " + endDate + " 止。")
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                } else if (!period.isEmpty()) {
                    document.add(new Paragraph("租赁期限：" + period)
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
                
                document.add(new Paragraph("\n"));
                sectionNumber++;
            }
            
            // 租金及费用相关
            if (hasAnyField(dynamicFields, "月租金", "租金", "押金", "保证金", "付款方式", "支付方式")) {
                document.add(new Paragraph("第" + sectionNumber + "条 租金及费用").setFont(font).setBold().setFontSize(14));
                
                String rent = getFirstAvailableValue(dynamicFields, "月租金", "租金");
                if (!rent.isEmpty()) {
                    document.add(new Paragraph("月租金：人民币 " + rent + " 元")
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
                
                String deposit = getFirstAvailableValue(dynamicFields, "押金", "保证金");
                if (!deposit.isEmpty()) {
                    document.add(new Paragraph("押金：人民币 " + deposit + " 元")
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
                
                String paymentMethod = getFirstAvailableValue(dynamicFields, "付款方式", "支付方式");
                if (!paymentMethod.isEmpty()) {
                    document.add(new Paragraph("付款方式：" + paymentMethod)
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
                
                document.add(new Paragraph("\n"));
                sectionNumber++;
            }
            
            // 其他用户填写的字段
            for (Map.Entry<String, Object> entry : dynamicFields.entrySet()) {
                String key = entry.getKey();
                String value = getStringValue(dynamicFields, key, "");
                
                // 跳过已经处理过的字段
                if (isProcessedField(key) || value.isEmpty()) {
                    continue;
                }
                
                // 处理特殊字段
                if (key.contains("备注") || key.contains("说明") || key.contains("约定") || key.contains("条款")) {
                    document.add(new Paragraph("第" + sectionNumber + "条 " + key).setFont(font).setBold().setFontSize(14));
                    document.add(new Paragraph(value).setFont(font).setMarginLeft(20).setFontSize(12));
                    document.add(new Paragraph("\n"));
                    sectionNumber++;
                } else {
                    // 其他字段作为补充信息
                    document.add(new Paragraph(key + "：" + value)
                            .setFont(font).setMarginLeft(20).setFontSize(12));
                }
            }
        }
        
        // 添加标准法律条款
        document.add(new Paragraph("第" + getNextSectionNumber(dynamicFields) + "条 合同效力").setFont(font).setBold().setFontSize(14));
        document.add(new Paragraph("1. 本合同一式两份，甲乙双方各执一份，具有同等法律效力。")
                .setFont(font).setMarginLeft(20).setFontSize(12));
        document.add(new Paragraph("2. 合同履行过程中如有争议，双方应协商解决；协商不成的，可向有管辖权的人民法院起诉。")
                .setFont(font).setMarginLeft(20).setFontSize(12));
        document.add(new Paragraph("3. 本合同自双方签字盖章之日起生效。")
                .setFont(font).setMarginLeft(20).setFontSize(12));
        document.add(new Paragraph("4. 本合同未尽事宜，双方可另行协商补充。")
                .setFont(font).setMarginLeft(20).setFontSize(12));
    }
    
    /**
     * 检查是否包含任何指定字段
     */
    private boolean hasAnyField(Map<String, Object> fields, String... fieldNames) {
        for (String fieldName : fieldNames) {
            if (fields.containsKey(fieldName) && !getStringValue(fields, fieldName, "").isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取第一个可用的字段值
     */
    private String getFirstAvailableValue(Map<String, Object> fields, String... fieldNames) {
        for (String fieldName : fieldNames) {
            String value = getStringValue(fields, fieldName, "");
            if (!value.isEmpty()) {
                return value;
            }
        }
        return "";
    }
    
    /**
     * 检查字段是否已被处理过
     */
    private boolean isProcessedField(String fieldName) {
        String[] processedFields = {
            "房屋地址", "物业地址", "租赁地址", "房屋面积", "物业面积", "租赁面积",
            "房屋用途", "物业用途", "使用用途", "租赁开始日期", "租赁结束日期",
            "开始日期", "结束日期", "租赁期限", "合同期限", "月租金", "租金",
            "押金", "保证金", "付款方式", "支付方式"
        };
        
        for (String processed : processedFields) {
            if (fieldName.equals(processed)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取下一个条款编号
     */
    private int getNextSectionNumber(Map<String, Object> dynamicFields) {
        if (dynamicFields == null || dynamicFields.isEmpty()) {
            return 1;
        }
        
        int count = 1;
        if (hasAnyField(dynamicFields, "房屋地址", "物业地址", "租赁地址", "房屋面积", "物业面积", "房屋用途", "物业用途")) {
            count++;
        }
        if (hasAnyField(dynamicFields, "租赁开始日期", "租赁结束日期", "租赁期限", "合同期限")) {
            count++;
        }
        if (hasAnyField(dynamicFields, "月租金", "租金", "押金", "保证金", "付款方式", "支付方式")) {
            count++;
        }
        
        // 计算其他特殊字段
        for (String key : dynamicFields.keySet()) {
            if (!isProcessedField(key) && !getStringValue(dynamicFields, key, "").isEmpty()) {
                if (key.contains("备注") || key.contains("说明") || key.contains("约定") || key.contains("条款")) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 添加签名区域
     */
    private void addSignatureArea(Document document, Map<String, Object> contractData, PdfFont font) {
        document.add(new Paragraph("\n\n"));
        
        String signDate = getStringValue(contractData, "signDate", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        
        // 创建签名表格
        Table signTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        signTable.setWidth(UnitValue.createPercentValue(100));
        
        signTable.addCell(new Cell().add(new Paragraph("甲方签字：\n\n\n日期：" + signDate).setFont(font))
                .setBorder(null));
        signTable.addCell(new Cell().add(new Paragraph("乙方签字：\n\n\n日期：" + signDate).setFont(font))
                .setBorder(null));
        
        document.add(signTable);
    }
    
    /**
     * 获取字符串值的辅助方法 - 安全的类型转换
     */
    private String getStringValue(Map<String, Object> data, String key, String defaultValue) {
        Object value = data.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        // 安全的类型转换
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return value.toString();
        } else {
            return value.toString();
        }
    }
}