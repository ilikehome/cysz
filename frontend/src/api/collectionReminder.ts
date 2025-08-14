import request from '@/utils/request'
import type { ApiResponse, PageResult, PageQuery } from './types'

// 催缴设置接口
export interface CollectionSettings {
  id?: number
  autoReminderEnabled: boolean
  reminderDays: number[]
  smsTemplateId: number
  workingHours: {
    start: string
    end: string
  }
  excludeWeekends: boolean
}

// 催缴记录接口
export interface CollectionRecord {
  id?: number
  receivableId: number
  tenantName: string
  contractNo: string
  amount: number
  reminderDate: string
  reminderType: 'AUTO' | 'MANUAL'
  status: 'SENT' | 'FAILED' | 'PENDING'
  templateId: number
  createTime?: string
}

// 短信模板接口
export interface SmsTemplate {
  id?: number
  templateName: string
  templateContent: string
  templateType: 'REMINDER' | 'WARNING' | 'NOTICE'
  status: 'ACTIVE' | 'INACTIVE'
  usageCount?: number
  createTime?: string
}

// 自动催缴相关API
export const collectionReminderApi = {
  // 获取催缴设置
  getSettings: () => {
    return request.get<ApiResponse<CollectionSettings>>('/collection-reminder/settings')
  },

  // 更新催缴设置
  updateSettings: (settings: CollectionSettings) => {
    return request.post<ApiResponse<CollectionSettings>>('/collection-reminder/settings', settings)
  },

  // 获取催缴记录列表
  getRecords: (params: PageQuery & {
    keyword?: string
    status?: string
    startDate?: string
    endDate?: string
  }) => {
    return request.get<ApiResponse<PageResult<CollectionRecord>>>('/collection-reminder/records', { params })
  },

  // 手动发送催缴短信
  sendManualReminder: (data: {
    receivableIds: number[]
    templateId: number
  }) => {
    return request.post<ApiResponse<void>>('/collection-reminder/send-manual', data)
  },

  // 获取催缴统计数据
  getStatistics: () => {
    return request.get<ApiResponse<any>>('/collection-reminder/statistics')
  },

  // 获取待催缴的应收款列表
  getPendingReceivables: (params: PageQuery) => {
    return request.get<ApiResponse<PageResult<any>>>('/collection-reminder/pending-receivables', { params })
  },

  // 批量设置催缴优先级
  setPriority: (data: {
    receivableIds: number[]
    priority: string
  }) => {
    return request.post<ApiResponse<void>>('/collection-reminder/set-priority', data)
  },

  // 获取短信模板列表
  getSmsTemplates: () => {
    return request.get<ApiResponse<SmsTemplate[]>>('/collection-reminder/sms-templates')
  },

  // 保存短信模板
  saveSmsTemplate: (template: SmsTemplate) => {
    return request.post<ApiResponse<SmsTemplate>>('/collection-reminder/sms-templates', template)
  }
}
