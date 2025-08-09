import request from '@/utils/request'

// 合同生成相关API

/**
 * 生成合同PDF
 */
export function generateContractPDF(data: any) {
  return request({
    url: '/contract-generate/generate-pdf',
    method: 'post',
    data
  })
}

/**
 * 下载合同PDF
 */
export function downloadContractPDF(contractNo: string) {
  return request({
    url: `/contract-generate/download/${contractNo}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 预览合同PDF
 */
export function previewContractPDF(contractNo: string) {
  return request({
    url: `/contract-generate/preview/${contractNo}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 获取生成进度
 */
export function getGenerateProgress(taskId: string) {
  return request({
    url: `/contract-generate/progress/${taskId}`,
    method: 'get'
  })
}

/**
 * 批量生成合同
 */
export function batchGenerateContracts(data: any) {
  return request({
    url: '/contract-generate/batch-generate',
    method: 'post',
    data
  })
}

/**
 * 获取合同生成历史
 */
export function getGenerateHistory(params: any) {
  return request({
    url: '/contract-generate/history',
    method: 'get',
    params
  })
}

/**
 * 删除生成的合同文件
 */
export function deleteGeneratedContract(contractId: string) {
  return request({
    url: `/contract-generate/delete/${contractId}`,
    method: 'delete'
  })
}

/**
 * 验证合同数据
 */
export function validateContractData(data: any) {
  return request({
    url: '/contract-generate/validate',
    method: 'post',
    data
  })
}