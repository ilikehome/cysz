-- 调试合同表结构和业态字段
USE cysz;

-- 1. 检查合同表是否存在
SHOW TABLES LIKE 'contract';

-- 2. 检查合同表结构
DESCRIBE contract;

-- 3. 检查business_format字段是否存在
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'cysz' 
  AND TABLE_NAME = 'contract' 
  AND COLUMN_NAME = 'business_format';

-- 4. 查看合同表中的业态数据
SELECT 
    id, 
    contract_no, 
    business_format, 
    create_time 
FROM contract 
WHERE business_format IS NOT NULL 
LIMIT 10;

-- 5. 统计业态字段的数据分布
SELECT 
    business_format, 
    COUNT(*) as count 
FROM contract 
GROUP BY business_format 
ORDER BY count DESC;

-- 6. 检查最近创建的合同记录
SELECT 
    id, 
    contract_no, 
    contract_name, 
    business_format, 
    create_time 
FROM contract 
ORDER BY create_time DESC 
LIMIT 5;