// 通用接口类型定义
export interface PageResult<T> {
  records: T[]
  total: number
  current: number
  size: number
}

export interface PageQuery {
  current: number
  size: number
  [key: string]: any
}

// 标准API响应格式
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 组织信息接口
export interface OrgInfo {
  orgCode: string
  orgName: string
  orgType: string
}