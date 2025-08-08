<template>
  <div class="layout-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="250px" class="sidebar">
        <div class="logo">
          <h2>云联智管</h2>
          <p class="slogan">云联万物 智管未来</p>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          router
          unique-opened
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <!-- 运营仪表盘 -->
          <el-menu-item index="/dashboard">
            <el-icon><DataBoard /></el-icon>
            <span>运营仪表盘</span>
          </el-menu-item>
          
          <!-- 资产管理 -->
          <el-sub-menu index="asset">
            <template #title>
              <el-icon><OfficeBuilding /></el-icon>
              <span>资产管理</span>
            </template>
            <el-menu-item index="/asset/project">
              <el-icon><Postcard /></el-icon>
              <span>项目管理</span>
            </el-menu-item>
            <el-menu-item index="/asset/building">
              <el-icon><House /></el-icon>
              <span>楼栋管理</span>
            </el-menu-item>
            <el-menu-item index="/asset/unit">
              <el-icon><Grid /></el-icon>
              <span>单元管理</span>
            </el-menu-item>
            <el-menu-item index="/asset/merge-split">
              <el-icon><Connection /></el-icon>
              <span>单元合并拆分</span>
            </el-menu-item>
          </el-sub-menu>
          
          <!-- 租户管理 -->
          <el-sub-menu index="tenant">
            <template #title>
              <el-icon><Avatar /></el-icon>
              <span>租户管理</span>
            </template>
            <el-menu-item index="/tenant/info">
              <el-icon><User /></el-icon>
              <span>租户信息</span>
            </el-menu-item>
            <el-menu-item index="/tenant/risk">
              <el-icon><Warning /></el-icon>
              <span>风险管控</span>
            </el-menu-item>
            <el-menu-item index="/tenant/profile">
              <el-icon><DataAnalysis /></el-icon>
              <span>租户画像</span>
            </el-menu-item>
          </el-sub-menu>
          
          <!-- 合同管理 -->
          <el-sub-menu index="contract">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>合同管理</span>
            </template>
            <el-menu-item index="/contract/info">
              <el-icon><Document /></el-icon>
              <span>合同信息</span>
            </el-menu-item>
            <el-menu-item index="/contract/business-analysis">
              <el-icon><TrendCharts /></el-icon>
              <span>业态分析</span>
            </el-menu-item>
            <el-menu-item index="/contract/config">
              <el-icon><Setting /></el-icon>
              <span>合同配置</span>
            </el-menu-item>
            <el-menu-item index="/contract/generate">
              <el-icon><DocumentAdd /></el-icon>
              <span>合同生成</span>
            </el-menu-item>
          </el-sub-menu>
          
          <!-- 应收账款管理 -->
          <el-menu-item index="/receivable">
            <el-icon><Money /></el-icon>
            <span>应收账款管理</span>
          </el-menu-item>
          
          <!-- 系统管理 -->
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/user">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/system/role">
              <el-icon><UserFilled /></el-icon>
              <span>角色管理</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-container>
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-icon><User /></el-icon>
                管理员
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 主内容 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentTitle = computed(() => {
  return route.meta?.title || '首页'
})

// 退出登录
const logout = () => {
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  overflow: hidden;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.sidebar {
  background: linear-gradient(180deg, #1f2937 0%, #374151 100%);
  height: 100vh;
  overflow-y: auto;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.sidebar::-webkit-scrollbar {
  width: 4px;
}

.sidebar::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
}

.sidebar::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
}

.logo {
  padding: 24px 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(0, 0, 0, 0.1);
}

.logo h2 {
  color: #fff;
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  letter-spacing: 1px;
  background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo .slogan {
  color: #9ca3af;
  margin: 8px 0 0 0;
  font-size: 12px;
  font-weight: 400;
  opacity: 0.8;
}

.sidebar-menu {
  border: none;
  background: transparent;
}

.sidebar-menu :deep(.el-menu-item) {
  margin: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background: rgba(59, 130, 246, 0.1) !important;
  transform: translateX(4px);
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%) !important;
  color: #fff !important;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.sidebar-menu :deep(.el-sub-menu__title) {
  margin: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(59, 130, 246, 0.1) !important;
  transform: translateX(4px);
}

.header {
  background: linear-gradient(135deg, #fff 0%, #f8fafc 100%);
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  flex-shrink: 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.header-left {
  flex: 1;
}

.header-left :deep(.el-breadcrumb__item) {
  font-weight: 500;
  color: #374151;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #374151;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.user-info:hover {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  transform: translateY(-1px);
}

.user-info .el-icon {
  margin: 0 6px;
  font-size: 16px;
}

.main-content {
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  padding: 0;
  height: calc(100vh - 64px);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
}

.main-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    radial-gradient(circle at 25% 25%, rgba(59, 130, 246, 0.05) 0%, transparent 50%),
    radial-gradient(circle at 75% 75%, rgba(16, 185, 129, 0.05) 0%, transparent 50%);
  pointer-events: none;
}
</style>
