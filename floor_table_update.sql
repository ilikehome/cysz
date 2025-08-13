-- 创建楼层表
CREATE TABLE IF NOT EXISTS `floor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID',
  `floor_name` varchar(100) NOT NULL COMMENT '楼层名称',
  `floor_code` varchar(50) NOT NULL COMMENT '楼层编码',
  `remark` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_floor_code` (`floor_code`),
  KEY `idx_building_id` (`building_id`),
  CONSTRAINT `fk_floor_building` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼层表';

-- 修改单元表结构，添加新字段并调整关联关系
ALTER TABLE `unit` 
ADD COLUMN `unit_name` varchar(100) NOT NULL COMMENT '单元名称' AFTER `id`,
ADD COLUMN `floor_id` bigint NOT NULL COMMENT '所属楼层ID' AFTER `unit_code`,
ADD COLUMN `is_multi_tenant` tinyint(1) DEFAULT 0 COMMENT '是否一位多租',
ADD COLUMN `remark` text COMMENT '备注',
DROP COLUMN `unit_description`,
DROP COLUMN `project_id`,
DROP COLUMN `building_id`,
DROP COLUMN `property_area`;

-- 添加楼层外键约束
ALTER TABLE `unit` 
ADD CONSTRAINT `fk_unit_floor` FOREIGN KEY (`floor_id`) REFERENCES `floor` (`id`) ON DELETE CASCADE;

-- 插入一些测试数据
INSERT INTO `floor` (`building_id`, `floor_name`, `floor_code`, `remark`) VALUES
(1, '1层', 'F001', '一层大厅及办公区'),
(1, '2层', 'F002', '二层办公区'),
(1, '3层', 'F003', '三层办公区'),
(2, '1层', 'F101', 'B栋一层'),
(2, '2层', 'F102', 'B栋二层');

-- 更新现有单元数据（如果有的话）
-- 注意：这里需要根据实际情况调整，因为我们改变了表结构
-- 如果有现有数据，需要先备份，然后根据新结构重新插入

-- 示例：插入一些测试单元数据
INSERT INTO `unit` (`unit_name`, `unit_code`, `floor_id`, `unit_status`, `unit_purpose`, `building_area`, `rent_area`, `is_multi_tenant`, `remark`) VALUES
('101室', 'U001', 1, 'VACANT', '办公', 120.50, 115.00, 0, '朝南办公室'),
('102室', 'U002', 1, 'OCCUPIED', '办公', 98.30, 95.00, 0, '朝北办公室'),
('201室', 'U003', 2, 'VACANT', '办公', 135.80, 130.00, 1, '大型办公区域'),
('202室', 'U004', 2, 'MAINTENANCE', '办公', 88.60, 85.00, 0, '小型办公室');