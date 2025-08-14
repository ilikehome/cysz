import request from '@/utils/request'

// 账务管理相关API
export const accountApi = {
  // 获取付款认领列表
  getPaymentClaimList: (params: any) => {
    return request.get('/api/account/payment-claims', { params })
  },

  // 创建付款认领
  createPaymentClaim: (data: any) => {
    return request.post('/api/account/payment-claims', data)
  },

  // 更新付款认领
  updatePaymentClaim: (id: number, data: any) => {
    return request.put(`/api/account/payment-claims/${id}`, data)
  },

  // 删除付款认领
  deletePaymentClaim: (id: number) => {
    return request.delete(`/api/account/payment-claims/${id}`)
  },

  // 获取应收已收列表
  getReceivableReceivedList: (params: any) => {
    return request.get('/api/account/receivable-received', { params })
  },

  // 创建应收已收记录
  createReceivableReceived: (data: any) => {
    return request.post('/api/account/receivable-received', data)
  },

  // 更新应收已收记录
  updateReceivableReceived: (id: number, data: any) => {
    return request.put(`/api/account/receivable-received/${id}`, data)
  },

  // 删除应收已收记录
  deleteReceivableReceived: (id: number) => {
    return request.delete(`/api/account/receivable-received/${id}`)
  }
}