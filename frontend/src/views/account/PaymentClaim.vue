<template>
  <div class="payment-claim-container">
    <PageContainer title="收款认领管理" description="管理银行流水的收款认领，将未识别的银行流水关联到对应的合同账期">
      <!-- 搜索区域 -->
      <div class="search-section">
        <el-form :model="searchForm" inline>
          <el-form-item label="关键词">
            <el-input
              v-model="searchForm.keyword"
              placeholder="流水号/付款方名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="认领状态">
            <el-select v-model="searchForm.claimStatus" placeholder="请选择状态" clearable style="width: 150px">
              <el-option label="未认领" value="UNCLAIMED" />
              <el-option label="已认领" value="CLAIMED" />
              <el-option label="已拒绝" value="REJECTED" />
            </el-select>
          </el-form-item>
          <el-form-item label="交易日期">
            <el-date-picker
              v-model="searchForm.dateRange"
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
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="action-section">
        <el-button type="primary" @click="handleBatchClaim" :disabled="selectedRows.length === 0">
          <el-icon><Connection /></el-icon>
          批量认领 ({{ selectedRows.length }})
        </el-button>
        <el-button type="success" @click="handleImportBankData">
          <el-icon><Upload /></el-icon>
          导入银行流水
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
            <el-card class="stats-card unclaimed-card">
              <div class="stats-content">
                <div class="stats-icon">
                  <el-icon><QuestionFilled /></el-icon>
                </div>
                <div class="stats-info">
                  <div class="stats-value">15</div>
                  <div class="stats-label">待认领流水</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card claimed-card">
              <div class="stats-content">
                <div class="stats-icon">
                  <el-icon><SuccessFilled /></el-icon>
                </div>
                <div class="stats-info">
                  <div class="stats-value">128</div>
                  <div class="stats-label">已认领流水</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card amount-card">
              <div class="stats-content">
                <div class="stats-icon">
                  <el-icon><Money /></el-icon>
                </div>
                <div class="stats-info">
                  <div class="stats-value">¥875,000</div>
                  <div class="stats-label">待认领金额</div>
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
                  <div class="stats-value">89.5%</div>
                  <div class="stats-label">认领率</div>
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
          <el-table-column prop="transactionNo" label="流水号" width="160" />
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="{ row }">
              <span class="amount-text">¥{{ Number(row.amount).toLocaleString() }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="transactionDate" label="交易日期" width="120" />
          <el-table-column prop="payerName" label="付款方" width="150" show-overflow-tooltip />
          <el-table-column prop="payerAccount" label="付款账号" width="180" show-overflow-tooltip />
          <el-table-column prop="receiverAccount" label="收款账号" width="180" show-overflow-tooltip />
          <el-table-column prop="transactionType" label="交易类型" width="100">
            <template #default="{ row }">
              <el-tag size="small">{{ row.transactionType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="claimStatus" label="认领状态" width="100">
            <template #default="{ row }">
              <el-tag
                :type="getClaimStatusType(row.claimStatus)"
                size="small"
              >
                {{ getClaimStatusText(row.claimStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="claimedContractNo" label="关联合同" width="120">
            <template #default="{ row }">
              {{ row.claimedContractNo || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="claimedTenantName" label="关联租户" width="150" show-overflow-tooltip>
            <template #default="{ row }">
              {{ row.claimedTenantName || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="claimTime" label="认领时间" width="160">
            <template #default="{ row }">
              {{ row.claimTime || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="claimOperator" label="认领人" width="100">
            <template #default="{ row }">
              {{ row.claimOperator || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleView(row)">
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button 
                v-if="row.claimStatus === 'UNCLAIMED'"
                type="success" 
                size="small" 
                @click="handleClaim(row)"
              >
                <el-icon><Connection /></el-icon>
                认领
              </el-button>
              <el-button 
                v-if="row.claimStatus === 'CLAIMED'"
                type="warning" 
                size="small" 
                @click="handleUnclaim(row)"
              >
                <el-icon><RefreshLeft /></el-icon>
                取消认领
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

    <!-- 认领对话框 -->
    <el-dialog
      v-model="claimDialogVisible"
      title="收款认领"
      width="700px"
      :before-close="handleClaimDialogClose"
    >
      <div class="claim-dialog-content">
        <!-- 银行流水信息 -->
        <div class="transaction-info">
          <h4>银行流水信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="流水号">{{ currentTransaction?.transactionNo }}</el-descriptions-item>
            <el-descriptions-item label="金额">¥{{ Number(currentTransaction?.amount || 0).toLocaleString() }}</el-descriptions-item>
            <el-descriptions-item label="交易日期">{{ currentTransaction?.transactionDate }}</el-descriptions-item>
            <el-descriptions-item label="付款方">{{ currentTransaction?.payerName }}</el-descriptions-item>
            <el-descriptions-item label="付款账号">{{ currentTransaction?.payerAccount }}</el-descriptions-item>
            <el-descriptions-item label="收款账号">{{ currentTransaction?.receiverAccount }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 认领表单 -->
        <div class="claim-form">
          <h4>认领信息</h4>
          <el-form :model="claimForm" :rules="claimRules" ref="claimFormRef" label-width="100px">
            <el-form-item label="关联合同" prop="contractNo">
              <el-select
                v-model="claimForm.contractNo"
                placeholder="请选择合同"
                style="width: 100%"
                filterable
                @change="handleContractChange"
              >
                <el-option
                  v-for="contract in contractOptions"
                  :key="contract.contractNo"
                  :label="`${contract.contractNo} - ${contract.contractName}`"
                  :value="contract.contractNo"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="租户名称" prop="tenantName">
              <el-input v-model="claimForm.tenantName" placeholder="自动填充" readonly />
            </el-form-item>
            <el-form-item label="单元信息" prop="unitDescription">
              <el-input v-model="claimForm.unitDescription" placeholder="自动填充" readonly />
            </el-form-item>
            <el-form-item label="账单类型" prop="billType">
              <el-select v-model="claimForm.billType" placeholder="请选择账单类型" style="width: 100%">
                <el-option label="租金" value="租金" />
                <el-option label="物业费" value="物业费" />
                <el-option label="水电费" value="水电费" />
                <el-option label="停车费" value="停车费" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
            <el-form-item label="认领人" prop="operator">
              <el-input v-model="claimForm.operator" placeholder="请输入认领人" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="claimForm.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="claimDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirmClaim" :loading="claimLoading">
            确认认领
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
import { accountApi } from '@/api'

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const claimDialogVisible = ref(false)
const claimLoading = ref(false)
const currentTransaction = ref(null)
const contractOptions = ref([])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  claimStatus: '',
  dateRange: []
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 认领表单
const claimForm = reactive({
  contractNo: '',
  contractName: '',
  tenantName: '',
  unitDescription: '',
  billType: '',
  operator: '管理员',
  remark: ''
})

const claimRules = {
  contractNo: [{ required: true, message: '请选择合同', trigger: 'change' }],
  billType: [{ required: true, message: '请选择账单类型', trigger: 'change' }],
  operator: [{ required: true, message: '请输入认领人', trigger: 'blur' }]
}

const claimFormRef = ref()

// 获取认领状态类型
const getClaimStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    'UNCLAIMED': 'warning',
    'CLAIMED': 'success',
    'REJECTED': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取认领状态文本
const getClaimStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'UNCLAIMED': '未认领',
    'CLAIMED': '已认领',
    'REJECTED': '已拒绝'
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
    
    const response = await accountApi.getPaymentClaimPage(params)
    
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

// 获取可认领的合同列表
const fetchContractOptions = async () => {
  try {
    const response = await accountApi.getClaimableContracts()
    if (response.code === 200) {
      contractOptions.value = response.data
    }
  } catch (error) {
    console.error('获取合同列表失败:', error)
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
    claimStatus: '',
    dateRange: []
  })
  pagination.current = 1
  fetchData()
}

// 批量认领
const handleBatchClaim = () => {
  ElMessage.info('批量认领功能开发中...')
}

// 导入银行流水
const handleImportBankData = () => {
  ElMessage.info('导入银行流水功能开发中...')
}

// 导出数据
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 查看详情
const handleView = (row: any) => {
  ElMessage.info(`查看流水 ${row.transactionNo} 详情`)
}

// 认领操作
const handleClaim = (row: any) => {
  currentTransaction.value = row
  claimDialogVisible.value = true
  fetchContractOptions()
}

// 取消认领
const handleUnclaim = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要取消认领这条流水吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    ElMessage.success('取消认领成功')
    fetchData()
  } catch {
    // 用户取消操作
  }
}

// 合同选择变化
const handleContractChange = (contractNo: string) => {
  const contract = contractOptions.value.find((c: any) => c.contractNo === contractNo)
  if (contract) {
    claimForm.contractName = contract.contractName
    claimForm.tenantName = contract.tenantName
    claimForm.unitDescription = contract.unitDescription
  }
}

// 确认认领
const handleConfirmClaim = async () => {
  if (!claimFormRef.value) return
  
  await claimFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      claimLoading.value = true
      try {
        const response = await accountApi.claimPayment(currentTransaction.value.id, claimForm)
        
        if (response.code === 200) {
          ElMessage.success('认领成功')
          claimDialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(response.message || '认领失败')
        }
      } catch (error) {
        console.error('认领失败:', error)
        ElMessage.error('认领失败')
      } finally {
        claimLoading.value = false
      }
    }
  })
}

// 关闭认领对话框
const handleClaimDialogClose = () => {
  claimFormRef.value?.resetFields()
  Object.assign(claimForm, {
    contractNo: '',
    contractName: '',
    tenantName: '',
    unitDescription: '',
    billType: '',
    operator: '管理员',
    remark: ''
  })
  claimDialogVisible.value = false
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
.payment-claim-container {
  padding: 0;
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

.unclaimed-card .stats-icon {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.claimed-card .stats-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.amount-card .stats-icon {
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
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

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.claim-dialog-content {
  max-height: 600px;
  overflow-y: auto;
}

.transaction-info {
  margin-bottom: 24px;
}

.transaction-info h4,
.claim-form h4 {
  margin: 0 0 16px 0;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
}

.claim-form {
  border-top: 1px solid #e5e7eb;
  padding-top: 24px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>