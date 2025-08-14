import request from '@/utils/request'
import type { PageResult, PageQuery } from './types'

// 提成规则接口
export interface CommissionRule {
  minAmount?: number
  maxAmount?: number
  commissionRate?: number
}

// 合同相关接口
export interface Contract {
  id?: number
  contractNo: string
  contractName: string
  contractType?: string
  projectId: number
  projectName?: string
  signatory?: string
  signatoryPhone?: string
  tenantId?: number
  tenantName?: string
  businessBrand?: string
  shopSign?: string
  businessFormat?: string
  signDate?: string
  startDate: string
  endDate: string
  buildingIds?: number[]
  floorIds?: number[]
  unitIds?: number[]
  buildingArea?: number
  rentableArea?: number
  contractArea?: number
  depositAmount?: number
  depositLatestDate?: string
  feeCompany?: string
  rentMode: string
  rentPeriodSetting?: string
  lateFeeRule?: string
  paymentAccount?: string
  paymentFrequency?: string
  latestPaymentDay?: number
  firstPaymentLatestDate?: string
  firstPeriodRent?: number
  periodRent?: number
  commissionRules?: CommissionRule[]
  contractStatus?: 'DRAFT' | 'ACTIVE' | 'EXPIRED' | 'TERMINATED' | 'UNSIGNED_EFFECTIVE' | 'SIGNED_EFFECTIVE'
  status?: number
  createTime?: string
  updateTime?: string
}

export const contractApi = {
  // 合同分页查询
  getContractPage: (params: PageQuery) => {
    return request.get<PageResult<Contract>>('/contract/page', { params })
  },
  
  // 创建合同
  createContract: (data: Contract) => {
    return request.post('/contract', data)
  },
  
  // 更新合同
  updateContract: (id: number, data: Contract) => {
    return request.put(`/contract/${id}`, data)
  },
  
  // 删除合同
  deleteContract: (id: number) => {
    return request.delete(`/contract/${id}`)
  }
}