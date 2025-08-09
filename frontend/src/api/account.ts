import request from '@/utils/request'

// 账款管理API接口

// 应收已收管理
export const accountApi = {
  // 获取应收已收分页数据
  getReceivableReceivedPage: (params: any) => {
    return request.get('/account/receivable-received/page', { params })
  },

  // 生成应收账款
  generateReceivable: (data: any) => {
    return request.post('/account/receivable/generate', data)
  },

  // 获取收款认领分页数据
  getPaymentClaimPage: (params: any) => {
    return request.get('/account/payment-claim/page', { params })
  },

  // 执行收款认领
  claimPayment: (transactionId: number, data: any) => {
    return request.post(`/account/payment-claim/${transactionId}/claim`, data)
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
  importBankTransactions: (data: any) => {
    return request.post('/account/bank-transactions/import', data)
  },

  // 自动匹配应收已收
  autoMatchReceivables: (data: any) => {
    return request.post('/account/receivable-received/auto-match', data)
  },

  // 导出应收已收数据
  exportReceivableReceived: (params: any) => {
    return request.get('/account/receivable-received/export', { params, responseType: 'blob' })
  },

  // 导出收款认领数据
  exportPaymentClaim: (params: any) => {
    return request.get('/account/payment-claim/export', { params, responseType: 'blob' })
  },

  // 获取账款统计数据
  getAccountStatistics: () => {
    return request.get('/account/statistics')
  },

  // 获取应收账款详情
  getReceivableDetail: (id: number) => {
    return request.get(`/account/receivable/${id}`)
  },

  // 获取已收账款详情
  getReceivedDetail: (id: number) => {
    return request.get(`/account/received/${id}`)
  },

  // 获取银行流水详情
  getBankTransactionDetail: (id: number) => {
    return request.get(`/account/bank-transaction/${id}`)
  },

  // 更新应收账款
  updateReceivable: (id: number, data: any) => {
    return request.put(`/account/receivable/${id}`, data)
  },

  // 删除应收账款
  deleteReceivable: (id: number) => {
    return request.delete(`/account/receivable/${id}`)
  },

  // 删除已收账款
  deleteReceived: (id: number) => {
    return request.delete(`/account/received/${id}`)
  },

  // 手动匹配应收已收
  manualMatchReceivable: (receivableId: number, receivedId: number) => {
    return request.post('/account/receivable-received/manual-match', {
      receivableId,
      receivedId
    })
  },

  // 取消匹配
  unmatchReceivable: (matchId: number) => {
    return request.post(`/account/receivable-received/unmatch/${matchId}`)
  }
}

export default accountApi