/**
 * 租金期间设定常量定义
 * 统一管理租金期间设定的英文代码和中文显示名称映射关系
 * 
 * @author CodeBuddy
 * @since 2025-08-16
 */

// 租金期间设定选项（用于下拉选择）
export const RENT_PERIOD_SETTING_OPTIONS = [
  { value: 'MONTH_END_CYCLE', label: '月末截止计租周期' },
  { value: 'LEASE_START_ROLLING', label: '起租日滚动周期' },
  { value: 'SPECIFIED_DATE_ROLLING', label: '指定日期滚动周期' }
] as const

// 租金期间设定映射（用于显示转换）
export const RENT_PERIOD_SETTING_MAP: Record<string, string> = {
  'MONTH_END_CYCLE': '月末截止计租周期',
  'LEASE_START_ROLLING': '起租日滚动周期',
  'SPECIFIED_DATE_ROLLING': '指定日期滚动周期'
}

// 租金期间设定标签颜色映射
export const RENT_PERIOD_SETTING_TAG_MAP: Record<string, string> = {
  'MONTH_END_CYCLE': 'primary',
  'LEASE_START_ROLLING': 'success',
  'SPECIFIED_DATE_ROLLING': 'info'
}

// 租金期间设定枚举类型
export type RentPeriodSettingCode = 'MONTH_END_CYCLE' | 'LEASE_START_ROLLING' | 'SPECIFIED_DATE_ROLLING'

/**
 * 根据租金期间设定代码获取显示名称
 * @param code 租金期间设定代码
 * @returns 中文显示名称
 */
export const getRentPeriodSettingName = (code: string): string => {
  return RENT_PERIOD_SETTING_MAP[code] || code
}

/**
 * 根据租金期间设定代码获取标签颜色
 * @param code 租金期间设定代码
 * @returns 标签颜色类型
 */
export const getRentPeriodSettingTag = (code: string): string => {
  return RENT_PERIOD_SETTING_TAG_MAP[code] || 'default'
}

/**
 * 验证租金期间设定代码是否有效
 * @param code 租金期间设定代码
 * @returns 是否有效
 */
export const isValidRentPeriodSetting = (code: string): code is RentPeriodSettingCode => {
  return Object.keys(RENT_PERIOD_SETTING_MAP).includes(code)
}

/**
 * 将中文显示名转换为英文代码（用于数据迁移兼容）
 * @param displayName 中文显示名
 * @returns 英文代码
 */
export const convertDisplayNameToCode = (displayName: string): string => {
  const reverseMap: Record<string, string> = {
    '月末截止计租周期': 'MONTH_END_CYCLE',
    '起租日滚动周期': 'LEASE_START_ROLLING',
    '指定日期滚动周期': 'SPECIFIED_DATE_ROLLING'
  }
  return reverseMap[displayName] || displayName
}