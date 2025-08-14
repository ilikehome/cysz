import request from '@/utils/request'
import type { PageResult, PageQuery } from './types'

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