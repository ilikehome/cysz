-- 合同状态字段数据验证脚本
-- 验证迁移后的数据是否正确

-- 检查合同表中的合同状态字段
SELECT 
    '合同表状态字段检查' as check_type,
    contract_status,
    COUNT(*) as count,
    CASE 
        WHEN contract_status IN ('unsigned_effective', 'signed_effective', 'terminated', 'expired') 
        THEN '✓ 有效' 
        ELSE '✗ 无效' 
    END as validity
FROM contract 
WHERE contract_status IS NOT NULL
GROUP BY contract_status
ORDER BY count DESC;

-- 检查是否存在无效的合同状态值
SELECT 
    '无效状态值检查' as check_type,
    contract_status,
    COUNT(*) as count,
    '需要手动处理' as action
FROM contract 
WHERE contract_status IS NOT NULL 
  AND contract_status NOT IN ('unsigned_effective', 'signed_effective', 'terminated', 'expired')
GROUP BY contract_status;

-- 统计各状态的数量和占比
SELECT 
    '状态分布统计' as check_type,
    contract_status,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM contract WHERE contract_status IS NOT NULL), 2) as percentage
FROM contract 
WHERE contract_status IS NOT NULL
GROUP BY contract_status
ORDER BY count DESC;

-- 检查空值情况
SELECT 
    '空值检查' as check_type,
    COUNT(*) as total_contracts,
    SUM(CASE WHEN contract_status IS NULL THEN 1 ELSE 0 END) as null_status_count,
    SUM(CASE WHEN contract_status IS NOT NULL THEN 1 ELSE 0 END) as valid_status_count
FROM contract;

-- 验证状态转换的合理性（如果有历史记录表）
-- SELECT 
--     '状态转换检查' as check_type,
--     old_status,
--     new_status,
--     COUNT(*) as transition_count
-- FROM contract_status_history 
-- GROUP BY old_status, new_status
-- ORDER BY transition_count DESC;

-- 最终验证报告
SELECT 
    '验证报告' as report_type,
    '合同状态标准化验证完成' as message,
    COUNT(*) as total_processed,
    SUM(CASE WHEN contract_status = 'unsigned_effective' THEN 1 ELSE 0 END) as unsigned_effective,
    SUM(CASE WHEN contract_status = 'signed_effective' THEN 1 ELSE 0 END) as signed_effective,
    SUM(CASE WHEN contract_status = 'terminated' THEN 1 ELSE 0 END) as terminated,
    SUM(CASE WHEN contract_status = 'expired' THEN 1 ELSE 0 END) as expired,
    SUM(CASE WHEN contract_status NOT IN ('unsigned_effective', 'signed_effective', 'terminated', 'expired') THEN 1 ELSE 0 END) as invalid_status,
    NOW() as verification_time
FROM contract 
WHERE contract_status IS NOT NULL;