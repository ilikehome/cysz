import request from '@/utils/request'
import type { PageResult, PageQuery } from './types'

// 租户相关接口
export interface Tenant {
  id?: number
  tenantName: string                    // 租户名称
  tenantNature: string                  // 租户性质：个人/公司/政府机构
  enterpriseNature?: string             // 企业性质
  socialCreditCode?: string             // 社会信用代码
  taxpayerId?: string                   // 纳税人识别号
  businessRegistrationNumber?: string   // 工商注册号
  individualLicenseNumber?: string      // 个体户证件号
  brand?: string                        // 品牌
  brandQualification?: string           // 品牌资质：直营/加盟/联营
  businessFormat?: string               // 业态
  businessScope?: string                // 经营范围
  legalPersonName?: string              // 法人姓名
  legalPersonPhone?: string             // 法人手机号
  legalPersonIdCard?: string            // 法人身份证
  financeContact?: string               // 财务联系人
  financePhone?: string                 // 财务电话
  payerName?: string                    // 付款人名称
  paymentAccount?: string               // 付款账号
  remark?: string                       // 备注
  status?: number                       // 状态：0-禁用，1-启用
  createTime?: string                   // 创建时间
  updateTime?: string                   // 更新时间
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