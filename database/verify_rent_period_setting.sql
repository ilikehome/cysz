-- 租金期间设定数据一致性验证脚本
-- 用于验证前后端和数据库中租金期间设定数据的一致性

USE cysz;

-- 1. 检查当前数据库中的租金期间设定分布
SELECT 
    '当前数据库租金期间设定分布' as '检查项目',
    rent_period_setting as '租金期间设定值',
    COUNT(*) as '记录数',
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM contract WHERE rent_period_setting IS NOT NULL), 2) as '占比%'
FROM contract 
WHERE rent_period_setting IS NOT NULL 
GROUP BY rent_period_setting
ORDER BY COUNT(*) DESC;

-- 2. 检查是否存在无效的租金期间设定代码
SELECT 
    '无效租金期间设定代码检查' as '检查项目',
    rent_period_setting as '无效值',
    COUNT(*) as '记录数'
FROM contract 
WHERE rent_period_setting IS NOT NULL 
  AND rent_period_setting NOT IN ('MONTH_END_CYCLE', 'LEASE_START_ROLLING', 'SPECIFIED_DATE_ROLLING')
GROUP BY rent_period_setting;

-- 3. 检查空值情况
SELECT 
    '空值检查' as '检查项目',
    CASE 
        WHEN rent_period_setting IS NULL THEN 'NULL值'
        WHEN rent_period_setting = '' THEN '空字符串'
        ELSE '有效值'
    END as '数据状态',
    COUNT(*) as '记录数'
FROM contract 
GROUP BY 
    CASE 
        WHEN rent_period_setting IS NULL THEN 'NULL值'
        WHEN rent_period_setting = '' THEN '空字符串'
        ELSE '有效值'
    END;

-- 4. 验证标准化后的数据映射关系
SELECT 
    '标准化映射验证' as '检查项目',
    rent_period_setting as '英文代码',
    CASE 
        WHEN rent_period_setting = 'MONTH_END_CYCLE' THEN '月末截止计租周期'
        WHEN rent_period_setting = 'LEASE_START_ROLLING' THEN '起租日滚动周期'
        WHEN rent_period_setting = 'SPECIFIED_DATE_ROLLING' THEN '指定日期滚动周期'
        ELSE '未知映射'
    END as '对应中文显示名',
    COUNT(*) as '记录数'
FROM contract 
WHERE rent_period_setting IS NOT NULL 
GROUP BY rent_period_setting
ORDER BY COUNT(*) DESC;

-- 5. 检查合同表的完整性
SELECT 
    '合同表完整性检查' as '检查项目',
    COUNT(*) as '总合同数',
    COUNT(rent_period_setting) as '有租金期间设定的合同数',
    COUNT(*) - COUNT(rent_period_setting) as '缺少租金期间设定的合同数',
    ROUND(COUNT(rent_period_setting) * 100.0 / COUNT(*), 2) as '租金期间设定完整度%'
FROM contract;

-- 6. 按项目统计租金期间设定使用情况
SELECT 
    '按项目统计租金期间设定' as '检查项目',
    p.project_name as '项目名称',
    c.rent_period_setting as '租金期间设定',
    COUNT(*) as '合同数量'
FROM contract c
LEFT JOIN project p ON c.project_id = p.id
WHERE c.rent_period_setting IS NOT NULL
GROUP BY p.project_name, c.rent_period_setting
ORDER BY p.project_name, COUNT(*) DESC;

-- 7. 同时检查付款频率和租金期间设定的组合使用情况
SELECT 
    '付款频率与租金期间设定组合使用' as '检查项目',
    c.payment_frequency as '付款频率',
    c.rent_period_setting as '租金期间设定',
    COUNT(*) as '合同数量'
FROM contract c
WHERE c.payment_frequency IS NOT NULL 
  AND c.rent_period_setting IS NOT NULL
GROUP BY c.payment_frequency, c.rent_period_setting
ORDER BY COUNT(*) DESC;