import request from '@/utils/request'

// 统计分析相关接口
export const statisticsApi = {
  // 获取仪表盘数据
  getDashboard: () => {
    return request.get('/statistics/dashboard')
  },
  
  // 获取项目统计数据
  getProjectStats: () => {
    return request.get('/statistics/project')
  },
  
  // 获取租户统计数据
  getTenantStats: () => {
    return request.get('/statistics/tenant')
  },
  
  // 获取单元统计数据
  getUnitStats: () => {
    return request.get('/statistics/unit')
  },
  
  // 获取合同统计数据
  getContractStats: () => {
    return request.get('/statistics/contract')
  },
  
  // 获取财务统计数据
  getFinancialStats: () => {
    return request.get('/statistics/financial')
  },
  
  // 获取运营分析数据
  getOperationStats: () => {
    return request.get('/statistics/operation')
  }
}