import request from '@/utils/request'
import type { ApiResponse } from './types'

// 概览数据接口
export interface ReceivableOverview {
  totalAmount: number
  paidAmount: number
  unpaidAmount: number
  overdueAmount: number
  totalContracts: number
  paidContracts: number
  unpaidContracts: number
  overdueContracts: number
}

// 饼图数据接口
export interface PieChartData {
  name: string
  value: number
  percentage: number
}

// 趋势图数据接口
export interface TrendChartData {
  month: string
  totalAmount: number
  paidAmount: number
  unpaidAmount: number
  overdueAmount: number
}

// 项目对比数据接口
export interface ProjectChartData {
  projectName: string
  totalAmount: number
  paidAmount: number
  unpaidAmount: number
  overdueAmount: number
}

// 预警数据接口
export interface WarningData {
  id: number
  contractNo: string
  tenantName: string
  amount: number
  dueDate: string
  overdueDays?: number
  projectName: string
  unitDescription: string
}

// 项目信息接口
export interface ProjectInfo {
  id: number
  projectName: string
}

// 账款分析相关API
export const receivableAnalysisApi = {
  // 获取概览数据
  getOverview: () => {
    return request.get<ApiResponse<ReceivableOverview>>('/receivable-analysis/overview')
  },

  // 获取饼图数据
  getPieChartData: (startMonth?: string, endMonth?: string) => {
    const params: any = {}
    if (startMonth) params.startMonth = startMonth
    if (endMonth) params.endMonth = endMonth
    return request.get<ApiResponse<PieChartData[]>>('/receivable-analysis/pie-chart', { params })
  },

  // 获取趋势图数据
  getTrendChartData: () => {
    return request.get<ApiResponse<TrendChartData[]>>('/receivable-analysis/trend-chart')
  },

  // 获取项目对比数据
  getProjectChartData: (projectIds?: number[]) => {
    const params: any = {}
    if (projectIds && projectIds.length > 0) {
      params.projectIds = projectIds.join(',')
    }
    return request.get<ApiResponse<ProjectChartData[]>>('/receivable-analysis/project-chart', { params })
  },

  // 获取逾期预警数据
  getOverdueWarning: () => {
    return request.get<ApiResponse<WarningData[]>>('/receivable-analysis/overdue-warning')
  },

  // 获取即将到期提醒数据
  getUpcomingWarning: () => {
    return request.get<ApiResponse<WarningData[]>>('/receivable-analysis/upcoming-warning')
  },

  // 获取项目列表
  getProjects: () => {
    return request.get<ApiResponse<ProjectInfo[]>>('/receivable-analysis/projects')
  }
}
