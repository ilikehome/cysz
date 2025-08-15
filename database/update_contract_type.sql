-- 合同类型字段标准化更新脚本
-- 将现有的中文合同类型转换为英文代码

-- 备份当前数据（可选）
-- CREATE TABLE contract_backup AS SELECT * FROM contract;

-- 更新合同类型字段，将中文转换为英文代码
UPDATE contract SET contract_type = 'SHOP' WHERE contract_type = '商铺';
UPDATE contract SET contract_type = 'OFFICE' WHERE contract_type = '办公';
UPDATE contract SET contract_type = 'OFFICE_BUILDING' WHERE contract_type = '写字楼';
UPDATE contract SET contract_type = 'COWORKING' WHERE contract_type = '联合办公';
UPDATE contract SET contract_type = 'WAREHOUSE' WHERE contract_type = '仓库';
UPDATE contract SET contract_type = 'PARKING' WHERE contract_type = '停车位';

-- 将空值或NULL设置为默认的租赁类型
UPDATE contract SET contract_type = 'RENT' WHERE contract_type IS NULL OR contract_type = '';

-- 查看更新后的数据分布
SELECT contract_type, COUNT(*) as count FROM contract GROUP BY contract_type;

-- 验证是否还有未标准化的数据
SELECT DISTINCT contract_type FROM contract 
WHERE contract_type NOT IN ('RENT', 'SHOP', 'OFFICE', 'OFFICE_BUILDING', 'COWORKING', 'WAREHOUSE', 'PARKING');