import request from '@/utils/request'

// 合同生成相关API

/**
 * 生成合同应收账款
 */
export function generateContractReceivables(contractId: number, generateType: number, periods?: number) {
  const params: any = { generateType }
  if (periods) params.periods = periods
  return request({
    url: `/contracts/${contractId}/generate-receivables`,
    method: 'post',
    params
  })
}

/**
 * 获取合同生成进度
 */
export function getContractGenerationProgress(contractId: number) {
  return request({
    url: `/contracts/${contractId}/generation-progress`,
    method: 'get'
  })
}

/**
 * 获取合同详细信息
 */
export function getContractDetail(contractId: number) {
  return request({
    url: `/contracts/${contractId}`,
    method: 'get'
  })
}

/**
 * 获取合同列表
 */
export function getContractPage(params: any) {
  return request({
    url: '/contracts',
    method: 'get',
    params
  })
}

/**
 * 创建合同
 */
export function createContract(data: any) {
  return request({
    url: '/contracts',
    method: 'post',
    data
  })
}

/**
 * 更新合同
 */
export function updateContract(contractId: number, data: any) {
  return request({
    url: `/contracts/${contractId}`,
    method: 'put',
    data
  })
}

/**
 * 删除合同
 */
export function deleteContract(contractId: number) {
  return request({
    url: `/contracts/${contractId}`,
    method: 'delete'
  })
}

/**
 * 获取合同统计信息
 */
export function getContractStatistics() {
  return request({
    url: '/contracts/statistics',
    method: 'get'
  })
}

/**
 * 获取合同应收账款列表
 */
export function getContractReceivables(contractId: number) {
  return request({
    url: `/contracts/${contractId}/receivables`,
    method: 'get'
  })
}

/**
 * 获取合同已收款列表
 */
export function getContractReceived(contractId: number) {
  return request({
    url: `/contracts/${contractId}/received`,
    method: 'get'
  })
}

/**
 * 导出合同数据
 */
export function exportContractData(params: any) {
  return request({
    url: '/contracts/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 生成合同报表
 */
export function generateContractReport(reportType: string, startDate: string, endDate: string, projectId?: number) {
  const params: any = { reportType, startDate, endDate }
  if (projectId) params.projectId = projectId
  return request({
    url: '/contracts/report',
    method: 'get',
    params
  })
}