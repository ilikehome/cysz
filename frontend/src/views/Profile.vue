<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span class="title">个人中心</span>
        </div>
      </template>
      
      <div class="profile-content">
        <!-- 用户基本信息 -->
        <div class="info-section">
          <h3>基本信息</h3>
          <el-form :model="userInfo" label-width="100px" class="profile-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名">
                  <el-input v-model="userInfo.username" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="真实姓名">
                  <el-input v-model="userInfo.realName" :disabled="!isEditing" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="邮箱">
                  <el-input v-model="userInfo.email" :disabled="!isEditing" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号">
                  <el-input v-model="userInfo.phone" :disabled="!isEditing" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="所属单位">
                  <el-input v-model="userInfo.orgName" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="用户角色">
                  <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'primary'">
                    {{ userInfo.role === 'admin' ? '管理员' : '普通用户' }}
                  </el-tag>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="注册时间">
                  <el-input v-model="userInfo.createdAt" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最后更新">
                  <el-input v-model="userInfo.updatedAt" disabled />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          
          <div class="form-actions">
            <el-button v-if="!isEditing" type="primary" @click="startEdit">
              <el-icon><Edit /></el-icon>
              编辑信息
            </el-button>
            <template v-else>
              <el-button type="primary" @click="saveChanges" :loading="saving">
                <el-icon><Check /></el-icon>
                保存修改
              </el-button>
              <el-button @click="cancelEdit">
                <el-icon><Close /></el-icon>
                取消
              </el-button>
            </template>
          </div>
        </div>
        
        <!-- 密码修改 -->
        <div class="info-section">
          <h3>密码修改</h3>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px" class="password-form">
            <el-form-item label="当前密码" prop="currentPassword">
              <el-input v-model="passwordForm.currentPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>
            
            <div class="form-actions">
              <el-button type="primary" @click="changePassword" :loading="changingPassword">
                <el-icon><Lock /></el-icon>
                修改密码
              </el-button>
              <el-button @click="resetPasswordForm">
                <el-icon><RefreshRight /></el-icon>
                重置
              </el-button>
            </div>
          </el-form>
        </div>
        
        <!-- 登录记录 -->
        <div class="info-section">
          <h3>最近登录记录</h3>
          <el-table :data="loginHistory" style="width: 100%">
            <el-table-column prop="loginTime" label="登录时间" width="180" />
            <el-table-column prop="ipAddress" label="IP地址" width="150" />
            <el-table-column prop="userAgent" label="设备信息" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'success' ? 'success' : 'danger'">
                  {{ scope.row.status === 'success' ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Check, Close, Lock, RefreshRight } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api/user'

const userStore = useUserStore()

// 用户信息
const userInfo = reactive({
  username: '',
  realName: '',
  email: '',
  phone: '',
  orgName: '',
  role: '',
  createdAt: '',
  updatedAt: ''
})

// 编辑状态
const isEditing = ref(false)
const saving = ref(false)
const originalUserInfo = ref({})

// 密码修改表单
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordFormRef = ref()
const changingPassword = ref(false)

// 登录记录
const loginHistory = ref([])

// 密码验证规则
const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 初始化用户信息
const initUserInfo = async () => {
  try {
    console.log('开始初始化用户信息')
    
    // 主动调用API获取最新用户信息
    await userStore.getUserInfo()
    
    console.log('userStore.getUserInfo()调用完成')
    console.log('userStore.userInfo:', userStore.userInfo)
    
    const user = userStore.userInfo
    if (user) {
      console.log('用户信息存在，开始赋值')
      userInfo.username = user.username || ''
      userInfo.realName = user.realName || ''
      userInfo.email = user.email || ''
      userInfo.phone = user.phone || ''
      userInfo.orgName = user.orgInfo?.orgName || ''
      userInfo.role = user.role || ''
      userInfo.createdAt = user.createTime ? new Date(user.createTime).toLocaleString() : '暂无数据'
      userInfo.updatedAt = user.updateTime ? new Date(user.updateTime).toLocaleString() : '暂无数据'
      
      console.log('用户信息赋值完成:', userInfo)
      ElMessage.success('用户信息加载成功')
    } else {
      console.log('用户信息为空')
      ElMessage.error('请求失败，未获取到基本信息')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('请求异常: ' + (error.message || '未知错误'))
  }
}

// 开始编辑
const startEdit = () => {
  isEditing.value = true
  originalUserInfo.value = { ...userInfo }
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
  Object.assign(userInfo, originalUserInfo.value)
}

// 保存修改
const saveChanges = async () => {
  try {
    saving.value = true
    await userApi.updateUserInfo({
      realName: userInfo.realName,
      email: userInfo.email,
      phone: userInfo.phone
    })
    
    // 更新用户store
    if (userStore.userInfo) {
      userStore.userInfo.realName = userInfo.realName
      userStore.userInfo.email = userInfo.email
      userStore.userInfo.phone = userInfo.phone
    }
    
    isEditing.value = false
    ElMessage.success('信息更新成功')
  } catch (error) {
    ElMessage.error('信息更新失败')
  } finally {
    saving.value = false
  }
}

// 修改密码
const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  try {
    await passwordFormRef.value.validate()
    
    await ElMessageBox.confirm('确定要修改密码吗？', '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    changingPassword.value = true
    await userApi.changeUserPassword({
      currentPassword: passwordForm.currentPassword,
      newPassword: passwordForm.newPassword
    })
    
    resetPasswordForm()
    ElMessage.success('密码修改成功，请重新登录')
    
    // 延迟跳转到登录页
    setTimeout(() => {
      userStore.logout()
    }, 1500)
    
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '密码修改失败')
    }
  } finally {
    changingPassword.value = false
  }
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.currentPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordFormRef.value?.clearValidate()
}

// 获取登录记录
const getLoginHistory = async () => {
  try {
    const response = await userApi.getUserLoginHistory()
    loginHistory.value = response.data || []
  } catch (error) {
    // 模拟数据
    loginHistory.value = [
      {
        loginTime: '2025-01-08 10:30:25',
        ipAddress: '192.168.1.100',
        userAgent: 'Chrome 120.0.0.0 Windows',
        status: 'success'
      },
      {
        loginTime: '2025-01-07 14:22:18',
        ipAddress: '192.168.1.100',
        userAgent: 'Chrome 120.0.0.0 Windows',
        status: 'success'
      },
      {
        loginTime: '2025-01-06 09:15:42',
        ipAddress: '192.168.1.100',
        userAgent: 'Chrome 120.0.0.0 Windows',
        status: 'success'
      }
    ]
  }
}

onMounted(async () => {
  await initUserInfo()
  getLoginHistory()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.profile-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.profile-content {
  padding: 20px 0;
}

.info-section {
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 1px solid #ebeef5;
}

.info-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.info-section h3 {
  margin: 0 0 20px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.profile-form,
.password-form {
  max-width: 800px;
}

.form-actions {
  margin-top: 20px;
  text-align: left;
}

.form-actions .el-button {
  margin-right: 10px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input.is-disabled .el-input__inner) {
  background-color: #f5f7fa;
  border-color: #e4e7ed;
  color: #606266;
}

:deep(.el-table) {
  margin-top: 10px;
}

:deep(.el-table th) {
  background-color: #fafafa;
  color: #606266;
  font-weight: 500;
}
</style>