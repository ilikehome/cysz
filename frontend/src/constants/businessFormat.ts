/**
 * 业态枚举常量
 * 与后端 BusinessFormat 枚举保持一致
 */

// 业态代码常量
export const BUSINESS_FORMAT = {
  // 零售业态
  DEPARTMENT_STORE: 'department_store',
  SHOPPING_MALL: 'shopping_mall',
  SUPERMARKET: 'supermarket',
  CONVENIENCE_STORE: 'convenience_store',
  SPECIALTY_STORE: 'specialty_store',
  BRAND_STORE: 'brand_store',
  DISCOUNT_STORE: 'discount_store',
  DUTY_FREE_SHOP: 'duty_free_shop',
  
  // 餐饮业态
  FINE_DINING: 'fine_dining',
  FAST_FOOD: 'fast_food',
  CASUAL_DINING: 'casual_dining',
  COFFEE_SHOP: 'coffee_shop',
  TEA_SHOP: 'tea_shop',
  BAR: 'bar',
  BAKERY: 'bakery',
  DESSERT_SHOP: 'dessert_shop',
  
  // 娱乐业态
  CINEMA: 'cinema',
  KTV: 'ktv',
  GAME_CENTER: 'game_center',
  GYM: 'gym',
  BEAUTY_SALON: 'beauty_salon',
  SPA: 'spa',
  KIDS_PLAYGROUND: 'kids_playground',
  ESCAPE_ROOM: 'escape_room',
  
  // 服务业态
  BANK: 'bank',
  INSURANCE: 'insurance',
  TELECOM_OFFICE: 'telecom_office',
  COURIER_SERVICE: 'courier_service',
  LAUNDRY: 'laundry',
  REPAIR_SERVICE: 'repair_service',
  EDUCATION_TRAINING: 'education_training',
  MEDICAL_CLINIC: 'medical_clinic',
  
  // 办公业态
  OFFICE_BUILDING: 'office_building',
  CO_WORKING: 'co_working',
  INCUBATOR: 'incubator',
  CONFERENCE_CENTER: 'conference_center',
  SHOWROOM: 'showroom',
  
  // 住宿业态
  HOTEL: 'hotel',
  HOMESTAY: 'homestay',
  HOSTEL: 'hostel',
  SERVICED_APARTMENT: 'serviced_apartment',
  
  // 其他业态
  WAREHOUSE_LOGISTICS: 'warehouse_logistics',
  AUTO_SERVICE: 'auto_service',
  PET_SERVICE: 'pet_service',
  CULTURE_ARTS: 'culture_arts',
  OTHER: 'other'
} as const

// 业态显示名称映射
export const BUSINESS_FORMAT_LABELS: Record<string, string> = {
  // 零售业态
  [BUSINESS_FORMAT.DEPARTMENT_STORE]: '百货商场',
  [BUSINESS_FORMAT.SHOPPING_MALL]: '购物中心',
  [BUSINESS_FORMAT.SUPERMARKET]: '超市',
  [BUSINESS_FORMAT.CONVENIENCE_STORE]: '便利店',
  [BUSINESS_FORMAT.SPECIALTY_STORE]: '专卖店',
  [BUSINESS_FORMAT.BRAND_STORE]: '品牌店',
  [BUSINESS_FORMAT.DISCOUNT_STORE]: '折扣店',
  [BUSINESS_FORMAT.DUTY_FREE_SHOP]: '免税店',
  
  // 餐饮业态
  [BUSINESS_FORMAT.FINE_DINING]: '正餐',
  [BUSINESS_FORMAT.FAST_FOOD]: '快餐',
  [BUSINESS_FORMAT.CASUAL_DINING]: '休闲餐饮',
  [BUSINESS_FORMAT.COFFEE_SHOP]: '咖啡厅',
  [BUSINESS_FORMAT.TEA_SHOP]: '茶饮店',
  [BUSINESS_FORMAT.BAR]: '酒吧',
  [BUSINESS_FORMAT.BAKERY]: '烘焙店',
  [BUSINESS_FORMAT.DESSERT_SHOP]: '甜品店',
  
  // 娱乐业态
  [BUSINESS_FORMAT.CINEMA]: '电影院',
  [BUSINESS_FORMAT.KTV]: 'KTV',
  [BUSINESS_FORMAT.GAME_CENTER]: '游戏厅',
  [BUSINESS_FORMAT.GYM]: '健身房',
  [BUSINESS_FORMAT.BEAUTY_SALON]: '美容美发',
  [BUSINESS_FORMAT.SPA]: 'SPA',
  [BUSINESS_FORMAT.KIDS_PLAYGROUND]: '儿童乐园',
  [BUSINESS_FORMAT.ESCAPE_ROOM]: '密室逃脱',
  
  // 服务业态
  [BUSINESS_FORMAT.BANK]: '银行',
  [BUSINESS_FORMAT.INSURANCE]: '保险',
  [BUSINESS_FORMAT.TELECOM_OFFICE]: '通讯营业厅',
  [BUSINESS_FORMAT.COURIER_SERVICE]: '快递服务',
  [BUSINESS_FORMAT.LAUNDRY]: '洗衣店',
  [BUSINESS_FORMAT.REPAIR_SERVICE]: '维修服务',
  [BUSINESS_FORMAT.EDUCATION_TRAINING]: '教育培训',
  [BUSINESS_FORMAT.MEDICAL_CLINIC]: '医疗诊所',
  
  // 办公业态
  [BUSINESS_FORMAT.OFFICE_BUILDING]: '写字楼',
  [BUSINESS_FORMAT.CO_WORKING]: '联合办公',
  [BUSINESS_FORMAT.INCUBATOR]: '创业孵化器',
  [BUSINESS_FORMAT.CONFERENCE_CENTER]: '会议中心',
  [BUSINESS_FORMAT.SHOWROOM]: '展示厅',
  
  // 住宿业态
  [BUSINESS_FORMAT.HOTEL]: '酒店',
  [BUSINESS_FORMAT.HOMESTAY]: '民宿',
  [BUSINESS_FORMAT.HOSTEL]: '青年旅社',
  [BUSINESS_FORMAT.SERVICED_APARTMENT]: '公寓式酒店',
  
  // 其他业态
  [BUSINESS_FORMAT.WAREHOUSE_LOGISTICS]: '仓储物流',
  [BUSINESS_FORMAT.AUTO_SERVICE]: '汽车服务',
  [BUSINESS_FORMAT.PET_SERVICE]: '宠物服务',
  [BUSINESS_FORMAT.CULTURE_ARTS]: '文化艺术',
  [BUSINESS_FORMAT.OTHER]: '其他'
}

// 业态分组选项（用于下拉框）
export const BUSINESS_FORMAT_OPTIONS = [
  {
    label: '零售业态',
    options: [
      { label: '百货商场', value: BUSINESS_FORMAT.DEPARTMENT_STORE },
      { label: '购物中心', value: BUSINESS_FORMAT.SHOPPING_MALL },
      { label: '超市', value: BUSINESS_FORMAT.SUPERMARKET },
      { label: '便利店', value: BUSINESS_FORMAT.CONVENIENCE_STORE },
      { label: '专卖店', value: BUSINESS_FORMAT.SPECIALTY_STORE },
      { label: '品牌店', value: BUSINESS_FORMAT.BRAND_STORE },
      { label: '折扣店', value: BUSINESS_FORMAT.DISCOUNT_STORE },
      { label: '免税店', value: BUSINESS_FORMAT.DUTY_FREE_SHOP }
    ]
  },
  {
    label: '餐饮业态',
    options: [
      { label: '正餐', value: BUSINESS_FORMAT.FINE_DINING },
      { label: '快餐', value: BUSINESS_FORMAT.FAST_FOOD },
      { label: '休闲餐饮', value: BUSINESS_FORMAT.CASUAL_DINING },
      { label: '咖啡厅', value: BUSINESS_FORMAT.COFFEE_SHOP },
      { label: '茶饮店', value: BUSINESS_FORMAT.TEA_SHOP },
      { label: '酒吧', value: BUSINESS_FORMAT.BAR },
      { label: '烘焙店', value: BUSINESS_FORMAT.BAKERY },
      { label: '甜品店', value: BUSINESS_FORMAT.DESSERT_SHOP }
    ]
  },
  {
    label: '娱乐业态',
    options: [
      { label: '电影院', value: BUSINESS_FORMAT.CINEMA },
      { label: 'KTV', value: BUSINESS_FORMAT.KTV },
      { label: '游戏厅', value: BUSINESS_FORMAT.GAME_CENTER },
      { label: '健身房', value: BUSINESS_FORMAT.GYM },
      { label: '美容美发', value: BUSINESS_FORMAT.BEAUTY_SALON },
      { label: 'SPA', value: BUSINESS_FORMAT.SPA },
      { label: '儿童乐园', value: BUSINESS_FORMAT.KIDS_PLAYGROUND },
      { label: '密室逃脱', value: BUSINESS_FORMAT.ESCAPE_ROOM }
    ]
  },
  {
    label: '服务业态',
    options: [
      { label: '银行', value: BUSINESS_FORMAT.BANK },
      { label: '保险', value: BUSINESS_FORMAT.INSURANCE },
      { label: '通讯营业厅', value: BUSINESS_FORMAT.TELECOM_OFFICE },
      { label: '快递服务', value: BUSINESS_FORMAT.COURIER_SERVICE },
      { label: '洗衣店', value: BUSINESS_FORMAT.LAUNDRY },
      { label: '维修服务', value: BUSINESS_FORMAT.REPAIR_SERVICE },
      { label: '教育培训', value: BUSINESS_FORMAT.EDUCATION_TRAINING },
      { label: '医疗诊所', value: BUSINESS_FORMAT.MEDICAL_CLINIC }
    ]
  },
  {
    label: '办公业态',
    options: [
      { label: '写字楼', value: BUSINESS_FORMAT.OFFICE_BUILDING },
      { label: '联合办公', value: BUSINESS_FORMAT.CO_WORKING },
      { label: '创业孵化器', value: BUSINESS_FORMAT.INCUBATOR },
      { label: '会议中心', value: BUSINESS_FORMAT.CONFERENCE_CENTER },
      { label: '展示厅', value: BUSINESS_FORMAT.SHOWROOM }
    ]
  },
  {
    label: '住宿业态',
    options: [
      { label: '酒店', value: BUSINESS_FORMAT.HOTEL },
      { label: '民宿', value: BUSINESS_FORMAT.HOMESTAY },
      { label: '青年旅社', value: BUSINESS_FORMAT.HOSTEL },
      { label: '公寓式酒店', value: BUSINESS_FORMAT.SERVICED_APARTMENT }
    ]
  },
  {
    label: '其他业态',
    options: [
      { label: '仓储物流', value: BUSINESS_FORMAT.WAREHOUSE_LOGISTICS },
      { label: '汽车服务', value: BUSINESS_FORMAT.AUTO_SERVICE },
      { label: '宠物服务', value: BUSINESS_FORMAT.PET_SERVICE },
      { label: '文化艺术', value: BUSINESS_FORMAT.CULTURE_ARTS },
      { label: '其他', value: BUSINESS_FORMAT.OTHER }
    ]
  }
]

// 扁平化的业态选项（不分组）
export const BUSINESS_FORMAT_FLAT_OPTIONS = BUSINESS_FORMAT_OPTIONS.flatMap(group => group.options)

/**
 * 根据业态代码获取显示名称
 */
export function getBusinessFormatLabel(code: string): string {
  return BUSINESS_FORMAT_LABELS[code] || code
}

/**
 * 根据显示名称获取业态代码（用于数据迁移）
 */
export function getBusinessFormatCode(label: string): string | undefined {
  const entry = Object.entries(BUSINESS_FORMAT_LABELS).find(([, value]) => value === label)
  return entry?.[0]
}

/**
 * 验证业态代码是否有效
 */
export function isValidBusinessFormatCode(code: string): boolean {
  return Object.values(BUSINESS_FORMAT).includes(code as any)
}

/**
 * 获取业态分类
 */
export function getBusinessFormatCategory(code: string): string {
  const group = BUSINESS_FORMAT_OPTIONS.find(group => 
    group.options.some(option => option.value === code)
  )
  return group?.label || '其他业态'
}