-- 检查单元用途数据的SQL脚本

-- 1. 查看所有不同的单元用途值
SELECT DISTINCT unit_purpose, COUNT(*) as count 
FROM unit 
WHERE unit_purpose IS NOT NULL 
GROUP BY unit_purpose 
ORDER BY count DESC;

-- 2. 查看单元用途为空的记录数量
SELECT COUNT(*) as null_purpose_count 
FROM unit 
WHERE unit_purpose IS NULL OR unit_purpose = '';

-- 3. 查看单元用途数据样例
SELECT id, unit_name, unit_code, unit_purpose, create_time 
FROM unit 
WHERE unit_purpose IS NOT NULL 
LIMIT 10;

-- 4. 检查是否有不在我们枚举中的用途值
SELECT DISTINCT unit_purpose 
FROM unit 
WHERE unit_purpose IS NOT NULL 
AND unit_purpose NOT IN (
    'OFFICE', 'WAREHOUSE', 'FREIGHT', 'COMMERCIAL', 'MEETING_ROOM',
    'BUSINESS_ROOM', 'PARKING', 'ADVERTISING', 'APARTMENT', 
    'MULTI_BUSINESS', 'PROMOTION_VENUE'
);