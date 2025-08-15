/**
 * 付款频率常量定义
 * 统一管理付款频率的英文代码和中文显示名称映射关系
 * 
 * @author CodeBuddy
 * @since 2025-08-16
 */

// 付款频率选项（用于下拉选择）
export const PAYMENT_FREQUENCY_OPTIONS = [
  { value: 'MONTHLY', label: '月' },
  { value: 'BI_MONTHLY', label: '双月' },
  { value: 'QUARTERLY', label: '季度' },
  { value: 'SEMI_ANNUALLY', label: '半年' },
  { value: 'ANNUALLY', label: '年' }
] as const

// 付款频率映射（用于显示转换）
export const PAYMENT_FREQUENCY_MAP: Record<string, string> = {
  'MONTHLY': '月',
  'BI_MONTHLY': '双月',
  'QUARTERLY': '季度',
  'SEMI_ANNUALLY': '半年',
  'ANNUALLY': '年'
}

// 付款频率标签颜色映射
export const PAYMENT_FREQUENCY_TAG_MAP: Record<string, string> = {
  'MONTHLY': 'primary',
  'BI_MONTHLY': 'success',
  'QUARTERLY': 'info',
  'SEMI_ANNUALLY': 'warning',
  'ANNUALLY': 'danger'
}

// 付款频率枚举类型
export type PaymentFrequencyCode = 'MONTHLY' | 'BI_MONTHLY' | 'QUARTERLY' | 'SEMI_ANNUALLY' | 'ANNUALLY'

/**
 * 根据付款频率代码获取显示名称
 * @param code 付款频率代码
 * @returns 中文显示名称
 */
export const getPaymentFrequencyName = (code: string): string => {
  return PAYMENT_FREQUENCY_MAP[code] || code
}

/**
 * 根据付款频率代码获取标签颜色
 * @param code 付款频率代码
 * @returns 标签颜色类型
 */
export const getPaymentFrequencyTag = (code: string): string => {
  return PAYMENT_FREQUENCY_TAG_MAP[code] || 'default'
}

/**
 * 验证付款频率代码是否有效
 * @param code 付款频率代码
 * @returns 是否有效
 */
export const isValidPaymentFrequency = (code: string): code is PaymentFrequencyCode => {
  return Object.keys(PAYMENT_FREQUENCY_MAP).includes(code)
}

/**
 * 将中文显示名转换为英文代码（用于数据迁移兼容）
 * @param displayName 中文显示名
 * @returns 英文代码
 */
export const convertDisplayNameToCode = (displayName: string): string => {
  const reverseMap: Record<string, string> = {
    '月': 'MONTHLY',
    '双月': 'BI_MONTHLY',
    '季度': 'QUARTERLY',
    '半年': 'SEMI_ANNUALLY',
    '年': 'ANNUALLY'
  }
  return reverseMap[displayName] || displayName
}