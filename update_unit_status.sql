-- 更新单元表中的状态值，将旧的状态值映射到新的状态值
UPDATE unit SET unit_status = 'RENTABLE' WHERE unit_status = 'VACANT';
UPDATE unit SET unit_status = 'SELF_RENTAL' WHERE unit_status = 'OCCUPIED';
UPDATE unit SET unit_status = 'DISABLED' WHERE unit_status = 'MAINTENANCE';
UPDATE unit SET unit_status = 'RENTABLE' WHERE unit_status = 'RESERVED';

-- 查看更新后的数据
SELECT id, unit_name, unit_code, unit_status, unit_purpose FROM unit;