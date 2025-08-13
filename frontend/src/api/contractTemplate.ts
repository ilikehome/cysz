import request from '@/utils/request'

export interface ContractTemplate {
  id?: number
  templateName: string
  templateType: 'LEASE' | 'SERVICE' | 'SALES' | 'OTHER'
  version: string
  status: 'ACTIVE' | 'INACTIVE'
  description?: string
  templateContent: string
  usageCount?: number
  fields?: ContractField[]
  createTime?: string
  updateTime?: string
}

export interface ContractField {
  fieldName: string
  fieldLabel: string
  fieldType: 'text' | 'number' | 'date' | 'select'
  required: boolean
  defaultValue?: string
  options?: string
}

export interface ContractTemplatePageParams {
  current: number
  size: number
  keyword?: string
  templateType?: string
  status?: string
}

export interface PreviewRequest {
  templateContent: string
  variables?: Record<string, string>
}

export const contractTemplateApi = {
  // 分页查询合同（作为模板列表）
  getTemplatePage: (params: ContractTemplatePageParams) => {
    return request.get('/contracts', { params })
  },

  // 根据ID查询合同详情（作为模板详情）
  getTemplateById: (id: number) => {
    return request.get(`/contracts/${id}`)
  },

  // 创建合同（作为创建模板）
  createTemplate: (data: ContractTemplate) => {
    return request.post('/contracts', data)
  },

  // 更新合同（作为更新模板）
  updateTemplate: (id: number, data: ContractTemplate) => {
    return request.put(`/contracts/${id}`, data)
  },

  // 删除合同（作为删除模板）
  deleteTemplate: (id: number) => {
    return request.delete(`/contracts/${id}`)
  },

  // 切换模板状态（暂时返回成功）
  toggleTemplateStatus: (id: number) => {
    return Promise.resolve({ code: 200, message: '操作成功', data: true })
  },

  // 复制合同模板（暂时返回成功）
  copyTemplate: (id: number) => {
    return Promise.resolve({ code: 200, message: '复制成功', data: { id: Date.now() } })
  },

  // 获取合同列表（作为模板列表）
  getTemplateList: (status?: string) => {
    return request.get('/contracts', { params: { status } })
  },

  // 预览模板内容（暂时返回成功）
  previewTemplate: (data: PreviewRequest) => {
    return Promise.resolve({ code: 200, message: '预览成功', data: { content: data.templateContent } })
  }
}

// 为了兼容Generate.vue中的使用方式，添加单独的导出函数
export const getContractTemplates = () => {
  return contractTemplateApi.getTemplateList('ACTIVE')
}
