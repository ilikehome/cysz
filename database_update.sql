-- 更新项目表结构，添加新字段并删除不需要的字段
-- 注意：在生产环境中执行前请先备份数据

-- 1. 添加新字段
ALTER TABLE project 
ADD COLUMN management_org VARCHAR(255) COMMENT '管理组织',
ADD COLUMN rent_bill_bank_account VARCHAR(100) COMMENT '租金账单银行账号',
ADD COLUMN project_manager VARCHAR(100) COMMENT '项目负责人',
ADD COLUMN contact_phone VARCHAR(20) COMMENT '联系电话';

-- 2. 如果存在旧字段，可以选择删除（请根据实际情况决定）
-- ALTER TABLE project DROP COLUMN company_name;
-- ALTER TABLE project DROP COLUMN property_bill_company;
-- ALTER TABLE project DROP COLUMN property_right_company;
-- ALTER TABLE project DROP COLUMN building_area;
-- ALTER TABLE project DROP COLUMN rent_area;
-- ALTER TABLE project DROP COLUMN property_area;

-- 3. 更新现有数据的默认值（可选）
UPDATE project SET 
    management_org = COALESCE(company_name, ''),
    project_manager = '待分配',
    contact_phone = '',
    rent_bill_bank_account = ''
WHERE management_org IS NULL;

-- 4. 修改status字段注释
ALTER TABLE project MODIFY COLUMN status INT DEFAULT 1 COMMENT '状态：1-正常，0-关闭';

-- 5. 添加索引以提高查询性能
CREATE INDEX idx_project_status ON project(status);
CREATE INDEX idx_project_type ON project(project_type);
CREATE INDEX idx_project_city ON project(city);
CREATE INDEX idx_project_create_time ON project(create_time);

-- 6. 查看更新后的表结构
DESCRIBE project;