import request from '@/utils/request'
import type { PageResult, PageQuery, OrgInfo } from './types'

// 用户相关接口
export interface User {
  id?: number
  username: string
  password?: string
  realName?: string
  email?: string
  phone?: string
  role?: string
  status?: number
  createTime?: string
  updateTime?: string
  orgInfo?: OrgInfo
}

export const userApi = {
  // 用户登录
  login: (data: { orgCode: string; username: string; password: string }) => {
    return request.post('/auth/login', data)
  },
  
  // 获取用户信息
  getUserInfo: () => {
    return request.get('/auth/user-info')
  },
  
  // 获取组织列表
  getOrganizations: () => {
    return request.get('/auth/organizations')
  },
  
  // 更新用户信息
  updateUserInfo: (data: {
    realName?: string
    email?: string
    phone?: string
  }) => {
    return request.put('/user/info', data)
  },
  
  // 修改密码
  changeUserPassword: (data: {
    currentPassword: string
    newPassword: string
  }) => {
    return request.put('/user/password', data)
  },
  
  // 获取用户登录记录
  getUserLoginHistory: () => {
    return request.get('/user/login-history')
  },
  
  // 用户分页查询
  getUserPage: (params: PageQuery) => {
    return request.get<PageResult<User>>('/user/page', { params })
  },
  
  // 创建用户
  createUser: (data: User) => {
    return request.post('/user', data)
  },
  
  // 更新用户
  updateUser: (id: number, data: User) => {
    return request.put(`/user/${id}`, data)
  },
  
  // 删除用户
  deleteUser: (id: number) => {
    return request.delete(`/user/${id}`)
  }
}