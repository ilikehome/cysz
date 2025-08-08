<template>
  <div class="page-container">
    <div v-if="title || subtitle" class="page-header">
      <h1 v-if="title" class="page-title">{{ title }}</h1>
      <p v-if="subtitle" class="page-subtitle">{{ subtitle }}</p>
    </div>
    <div class="page-content">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
// 通用页面容器组件，确保页面正确利用固定高度布局
interface Props {
  title?: string
  subtitle?: string
}

defineProps<Props>()
</script>

<style scoped>
.page-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 20px;
  overflow: hidden;
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.page-subtitle {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.page-content {
  flex: 1;
  overflow: auto;
}

.page-container :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-container :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.page-container :deep(.el-table) {
  flex: 1;
}

.page-container :deep(.el-table__body-wrapper) {
  overflow-y: auto;
}
</style>
