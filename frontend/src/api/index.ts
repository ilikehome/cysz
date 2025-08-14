import request from '@/utils/request'

// 通用接口类型定义
export interface PageResult<T> {
  records: T[]
  total: number
  current: number
  size: number
}

export interface PageQuery {
  current: number
  size: number
  [key: string]: any
}

// 组织信息接口
export interface OrgInfo {
  orgCode: string
  orgName: string
  orgType: string
}

// 用户相关接口
export interface User {
  id?: number
  username: string
  password?: string
  realName?: string
  email?: string
  phone?: string
  role?: string
  status?: number
  createTime?: string
  updateTime?: string
  orgInfo?: OrgInfo
}

export const userApi = {
  // 用户登录
  login: (data: { orgCode: string; username: string; password: string }) => {
    return request.post('/api/auth/login', data)
  },
  
  // 获取用户信息
  getUserInfo: () => {
    return request.get('/api/auth/user-info')
  },
  
  // 获取组织列表
  getOrganizations: () => {
    return request.get('/api/auth/organizations')
  },
  
  // 更新用户信息
  updateUserInfo: (id: number, data: {
    realName?: string
    email?: string
    phone?: string
  }) => {
    return request.put(`/api/users/${id}`, data)
  },
  
  // 修改密码
  changeUserPassword: (id: number, data: {
    oldPassword: string
    newPassword: string
  }) => {
    return request.post(`/api/users/${id}/change-password`, null, {
      params: { oldPassword: data.oldPassword, newPassword: data.newPassword }
    })
  },
  
  // 获取用户统计信息
  getUserStatistics: () => {
    return request.get('/api/users/statistics')
  },
  
  // 用户分页查询
  getUserPage: (params: PageQuery) => {
    return request.get<PageResult<User>>('/api/users', { params })
  },
  
  // 创建用户
  createUser: (data: User) => {
    return request.post('/api/users', data)
  },
  
  // 更新用户
  updateUser: (id: number, data: User) => {
    return request.put(`/api/users/${id}`, data)
  },
  
  // 删除用户
  deleteUser: (id: number) => {
    return request.delete(`/api/users/${id}`)
  }
}

// 项目相关接口
export interface Project {
  id?: number
  projectName: string
  projectType: 'COMPLEX' | 'COMMERCIAL_DISTRICT' | 'HOTEL' | 'APARTMENT' | 'OFFICE'
  managementOrg: string
  rentBillCompany: string
  rentBillBankAccount?: string
  city: string
  address: string
  projectManager: string
  contactPhone: string
  status: number
  createTime?: string
  updateTime?: string
}

export const projectApi = {
  // 项目分页查询
  getProjectPage: (params: PageQuery) => {
    return request.get<PageResult<Project>>('/api/projects', { params })
  },
  
  // 获取所有项目列表
  getProjectList: () => {
    return request.get<Project[]>('/api/projects/list')
  },
  
  // 创建项目
  createProject: (data: Project) => {
    return request.post('/api/projects', data)
  },
  
  // 更新项目
  updateProject: (id: number, data: Project) => {
    return request.put(`/api/projects/${id}`, data)
  },
  
  // 删除项目
  deleteProject: (id: number) => {
    return request.delete(`/api/projects/${id}`)
  }
}

// 楼栋相关接口
export interface Building {
  id?: number
  buildingName: string
  buildingCode: string
  projectId: number
  projectName?: string
  buildingArea?: number
  rentArea?: number
  propertyArea?: number
  usableArea?: number
  status?: number
  createTime?: string
  updateTime?: string
}

export const buildingApi = {
  // 楼栋分页查询
  getBuildingPage: (params: PageQuery) => {
    return request.get<PageResult<Building>>('/api/buildings', { params })
  },
  
  // 根据项目ID获取楼栋列表
  getBuildingsByProject: (projectId: number) => {
    return request.get<Building[]>(`/api/buildings/by-project/${projectId}`)
  },
  
  // 创建楼栋
  createBuilding: (data: Building) => {
    return request.post('/api/buildings', data)
  },
  
  // 更新楼栋
  updateBuilding: (id: number, data: Building) => {
    return request.put(`/api/buildings/${id}`, data)
  },
  
  // 删除楼栋
  deleteBuilding: (id: number) => {
    return request.delete(`/api/buildings/${id}`)
  }
}

// 楼层相关接口
export interface Floor {
  id?: number
  buildingId: number
  buildingName?: string
  floorName: string
  floorCode: string
  remark?: string
  createTime?: string
  updateTime?: string
}

// 单元相关接口
export interface Unit {
  id?: number
  unitName: string
  unitCode: string
  floorId: number
  floorName?: string
  buildingName?: string
  projectName?: string
  unitStatus: 'RENTABLE' | 'SELF_USE' | 'PUBLIC_USE' | 'LEASE_BACK' | 'DISABLED' | 'SELF_RENTAL'
  unitPurpose?: string
  buildingArea?: number
  rentArea?: number
  isMultiTenant: boolean
  remark?: string
  status?: number
  createTime?: string
  updateTime?: string
}

export const floorApi = {
  // 楼层分页查询
  getFloorPage: (params: PageQuery) => {
    return request.get<PageResult<Floor>>('/api/floors', { params })
  },
  
  // 根据楼栋ID获取楼层列表
  getFloorsByBuilding: (buildingId: number) => {
    return request.get<Floor[]>(`/api/floors/building/${buildingId}`)
  },
  
  // 创建楼层
  createFloor: (data: Floor) => {
    return request.post('/api/floors', data)
  },
  
  // 更新楼层
  updateFloor: (id: number, data: Floor) => {
    return request.put(`/api/floors/${id}`, data)
  },
  
  // 删除楼层
  deleteFloor: (id: number) => {
    return request.delete(`/api/floors/${id}`)
  }
}

export const unitApi = {
  // 单元分页查询
  getUnitPage: (params: PageQuery) => {
    return request.get<PageResult<Unit>>('/api/units', { params })
  },
  
  // 获取单元列表
  getUnitList: (params?: {
    projectId?: number
    buildingId?: number
    unitStatus?: string
  }) => {
    return request.get('/api/units/list', { params })
  },
  
  // 创建单元
  createUnit: (data: Unit) => {
    return request.post('/api/units', data)
  },
  
  // 更新单元
  updateUnit: (id: number, data: Unit) => {
    return request.put(`/api/units/${id}`, data)
  },
  
  // 删除单元
  deleteUnit: (id: number) => {
    return request.delete(`/api/units/${id}`)
  },
  
  // 单元合并
  mergeUnits: (data: {
    sourceUnitIds: number[]
    targetUnitCode: string
    targetUnitDescription: string
    operationReason: string
  }) => {
    return request.post('/api/units/merge', data)
  },
  
  // 单元拆分
  splitUnit: (data: {
    sourceUnitId: number
    targetUnits: Array<{ unitCode: string; unitDescription: string }>
    operationReason: string
  }) => {
    return request.post('/api/units/split', data)
  }
}

// 租户相关接口
export interface Tenant {
  id?: number
  tenantCode: string
  tenantName: string
  tenantCategory?: string
  projectId?: number
  socialCreditCode?: string
  certificateType?: string
  taxpayerId?: string
  businessLicense?: string
  legalPerson?: string
  registeredAddress?: string
  contactPhone?: string
  contactEmail?: string
  status?: number
  createTime?: string
  updateTime?: string
}

export const tenantApi = {
  // 租户信息管理
  getTenantPage: (params: PageQuery) => {
    return request.get<PageResult<Tenant>>('/api/tenants', { params })
  },
  
  createTenant: (data: Tenant) => {
    return request.post('/api/tenants', data)
  },
  
  updateTenant: (id: number, data: Tenant) => {
    return request.put(`/api/tenants/${id}`, data)
  },
  
  deleteTenant: (id: number) => {
    return request.delete(`/api/tenants/${id}`)
  },
  
  // 租户风险管控
  getRiskStats: () => {
    return request.get('/api/tenants/statistics')
  },
  
  getTenantRiskList: (params: PageQuery) => {
    return request.get('/api/tenants', { params })
  },
  
  assessTenantRisk: (id: number) => {
    return request.get(`/api/tenants/${id}/risk-assessment`)
  },
  
  // 租户画像
  getTenantProfileList: (params: PageQuery) => {
    return request.get('/api/tenants', { params })
  },
  
  getTenantProfileDetail: (id: number) => {
    return request.get(`/api/tenants/${id}/profile`)
  },
  
  generateTenantProfile: (id: number) => {
    return request.get(`/api/tenants/${id}/profile`)
  },
  
  // 业态分析相关接口
  getBusinessOverview: () => {
    return request.get('/api/tenants/statistics')
  },
  
  getBusinessDetails: (params?: { businessType?: string }) => {
    return request.get('/api/tenants/statistics/business-format', { params })
  },
  
  getBusinessTrends: () => {
    return request.get('/api/tenants/statistics/business-format')
  },
  
  getBusinessSuggestions: () => {
    return request.get('/api/tenants/statistics')
  },
  
  exportBusinessReport: () => {
    return request.get('/api/tenants/export')
  }
}

// 合同相关接口
export interface Contract {
  id?: number
  contractNo: string
  contractName: string
  projectId: number
  startDate: string
  endDate: string
  signDate?: string
  signatory?: string
  contractType?: string
  contractStatus: 'DRAFT' | 'ACTIVE' | 'EXPIRED' | 'TERMINATED' | 'UNSIGNED_EFFECTIVE' | 'SIGNED_EFFECTIVE'
  rentBillCompany?: string
  propertyBillCompany?: string
  leaseNo?: string
  tenantId: number
  tenantName?: string
  unitId: number
  unitIds?: number[]
  unitDescription?: string
  rentMode?: string
  monthlyRent?: number
  deposit?: number
  status?: number
  createTime?: string
  updateTime?: string
}

export const contractApi = {
  // 合同分页查询
  getContractPage: (params: PageQuery) => {
    return request.get<PageResult<Contract>>('/api/contracts', { params })
  },
  
  // 创建合同
  createContract: (data: Contract) => {
    return request.post('/api/contracts', data)
  },
  
  // 更新合同
  updateContract: (id: number, data: Contract) => {
    return request.put(`/api/contracts/${id}`, data)
  },
  
  // 删除合同
  deleteContract: (id: number) => {
    return request.delete(`/api/contracts/${id}`)
  }
}

// 应收账款相关接口
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
    return request.get<PageResult<ReceivableAccount>>('/api/receivables', { params })
  },
  
  // 创建应收账款
  createReceivable: (data: ReceivableAccount) => {
    return request.post('/api/receivables', data)
  },
  
  // 更新应收账款
  updateReceivable: (id: number, data: ReceivableAccount) => {
    return request.put(`/api/receivables/${id}`, data)
  },
  
  // 删除应收账款
  deleteReceivable: (id: number) => {
    return request.delete(`/api/receivables/${id}`)
  },
  
  // 认领账款
  claimReceivable: (id: number, data: { claimer: string; claimDate: string }) => {
    return request.post(`/api/receivables/${id}/claim`, data)
  }
}

// 统计分析相关接口
export const statisticsApi = {
  // 获取仪表盘数据
  getDashboard: () => {
    return request.get('/statistics/dashboard')
  },
  
  // 获取项目统计数据
  getProjectStats: () => {
    return request.get('/statistics/project')
  },
  
  // 获取租户统计数据
  getTenantStats: () => {
    return request.get('/statistics/tenant')
  },
  
  // 获取单元统计数据
  getUnitStats: () => {
    return request.get('/statistics/unit')
  },
  
  // 获取合同统计数据
  getContractStats: () => {
    return request.get('/statistics/contract')
  },
  
  // 获取财务统计数据
  getFinancialStats: () => {
    return request.get('/statistics/financial')
  },
  
  // 获取运营分析数据
  getOperationStats: () => {
    return request.get('/statistics/operation')
  }
}

// 角色管理相关接口
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

// 导出合同模板相关接口
export * from './contractTemplate'

// 导出账款管理相关接口
export * from './account'
