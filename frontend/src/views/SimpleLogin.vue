<template>
  <div style="height: 100vh; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center;">
    <div style="width: 400px; padding: 40px; background: white; border-radius: 10px; box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);">
      <h1 style="text-align: center; margin-bottom: 30px; color: #333;">云联智管</h1>
      <p style="text-align: center; margin-bottom: 30px; color: #666;">云联万物 智管未来</p>
      
      <form @submit.prevent="handleLogin">
        <div style="margin-bottom: 20px;">
          <input 
            v-model="username" 
            type="text" 
            placeholder="请输入用户名"
            style="width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px;"
          />
        </div>
        
        <div style="margin-bottom: 20px;">
          <input 
            v-model="password" 
            type="password" 
            placeholder="请输入密码"
            style="width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px;"
          />
        </div>
        
        <button 
          type="submit"
          :disabled="loading"
          style="width: 100%; padding: 12px; background: #409EFF; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer;"
        >
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
      
      <p style="text-align: center; margin-top: 20px; color: #999; font-size: 12px;">
        默认账号：admin / 123456
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const username = ref('admin')
const password = ref('123456')
const loading = ref(false)

const handleLogin = async () => {
  if (!username.value || !password.value) {
    alert('请输入用户名和密码')
    return
  }
  
  loading.value = true
  
  try {
    await userStore.login({
      username: username.value,
      password: password.value
    })
    
    alert('登录成功')
    router.push('/')
  } catch (error) {
    console.error('登录失败:', error)
    alert('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}
</script>