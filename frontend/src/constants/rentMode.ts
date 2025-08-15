/**
 * 租金模式常量定义
 * 
 * @author CodeBuddy
 * @since 2025-01-16
 */

/**
 * 租金模式代码常量
 */
export const RENT_MODE = {
  /** 固定租金 */
  FIXED: 'fixed',
  /** 提成租金 */
  COMMISSION: 'commission'
} as const

/**
 * 租金模式类型
 */
export type RentModeType = typeof RENT_MODE[keyof typeof RENT_MODE]

/**
 * 租金模式选项配置
 */
export const RENT_MODE_OPTIONS = [
  { label: '固定租金', value: RENT_MODE.FIXED },
  { label: '提成租金', value: RENT_MODE.COMMISSION }
] as const

/**
 * 根据代码获取中文显示名
 * 
 * @param code 租金模式代码
 * @returns 中文显示名
 */
export const getRentModeLabel = (code: string): string => {
  const option = RENT_MODE_OPTIONS.find(item => item.value === code)
  return option?.label || code
}

/**
 * 验证租金模式代码是否有效
 * 
 * @param code 租金模式代码
 * @returns 是否有效
 */
export const isValidRentMode = (code: string): boolean => {
  return Object.values(RENT_MODE).includes(code as RentModeType)
}

/**
 * 获取所有租金模式代码
 * 
 * @returns 所有租金模式代码数组
 */
export const getAllRentModeCodes = (): string[] => {
  return Object.values(RENT_MODE)
}