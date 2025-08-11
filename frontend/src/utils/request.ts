import axios, { type AxiosInstance, type InternalAxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.DEV ? '/api' : (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'),
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token')
    console.log('请求拦截器 - token:', token) // 添加调试日志
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    // 对请求错误做些什么
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 对响应数据做点什么
    const responseData = response.data
    
    console.log('API响应:', responseData) // 添加调试日志
    
    // 检查是否是标准的 {code, message, data} 格式
    if (responseData && typeof responseData === 'object' && 'code' in responseData) {
      const { code, message, data } = responseData
      console.log('响应拦截器 - 标准格式, code:', code, 'message:', message)
      
      if (code === 200) {
        console.log('响应拦截器 - 成功返回数据')
        return responseData // 返回完整的响应数据，包含data字段
      } else if (code === 401) {
        // 未授权，跳转到登录页
        console.log('响应拦截器 - 401未授权')
        localStorage.removeItem('token')
        window.location.href = '/login'
        return Promise.reject(new Error(message || '未授权'))
      } else {
        console.log('响应拦截器 - 其他错误, code:', code, 'message:', message)
        ElMessage.error(message || '请求失败')
        return Promise.reject(new Error(message || '请求失败'))
      }
    } else {
      // 非标准格式，直接返回原始数据
      console.log('响应拦截器 - 非标准格式，直接返回')
      return responseData
    }
  },
  (error) => {
    // 对响应错误做点什么
    console.error('响应错误:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          ElMessage.error('未授权，请重新登录')
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('权限不足')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.message || '网络错误')
      }
    } else if (error.request) {
      ElMessage.error('网络连接失败')
    } else {
      ElMessage.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

export default service