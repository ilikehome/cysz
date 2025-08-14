import request from '@/utils/request'
import type { ApiResponse, PageResult, PageQuery } from './types'

// 合同生成请求接口
export interface ContractGenerateRequest {
  contractId: number
  templateId: number
  variables: Record<string, any>
  outputFormat: 'PDF' | 'WORD'
}

// 生成进度接口
export interface GenerateProgress {
  taskId: string
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED'
  progress: number
  message?: string
  downloadUrl?: string
}

// 生成历史接口
export interface GenerateHistory {
  id: number
  contractNo: string
  templateName: string
  generateTime: string
  status: 'SUCCESS' | 'FAILED'
  fileSize?: number
  downloadCount: number
  createTime: string
}

// 批量生成请求接口
export interface BatchGenerateRequest {
  contractIds: number[]
  templateId: number
  outputFormat: 'PDF' | 'WORD'
}

// 合同生成相关API
export const contractGenerateApi = {
  // 生成合同PDF
  generateContractPDF: (data: ContractGenerateRequest) => {
    return request.post<ApiResponse<{ taskId: string }>>('/contract-generate/generate-pdf', data)
  },

  // 下载合同PDF
  downloadContractPDF: (contractNo: string) => {
    return request.get(`/contract-generate/download/${contractNo}`, {
      responseType: 'blob'
    })
  },

  // 预览合同PDF
  previewContractPDF: (contractNo: string) => {
    return request.get(`/contract-generate/preview/${contractNo}`, {
      responseType: 'blob'
    })
  },

  // 获取生成进度
  getGenerateProgress: (taskId: string) => {
    return request.get<ApiResponse<GenerateProgress>>(`/contract-generate/progress/${taskId}`)
  },

  // 批量生成合同
  batchGenerateContracts: (data: BatchGenerateRequest) => {
    return request.post<ApiResponse<{ taskId: string }>>('/contract-generate/batch-generate', data)
  },

  // 获取合同生成历史
  getGenerateHistory: (params: PageQuery & { contractNo?: string; status?: string }) => {
    return request.get<ApiResponse<PageResult<GenerateHistory>>>('/contract-generate/history', { params })
  },

  // 删除生成的合同文件
  deleteGeneratedContract: (contractId: string) => {
    return request.delete<ApiResponse<void>>(`/contract-generate/delete/${contractId}`)
  },

  // 验证合同数据
  validateContractData: (data: any) => {
    return request.post<ApiResponse<{ isValid: boolean; errors?: string[] }>>('/contract-generate/validate', data)
  }
}

// 为了兼容现有代码，保留原有的函数导出
export const generateContractPDF = contractGenerateApi.generateContractPDF
export const downloadContractPDF = contractGenerateApi.downloadContractPDF
export const previewContractPDF = contractGenerateApi.previewContractPDF
export const getGenerateProgress = contractGenerateApi.getGenerateProgress
export const batchGenerateContracts = contractGenerateApi.batchGenerateContracts
export const getGenerateHistory = contractGenerateApi.getGenerateHistory
export const deleteGeneratedContract = contractGenerateApi.deleteGeneratedContract
export const validateContractData = contractGenerateApi.validateContractData
