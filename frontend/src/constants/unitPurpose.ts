/**
 * 单元用途相关常量和工具函数
 */

// 单元用途选项
export const UNIT_PURPOSE_OPTIONS = [
  { value: 'OFFICE', label: '办公' },
  { value: 'WAREHOUSE', label: '仓库' },
  { value: 'FREIGHT', label: '货运' },
  { value: 'COMMERCIAL', label: '商业' },
  { value: 'MEETING_ROOM', label: '会议室' },
  { value: 'BUSINESS_ROOM', label: '营业房' },
  { value: 'PARKING', label: '停车位' },
  { value: 'ADVERTISING', label: '广告位' },
  { value: 'APARTMENT', label: '公寓' },
  { value: 'MULTI_BUSINESS', label: '多经点位' },
  { value: 'PROMOTION_VENUE', label: '推广场地' }
] as const

// 单元用途枚举类型
export type UnitPurpose = typeof UNIT_PURPOSE_OPTIONS[number]['value']

// 单元用途映射对象
export const UNIT_PURPOSE_MAP = Object.fromEntries(
  UNIT_PURPOSE_OPTIONS.map(item => [item.value, item.label])
) as Record<UnitPurpose, string>

/**
 * 根据用途代码获取显示名称
 * @param purpose 用途代码
 * @returns 显示名称
 */
export const getUnitPurposeName = (purpose: string | null | undefined): string => {
  if (!purpose) return '-'
  return UNIT_PURPOSE_MAP[purpose as UnitPurpose] || purpose
}

/**
 * 检查用途代码是否有效
 * @param purpose 用途代码
 * @returns 是否有效
 */
export const isValidUnitPurpose = (purpose: string): purpose is UnitPurpose => {
  return UNIT_PURPOSE_OPTIONS.some(option => option.value === purpose)
}