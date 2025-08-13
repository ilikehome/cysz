import request from '@/utils/request'

// 账款管理API接口

// 应收已收管理
export const accountApi = {
  // 获取应收账款分页数据
  getReceivablePage: (params: any) => {
    return request.get('/receivables', { params })
  },

  // 获取已收款分页数据
  getReceivedPage: (params: any) => {
    return request.get('/received', { params })
  },

  // 生成应收账款
  generateReceivable: (data: any) => {
    return request.post('/receivables/generate', data)
  },

  // 获取收款认领分页数据
  getPaymentClaimPage: (params: any) => {
    return request.get('/account/payment-claim/page', { params })
  },

  // 执行收款认领
  claimPayment: (transactionId: number, data: any) => {
    return request.post(`/received/claim`, data)
  },

  // 获取可认领的合同列表
  getClaimableContracts: () => {
    return request.get('/account/contracts/claimable')
  },

  // 取消认领
  unclaimPayment: (transactionId: number) => {
    return request.post(`/account/payment-claim/${transactionId}/unclaim`)
  },

  // 批量认领
  batchClaimPayments: (data: any) => {
    return request.post('/account/payment-claim/batch-claim', data)
  },

  // 导入银行流水
  importBankFlow: (data: FormData) => {
    return request.post('/received/import', data, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 自动匹配应收已收
  autoMatchReceivables: (data: any) => {
    return request.post('/account/receivable-received/auto-match', data)
  },

  // 导出应收账款
  exportReceivable: (params: any) => {
    return request.get('/receivables/export', { 
      params,
      responseType: 'blob'
    })
  },

  // 导出已收账款
  exportReceived: (params: any) => {
    return request.get('/received/export', { 
      params,
      responseType: 'blob'
    })
  },

  // 获取应收账款统计数据
  getReceivableStatistics: (params: any) => {
    return request.get('/receivables/statistics', { params })
  },

  // 获取已收款统计数据
  getReceivedStatistics: (params: any) => {
    return request.get('/received/statistics', { params })
  },

  // 获取应收账款详情
  getReceivableDetail: (id: number) => {
    return request.get(`/receivables/${id}`)
  },

  // 获取已收账款详情
  getReceivedDetail: (id: number) => {
    return request.get(`/received/${id}`)
  },

  // 获取银行流水详情
  getBankTransactionDetail: (id: number) => {
    return request.get(`/account/bank-transaction/${id}`)
  },

  // 更新应收账款
  updateReceivable: (id: number, data: any) => {
    return request.put(`/receivables/${id}`, data)
  },

  // 删除应收账款
  deleteReceivable: (id: number) => {
    return request.delete(`/receivables/${id}`)
  },

  // 删除已收账款
  deleteReceived: (id: number) => {
    return request.delete(`/received/${id}`)
  },

  // 手动匹配应收账款
  manualMatchReceivable: (data: any) => {
    return request.post('/receivables/manual-match', data)
  },

  // 手动匹配已收款
  manualMatchReceived: (data: any) => {
    return request.post('/received/manual-match', data)
  },

  // 自动匹配应收账款
  autoMatchReceivable: (data: any) => {
    return request.post('/receivables/auto-match', data)
  },

  // 自动匹配已收款
  autoMatchReceived: (data: any) => {
    return request.post('/received/auto-match', data)
  }
}

export default accountApi