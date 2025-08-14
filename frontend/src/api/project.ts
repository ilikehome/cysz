import request from '@/utils/request'
import type { PageResult, PageQuery, ApiResponse } from './types'

// 项目相关接口
export interface Project {
  id?: number
  projectName: string
  projectType: 'COMPLEX' | 'COMMERCIAL_DISTRICT' | 'HOTEL' | 'APARTMENT' | 'OFFICE'
  managementOrg: string
  rentBillCompany: string
  rentBillBankAccount?: string
  city: string
  address: string
  projectManager: string
  contactPhone: string
  status: number
  createTime?: string
  updateTime?: string
}

export const projectApi = {
  // 项目分页查询
  getProjectPage: (params: PageQuery) => {
    return request.get<ApiResponse<PageResult<Project>>>('/project/page', { params })
  },
  
  // 获取所有项目列表
  getProjectList: () => {
    return request.get<ApiResponse<Project[]>>('/project/list')
  },
  
  // 创建项目
  createProject: (data: Project) => {
    return request.post<ApiResponse<Project>>('/project', data)
  },
  
  // 更新项目
  updateProject: (id: number, data: Project) => {
    return request.put<ApiResponse<Project>>(`/project/${id}`, data)
  },
  
  // 删除项目
  deleteProject: (id: number) => {
    return request.delete<ApiResponse<void>>(`/project/${id}`)
  }
}
