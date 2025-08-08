import { createApp } from 'vue'

// 创建一个最简单的Vue应用
const app = createApp({
  template: `
    <div style="height: 100vh; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center;">
      <div style="background: white; padding: 40px; border-radius: 10px; text-align: center;">
        <h1 style="color: #333; margin-bottom: 20px;">云联智管</h1>
        <p style="color: #666; margin-bottom: 30px;">云联万物 智管未来</p>
        <div style="margin-bottom: 20px;">
          <input v-model="username" placeholder="用户名" style="width: 100%; padding: 10px; margin-bottom: 10px; border: 1px solid #ddd; border-radius: 4px;" />
          <input v-model="password" type="password" placeholder="密码" style="width: 100%; padding: 10px; margin-bottom: 10px; border: 1px solid #ddd; border-radius: 4px;" />
        </div>
        <button @click="login" style="width: 100%; padding: 12px; background: #409EFF; color: white; border: none; border-radius: 4px; cursor: pointer;">
          {{ loading ? '登录中...' : '登录' }}
        </button>
        <p v-if="message" style="margin-top: 20px; color: green;">{{ message }}</p>
      </div>
    </div>
  `,
  data() {
    return {
      username: 'admin',
      password: '123456',
      loading: false,
      message: ''
    }
  },
  methods: {
    async login() {
      this.loading = true
      try {
        const response = await fetch('/api/auth/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            username: this.username,
            password: this.password
          })
        })
        const data = await response.json()
        if (data.code === 200) {
          this.message = '登录成功！Token: ' + data.data.token.substring(0, 10) + '...'
        } else {
          this.message = '登录失败: ' + data.message
        }
      } catch (error) {
        this.message = '登录错误: ' + error.message
      } finally {
        this.loading = false
      }
    }
  }
})

app.mount('#app')