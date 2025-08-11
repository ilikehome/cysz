<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>云联智管</h1>
        <p>云联万物 智管未来</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="orgCode">
          <el-select
            v-model="loginForm.orgCode"
            placeholder="请选择您的单位"
            size="large"
            class="org-select"
            filterable
            :prefix-icon="OfficeBuilding"
          >
            <el-option
              v-for="org in organizations"
              :key="org.orgCode"
              :label="org.orgName"
              :value="org.orgCode"
            >
              <div class="org-option">
                <span class="org-name">{{ org.orgName }}</span>
                <span class="org-type">{{ getOrgTypeText(org.orgType) }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
            class="login-btn"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-tips">
        <div class="tips-title">测试账号</div>
        <div class="tips-content">
          <div class="tip-item">
            <strong>万达集团：</strong>admin / 123456 (管理员)
          </div>
          <div class="tip-item">
            <strong>保利地产：</strong>poly_admin / 123456 (管理员)
          </div>
          <div class="tip-item">
            <strong>绿地控股：</strong>greenland_admin / 123456 (管理员)
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock, OfficeBuilding } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

// 组织列表
const organizations = ref<Array<{orgCode: string; orgName: string; orgType: string}>>([])

const loginForm = reactive({
  orgCode: 'ORG001',
  username: 'admin',
  password: '123456'
})

const loginRules = {
  orgCode: [
    { required: true, message: '请选择您的单位', trigger: 'change' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

// 获取组织类型文本
const getOrgTypeText = (type: string) => {
  const typeMap: Record<string, string> = {
    'COMPANY': '企业',
    'GOVERNMENT': '政府机构',
    'INSTITUTION': '事业单位',
    'OTHER': '其他'
  }
  return typeMap[type] || '企业'
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.login(loginForm)
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (error: any) {
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 加载组织列表
const loadOrganizations = async () => {
  try {
    const response = await userApi.getOrganizations()
    if (response.code === 200 && response.data) {
      organizations.value = response.data
    }
  } catch (error) {
    console.error('加载组织列表失败:', error)
    // 使用默认组织列表作为备选
    organizations.value = [
      { orgCode: 'ORG001', orgName: '万达集团', orgType: 'COMPANY' },
      { orgCode: 'ORG002', orgName: '保利地产', orgType: 'COMPANY' },
      { orgCode: 'ORG003', orgName: '绿地控股', orgType: 'COMPANY' },
      { orgCode: 'ORG004', orgName: '碧桂园集团', orgType: 'COMPANY' },
      { orgCode: 'ORG005', orgName: '华润置地', orgType: 'COMPANY' }
    ]
  }
}

// 组件挂载时加载组织列表
onMounted(() => {
  loadOrganizations()
})
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 25%, #6366f1 50%, #3b82f6 75%, #06b6d4 100%);
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grid" width="10" height="10" patternUnits="userSpaceOnUse"><path d="M 10 0 L 0 0 0 10" fill="none" stroke="rgba(255,255,255,0.1)" stroke-width="0.5"/></pattern></defs><rect width="100" height="100" fill="url(%23grid)"/></svg>');
  animation: float 20s ease-in-out infinite;
}

.login-box {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 48px;
  border-radius: 24px;
  box-shadow: 
    0 25px 50px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.6);
  width: 420px;
  max-width: 90vw;
  position: relative;
  z-index: 1;
  animation: slideInUp 0.8s ease-out;
  transition: all 0.3s ease;
}

.login-box:hover {
  transform: translateY(-5px);
  box-shadow: 
    0 35px 70px rgba(0, 0, 0, 0.2),
    0 0 0 1px rgba(255, 255, 255, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.7);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
}

.login-header::after {
  content: '';
  position: absolute;
  bottom: -15px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #8b5cf6, #7c3aed);
  border-radius: 2px;
  animation: pulse 2s ease-in-out infinite;
}

.login-header h1 {
  background: linear-gradient(135deg, #8b5cf6, #7c3aed, #6366f1);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 12px;
  font-size: 32px;
  font-weight: 700;
  letter-spacing: -0.5px;
  animation: textGlow 3s ease-in-out infinite alternate;
}

.login-header p {
  color: #64748b;
  font-size: 15px;
  font-weight: 500;
  opacity: 0.8;
}

.login-form {
  margin-bottom: 24px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.login-form :deep(.el-input) {
  border-radius: 12px;
  overflow: hidden;
}

.login-form :deep(.el-input__wrapper) {
  background: rgba(248, 250, 252, 0.8);
  border: 1px solid rgba(226, 232, 240, 0.6);
  border-radius: 12px;
  box-shadow: 
    0 1px 3px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.6);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: rgba(139, 92, 246, 0.4);
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-1px);
  box-shadow: 
    0 4px 12px rgba(139, 92, 246, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #8b5cf6;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 
    0 0 0 3px rgba(139, 92, 246, 0.1),
    0 4px 12px rgba(139, 92, 246, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
}

.login-form :deep(.el-input__inner) {
  color: #334155;
  font-weight: 500;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: #94a3b8;
  font-weight: 400;
}

.org-select :deep(.el-select__wrapper) {
  background: rgba(248, 250, 252, 0.8);
  border: 1px solid rgba(226, 232, 240, 0.6);
  border-radius: 12px;
  box-shadow: 
    0 1px 3px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.6);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.org-select :deep(.el-select__wrapper:hover) {
  border-color: rgba(139, 92, 246, 0.4);
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-1px);
  box-shadow: 
    0 4px 12px rgba(139, 92, 246, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
}

.org-select :deep(.el-select__wrapper.is-focused) {
  border-color: #8b5cf6;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 
    0 0 0 3px rgba(139, 92, 246, 0.1),
    0 4px 12px rgba(139, 92, 246, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
}

.org-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.org-name {
  font-weight: 500;
  color: #334155;
}

.org-type {
  font-size: 12px;
  color: #64748b;
  background: rgba(139, 92, 246, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #8b5cf6, #7c3aed);
  border: none;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.5px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 
    0 8px 25px rgba(139, 92, 246, 0.4),
    0 0 0 1px rgba(255, 255, 255, 0.1);
}

.login-btn:hover::before {
  left: 100%;
}

.login-btn:active {
  transform: translateY(0);
}

.login-tips {
  color: #64748b;
  font-size: 13px;
  font-weight: 500;
  opacity: 0.8;
  padding: 16px;
  background: rgba(248, 250, 252, 0.5);
  border-radius: 12px;
  border: 1px solid rgba(226, 232, 240, 0.3);
  backdrop-filter: blur(10px);
}

.tips-title {
  text-align: center;
  font-weight: 600;
  color: #475569;
  margin-bottom: 8px;
  font-size: 14px;
}

.tips-content {
  text-align: left;
}

.tip-item {
  margin-bottom: 4px;
  line-height: 1.4;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-item strong {
  color: #334155;
  font-weight: 600;
}

@keyframes gradientShift {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(1deg);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes textGlow {
  from {
    text-shadow: 0 0 10px rgba(139, 92, 246, 0.3);
  }
  to {
    text-shadow: 0 0 20px rgba(139, 92, 246, 0.6);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
    transform: translateX(-50%) scaleX(1);
  }
  50% {
    opacity: 0.7;
    transform: translateX(-50%) scaleX(1.1);
  }
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-box {
    padding: 32px 24px;
    margin: 20px;
    border-radius: 20px;
  }
  
  .login-header h1 {
    font-size: 28px;
  }
  
  .login-btn {
    height: 44px;
    font-size: 15px;
  }
}

/* 暗色模式适配 */
@media (prefers-color-scheme: dark) {
  .login-box {
    background: rgba(15, 23, 42, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .login-header p {
    color: #94a3b8;
  }
  
  .login-form :deep(.el-input__wrapper) {
    background: rgba(30, 41, 59, 0.8);
    border: 1px solid rgba(71, 85, 105, 0.6);
  }
  
  .login-form :deep(.el-input__inner) {
    color: #e2e8f0;
  }
  
  .login-tips {
    background: rgba(30, 41, 59, 0.5);
    border: 1px solid rgba(71, 85, 105, 0.3);
    color: #94a3b8;
  }
}
</style>