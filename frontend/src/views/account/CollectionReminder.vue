<template>
  <div class="collection-reminder">
    <PageContainer title="自动催缴" description="智能化催缴管理，提高收款效率，降低逾期风险">
      <!-- 功能导航 -->
      <el-card class="nav-card" shadow="never">
        <el-tabs v-model="activeTab" @tab-change="handleTabChange">
          <el-tab-pane label="催缴概览" name="overview">
            <CollectionOverview />
          </el-tab-pane>
          <el-tab-pane label="待催缴列表" name="pending">
            <PendingList />
          </el-tab-pane>
          <el-tab-pane label="催缴记录" name="records">
            <ReminderRecords />
          </el-tab-pane>
          <el-tab-pane label="催缴设置" name="settings">
            <ReminderSettings />
          </el-tab-pane>
          <el-tab-pane label="短信模板" name="templates">
            <SmsTemplates />
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </PageContainer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import PageContainer from '@/components/PageContainer.vue'
import CollectionOverview from './components/CollectionOverview.vue'
import PendingList from './components/PendingList.vue'
import ReminderRecords from './components/ReminderRecords.vue'
import ReminderSettings from './components/ReminderSettings.vue'
import SmsTemplates from './components/SmsTemplates.vue'

const activeTab = ref('overview')

const handleTabChange = (tabName: string) => {
  console.log('切换到标签页:', tabName)
}
</script>

<style scoped>
.collection-reminder {
  padding: 0;
}

.nav-card {
  min-height: 600px;
}

:deep(.el-tabs__content) {
  padding: 20px 0;
}
</style>
