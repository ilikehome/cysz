/**
 * 合同状态常量定义
 * 定义合同状态的英文代码与中文显示名映射关系
 */

// 合同状态常量
export const CONTRACT_STATUS = {
  UNSIGNED_EFFECTIVE: 'unsigned_effective',
  SIGNED_EFFECTIVE: 'signed_effective', 
  TERMINATED: 'terminated',
  EXPIRED: 'expired'
} as const

// 合同状态标签映射
export const CONTRACT_STATUS_LABELS = {
  [CONTRACT_STATUS.UNSIGNED_EFFECTIVE]: '未盖章生效',
  [CONTRACT_STATUS.SIGNED_EFFECTIVE]: '已盖章生效',
  [CONTRACT_STATUS.TERMINATED]: '提前终止',
  [CONTRACT_STATUS.EXPIRED]: '自然到期'
} as const

// 合同状态选项（用于下拉选择）
export const CONTRACT_STATUS_OPTIONS = [
  { label: '未盖章生效', value: CONTRACT_STATUS.UNSIGNED_EFFECTIVE },
  { label: '已盖章生效', value: CONTRACT_STATUS.SIGNED_EFFECTIVE },
  { label: '提前终止', value: CONTRACT_STATUS.TERMINATED },
  { label: '自然到期', value: CONTRACT_STATUS.EXPIRED }
]

// 合同状态标签颜色映射
export const CONTRACT_STATUS_TAG_COLORS = {
  [CONTRACT_STATUS.UNSIGNED_EFFECTIVE]: 'warning',
  [CONTRACT_STATUS.SIGNED_EFFECTIVE]: 'success',
  [CONTRACT_STATUS.TERMINATED]: 'danger',
  [CONTRACT_STATUS.EXPIRED]: 'info'
} as const

/**
 * 根据合同状态代码获取中文显示名
 */
export function getContractStatusLabel(code: string): string {
  return CONTRACT_STATUS_LABELS[code as keyof typeof CONTRACT_STATUS_LABELS] || code
}

/**
 * 根据合同状态代码获取标签颜色
 */
export function getContractStatusTagColor(code: string): string {
  return CONTRACT_STATUS_TAG_COLORS[code as keyof typeof CONTRACT_STATUS_TAG_COLORS] || 'default'
}

/**
 * 验证合同状态代码是否有效
 */
export function isValidContractStatus(code: string): boolean {
  return Object.values(CONTRACT_STATUS).includes(code as any)
}

/**
 * 获取所有合同状态代码
 */
export function getAllContractStatusCodes(): string[] {
  return Object.values(CONTRACT_STATUS)
}

/**
 * 获取所有合同状态显示名
 */
export function getAllContractStatusLabels(): string[] {
  return Object.values(CONTRACT_STATUS_LABELS)
}

// 类型定义
export type ContractStatusCode = typeof CONTRACT_STATUS[keyof typeof CONTRACT_STATUS]
export type ContractStatusLabel = typeof CONTRACT_STATUS_LABELS[keyof typeof CONTRACT_STATUS_LABELS]