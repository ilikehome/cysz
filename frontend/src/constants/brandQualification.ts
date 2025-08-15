/**
 * 品牌资质常量定义
 * 统一管理品牌资质的英文代码和中文显示名称映射关系
 */

// 品牌资质枚举定义
export const BRAND_QUALIFICATION = {
  DIRECT: 'direct',
  FRANCHISE: 'franchise',
  JOINT: 'joint'
} as const

// 品牌资质显示名称映射
export const BRAND_QUALIFICATION_LABELS = {
  [BRAND_QUALIFICATION.DIRECT]: '直营',
  [BRAND_QUALIFICATION.FRANCHISE]: '加盟',
  [BRAND_QUALIFICATION.JOINT]: '联营'
} as const

// 品牌资质选项数组（用于下拉框）
export const BRAND_QUALIFICATION_OPTIONS = [
  { label: '直营', value: '直营' },
  { label: '加盟', value: '加盟' },
  { label: '联营', value: '联营' }
] as const

// 品牌资质标签颜色映射
export const BRAND_QUALIFICATION_TAG_COLORS = {
  '直营': 'success',
  '加盟': 'warning',
  '联营': 'info'
} as const

// 类型定义
export type BrandQualificationCode = typeof BRAND_QUALIFICATION[keyof typeof BRAND_QUALIFICATION]
export type BrandQualificationLabel = typeof BRAND_QUALIFICATION_LABELS[BrandQualificationCode]
export type BrandQualificationTagColor = typeof BRAND_QUALIFICATION_TAG_COLORS[keyof typeof BRAND_QUALIFICATION_TAG_COLORS]

/**
 * 将前端中文显示名称转换为数据库英文代码
 * @param displayName 中文显示名称
 * @returns 英文代码
 */
export function convertToDbCode(displayName: string): string {
  const codeEntry = Object.entries(BRAND_QUALIFICATION_LABELS).find(([_, label]) => label === displayName)
  return codeEntry ? codeEntry[0] : displayName
}

/**
 * 将数据库英文代码转换为前端中文显示名称
 * @param code 英文代码
 * @returns 中文显示名称
 */
export function convertToDisplayName(code: string): string {
  return BRAND_QUALIFICATION_LABELS[code as BrandQualificationCode] || code
}

/**
 * 获取品牌资质标签颜色
 * @param displayName 中文显示名称
 * @returns 标签颜色类型
 */
export function getBrandQualificationTagColor(displayName: string): string {
  return BRAND_QUALIFICATION_TAG_COLORS[displayName as keyof typeof BRAND_QUALIFICATION_TAG_COLORS] || 'info'
}

/**
 * 验证品牌资质代码是否有效
 * @param code 英文代码
 * @returns 是否有效
 */
export function isValidCode(code: string): boolean {
  return Object.values(BRAND_QUALIFICATION).includes(code as BrandQualificationCode)
}

/**
 * 验证品牌资质显示名称是否有效
 * @param displayName 中文显示名称
 * @returns 是否有效
 */
export function isValidDisplayName(displayName: string): boolean {
  return Object.values(BRAND_QUALIFICATION_LABELS).includes(displayName as BrandQualificationLabel)
}