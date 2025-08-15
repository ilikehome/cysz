-- 租金模式枚举标准化验证脚本
-- 用于验证数据迁移是否成功
-- 
-- @author CodeBuddy
-- @since 2025-01-16

-- 1. 检查租金模式分布
SELECT 
    '租金模式分布统计' as check_type,
    rent_mode,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM contract), 2) as percentage
FROM contract 
GROUP BY rent_mode
ORDER BY count DESC;

-- 2. 检查是否存在无效的租金模式代码
SELECT 
    '无效租金模式检查' as check_type,
    rent_mode,
    COUNT(*) as count
FROM contract 
WHERE rent_mode NOT IN ('fixed', 'commission')
GROUP BY rent_mode;

-- 3. 检查空值情况
SELECT 
    '空值检查' as check_type,
    COUNT(*) as null_count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM contract), 2) as null_percentage
FROM contract 
WHERE rent_mode IS NULL OR rent_mode = '';

-- 4. 验证枚举值的完整性
SELECT 
    '枚举完整性检查' as check_type,
    CASE 
        WHEN EXISTS (SELECT 1 FROM contract WHERE rent_mode = 'fixed') THEN 'YES' 
        ELSE 'NO' 
    END as has_fixed,
    CASE 
        WHEN EXISTS (SELECT 1 FROM contract WHERE rent_mode = 'commission') THEN 'YES' 
        ELSE 'NO' 
    END as has_commission;

-- 5. 总体数据质量检查
SELECT 
    '数据质量总结' as check_type,
    COUNT(*) as total_records,
    COUNT(CASE WHEN rent_mode IN ('fixed', 'commission') THEN 1 END) as valid_records,
    COUNT(CASE WHEN rent_mode NOT IN ('fixed', 'commission') OR rent_mode IS NULL THEN 1 END) as invalid_records,
    ROUND(COUNT(CASE WHEN rent_mode IN ('fixed', 'commission') THEN 1 END) * 100.0 / COUNT(*), 2) as data_quality_percentage
FROM contract;

-- 6. 显示迁移成功消息
SELECT 
    CASE 
        WHEN (SELECT COUNT(*) FROM contract WHERE rent_mode NOT IN ('fixed', 'commission') OR rent_mode IS NULL) = 0 
        THEN '✅ 租金模式枚举标准化迁移成功！所有数据已转换为标准英文代码。'
        ELSE '⚠️  租金模式枚举标准化迁移存在问题，请检查上述统计结果。'
    END as migration_result;