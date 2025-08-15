-- 租金模式枚举标准化数据迁移脚本
-- 将现有中文数据转换为英文代码
-- 
-- 执行前请备份数据库！
-- 
-- @author CodeBuddy
-- @since 2025-01-16

-- 开始事务
START TRANSACTION;

-- 1. 备份当前数据（可选）
-- CREATE TABLE contract_rent_mode_backup AS 
-- SELECT id, rent_mode FROM contract WHERE rent_mode IS NOT NULL;

-- 2. 数据转换
-- 固定租金 -> fixed
UPDATE contract 
SET rent_mode = 'fixed' 
WHERE rent_mode = '固定租金';

-- 提成租金 -> commission  
UPDATE contract 
SET rent_mode = 'commission' 
WHERE rent_mode = '提成租金';

-- 3. 处理可能的空值或其他值
-- 将空值或未知值设置为默认的固定租金
UPDATE contract 
SET rent_mode = 'fixed' 
WHERE rent_mode IS NULL OR rent_mode = '' OR rent_mode NOT IN ('fixed', 'commission');

-- 4. 验证数据转换结果
SELECT 
    rent_mode,
    COUNT(*) as count
FROM contract 
GROUP BY rent_mode
ORDER BY rent_mode;

-- 5. 检查是否还有未转换的数据
SELECT COUNT(*) as unprocessed_count
FROM contract 
WHERE rent_mode NOT IN ('fixed', 'commission');

-- 6. 显示转换统计
SELECT 
    '租金模式数据转换完成' as message,
    (SELECT COUNT(*) FROM contract WHERE rent_mode = 'fixed') as fixed_count,
    (SELECT COUNT(*) FROM contract WHERE rent_mode = 'commission') as commission_count,
    (SELECT COUNT(*) FROM contract) as total_count;

-- 提交事务（如果一切正常）
COMMIT;

-- 如果出现问题，可以使用以下命令回滚：
-- ROLLBACK;