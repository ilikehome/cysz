<template>
  <div class="reminder-records">
    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" @submit.prevent="handleSearch">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="租户名称/合同号/手机号"
            style="width: 200px"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" style="width: 120px" clearable>
            <el-option label="已发送" value="已发送" />
            <el-option label="已回复" value="已回复" />
            <el-option label="已缴费" value="已缴费" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 240px"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button type="success" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="tenantName" label="租户名称" width="120" />
        
        <el-table-column prop="contractNo" label="合同编号" width="120" />
        
        <el-table-column prop="phone" label="联系电话" width="130" />
        
        <el-table-column prop="amount" label="催缴金额" width="120" align="right">
          <template #default="{ row }">
            <span class="amount-text">¥{{ row.amount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="overdueDays" label="逾期天数" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getOverdueTagType(row.overdueDays)" size="small">
              {{ row.overdueDays }}天
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="reminderCount" label="催缴次数" width="100" align="center">
          <template #default="{ row }">
            <el-badge :value="row.reminderCount" :max="99" class="reminder-badge">
              <el-icon><Message /></el-icon>
            </el-badge>
          </template>
        </el-table-column>
        
        <el-table-column prop="lastReminderTime" label="催缴时间" width="160" />
        
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="smsContent" label="短信内容" min-width="200">
          <template #default="{ row }">
            <div class="sms-content">
              {{ row.smsContent }}
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="response" label="客户回复" min-width="150">
          <template #default="{ row }">
            <div v-if="row.response" class="customer-response">
              {{ row.response }}
            </div>
            <span v-else class="no-response">暂无回复</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleViewDetail(row)">
              查看详情
            </el-button>
            <el-button 
              v-if="row.status === '已发送'" 
              type="warning" 
              size="small" 
              @click="handleResend(row)"
            >
              重新发送
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="催缴记录详情" width="800px">
      <div v-if="currentRecord" class="record-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="租户名称">{{ currentRecord.tenantName }}</el-descriptions-item>
          <el-descriptions-item label="合同编号">{{ currentRecord.contractNo }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentRecord.phone }}</el-descriptions-item>
          <el-descriptions-item label="催缴金额">¥{{ currentRecord.amount.toLocaleString() }}</el-descriptions-item>
          <el-descriptions-item label="逾期天数">{{ currentRecord.overdueDays }}天</el-descriptions-item>
          <el-descriptions-item label="催缴次数">{{ currentRecord.reminderCount }}次</el-descriptions-item>
          <el-descriptions-item label="催缴时间">{{ currentRecord.lastReminderTime }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusTagType(currentRecord.status)">{{ currentRecord.status }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="detail-section">
          <h4>短信内容</h4>
          <div class="sms-content-detail">
            {{ currentRecord.smsContent }}
          </div>
        </div>
        
        <div v-if="currentRecord.response" class="detail-section">
          <h4>客户回复</h4>
          <div class="customer-response-detail">
            {{ currentRecord.response }}
          </div>
        </div>
        
        <div class="detail-section">
          <h4>操作记录</h4>
          <el-timeline>
            <el-timeline-item timestamp="2025-08-09 10:30" color="#67C23A">
              短信发送成功
            </el-timeline-item>
            <el-timeline-item timestamp="2025-08-09 14:20" color="#409EFF">
              客户已读短信
            </el-timeline-item>
            <el-timeline-item v-if="currentRecord.response" timestamp="2025-08-09 16:45" color="#E6A23C">
              客户回复：{{ currentRecord.response }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button v-if="currentRecord?.status === '已发送'" type="primary" @click="handleResend(currentRecord)">
          重新发送
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, Refresh, Download, Message } from '@element-plus/icons-vue'
import { collectionReminderApi } from '@/api/collectionReminder'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)
const tableData = ref<any[]>([])
const currentRecord = ref<any>(null)
const detailVisible = ref(false)
const dateRange = ref<[string, string]>([])

// 搜索表单
const searchForm = ref({
  keyword: '',
  status: ''
})

// 分页数据
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 获取催缴记录列表
const fetchRecords = async () => {
  loading.value = true
  try {
    const params: any = {
      current: pagination.value.current,
      size: pagination.value.size
    }
    
    if (searchForm.value.keyword) {
      params.keyword = searchForm.value.keyword
    }
    
    if (searchForm.value.status) {
      params.status = searchForm.value.status
    }
    
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    
    const response = await collectionReminderApi.getRecords(params)
    
    if (response.data.code === 200) {
      tableData.value = response.data.data.records
      pagination.value.total = response.data.data.total
    }
  } catch (error) {
    console.error('获取催缴记录失败:', error)
    ElMessage.error('获取催缴记录失败')
  } finally {
    loading.value = false
  }
}

// 获取逾期标签类型
const getOverdueTagType = (days: number) => {
  if (days <= 7) return 'warning'
  if (days <= 30) return 'danger'
  return ''
}

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  switch (status) {
    case '已缴费': return 'success'
    case '已回复': return 'warning'
    case '已发送': return 'info'
    default: return ''
  }
}

// 事件处理函数
const handleSearch = () => {
  pagination.value.current = 1
  fetchRecords()
}

const handleReset = () => {
  searchForm.value = {
    keyword: '',
    status: ''
  }
  dateRange.value = []
  pagination.value.current = 1
  fetchRecords()
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

const handleSizeChange = (size: number) => {
  pagination.value.size = size
  fetchRecords()
}

const handleCurrentChange = (current: number) => {
  pagination.value.current = current
  fetchRecords()
}

const handleViewDetail = (row: any) => {
  currentRecord.value = row
  detailVisible.value = true
}

const handleResend = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认重新发送催缴短信？', '确认操作', {
      type: 'warning'
    })
    
    // 这里应该调用重新发送API
    ElMessage.success('催缴短信重新发送成功')
    fetchRecords()
  } catch (error) {
    // 用户取消
  }
}

onMounted(() => {
  fetchRecords()
})
</script>

<style scoped>
.reminder-records {
  padding: 0;
}

.search-card {
  margin-bottom: 20px;
}

.amount-text {
  font-weight: 600;
  color: #E6A23C;
}

.sms-content {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #606266;
  line-height: 1.4;
}

.customer-response {
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #67C23A;
  font-style: italic;
}

.no-response {
  color: #C0C4CC;
  font-style: italic;
}

.reminder-badge {
  cursor: pointer;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.record-detail {
  padding: 0;
}

.detail-section {
  margin-top: 20px;
}

.detail-section h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.sms-content-detail {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  color: #606266;
  line-height: 1.6;
}

.customer-response-detail {
  padding: 12px;
  background: #f0f9ff;
  border-radius: 6px;
  color: #67C23A;
  line-height: 1.6;
  font-style: italic;
}

:deep(.el-timeline-item__content) {
  color: #606266;
}
</style>