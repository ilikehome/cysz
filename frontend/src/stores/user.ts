import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi, type User } from '@/api'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<User | null>(null)
  const permissions = ref<string[]>([])

  // 设置token
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 清除token
  const clearToken = () => {
    token.value = ''
    localStorage.removeItem('token')
  }

  // 设置用户信息
  const setUserInfo = (info: User) => {
    userInfo.value = info
  }

  // 设置权限
  const setPermissions = (perms: string[]) => {
    permissions.value = perms
  }

  // 登录
  const login = async (loginData: { username: string; password: string }) => {
    try {
      const response = await userApi.login(loginData)
      console.log('登录响应:', response) // 添加调试日志
      
      // 后端返回格式: {code: 200, message: "登录成功", data: {token, userInfo, permissions}}
      if (response.code === 200 && response.data) {
        const { token: newToken, userInfo: info, permissions: perms } = response.data
        
        setToken(newToken)
        setUserInfo(info)
        setPermissions(perms || [])
        
        return response
      } else {
        throw new Error(response.message || '登录失败')
      }
    } catch (error) {
      console.error('登录错误:', error)
      throw error
    }
  }

  // 获取用户信息
  const getUserInfo = async () => {
    // 如果没有token，直接返回错误
    if (!token.value) {
      console.log('没有token，跳过获取用户信息')
      throw new Error('没有token')
    }
    
    try {
      const response = await userApi.getUserInfo()
      console.log('获取用户信息响应:', response) // 添加调试日志
      
      // 后端返回格式: {code: 200, message: "获取成功", data: {userInfo, permissions}}
      if (response.code === 200 && response.data) {
        const { userInfo: info, permissions: perms } = response.data
        
        setUserInfo(info)
        setPermissions(perms || [])
        
        return response
      } else {
        throw new Error(response.message || '获取用户信息失败')
      }
    } catch (error) {
      console.error('获取用户信息错误:', error)
      throw error
    }
  }

  // 登出
  const logout = () => {
    clearToken()
    userInfo.value = null
    permissions.value = []
  }

  // 检查权限
  const hasPermission = (permission: string) => {
    return permissions.value.includes(permission)
  }

  // 检查角色
  const hasRole = (role: string) => {
    return permissions.value.some(perm => perm.startsWith(`role:${role}`))
  }

  return {
    // 状态
    token,
    userInfo,
    permissions,
    
    // 方法
    setToken,
    clearToken,
    setUserInfo,
    setPermissions,
    login,
    getUserInfo,
    logout,
    hasPermission,
    hasRole
  }
})