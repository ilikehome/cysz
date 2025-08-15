-- 业态字段数据迁移脚本
-- 将中文显示名转换为英文短字符串

-- 开始事务
START TRANSACTION;

-- 备份原始数据（可选）
-- CREATE TABLE tenant_business_format_backup AS SELECT id, business_format FROM tenant WHERE business_format IS NOT NULL;
-- CREATE TABLE contract_business_format_backup AS SELECT id, business_format FROM contract WHERE business_format IS NOT NULL;

-- 更新租户表的业态字段
UPDATE tenant SET business_format = 'department_store' WHERE business_format = '百货商场';
UPDATE tenant SET business_format = 'shopping_mall' WHERE business_format = '购物中心';
UPDATE tenant SET business_format = 'supermarket' WHERE business_format = '超市';
UPDATE tenant SET business_format = 'convenience_store' WHERE business_format = '便利店';
UPDATE tenant SET business_format = 'specialty_store' WHERE business_format = '专卖店';
UPDATE tenant SET business_format = 'brand_store' WHERE business_format = '品牌店';
UPDATE tenant SET business_format = 'discount_store' WHERE business_format = '折扣店';
UPDATE tenant SET business_format = 'duty_free_shop' WHERE business_format = '免税店';

UPDATE tenant SET business_format = 'fine_dining' WHERE business_format = '正餐';
UPDATE tenant SET business_format = 'fast_food' WHERE business_format = '快餐';
UPDATE tenant SET business_format = 'casual_dining' WHERE business_format = '休闲餐饮';
UPDATE tenant SET business_format = 'coffee_shop' WHERE business_format = '咖啡厅';
UPDATE tenant SET business_format = 'tea_shop' WHERE business_format = '茶饮店';
UPDATE tenant SET business_format = 'bar' WHERE business_format = '酒吧';
UPDATE tenant SET business_format = 'bakery' WHERE business_format = '烘焙店';
UPDATE tenant SET business_format = 'dessert_shop' WHERE business_format = '甜品店';

UPDATE tenant SET business_format = 'cinema' WHERE business_format = '电影院';
UPDATE tenant SET business_format = 'ktv' WHERE business_format = 'KTV';
UPDATE tenant SET business_format = 'game_center' WHERE business_format = '游戏厅';
UPDATE tenant SET business_format = 'gym' WHERE business_format = '健身房';
UPDATE tenant SET business_format = 'beauty_salon' WHERE business_format = '美容美发';
UPDATE tenant SET business_format = 'spa' WHERE business_format = 'SPA';
UPDATE tenant SET business_format = 'kids_playground' WHERE business_format = '儿童乐园';
UPDATE tenant SET business_format = 'escape_room' WHERE business_format = '密室逃脱';

UPDATE tenant SET business_format = 'bank' WHERE business_format = '银行';
UPDATE tenant SET business_format = 'insurance' WHERE business_format = '保险';
UPDATE tenant SET business_format = 'telecom_office' WHERE business_format = '通讯营业厅';
UPDATE tenant SET business_format = 'courier_service' WHERE business_format = '快递服务';
UPDATE tenant SET business_format = 'laundry' WHERE business_format = '洗衣店';
UPDATE tenant SET business_format = 'repair_service' WHERE business_format = '维修服务';
UPDATE tenant SET business_format = 'education_training' WHERE business_format = '教育培训';
UPDATE tenant SET business_format = 'medical_clinic' WHERE business_format = '医疗诊所';

UPDATE tenant SET business_format = 'office_building' WHERE business_format = '写字楼';
UPDATE tenant SET business_format = 'co_working' WHERE business_format = '联合办公';
UPDATE tenant SET business_format = 'incubator' WHERE business_format = '创业孵化器';
UPDATE tenant SET business_format = 'conference_center' WHERE business_format = '会议中心';
UPDATE tenant SET business_format = 'showroom' WHERE business_format = '展示厅';

UPDATE tenant SET business_format = 'hotel' WHERE business_format = '酒店';
UPDATE tenant SET business_format = 'homestay' WHERE business_format = '民宿';
UPDATE tenant SET business_format = 'hostel' WHERE business_format = '青年旅社';
UPDATE tenant SET business_format = 'serviced_apartment' WHERE business_format = '公寓式酒店';

UPDATE tenant SET business_format = 'warehouse_logistics' WHERE business_format = '仓储物流';
UPDATE tenant SET business_format = 'auto_service' WHERE business_format = '汽车服务';
UPDATE tenant SET business_format = 'pet_service' WHERE business_format = '宠物服务';
UPDATE tenant SET business_format = 'culture_arts' WHERE business_format = '文化艺术';
UPDATE tenant SET business_format = 'other' WHERE business_format = '其他';

-- 更新合同表的业态字段
UPDATE contract SET business_format = 'department_store' WHERE business_format = '百货商场';
UPDATE contract SET business_format = 'shopping_mall' WHERE business_format = '购物中心';
UPDATE contract SET business_format = 'supermarket' WHERE business_format = '超市';
UPDATE contract SET business_format = 'convenience_store' WHERE business_format = '便利店';
UPDATE contract SET business_format = 'specialty_store' WHERE business_format = '专卖店';
UPDATE contract SET business_format = 'brand_store' WHERE business_format = '品牌店';
UPDATE contract SET business_format = 'discount_store' WHERE business_format = '折扣店';
UPDATE contract SET business_format = 'duty_free_shop' WHERE business_format = '免税店';

UPDATE contract SET business_format = 'fine_dining' WHERE business_format = '正餐';
UPDATE contract SET business_format = 'fast_food' WHERE business_format = '快餐';
UPDATE contract SET business_format = 'casual_dining' WHERE business_format = '休闲餐饮';
UPDATE contract SET business_format = 'coffee_shop' WHERE business_format = '咖啡厅';
UPDATE contract SET business_format = 'tea_shop' WHERE business_format = '茶饮店';
UPDATE contract SET business_format = 'bar' WHERE business_format = '酒吧';
UPDATE contract SET business_format = 'bakery' WHERE business_format = '烘焙店';
UPDATE contract SET business_format = 'dessert_shop' WHERE business_format = '甜品店';

UPDATE contract SET business_format = 'cinema' WHERE business_format = '电影院';
UPDATE contract SET business_format = 'ktv' WHERE business_format = 'KTV';
UPDATE contract SET business_format = 'game_center' WHERE business_format = '游戏厅';
UPDATE contract SET business_format = 'gym' WHERE business_format = '健身房';
UPDATE contract SET business_format = 'beauty_salon' WHERE business_format = '美容美发';
UPDATE contract SET business_format = 'spa' WHERE business_format = 'SPA';
UPDATE contract SET business_format = 'kids_playground' WHERE business_format = '儿童乐园';
UPDATE contract SET business_format = 'escape_room' WHERE business_format = '密室逃脱';

UPDATE contract SET business_format = 'bank' WHERE business_format = '银行';
UPDATE contract SET business_format = 'insurance' WHERE business_format = '保险';
UPDATE contract SET business_format = 'telecom_office' WHERE business_format = '通讯营业厅';
UPDATE contract SET business_format = 'courier_service' WHERE business_format = '快递服务';
UPDATE contract SET business_format = 'laundry' WHERE business_format = '洗衣店';
UPDATE contract SET business_format = 'repair_service' WHERE business_format = '维修服务';
UPDATE contract SET business_format = 'education_training' WHERE business_format = '教育培训';
UPDATE contract SET business_format = 'medical_clinic' WHERE business_format = '医疗诊所';

UPDATE contract SET business_format = 'office_building' WHERE business_format = '写字楼';
UPDATE contract SET business_format = 'co_working' WHERE business_format = '联合办公';
UPDATE contract SET business_format = 'incubator' WHERE business_format = '创业孵化器';
UPDATE contract SET business_format = 'conference_center' WHERE business_format = '会议中心';
UPDATE contract SET business_format = 'showroom' WHERE business_format = '展示厅';

UPDATE contract SET business_format = 'hotel' WHERE business_format = '酒店';
UPDATE contract SET business_format = 'homestay' WHERE business_format = '民宿';
UPDATE contract SET business_format = 'hostel' WHERE business_format = '青年旅社';
UPDATE contract SET business_format = 'serviced_apartment' WHERE business_format = '公寓式酒店';

UPDATE contract SET business_format = 'warehouse_logistics' WHERE business_format = '仓储物流';
UPDATE contract SET business_format = 'auto_service' WHERE business_format = '汽车服务';
UPDATE contract SET business_format = 'pet_service' WHERE business_format = '宠物服务';
UPDATE contract SET business_format = 'culture_arts' WHERE business_format = '文化艺术';
UPDATE contract SET business_format = 'other' WHERE business_format = '其他';

-- 提交事务
COMMIT;

-- 显示迁移结果统计
SELECT 
    '租户表业态迁移统计' as table_name,
    business_format,
    COUNT(*) as count
FROM tenant 
WHERE business_format IS NOT NULL 
GROUP BY business_format
ORDER BY count DESC;

SELECT 
    '合同表业态迁移统计' as table_name,
    business_format,
    COUNT(*) as count
FROM contract 
WHERE business_format IS NOT NULL 
GROUP BY business_format
ORDER BY count DESC;