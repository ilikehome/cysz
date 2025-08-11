<template>
  <div class="role-management">
    <div class="page-header">
      <h1>角色管理</h1>
      <p>管理系统中的用户角色和权限</p>
    </div>

    <!-- 角色统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card super-admin">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><Crown /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.superAdminCount }}</div>
                <div class="stat-label">超级管理员</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card tenant-admin">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><UserFilled /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.tenantAdminCount }}</div>
                <div class="stat-label">租户管理员</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card user">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.userCount }}</div>
                <div class="stat-label">普通用户</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card total">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><Users /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.totalUsers }}</div>
                <div class="stat-label">用户总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 角色列表 -->
    <el-card class="role-list-card">
      <template #header>
        <div class="card-header">
          <span>角色列表</span>
        </div>
      </template>

      <el-table :data="roles" style="width: 100%" v-loading="loading">
        <el-table-column prop="roleName" label="角色名称" width="150">
          <template #default="scope">
            <div class="role-name">
              <el-tag 
                :type="getRoleTagType(scope.row.roleCode)" 
                :icon="getRoleIcon(scope.row.roleCode)"
                size="large"
              >
                {{ scope.row.roleName }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="roleLevel" label="角色级别" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.roleLevel === 'SYSTEM' ? 'danger' : 'primary'" size="small">
              {{ scope.row.roleLevel === 'SYSTEM' ? '系统级' : '租户级' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="description" label="角色描述" min-width="200" />
        
        <el-table-column prop="userCount" label="用户数量" width="100" align="center">
          <template #default="scope">
            <el-badge :value="scope.row.userCount" class="user-count-badge">
              <el-icon><User /></el-icon>
            </el-badge>
          </template>
        </el-table-column>
        
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="viewPermissions(scope.row)"
              :icon="View"
            >
              查看权限
            </el-button>
            <el-button 
              type="info" 
              size="small" 
              @click="viewUsers(scope.row)"
              :icon="UserFilled"
            >
              查看用户
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 权限详情对话框 -->
    <el-dialog 
      v-model="permissionDialogVisible" 
      :title="`${selectedRole?.roleName} - 权限详情`"
      width="600px"
    >
      <div class="permission-content">
        <div class="permission-header">
          <el-tag 
            :type="getRoleTagType(selectedRole?.roleCode)" 
            size="large"
          >
            {{ selectedRole?.roleName }}
          </el-tag>
          <span class="permission-count">共 {{ rolePermissions.length }} 项权限</span>
        </div>
        
        <div class="permission-list">
          <el-row :gutter="10">
            <el-col :span="12" v-for="permission in rolePermissions" :key="permission">
              <div class="permission-item">
                <el-icon class="permission-icon"><Check /></el-icon>
                <span class="permission-text">{{ getPermissionName(permission) }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Crown, UserFilled, User, Users, View, Check
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const roles = ref([])
const statistics = reactive({
  superAdminCount: 0,
  tenantAdminCount: 0,
  userCount: 0,
  totalUsers: 0
})

const permissionDialogVisible = ref(false)
const selectedRole = ref(null)
const rolePermissions = ref([])

// 获取角色标签类型
const getRoleTagType = (roleCode: string) => {
  switch (roleCode) {
    case 'SUPER_ADMIN': return 'danger'
    case 'TENANT_ADMIN': return 'warning'
    case 'USER': return 'success'
    default: return 'info'
  }
}

// 获取角色图标
const getRoleIcon = (roleCode: string) => {
  switch (roleCode) {
    case 'SUPER_ADMIN': return Crown
    case 'TENANT_ADMIN': return UserFilled
    case 'USER': return User
    default: return User
  }
}

// 获取权限名称
const getPermissionName = (permission: string) => {
  const permissionMap = {
    'system:manage': '系统管理',
    'tenant:create': '创建租户',
    'tenant:delete': '删除租户',
    'tenant:manage': '租户管理',
    'user:manage:all': '管理所有用户',
    'role:manage': '角色管理',
    'system:monitor': '系统监控',
    'data:export:all': '导出所有数据',
    'audit:view:all': '查看所有审计日志',
    'tenant:view': '查看租户信息',
    'user:create': '创建用户',
    'user:update': '更新用户',
    'user:delete': '删除用户',
    'user:manage': '用户管理',
    'project:manage': '项目管理',
    'building:manage': '楼栋管理',
    'unit:manage': '单元管理',
    'tenant:business:manage': '租户业务管理',
    'contract:manage': '合同管理',
    'receivable:manage': '应收账款管理',
    'data:export': '数据导出',
    'statistics:view': '统计查看',
    'audit:view': '审计日志查看',
    'dashboard:view': '仪表盘查看',
    'profile:view': '个人信息查看',
    'profile:update': '个人信息更新',
    'project:view': '项目查看',
    'building:view': '楼栋查看',
    'unit:view': '单元查看',
    'contract:view': '合同查看',
    'statistics:view:basic': '基础统计查看'
  }
  return permissionMap[permission] || permission
}

// 查看权限
const viewPermissions = async (role: any) => {
  selectedRole.value = role
  try {
    const response = await fetch(`http://localhost:8080/api/role/${role.roleCode}/permissions`, {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    const result = await response.json()
    
    if (result.code === 200) {
      rolePermissions.value = result.data
      permissionDialogVisible.value = true
    } else {
      ElMessage.error(result.message || '获取权限失败')
    }
  } catch (error) {
    ElMessage.error('获取权限失败')
  }
}

// 查看用户
const viewUsers = (role: any) => {
  ElMessage.info(`查看 ${role.roleName} 用户功能开发中...`)
}

// 加载角色列表
const loadRoles = async () => {
  loading.value = true
  try {
    const response = await fetch('http://localhost:8080/api/role/list', {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    const result = await response.json()
    
    if (result.code === 200) {
      roles.value = result.data
    } else {
      ElMessage.error(result.message || '获取角色列表失败')
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

// 加载统计数据
const loadStatistics = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/role/statistics', {
      headers: {
        'Authorization': `Bearer ${userStore.token}`
      }
    })
    const result = await response.json()
    
    if (result.code === 200) {
      Object.assign(statistics, result.data)
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

onMounted(() => {
  loadRoles()
  loadStatistics()
})
</script>

<style scoped>
.role-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 24px;
  color: white;
}

.super-admin .stat-icon {
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
}

.tenant-admin .stat-icon {
  background: linear-gradient(135deg, #feca57, #ff9ff3);
}

.user .stat-icon {
  background: linear-gradient(135deg, #48dbfb, #0abde3);
}

.total .stat-icon {
  background: linear-gradient(135deg, #1dd1a1, #10ac84);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.role-list-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.role-name {
  display: flex;
  align-items: center;
}

.user-count-badge {
  display: inline-flex;
  align-items: center;
}

.permission-content {
  padding: 10px 0;
}

.permission-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.permission-count {
  color: #909399;
  font-size: 14px;
}

.permission-list {
  max-height: 400px;
  overflow-y: auto;
}

.permission-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  margin-bottom: 8px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border-left: 3px solid #67c23a;
}

.permission-icon {
  color: #67c23a;
  margin-right: 8px;
  font-size: 14px;
}

.permission-text {
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #fafafa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  background-color: #fafafa;
  border-radius: 12px 12px 0 0;
  padding: 20px 24px;
}

:deep(.el-dialog__body) {
  padding: 24px;
}
</style>