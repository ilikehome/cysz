import request from '@/utils/request'
import type { PageResult, PageQuery } from './types'

// 楼栋相关接口
export interface Building {
  id?: number
  buildingName: string
  buildingCode: string
  projectId: number
  projectName?: string
  buildingArea?: number
  rentArea?: number
  propertyArea?: number
  usableArea?: number
  remark?: string
  status?: number
  createTime?: string
  updateTime?: string
}

// 楼层相关接口
export interface Floor {
  id?: number
  buildingId: number
  buildingName?: string
  floorName: string
  floorCode: string
  remark?: string
  createTime?: string
  updateTime?: string
}

// 单元相关接口
export interface Unit {
  id?: number
  unitName: string
  unitCode: string
  projectId?: number
  buildingId?: number
  floorId: number
  floorName?: string
  buildingName?: string
  projectName?: string
  unitStatus: 'RENTABLE' | 'SELF_USE' | 'PUBLIC_USE' | 'LEASE_BACK' | 'DISABLED' | 'SELF_RENTAL'
  unitPurpose?: string
  buildingArea?: number
  rentArea?: number
  isMultiTenant: boolean
  remark?: string
  status?: number
  createTime?: string
  updateTime?: string
}

// 楼栋API
export const buildingApi = {
  // 楼栋分页查询
  getBuildingPage: (params: PageQuery) => {
    return request.get<PageResult<Building>>('/building/page', { params })
  },
  
  // 根据项目ID获取楼栋列表
  getBuildingsByProject: (projectId: number) => {
    return request.get<Building[]>(`/building/project/${projectId}`)
  },
  
  // 创建楼栋
  createBuilding: (data: Building) => {
    return request.post('/building', data)
  },
  
  // 更新楼栋
  updateBuilding: (id: number, data: Building) => {
    return request.put(`/building/${id}`, data)
  },
  
  // 删除楼栋
  deleteBuilding: (id: number) => {
    return request.delete(`/building/${id}`)
  }
}

// 楼层API
export const floorApi = {
  // 楼层分页查询
  getFloorPage: (params: PageQuery) => {
    return request.get<PageResult<Floor>>('/floor/page', { params })
  },
  
  // 根据楼栋ID获取楼层列表
  getFloorsByBuilding: (buildingId: number) => {
    return request.get<Floor[]>(`/floor/building/${buildingId}`)
  },
  
  // 创建楼层
  createFloor: (data: Floor) => {
    return request.post('/floor', data)
  },
  
  // 更新楼层
  updateFloor: (id: number, data: Floor) => {
    return request.put(`/floor/${id}`, data)
  },
  
  // 删除楼层
  deleteFloor: (id: number) => {
    return request.delete(`/floor/${id}`)
  }
}

// 单元API
export const unitApi = {
  // 单元分页查询
  getUnitPage: (params: PageQuery) => {
    return request.get<PageResult<Unit>>('/unit/page', { params })
  },
  
  // 获取单元列表
  getUnitList: (params?: {
    projectId?: number
    buildingId?: number
    floorId?: number
    unitStatus?: string
  }) => {
    return request.get('/unit/list', { params })
  },
  
  // 根据楼层ID获取单元列表（用于级联选择）
  getUnitsByFloor: (floorId: number) => {
    return request.get(`/unit/by-floor/${floorId}`)
  },
  
  // 根据项目ID获取可用单元列表
  getAvailableUnits: (projectId: number) => {
    return request.get(`/unit/available/${projectId}`)
  },
  
  // 创建单元
  createUnit: (data: Unit) => {
    return request.post('/unit', data)
  },
  
  // 更新单元
  updateUnit: (id: number, data: Unit) => {
    return request.put(`/unit/${id}`, data)
  },
  
  // 删除单元
  deleteUnit: (id: number) => {
    return request.delete(`/unit/${id}`)
  },
  
  // 单元合并
  mergeUnits: (data: {
    sourceUnitIds: number[]
    targetUnitCode: string
    targetUnitDescription: string
    operationReason: string
  }) => {
    return request.post('/unit/merge', data)
  },
  
  // 单元拆分
  splitUnit: (data: {
    sourceUnitId: number
    targetUnits: Array<{ unitCode: string; unitDescription: string }>
    operationReason: string
  }) => {
    return request.post('/unit/split', data)
  }
}