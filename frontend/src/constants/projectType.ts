/**
 * 项目类型常量定义
 * 统一管理项目类型的英文代码和中文显示名称映射关系
 * 
 * @author CodeBuddy
 * @since 2025-08-15
 */

// 项目类型选项（用于下拉选择）
export const PROJECT_TYPE_OPTIONS = [
  { value: 'COMPLEX', label: '综合体' },
  { value: 'COMMERCIAL_DISTRICT', label: '商业街区' },
  { value: 'HOTEL', label: '酒店' },
  { value: 'APARTMENT', label: '公寓' },
  { value: 'OFFICE', label: '写字楼' }
] as const

// 项目类型映射（用于显示转换）
export const PROJECT_TYPE_MAP: Record<string, string> = {
  'COMPLEX': '综合体',
  'COMMERCIAL_DISTRICT': '商业街区',
  'HOTEL': '酒店',
  'APARTMENT': '公寓',
  'OFFICE': '写字楼'
}

// 项目类型标签颜色映射
export const PROJECT_TYPE_TAG_MAP: Record<string, string> = {
  'COMPLEX': 'primary',
  'COMMERCIAL_DISTRICT': 'success',
  'HOTEL': 'warning',
  'APARTMENT': 'info',
  'OFFICE': 'danger'
}

// 项目类型枚举类型
export type ProjectTypeCode = 'COMPLEX' | 'COMMERCIAL_DISTRICT' | 'HOTEL' | 'APARTMENT' | 'OFFICE'

/**
 * 根据项目类型代码获取显示名称
 * @param code 项目类型代码
 * @returns 中文显示名称
 */
export const getProjectTypeName = (code: string): string => {
  return PROJECT_TYPE_MAP[code] || code
}

/**
 * 根据项目类型代码获取标签颜色
 * @param code 项目类型代码
 * @returns 标签颜色类型
 */
export const getProjectTypeTag = (code: string): string => {
  return PROJECT_TYPE_TAG_MAP[code] || 'info'
}

/**
 * 验证项目类型代码是否有效
 * @param code 项目类型代码
 * @returns 是否有效
 */
export const isValidProjectType = (code: string): code is ProjectTypeCode => {
  return Object.keys(PROJECT_TYPE_MAP).includes(code)
}