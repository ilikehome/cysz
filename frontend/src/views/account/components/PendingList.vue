<template>
  <div class="pending-list">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="left-actions">
        <el-button type="primary" @click="handleBatchReminder" :disabled="selectedRows.length === 0">
          <el-icon><Message /></el-icon>
          批量催缴 ({{ selectedRows.length }})
        </el-button>
        <el-button type="warning" @click="handleSetPriority" :disabled="selectedRows.length === 0">
          <el-icon><Star /></el-icon>
          设置优先级
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
      <div class="right-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索租户名称或合同号"
          style="width: 250px"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
      stripe
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      
      <el-table-column prop="tenantName" label="租户名称" width="120" />
      
      <el-table-column prop="contractNo" label="合同编号" width="120" />
      
      <el-table-column prop="phone" label="联系电话" width="130" />
      
      <el-table-column prop="amount" label="应收金额" width="120" align="right">
        <template #default="{ row }">
          <span class="amount-text">¥{{ row.amount.toLocaleString() }}</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="dueDate" label="到期日期" width="120" />
      
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
      
      <el-table-column prop="lastReminderTime" label="最后催缴时间" width="160">
        <template #default="{ row }">
          <span v-if="row.lastReminderTime">{{ row.lastReminderTime }}</span>
          <span v-else class="no-data">未催缴</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="priority" label="优先级" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getPriorityTagType(row.priority)" size="small">
            {{ row.priority }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleSingleReminder(row)">
            立即催缴
          </el-button>
          <el-button type="info" size="small" @click="handleViewHistory(row)">
            查看记录
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

    <!-- 批量催缴对话框 -->
    <el-dialog v-model="batchReminderVisible" title="批量催缴" width="600px">
      <div class="batch-reminder-content">
        <p>已选择 <strong>{{ selectedRows.length }}</strong> 条记录，总金额 <strong>¥{{ selectedTotalAmount.toLocaleString() }}</strong></p>
        
        <el-form :model="batchForm" label-width="100px">
          <el-form-item label="短信模板">
            <el-select v-model="batchForm.templateId" placeholder="请选择短信模板" style="width: 100%">
              <el-option
                v-for="template in smsTemplates"
                :key="template.id"
                :label="template.name"
                :value="template.id"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="模板预览">
            <div class="template-preview">
              {{ getTemplatePreview() }}
            </div>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <el-button @click="batchReminderVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBatchReminder" :loading="sending">
          确认发送
        </el-button>
      </template>
    </el-dialog>

    <!-- 设置优先级对话框 -->
    <el-dialog v-model="priorityVisible" title="设置优先级" width="400px">
      <el-form :model="priorityForm" label-width="80px">
        <el-form-item label="优先级">
          <el-radio-group v-model="priorityForm.priority">
            <el-radio label="高">高优先级</el-radio>
            <el-radio label="中">中优先级</el-radio>
            <el-radio label="低">低优先级</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="priorityVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSetPriority">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { Message, Star, Refresh, Search } from '@element-plus/icons-vue'
import { collectionReminderApi } from '@/api/collectionReminder'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)
const searchKeyword = ref('')
const tableData = ref<any[]>([])
const selectedRows = ref<any[]>([])
const smsTemplates = ref<any[]>([])

// 分页数据
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 对话框状态
const batchReminderVisible = ref(false)
const priorityVisible = ref(false)
const sending = ref(false)

// 表单数据
const batchForm = ref({
  templateId: null
})

const priorityForm = ref({
  priority: '中'
})

// 计算属性
const selectedTotalAmount = computed(() => {
  return selectedRows.value.reduce((sum, row) => sum + row.amount, 0)
})

// 获取待催缴列表
const fetchPendingList = async () => {
  loading.value = true
  try {
    const response = await collectionReminderApi.getPendingReceivables({
      current: pagination.value.current,
      size: pagination.value.size
    })
    
    if (response.data.code === 200) {
      tableData.value = response.data.data.records
      pagination.value.total = response.data.data.total
    }
  } catch (error) {
    console.error('获取待催缴列表失败:', error)
    ElMessage.error('获取待催缴列表失败')
  } finally {
    loading.value = false
  }
}

// 获取短信模板
const fetchSmsTemplates = async () => {
  try {
    const response = await collectionReminderApi.getSmsTemplates()
    if (response.data.code === 200) {
      smsTemplates.value = response.data.data
    }
  } catch (error) {
    console.error('获取短信模板失败:', error)
  }
}

// 获取逾期标签类型
const getOverdueTagType = (days: number) => {
  if (days <= 7) return 'warning'
  if (days <= 30) return 'danger'
  return ''
}

// 获取优先级标签类型
const getPriorityTagType = (priority: string) => {
  switch (priority) {
    case '高': return 'danger'
    case '中': return 'warning'
    case '低': return 'info'
    default: return ''
  }
}

// 获取模板预览
const getTemplatePreview = () => {
  const template = smsTemplates.value.find(t => t.id === batchForm.value.templateId)
  if (!template) return '请选择短信模板'
  
  return template.content
    .replace('{tenantName}', '租户名称')
    .replace('{amount}', '金额')
    .replace('{days}', '天数')
    .replace('{phone}', '联系电话')
}

// 事件处理函数
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

const handleSearch = () => {
  pagination.value.current = 1
  fetchPendingList()
}

const handleRefresh = () => {
  fetchPendingList()
}

const handleSizeChange = (size: number) => {
  pagination.value.size = size
  fetchPendingList()
}

const handleCurrentChange = (current: number) => {
  pagination.value.current = current
  fetchPendingList()
}

const handleSingleReminder = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认对该租户发送催缴短信？', '确认催缴', {
      type: 'warning'
    })
    
    // 这里应该调用单个催缴API
    ElMessage.success('催缴短信发送成功')
    fetchPendingList()
  } catch (error) {
    // 用户取消
  }
}

const handleViewHistory = (row: any) => {
  ElMessage.info('查看催缴历史记录')
}

const handleBatchReminder = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要催缴的记录')
    return
  }
  batchReminderVisible.value = true
}

const handleSetPriority = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要设置优先级的记录')
    return
  }
  priorityVisible.value = true
}

const confirmBatchReminder = async () => {
  if (!batchForm.value.templateId) {
    ElMessage.warning('请选择短信模板')
    return
  }
  
  sending.value = true
  try {
    const receivableIds = selectedRows.value.map(row => row.id)
    await collectionReminderApi.sendManualReminder({
      receivableIds,
      templateId: batchForm.value.templateId
    })
    
    ElMessage.success('批量催缴短信发送成功')
    batchReminderVisible.value = false
    selectedRows.value = []
    fetchPendingList()
  } catch (error) {
    ElMessage.error('发送失败，请重试')
  } finally {
    sending.value = false
  }
}

const confirmSetPriority = async () => {
  try {
    const receivableIds = selectedRows.value.map(row => row.id)
    await collectionReminderApi.setPriority({
      receivableIds,
      priority: priorityForm.value.priority
    })
    
    ElMessage.success('优先级设置成功')
    priorityVisible.value = false
    selectedRows.value = []
    fetchPendingList()
  } catch (error) {
    ElMessage.error('设置失败，请重试')
  }
}

onMounted(() => {
  fetchPendingList()
  fetchSmsTemplates()
})
</script>

<style scoped>
.pending-list {
  padding: 0;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.left-actions {
  display: flex;
  gap: 12px;
}

.amount-text {
  font-weight: 600;
  color: #E6A23C;
}

.no-data {
  color: #909399;
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

.batch-reminder-content p {
  margin-bottom: 20px;
  padding: 12px;
  background: #f0f9ff;
  border-radius: 6px;
  color: #1890ff;
}

.template-preview {
  padding: 12px;
  background: #f5f5f5;
  border-radius: 6px;
  color: #666;
  line-height: 1.6;
  min-height: 60px;
}
</style>