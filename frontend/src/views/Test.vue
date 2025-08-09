<template>
  <div style="padding: 20px; background: white; height: 100vh;">
    <h1>测试页面</h1>
    <p>如果你能看到这个页面，说明Vue应用正常工作</p>
    <el-button type="primary" @click="testLogin">测试登录</el-button>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'

const testLogin = () => {
  ElMessage.success('测试按钮点击成功！')
}
</script>