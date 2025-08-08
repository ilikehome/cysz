import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 侧边栏状态
  const sidebarCollapsed = ref(false)
  
  // 面包屑导航
  const breadcrumbs = ref<Array<{ name: string; path?: string }>>([])
  
  // 页面加载状态
  const pageLoading = ref(false)
  
  // 主题设置
  const theme = ref<'light' | 'dark'>('light')
  
  // 语言设置
  const locale = ref('zh-cn')

  // 切换侧边栏
  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  // 设置侧边栏状态
  const setSidebarCollapsed = (collapsed: boolean) => {
    sidebarCollapsed.value = collapsed
  }

  // 设置面包屑
  const setBreadcrumbs = (crumbs: Array<{ name: string; path?: string }>) => {
    breadcrumbs.value = crumbs
  }

  // 设置页面加载状态
  const setPageLoading = (loading: boolean) => {
    pageLoading.value = loading
  }

  // 设置主题
  const setTheme = (newTheme: 'light' | 'dark') => {
    theme.value = newTheme
    localStorage.setItem('theme', newTheme)
  }

  // 设置语言
  const setLocale = (newLocale: string) => {
    locale.value = newLocale
    localStorage.setItem('locale', newLocale)
  }

  // 初始化应用设置
  const initApp = () => {
    // 从localStorage恢复设置
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark'
    if (savedTheme) {
      theme.value = savedTheme
    }
    
    const savedLocale = localStorage.getItem('locale')
    if (savedLocale) {
      locale.value = savedLocale
    }
    
    const savedSidebarState = localStorage.getItem('sidebarCollapsed')
    if (savedSidebarState) {
      sidebarCollapsed.value = JSON.parse(savedSidebarState)
    }
  }

  return {
    // 状态
    sidebarCollapsed,
    breadcrumbs,
    pageLoading,
    theme,
    locale,
    
    // 方法
    toggleSidebar,
    setSidebarCollapsed,
    setBreadcrumbs,
    setPageLoading,
    setTheme,
    setLocale,
    initApp
  }
})