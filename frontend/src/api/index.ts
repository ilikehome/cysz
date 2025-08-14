// 统一导出所有API接口和类型定义
// 这个文件作为API模块的统一入口

// 导出通用类型
export * from './types'

// 导出各业务模块的API
export * from './user'
export * from './project'
export * from './unit'
export * from './tenant'
export * from './contract'
export * from './statistics'

// 导出其他现有的API模块
export * from './contractTemplate'
export * from './account'
export * from './contractGenerate'
export * from './receivableAnalysis'
export * from './collectionReminder'

// 角色管理相关接口（保留在这里，因为没有对应的views文件）
import request from '@/utils/request'
import type { PageResult } from './types'

export interface Role {
  id?: number
  roleName: string
  roleCode: string
  description?: string
  permissions: string[]
  userCount?: number
  createTime?: string
  updateTime?: string
}

export interface RoleStats {
  superAdminCount: number
  tenantAdminCount: number
  userCount: number
  totalUsers: number
}

export const roleApi = {
  // 获取角色统计数据
  getRoleStats: () => {
    return request.get<RoleStats>('/role/stats')
  },
  
  // 获取角色列表
  getRoleList: () => {
    return request.get<Role[]>('/role/list')
  },
  
  // 获取角色权限详情
  getRolePermissions: (roleCode: string) => {
    return request.get<{ permissions: string[] }>(`/role/${roleCode}/permissions`)
  }
}

// 应收账款相关接口（保留在这里，因为在account目录下）
export interface ReceivableAccount {
  id?: number
  batchNo: string
  lineNo: string
  processStatus: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'REJECTED'
  company?: string
  projectId?: number
  paymentMethod?: string
  payeeName?: string
  payeeAccount?: string
  payeeBank?: string
  payee?: string
  payerName?: string
  tenantName?: string
  contractNo?: string
  payerAccount?: string
  payer?: string
  payerBankCode?: string
  transactionTime?: string
  amount?: number
  pendingAmount?: number
  inputDate?: string
  claimer?: string
  claimDate?: string
  debitCreditFlag?: 'DEBIT' | 'CREDIT'
  summary?: string
  remark?: string
  status?: number
  createTime?: string
  updateTime?: string
}

export const receivableApi = {
  // 应收账款分页查询
  getReceivablePage: (params: PageQuery) => {
    return request.get<PageResult<ReceivableAccount>>('/receivable/page', { params })
  },
  
  // 创建应收账款
  createReceivable: (data: ReceivableAccount) => {
    return request.post('/receivable', data)
  },
  
  // 更新应收账款
  updateReceivable: (id: number, data: ReceivableAccount) => {
    return request.put(`/receivable/${id}`, data)
  },
  
  // 删除应收账款
  deleteReceivable: (id: number) => {
    return request.delete(`/receivable/${id}`)
  },
  
  // 认领账款
  claimReceivable: (id: number, data: { claimer: string; claimDate: string }) => {
    return request.post(`/receivable/${id}/claim`, data)
  }
}
