-- 合同状态字段数据迁移脚本
-- 将现有的合同状态值转换为标准的英文短字符串

-- 开始事务
START TRANSACTION;

-- 备份原始数据（可选）
CREATE TABLE IF NOT EXISTS contract_status_backup AS 
SELECT id, contract_status, update_time 
FROM contract 
WHERE contract_status IS NOT NULL;

-- 数据迁移：将现有的合同状态值转换为英文短字符串
UPDATE contract 
SET contract_status = CASE 
    -- 未盖章生效相关
    WHEN contract_status IN ('UNSIGNED_EFFECTIVE', '未盖章生效') THEN 'unsigned_effective'
    
    -- 已盖章生效相关  
    WHEN contract_status IN ('SIGNED_EFFECTIVE', '已盖章生效', 'ACTIVE', '生效中', '有效') THEN 'signed_effective'
    
    -- 提前终止相关
    WHEN contract_status IN ('TERMINATED', '提前终止', '终止', '已终止') THEN 'terminated'
    
    -- 自然到期相关
    WHEN contract_status IN ('EXPIRED', '自然到期', '已到期', '到期') THEN 'expired'
    
    -- 其他未知状态保持原值，需要手动处理
    ELSE contract_status
END,
update_time = NOW()
WHERE contract_status IS NOT NULL;

-- 检查迁移结果
SELECT 
    '迁移后合同状态分布' as check_type,
    contract_status,
    COUNT(*) as count
FROM contract 
WHERE contract_status IS NOT NULL
GROUP BY contract_status
ORDER BY count DESC;

-- 检查是否还有未转换的状态值
SELECT 
    '未转换的状态值' as check_type,
    contract_status,
    COUNT(*) as count
FROM contract 
WHERE contract_status IS NOT NULL 
  AND contract_status NOT IN ('unsigned_effective', 'signed_effective', 'terminated', 'expired')
GROUP BY contract_status;

-- 提交事务
COMMIT;

-- 输出迁移完成信息
SELECT 
    '合同状态迁移完成' as message,
    COUNT(*) as total_contracts,
    SUM(CASE WHEN contract_status = 'unsigned_effective' THEN 1 ELSE 0 END) as unsigned_effective_count,
    SUM(CASE WHEN contract_status = 'signed_effective' THEN 1 ELSE 0 END) as signed_effective_count,
    SUM(CASE WHEN contract_status = 'terminated' THEN 1 ELSE 0 END) as terminated_count,
    SUM(CASE WHEN contract_status = 'expired' THEN 1 ELSE 0 END) as expired_count,
    NOW() as migration_time
FROM contract 
WHERE contract_status IS NOT NULL;