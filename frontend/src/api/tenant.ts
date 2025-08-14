import request from '@/utils/request'
import type { PageResult, PageQuery } from './types'

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
    return request.get<PageResult<Tenant>>('/tenant/page', { params })
  },
  
  createTenant: (data: Tenant) => {
    return request.post('/tenant', data)
  },
  
  updateTenant: (id: number, data: Tenant) => {
    return request.put(`/tenant/${id}`, data)
  },
  
  deleteTenant: (id: number) => {
    return request.delete(`/tenant/${id}`)
  },
  
  // 租户风险管控
  getRiskStats: () => {
    return request.get('/tenant/risk/stats')
  },
  
  getTenantRiskList: (params: PageQuery) => {
    return request.get('/tenant/risk/list', { params })
  },
  
  assessTenantRisk: (data: {
    tenantId: number
    creditScore: number
    businessStatus: string
    financialStatus: string
    complianceStatus: string
    riskNote?: string
  }) => {
    return request.post('/tenant/risk/assess', data)
  },
  
  // 租户画像
  getTenantProfileList: (params: PageQuery) => {
    return request.get('/tenant/profile/list', { params })
  },
  
  getTenantProfileDetail: (id: number) => {
    return request.get(`/tenant/profile/${id}`)
  },
  
  generateTenantProfile: (data: { tenantId: number }) => {
    return request.post('/tenant/profile/generate', data)
  },
  
  // 业态分析相关接口
  getBusinessOverview: () => {
    return request.get('/tenant/business-analysis/overview')
  },
  
  getBusinessDetails: (params?: { businessType?: string }) => {
    return request.get('/tenant/business-analysis/details', { params })
  },
  
  getBusinessTrends: () => {
    return request.get('/tenant/business-analysis/trends')
  },
  
  getBusinessSuggestions: () => {
    return request.get('/tenant/business-analysis/suggestions')
  },
  
  exportBusinessReport: () => {
    return request.get('/tenant/business-analysis/export')
  }
}