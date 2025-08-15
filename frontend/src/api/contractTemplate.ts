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
  // 分页查询合同模板
  getTemplatePage: (params: ContractTemplatePageParams) => {
    return request.get('/contract-template/page', { params })
  },

  // 根据ID查询合同模板
  getTemplateById: (id: number) => {
    return request.get(`/contract-template/${id}`)
  },

  // 创建合同模板
  createTemplate: (data: ContractTemplate) => {
    return request.post('/contract-template', data)
  },

  // 更新合同模板
  updateTemplate: (id: number, data: ContractTemplate) => {
    return request.put(`/contract-template/${id}`, data)
  },

  // 删除合同模板
  deleteTemplate: (id: number) => {
    return request.delete(`/contract-template/${id}`)
  },

  // 切换模板状态
  toggleTemplateStatus: (id: number) => {
    return request.put(`/contract-template/${id}/status`)
  },

  // 复制合同模板
  copyTemplate: (id: number) => {
    return request.post(`/contract-template/${id}/copy`)
  },

  // 获取模板列表（不分页）
  getTemplateList: (status?: string) => {
    return request.get('/contract-template/list', { params: { status } })
  },

  // 预览模板内容
  previewTemplate: (data: PreviewRequest) => {
    return request.post('/contract-template/preview', data)
  }
}

// 获取活跃的合同模板列表
export const getContractTemplates = () => {
  return contractTemplateApi.getTemplateList('ACTIVE')
}
