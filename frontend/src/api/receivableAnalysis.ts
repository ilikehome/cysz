import axios from 'axios'

// 账款分析相关API
export const receivableAnalysisApi = {
  // 获取概览数据
  getOverview: () => {
    return axios.get('/api/receivable-analysis/overview')
  },

  // 获取饼图数据
  getPieChartData: (startMonth?: string, endMonth?: string) => {
    const params: any = {}
    if (startMonth) params.startMonth = startMonth
    if (endMonth) params.endMonth = endMonth
    return axios.get('/api/receivable-analysis/pie-chart', { params })
  },

  // 获取趋势图数据
  getTrendChartData: () => {
    return axios.get('/api/receivable-analysis/trend-chart')
  },

  // 获取项目对比数据
  getProjectChartData: (projectIds?: number[]) => {
    const params: any = {}
    if (projectIds && projectIds.length > 0) {
      params.projectIds = projectIds.join(',')
    }
    return axios.get('/api/receivable-analysis/project-chart', { params })
  },

  // 获取逾期预警数据
  getOverdueWarning: () => {
    return axios.get('/api/receivable-analysis/overdue-warning')
  },

  // 获取即将到期提醒数据
  getUpcomingWarning: () => {
    return axios.get('/api/receivable-analysis/upcoming-warning')
  },

  // 获取项目列表
  getProjects: () => {
    return axios.get('/api/receivable-analysis/projects')
  }
}