<template>
  <div class="layout-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="250px" class="sidebar">
        <div class="logo">
          <div class="logo-content">
            <el-icon class="logo-icon"><OfficeBuilding /></el-icon>
            <h2>云联智管</h2>
          </div>
        </div>
        
        <!-- 用户信息区域 -->
        <div class="user-section">
          <el-dropdown>
            <div class="user-info">
              <el-icon><User /></el-icon>
              <span class="username">管理员</span>
              <el-icon class="arrow-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
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
          
          <!-- 账款管理 -->
          <el-sub-menu index="account">
            <template #title>
              <el-icon><Money /></el-icon>
              <span>账款管理</span>
            </template>
            <el-menu-item index="/account/receivable-received">
              <el-icon><Wallet /></el-icon>
              <span>应收已收</span>
            </el-menu-item>
            <el-menu-item index="/account/receivable-analysis">
              <el-icon><TrendCharts /></el-icon>
              <span>收款进度</span>
            </el-menu-item>
            <el-menu-item index="/account/payment-claim">
              <el-icon><CreditCard /></el-icon>
              <span>手动认领</span>
            </el-menu-item>
            <el-menu-item index="/account/collection-reminder">
              <el-icon><Message /></el-icon>
              <span>自动催缴</span>
            </el-menu-item>
          </el-sub-menu>
          
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
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  OfficeBuilding, 
  User, 
  ArrowDown, 
  DataBoard, 
  Postcard, 
  House, 
  Grid, 
  Connection, 
  Avatar, 
  Warning, 
  DataAnalysis, 
  Document, 
  TrendCharts, 
  Setting, 
  DocumentAdd, 
  Money, 
  Wallet, 
  CreditCard, 
  UserFilled,
  Message 
} from '@element-plus/icons-vue'

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

.logo-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  transition: all 0.3s ease;
}

.logo-content:hover {
  transform: translateY(-2px);
}

.logo-icon {
  font-size: 28px;
  color: #60a5fa;
  filter: drop-shadow(0 2px 4px rgba(96, 165, 250, 0.3));
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.8;
  }
}

.logo h2 {
  color: #fff;
  margin: 0;
  font-size: 26px;
  font-weight: 700;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 50%, #1e40af 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
}

.logo h2::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
  transition: width 0.3s ease;
}

.logo-content:hover .logo h2::after {
  width: 100%;
}

.user-section {
  padding: 16px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(0, 0, 0, 0.05);
}

.user-section .user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #e5e7eb;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
  width: 100%;
  justify-content: space-between;
}

.user-section .user-info:hover {
  background: rgba(59, 130, 246, 0.1);
  color: #60a5fa;
}

.user-section .username {
  flex: 1;
  margin-left: 8px;
  font-size: 14px;
  font-weight: 500;
}

.user-section .arrow-icon {
  font-size: 12px;
  opacity: 0.7;
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

.main-content {
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  padding: 0;
  height: 100vh;
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
