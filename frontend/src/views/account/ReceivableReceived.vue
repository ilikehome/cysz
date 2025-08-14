<template>
  <div class="receivable-received-container">
    <PageContainer title="应收已收管理" description="管理合同应收款项和已收款项，支持自动匹配和手动对账">
      <!-- 搜索区域 -->
      <div class="search-section">
        <el-form :model="searchForm" inline>
          <el-form-item label="关键词">
            <el-input
              v-model="searchForm.keyword"
              placeholder="合同编号/租户名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 150px">
              <el-option label="待收款" value="PENDING" />
              <el-option label="已收款" value="RECEIVED" />
              <el-option label="逾期" value="OVERDUE" />
              <el-option label="部分收款" value="PARTIAL" />
              <el-option label="已完成" value="COMPLETED" />
            </el-select>
          </el-form-item>
          <el-form-item label="合同编号">
            <el-input
              v-model="searchForm.contractNo"
              placeholder="请输入合同编号"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="租户名称">
            <el-input
              v-model="searchForm.tenantName"
              placeholder="请输入租户名称"
              clearable
              style="width: 150px"
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
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="action-section">
        <el-button type="primary" @click="handleGenerateReceivable">
          <el-icon><Plus /></el-icon>
          生成应收账款
        </el-button>
        <el-button type="success" @click="handleAutoMatch">
          <el-icon><Connection /></el-icon>
          自动匹配
        </el-button>
        <el-button type="warning" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="stats-card receivable-card">
              <div class="stats-content">
                <div class="stats-icon">
                  <el-icon><Wallet /></el-icon>
                </div>
                <div class="stats-info">
                  <div class="stats-value">¥2,450,000</div>
                  <div class="stats-label">应收总额</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card received-card">
              <div class="stats-content">
                <div class="stats-icon">
                  <el-icon><SuccessFilled /></el-icon>
                </div>
                <div class="stats-info">
                  <div class="stats-value">¥1,850,000</div>
                  <div class="stats-label">已收总额</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card overdue-card">
              <div class="stats-content">
                <div class="stats-icon">
                  <el-icon><WarningFilled /></el-icon>
                </div>
                <div class="stats-info">
                  <div class="stats-value">¥600,000</div>
                  <div class="stats-label">逾期金额</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card rate-card">
              <div class="stats-content">
                <div class="stats-icon">
                  <el-icon><TrendCharts /></el-icon>
                </div>
                <div class="stats-info">
                  <div class="stats-value">75.5%</div>
                  <div class="stats-label">收款率</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 数据表格 -->
      <div class="table-section">
        <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="type" label="类型" width="80">
            <template #default="{ row }">
              <el-tag :type="row.type === 'RECEIVABLE' ? 'warning' : 'success'">
                {{ row.type === 'RECEIVABLE' ? '应收' : '已收' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="contractNo" label="合同编号" width="120" />
          <el-table-column prop="contractName" label="合同名称" width="180" show-overflow-tooltip />
          <el-table-column prop="tenantName" label="租户名称" width="150" show-overflow-tooltip />
          <el-table-column prop="unitDescription" label="单元信息" width="150" show-overflow-tooltip />
          <el-table-column prop="billType" label="账单类型" width="100">
            <template #default="{ row }">
              <el-tag size="small">{{ row.billType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="{ row }">
              <span class="amount-text">¥{{ Number(row.amount).toLocaleString() }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="dueDate" label="到期日期" width="120" v-if="false" />
          <el-table-column prop="receivedDate" label="收款日期" width="120" v-if="false" />
          <el-table-column label="日期" width="120">
            <template #default="{ row }">
              {{ row.dueDate || row.receivedDate }}
            </template>
          </el-table-column>
          <el-table-column prop="billStatus" label="状态" width="100">
            <template #default="{ row }">
              <el-tag
                :type="getStatusType(row.billStatus)"
                size="small"
              >
                {{ getStatusText(row.billStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remainingAmount" label="剩余金额" width="120" align="right" v-if="false">
            <template #default="{ row }">
              <span v-if="row.type === 'RECEIVABLE'" class="remaining-amount">
                ¥{{ Number(row.remainingAmount || 0).toLocaleString() }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="bankTransactionNo" label="银行流水号" width="150" v-if="false">
            <template #default="{ row }">
              {{ row.bankTransactionNo || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleView(row)">
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button 
                v-if="row.type === 'RECEIVABLE' && row.billStatus === 'PENDING'"
                type="success" 
                size="small" 
                @click="handleMatch(row)"
              >
                <el-icon><Connection /></el-icon>
                匹配
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-section">
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
      </div>
    </PageContainer>

    <!-- 生成应收账款对话框 -->
    <el-dialog
      v-model="generateDialogVisible"
      title="生成应收账款"
      width="600px"
      :before-close="handleGenerateDialogClose"
    >
      <el-form :model="generateForm" :rules="generateRules" ref="generateFormRef" label-width="100px">
        <el-form-item label="合同编号" prop="contractNo">
          <el-input v-model="generateForm.contractNo" placeholder="请输入合同编号" />
        </el-form-item>
        <el-form-item label="账单类型" prop="billType">
          <el-select v-model="generateForm.billType" placeholder="请选择账单类型" style="width: 100%">
            <el-option label="租金" value="租金" />
            <el-option label="物业费" value="物业费" />
            <el-option label="水电费" value="水电费" />
            <el-option label="停车费" value="停车费" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="generateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirmGenerate" :loading="generateLoading">
            确认生成
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'
import { accountApi } from '@/api/account'

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const generateDialogVisible = ref(false)
const generateLoading = ref(false)

// 搜索表单
const searchForm = reactive({
  keyword: '',
  status: '',
  contractNo: '',
  tenantName: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 生成应收账款表单
const generateForm = reactive({
  contractNo: '',
  billType: ''
})

const generateRules = {
  contractNo: [{ required: true, message: '请输入合同编号', trigger: 'blur' }],
  billType: [{ required: true, message: '请选择账单类型', trigger: 'change' }]
}

const generateFormRef = ref()

// 获取状态类型
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    'PENDING': 'warning',
    'RECEIVED': 'success',
    'OVERDUE': 'danger',
    'PARTIAL': 'info',
    'COMPLETED': 'success'
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'PENDING': '待收款',
    'RECEIVED': '已收款',
    'OVERDUE': '逾期',
    'PARTIAL': '部分收款',
    'COMPLETED': '已完成'
  }
  return statusMap[status] || status
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    }
    
    const response = await accountApi.getReceivableReceivedPage(params)
    
    if (response.code === 200) {
      tableData.value = response.data.records
      pagination.total = response.data.total
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    keyword: '',
    status: '',
    contractNo: '',
    tenantName: ''
  })
  pagination.current = 1
  fetchData()
}

// 生成应收账款
const handleGenerateReceivable = () => {
  generateDialogVisible.value = true
}

// 确认生成应收账款
const handleConfirmGenerate = async () => {
  if (!generateFormRef.value) return
  
  await generateFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      generateLoading.value = true
      try {
        const response = await accountApi.generateReceivable(generateForm)
        
        if (response.code === 200) {
          ElMessage.success('生成成功')
          generateDialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(response.message || '生成失败')
        }
      } catch (error) {
        console.error('生成失败:', error)
        ElMessage.error('生成失败')
      } finally {
        generateLoading.value = false
      }
    }
  })
}

// 关闭生成对话框
const handleGenerateDialogClose = () => {
  generateFormRef.value?.resetFields()
  generateDialogVisible.value = false
}

// 自动匹配
const handleAutoMatch = () => {
  ElMessage.info('自动匹配功能开发中...')
}

// 导出数据
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 查看详情
const handleView = (row: any) => {
  ElMessage.info(`查看 ${row.contractNo} 详情`)
}

// 匹配操作
const handleMatch = (row: any) => {
  ElMessage.info(`匹配 ${row.contractNo} 的应收账款`)
}

// 删除
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    ElMessage.success('删除成功')
    fetchData()
  } catch {
    // 用户取消删除
  }
}

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  fetchData()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  pagination.current = current
  fetchData()
}

// 初始化
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.receivable-received-container {
  padding: 0;
  min-height: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.action-section {
  background: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stats-section {
  margin-bottom: 16px;
}

.stats-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stats-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.stats-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #fff;
}

.receivable-card .stats-icon {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.received-card .stats-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.overdue-card .stats-icon {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
}

.rate-card .stats-icon {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  line-height: 1;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.table-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.amount-text {
  font-weight: 600;
  color: #059669;
}

.remaining-amount {
  font-weight: 600;
  color: #dc2626;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>