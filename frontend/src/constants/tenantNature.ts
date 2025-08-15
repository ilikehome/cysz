/**
 * 租户性质常量定义
 * 统一管理租户性质的英文代码和中文显示名称映射关系
 */

// 租户性质枚举定义
export const TENANT_NATURE = {
  INDIVIDUAL: 'individual',
  COMPANY: 'company', 
  GOVERNMENT: 'government'
} as const

// 租户性质显示名称映射
export const TENANT_NATURE_LABELS = {
  [TENANT_NATURE.INDIVIDUAL]: '个人',
  [TENANT_NATURE.COMPANY]: '公司',
  [TENANT_NATURE.GOVERNMENT]: '政府机构'
} as const

// 租户性质选项数组（用于下拉框）
export const TENANT_NATURE_OPTIONS = [
  { label: '个人', value: '个人' },
  { label: '公司', value: '公司' },
  { label: '政府机构', value: '政府机构' }
] as const

// 租户性质标签颜色映射
export const TENANT_NATURE_TAG_COLORS = {
  '个人': 'success',
  '公司': 'primary', 
  '政府机构': 'warning'
} as const

// 类型定义
export type TenantNatureCode = typeof TENANT_NATURE[keyof typeof TENANT_NATURE]
export type TenantNatureLabel = typeof TENANT_NATURE_LABELS[TenantNatureCode]
export type TenantNatureTagColor = typeof TENANT_NATURE_TAG_COLORS[keyof typeof TENANT_NATURE_TAG_COLORS]

/**
 * 将前端中文显示名称转换为数据库英文代码
 * @param displayName 中文显示名称
 * @returns 英文代码
 */
export function convertToDbCode(displayName: string): string {
  const codeEntry = Object.entries(TENANT_NATURE_LABELS).find(([_, label]) => label === displayName)
  return codeEntry ? codeEntry[0] : displayName
}

/**
 * 将数据库英文代码转换为前端中文显示名称
 * @param code 英文代码
 * @returns 中文显示名称
 */
export function convertToDisplayName(code: string): string {
  return TENANT_NATURE_LABELS[code as TenantNatureCode] || code
}

/**
 * 获取租户性质标签颜色
 * @param displayName 中文显示名称
 * @returns 标签颜色类型
 */
export function getTenantNatureTagColor(displayName: string): string {
  return TENANT_NATURE_TAG_COLORS[displayName as keyof typeof TENANT_NATURE_TAG_COLORS] || 'info'
}

/**
 * 验证租户性质代码是否有效
 * @param code 英文代码
 * @returns 是否有效
 */
export function isValidCode(code: string): boolean {
  return Object.values(TENANT_NATURE).includes(code as TenantNatureCode)
}

/**
 * 验证租户性质显示名称是否有效
 * @param displayName 中文显示名称
 * @returns 是否有效
 */
export function isValidDisplayName(displayName: string): boolean {
  return Object.values(TENANT_NATURE_LABELS).includes(displayName as TenantNatureLabel)
}