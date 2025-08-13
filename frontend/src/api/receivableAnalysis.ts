import request from '@/utils/request'

// 账款分析相关API
export const receivableAnalysisApi = {
  // 获取应收账款统计信息
  getReceivableStatistics: () => {
    return request.get('/receivables/statistics')
  },

  // 获取已收款统计信息
  getReceivedStatistics: () => {
    return request.get('/received/statistics')
  },

  // 获取应收账款分析数据
  getReceivableAnalysisData: (analysisType: string, startDate: string, endDate: string, projectId?: number) => {
    const params: any = { analysisType, startDate, endDate }
    if (projectId) params.projectId = projectId
    return request.get('/receivables/analysis', { params })
  },

  // 获取已收款分析数据
  getReceivedAnalysisData: (analysisType: string, startDate: string, endDate: string) => {
    const params = { analysisType, startDate, endDate }
    return request.get('/received/analysis', { params })
  },

  // 获取已收款趋势统计
  getReceivedTrendStatistics: (year?: number) => {
    const params: any = {}
    if (year) params.year = year
    return request.get('/received/statistics/trend', { params })
  },

  // 根据收款方式统计已收款
  getReceivedStatisticsByPaymentMethod: () => {
    return request.get('/received/statistics/payment-method')
  },

  // 根据匹配状态统计已收款
  getReceivedStatisticsByMatchStatus: () => {
    return request.get('/received/statistics/match-status')
  },

  // 收款人统计
  getPayerStatistics: (startDate?: string, endDate?: string) => {
    const params: any = {}
    if (startDate) params.startDate = startDate
    if (endDate) params.endDate = endDate
    return request.get('/received/payer-statistics', { params })
  },

  // 收款对账报表
  getReconciliationReport: (startDate: string, endDate: string, projectId?: number) => {
    const params: any = { startDate, endDate }
    if (projectId) params.projectId = projectId
    return request.get('/received/reconciliation-report', { params })
  },

  // 获取项目列表
  getProjects: () => {
    return request.get('/projects')
  }
}