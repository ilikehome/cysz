import axios from 'axios'

// 自动催缴相关API
export const collectionReminderApi = {
  // 获取催缴设置
  getSettings: () => {
    return axios.get('/api/collection-reminder/settings')
  },

  // 更新催缴设置
  updateSettings: (settings: any) => {
    return axios.post('/api/collection-reminder/settings', settings)
  },

  // 获取催缴记录列表
  getRecords: (params: {
    current?: number
    size?: number
    keyword?: string
    status?: string
    startDate?: string
    endDate?: string
  }) => {
    return axios.get('/api/collection-reminder/records', { params })
  },

  // 手动发送催缴短信
  sendManualReminder: (data: {
    receivableIds: number[]
    templateId: number
  }) => {
    return axios.post('/api/collection-reminder/send-manual', data)
  },

  // 获取催缴统计数据
  getStatistics: () => {
    return axios.get('/api/collection-reminder/statistics')
  },

  // 获取待催缴的应收款列表
  getPendingReceivables: (params: {
    current?: number
    size?: number
  }) => {
    return axios.get('/api/collection-reminder/pending-receivables', { params })
  },

  // 批量设置催缴优先级
  setPriority: (data: {
    receivableIds: number[]
    priority: string
  }) => {
    return axios.post('/api/collection-reminder/set-priority', data)
  },

  // 获取短信模板列表
  getSmsTemplates: () => {
    return axios.get('/api/collection-reminder/sms-templates')
  },

  // 保存短信模板
  saveSmsTemplate: (template: any) => {
    return axios.post('/api/collection-reminder/sms-templates', template)
  }
}