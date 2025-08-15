/**
 * 单元资产状态常量定义
 */

// 单元状态枚举
export enum UnitStatus {
  RENTABLE = 'RENTABLE',
  SELF_USE = 'SELF_USE',
  PUBLIC_USE = 'PUBLIC_USE',
  LEASE_BACK = 'LEASE_BACK',
  DISABLED = 'DISABLED',
  SELF_RENTAL = 'SELF_RENTAL'
}

// 单元状态显示名称映射
export const UNIT_STATUS_NAMES: Record<string, string> = {
  [UnitStatus.RENTABLE]: '可租',
  [UnitStatus.SELF_USE]: '自用',
  [UnitStatus.PUBLIC_USE]: '公用',
  [UnitStatus.LEASE_BACK]: '返租',
  [UnitStatus.DISABLED]: '停用',
  [UnitStatus.SELF_RENTAL]: '自持出租'
}

// 单元状态标签颜色映射
export const UNIT_STATUS_TAG_COLORS: Record<string, string> = {
  [UnitStatus.RENTABLE]: 'success',
  [UnitStatus.SELF_USE]: 'primary',
  [UnitStatus.PUBLIC_USE]: 'info',
  [UnitStatus.LEASE_BACK]: 'warning',
  [UnitStatus.DISABLED]: 'danger',
  [UnitStatus.SELF_RENTAL]: 'success'
}

// 单元状态选项列表（用于下拉框）
export const UNIT_STATUS_OPTIONS = [
  { label: UNIT_STATUS_NAMES[UnitStatus.RENTABLE], value: UnitStatus.RENTABLE },
  { label: UNIT_STATUS_NAMES[UnitStatus.SELF_USE], value: UnitStatus.SELF_USE },
  { label: UNIT_STATUS_NAMES[UnitStatus.PUBLIC_USE], value: UnitStatus.PUBLIC_USE },
  { label: UNIT_STATUS_NAMES[UnitStatus.LEASE_BACK], value: UnitStatus.LEASE_BACK },
  { label: UNIT_STATUS_NAMES[UnitStatus.DISABLED], value: UnitStatus.DISABLED },
  { label: UNIT_STATUS_NAMES[UnitStatus.SELF_RENTAL], value: UnitStatus.SELF_RENTAL }
]

/**
 * 获取单元状态显示名称
 */
export const getUnitStatusName = (status: string): string => {
  return UNIT_STATUS_NAMES[status] || status
}

/**
 * 获取单元状态标签颜色
 */
export const getUnitStatusTagColor = (status: string): string => {
  return UNIT_STATUS_TAG_COLORS[status] || 'info'
}

/**
 * 检查单元状态是否有效
 */
export const isValidUnitStatus = (status: string): boolean => {
  return Object.values(UnitStatus).includes(status as UnitStatus)
}