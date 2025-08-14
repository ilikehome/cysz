import request from '@/utils/request'

// 自动催缴相关API
export const collectionReminderApi = {
  // 获取应收账款提醒
  getReceivableReminders: (reminderType: string, days: number = 7) => {
    return request.get('/api/receivables/reminders', { 
      params: { reminderType, days } 
    })
  },

  // 获取应收账款统计信息（用作催缴统计）
  getStatistics: () => {
    return request.get('/api/receivables/statistics')
  },

  // 获取应收账款分页列表（用作待催缴列表）
  getPendingReceivables: (params: {
    current?: number
    size?: number
    keyword?: string
    status?: string
    startDate?: string
    endDate?: string
  }) => {
    return request.get('/api/receivables', { params })
  },

  // 更新应收账款状态（用于催缴后更新状态）
  updateReceivableStatus: (id: number, receivedAmount: number) => {
    return request.post(`/api/receivables/${id}/update-status`, null, {
      params: { receivedAmount }
    })
  },

  // 获取应收账款分析数据（用作催缴记录分析）
  getAnalysisData: (analysisType: string, startDate: string, endDate: string, projectId?: number) => {
    const params: any = { analysisType, startDate, endDate }
    if (projectId) params.projectId = projectId
    return request.get('/api/receivables/analysis', { params })
  },

  // 批量生成应收账款（可用于催缴相关操作）
  batchGenerate: (data: any) => {
    return request.post('/api/receivables/generate-batch', data)
  },

  // 导出应收账款（可用于催缴记录导出）
  exportData: (params: any) => {
    return request.get('/api/receivables/export', { 
      params,
      responseType: 'blob'
    })
  }
}
