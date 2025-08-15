/**
 * 合同类型常量定义
 * 统一管理合同类型的英文代码和中文显示名称映射关系
 * 
 * @author CodeBuddy
 * @since 2025-08-15
 */

// 合同类型选项（用于下拉选择）
export const CONTRACT_TYPE_OPTIONS = [
  { value: 'RENT', label: '租赁' },
  { value: 'SHOP', label: '商铺' },
  { value: 'OFFICE', label: '办公' },
  { value: 'OFFICE_BUILDING', label: '写字楼' },
  { value: 'COWORKING', label: '联合办公' },
  { value: 'WAREHOUSE', label: '仓库' },
  { value: 'PARKING', label: '停车位' }
] as const

// 合同类型映射（用于显示转换）
export const CONTRACT_TYPE_MAP: Record<string, string> = {
  'RENT': '租赁',
  'SHOP': '商铺',
  'OFFICE': '办公',
  'OFFICE_BUILDING': '写字楼',
  'COWORKING': '联合办公',
  'WAREHOUSE': '仓库',
  'PARKING': '停车位'
}

// 合同类型标签颜色映射
export const CONTRACT_TYPE_TAG_MAP: Record<string, string> = {
  'RENT': 'primary',
  'SHOP': 'success',
  'OFFICE': 'info',
  'OFFICE_BUILDING': 'warning',
  'COWORKING': 'danger',
  'WAREHOUSE': 'default',
  'PARKING': 'processing'
}

// 合同类型枚举类型
export type ContractTypeCode = 'RENT' | 'SHOP' | 'OFFICE' | 'OFFICE_BUILDING' | 'COWORKING' | 'WAREHOUSE' | 'PARKING'

/**
 * 根据合同类型代码获取显示名称
 * @param code 合同类型代码
 * @returns 中文显示名称
 */
export const getContractTypeName = (code: string): string => {
  return CONTRACT_TYPE_MAP[code] || code
}

/**
 * 根据合同类型代码获取标签颜色
 * @param code 合同类型代码
 * @returns 标签颜色类型
 */
export const getContractTypeTag = (code: string): string => {
  return CONTRACT_TYPE_TAG_MAP[code] || 'default'
}

/**
 * 验证合同类型代码是否有效
 * @param code 合同类型代码
 * @returns 是否有效
 */
export const isValidContractType = (code: string): code is ContractTypeCode => {
  return Object.keys(CONTRACT_TYPE_MAP).includes(code)
}