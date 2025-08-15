-- 租金期间设定字段数据迁移脚本
-- 将中文显示名转换为英文代码
-- 执行前请备份数据库

USE cysz;

-- 1. 首先查看当前数据库中的租金期间设定数据分布
SELECT 
    rent_period_setting,
    COUNT(*) as count
FROM contract 
WHERE rent_period_setting IS NOT NULL 
GROUP BY rent_period_setting
ORDER BY count DESC;

-- 2. 创建临时列存储转换后的英文代码
ALTER TABLE contract ADD COLUMN rent_period_setting_code VARCHAR(30) COMMENT '租金期间设定英文代码';

-- 3. 数据转换：将中文显示名转换为英文代码
UPDATE contract 
SET rent_period_setting_code = CASE 
    WHEN rent_period_setting = '月末截止计租周期' THEN 'MONTH_END_CYCLE'
    WHEN rent_period_setting = '起租日滚动周期' THEN 'LEASE_START_ROLLING'
    WHEN rent_period_setting = '指定日期滚动周期' THEN 'SPECIFIED_DATE_ROLLING'
    ELSE rent_period_setting  -- 保留未知值，便于后续处理
END
WHERE rent_period_setting IS NOT NULL;

-- 4. 验证转换结果
SELECT 
    rent_period_setting as '原始中文',
    rent_period_setting_code as '转换后英文代码',
    COUNT(*) as '记录数'
FROM contract 
WHERE rent_period_setting IS NOT NULL 
GROUP BY rent_period_setting, rent_period_setting_code
ORDER BY COUNT(*) DESC;

-- 5. 检查是否有未转换的数据
SELECT 
    rent_period_setting,
    rent_period_setting_code,
    COUNT(*) as count
FROM contract 
WHERE rent_period_setting IS NOT NULL 
  AND rent_period_setting_code NOT IN ('MONTH_END_CYCLE', 'LEASE_START_ROLLING', 'SPECIFIED_DATE_ROLLING')
GROUP BY rent_period_setting, rent_period_setting_code;

-- 6. 如果转换结果正确，将英文代码复制回原字段
-- 注意：执行前请确认上述验证步骤的结果
-- UPDATE contract SET rent_period_setting = rent_period_setting_code WHERE rent_period_setting_code IS NOT NULL;

-- 7. 删除临时列（在确认数据正确后执行）
-- ALTER TABLE contract DROP COLUMN rent_period_setting_code;

-- 8. 添加字段约束（可选，在确认数据正确后执行）
-- ALTER TABLE contract ADD CONSTRAINT chk_rent_period_setting 
-- CHECK (rent_period_setting IN ('MONTH_END_CYCLE', 'LEASE_START_ROLLING', 'SPECIFIED_DATE_ROLLING'));

-- 执行说明：
-- 1. 先执行步骤1-5，验证转换结果
-- 2. 确认无误后，取消步骤6的注释并执行
-- 3. 最后执行步骤7-8（可选）