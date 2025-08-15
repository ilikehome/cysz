-- 业态字段数据验证脚本
-- 验证迁移后的数据是否正确

-- 检查租户表中的业态字段
SELECT 
    '租户表业态字段检查' as check_type,
    business_format,
    COUNT(*) as count,
    CASE 
        WHEN business_format IN (
            'department_store', 'shopping_mall', 'supermarket', 'convenience_store', 'specialty_store', 'brand_store', 'discount_store', 'duty_free_shop',
            'fine_dining', 'fast_food', 'casual_dining', 'coffee_shop', 'tea_shop', 'bar', 'bakery', 'dessert_shop',
            'cinema', 'ktv', 'game_center', 'gym', 'beauty_salon', 'spa', 'kids_playground', 'escape_room',
            'bank', 'insurance', 'telecom_office', 'courier_service', 'laundry', 'repair_service', 'education_training', 'medical_clinic',
            'office_building', 'co_working', 'incubator', 'conference_center', 'showroom',
            'hotel', 'homestay', 'hostel', 'serviced_apartment',
            'warehouse_logistics', 'auto_service', 'pet_service', 'culture_arts', 'other'
        ) THEN '有效'
        WHEN business_format IS NULL THEN '空值'
        ELSE '无效'
    END as status
FROM tenant 
GROUP BY business_format
ORDER BY count DESC;

-- 检查合同表中的业态字段
SELECT 
    '合同表业态字段检查' as check_type,
    business_format,
    COUNT(*) as count,
    CASE 
        WHEN business_format IN (
            'department_store', 'shopping_mall', 'supermarket', 'convenience_store', 'specialty_store', 'brand_store', 'discount_store', 'duty_free_shop',
            'fine_dining', 'fast_food', 'casual_dining', 'coffee_shop', 'tea_shop', 'bar', 'bakery', 'dessert_shop',
            'cinema', 'ktv', 'game_center', 'gym', 'beauty_salon', 'spa', 'kids_playground', 'escape_room',
            'bank', 'insurance', 'telecom_office', 'courier_service', 'laundry', 'repair_service', 'education_training', 'medical_clinic',
            'office_building', 'co_working', 'incubator', 'conference_center', 'showroom',
            'hotel', 'homestay', 'hostel', 'serviced_apartment',
            'warehouse_logistics', 'auto_service', 'pet_service', 'culture_arts', 'other'
        ) THEN '有效'
        WHEN business_format IS NULL THEN '空值'
        ELSE '无效'
    END as status
FROM contract 
GROUP BY business_format
ORDER BY count DESC;

-- 检查是否还有中文业态值（应该为0）
SELECT 
    '租户表中文业态检查' as check_type,
    business_format,
    COUNT(*) as count
FROM tenant 
WHERE business_format IS NOT NULL 
  AND business_format NOT IN (
    'department_store', 'shopping_mall', 'supermarket', 'convenience_store', 'specialty_store', 'brand_store', 'discount_store', 'duty_free_shop',
    'fine_dining', 'fast_food', 'casual_dining', 'coffee_shop', 'tea_shop', 'bar', 'bakery', 'dessert_shop',
    'cinema', 'ktv', 'game_center', 'gym', 'beauty_salon', 'spa', 'kids_playground', 'escape_room',
    'bank', 'insurance', 'telecom_office', 'courier_service', 'laundry', 'repair_service', 'education_training', 'medical_clinic',
    'office_building', 'co_working', 'incubator', 'conference_center', 'showroom',
    'hotel', 'homestay', 'hostel', 'serviced_apartment',
    'warehouse_logistics', 'auto_service', 'pet_service', 'culture_arts', 'other'
  )
GROUP BY business_format;

SELECT 
    '合同表中文业态检查' as check_type,
    business_format,
    COUNT(*) as count
FROM contract 
WHERE business_format IS NOT NULL 
  AND business_format NOT IN (
    'department_store', 'shopping_mall', 'supermarket', 'convenience_store', 'specialty_store', 'brand_store', 'discount_store', 'duty_free_shop',
    'fine_dining', 'fast_food', 'casual_dining', 'coffee_shop', 'tea_shop', 'bar', 'bakery', 'dessert_shop',
    'cinema', 'ktv', 'game_center', 'gym', 'beauty_salon', 'spa', 'kids_playground', 'escape_room',
    'bank', 'insurance', 'telecom_office', 'courier_service', 'laundry', 'repair_service', 'education_training', 'medical_clinic',
    'office_building', 'co_working', 'incubator', 'conference_center', 'showroom',
    'hotel', 'homestay', 'hostel', 'serviced_apartment',
    'warehouse_logistics', 'auto_service', 'pet_service', 'culture_arts', 'other'
  )
GROUP BY business_format;

-- 业态分类统计
SELECT 
    '业态分类统计' as report_type,
    CASE 
        WHEN business_format IN ('department_store', 'shopping_mall', 'supermarket', 'convenience_store', 'specialty_store', 'brand_store', 'discount_store', 'duty_free_shop') THEN '零售业态'
        WHEN business_format IN ('fine_dining', 'fast_food', 'casual_dining', 'coffee_shop', 'tea_shop', 'bar', 'bakery', 'dessert_shop') THEN '餐饮业态'
        WHEN business_format IN ('cinema', 'ktv', 'game_center', 'gym', 'beauty_salon', 'spa', 'kids_playground', 'escape_room') THEN '娱乐业态'
        WHEN business_format IN ('bank', 'insurance', 'telecom_office', 'courier_service', 'laundry', 'repair_service', 'education_training', 'medical_clinic') THEN '服务业态'
        WHEN business_format IN ('office_building', 'co_working', 'incubator', 'conference_center', 'showroom') THEN '办公业态'
        WHEN business_format IN ('hotel', 'homestay', 'hostel', 'serviced_apartment') THEN '住宿业态'
        WHEN business_format IN ('warehouse_logistics', 'auto_service', 'pet_service', 'culture_arts', 'other') THEN '其他业态'
        ELSE '未分类'
    END as category,
    COUNT(*) as tenant_count
FROM tenant 
WHERE business_format IS NOT NULL
GROUP BY category
ORDER BY tenant_count DESC;

-- 验证总结
SELECT 
    '验证总结' as summary_type,
    '租户表总记录数' as item,
    COUNT(*) as value
FROM tenant
UNION ALL
SELECT 
    '验证总结' as summary_type,
    '租户表有业态记录数' as item,
    COUNT(*) as value
FROM tenant WHERE business_format IS NOT NULL
UNION ALL
SELECT 
    '验证总结' as summary_type,
    '合同表总记录数' as item,
    COUNT(*) as value
FROM contract
UNION ALL
SELECT 
    '验证总结' as summary_type,
    '合同表有业态记录数' as item,
    COUNT(*) as value
FROM contract WHERE business_format IS NOT NULL;