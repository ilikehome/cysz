<template>
  <div class="receivable-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>应收账款管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建账款
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="批次号">
            <el-input
              v-model="searchForm.batchNo"
              placeholder="请输入批次号"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="处理状态">
            <el-select
              v-model="searchForm.processStatus"
              placeholder="请选择处理状态"
              clearable
              style="width: 150px"
            >
              <el-option label="待处理" value="PENDING" />
              <el-option label="处理中" value="PROCESSING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已拒绝" value="REJECTED" />
            </el-select>
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
      
      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
      >
        <el-table-column prop="batchNo" label="批次号" width="120" />
        <el-table-column prop="lineNo" label="行号" width="80" />
        <el-table-column prop="processStatus" label="处理状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getProcessStatusTag(row.processStatus)">
              {{ getProcessStatusName(row.processStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="company" label="公司" width="120" />
        <el-table-column prop="tenantName" label="租户名称" width="150" />
        <el-table-column prop="contractNo" label="合同编号" width="120" />
        <el-table-column prop="payerName" label="付款人名称" width="150" />
        <el-table-column prop="amount" label="金额(元)" width="120">
          <template #default="{ row }">
            <span :class="{ 'amount-positive': row.debitCreditFlag === 'CREDIT', 'amount-negative': row.debitCreditFlag === 'DEBIT' }">
              ¥{{ row.amount?.toLocaleString() }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="pendingAmount" label="待认领金额(元)" width="140">
          <template #default="{ row }">
            ¥{{ row.pendingAmount?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="transactionTime" label="交易时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看</el-button>
            <el-button type="text" @click="handleClaim(row)" v-if="row.processStatus === 'PENDING'">认领</el-button>
            <el-button type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(row)" style="color: #f56c6c">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
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
    
    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="批次号" prop="batchNo">
              <el-input v-model="formData.batchNo" placeholder="请输入批次号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行号" prop="lineNo">
              <el-input v-model="formData.lineNo" placeholder="请输入行号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="处理状态" prop="processStatus">
              <el-select v-model="formData.processStatus" placeholder="请选择处理状态">
                <el-option label="待处理" value="PENDING" />
                <el-option label="处理中" value="PROCESSING" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="已拒绝" value="REJECTED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司">
              <el-input v-model="formData.company" placeholder="请输入公司" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="缴款方式">
              <el-select v-model="formData.paymentMethod" placeholder="请选择缴款方式">
                <el-option label="银行转账" value="银行转账" />
                <el-option label="现金" value="现金" />
                <el-option label="支票" value="支票" />
                <el-option label="网银" value="网银" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借贷标记">
              <el-select v-model="formData.debitCreditFlag" placeholder="请选择借贷标记">
                <el-option label="借方" value="DEBIT" />
                <el-option label="贷方" value="CREDIT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="收款人名称">
              <el-input v-model="formData.payeeName" placeholder="请输入收款人名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款人账号">
              <el-input v-model="formData.payeeAccount" placeholder="请输入收款人账号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="付款人名称">
              <el-input v-model="formData.payerName" placeholder="请输入付款人名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款人账号">
              <el-input v-model="formData.payerAccount" placeholder="请输入付款人账号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="租户名称">
              <el-input v-model="formData.tenantName" placeholder="请输入租户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同编号">
              <el-input v-model="formData.contractNo" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="金额(元)" prop="amount">
              <el-input-number
                v-model="formData.amount"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="待认领金额(元)">
              <el-input-number
                v-model="formData.pendingAmount"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入待认领金额"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交易时间">
              <el-date-picker
                v-model="formData.transactionTime"
                type="datetime"
                placeholder="请选择交易时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="录入日期">
              <el-date-picker
                v-model="formData.inputDate"
                type="date"
                placeholder="请选择录入日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="摘要">
          <el-input v-model="formData.summary" placeholder="请输入摘要" />
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
    
    <!-- 认领对话框 -->
    <el-dialog
      v-model="claimDialogVisible"
      title="认领账款"
      width="400px"
    >
      <el-form
        ref="claimFormRef"
        :model="claimForm"
        :rules="claimRules"
        label-width="100px"
      >
        <el-form-item label="认领人" prop="claimer">
          <el-input v-model="claimForm.claimer" placeholder="请输入认领人" />
        </el-form-item>
        <el-form-item label="认领日期" prop="claimDate">
          <el-date-picker
            v-model="claimForm.claimDate"
            type="date"
            placeholder="请选择认领日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleClaimSubmit" :loading="claimLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { receivableApi, type ReceivableAccount } from '@/api'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const claimDialogVisible = ref(false)
const submitLoading = ref(false)
const claimLoading = ref(false)
const formRef = ref<FormInstance>()
const claimFormRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  batchNo: '',
  processStatus: '',
  tenantName: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<ReceivableAccount[]>([])

// 表单数据
const formData = reactive({
  id: null,
  batchNo: '',
  lineNo: '',
  processStatus: 'PENDING',
  company: '',
  paymentMethod: '',
  payeeName: '',
  payeeAccount: '',
  payerName: '',
  payerAccount: '',
  tenantName: '',
  contractNo: '',
  amount: null,
  pendingAmount: null,
  transactionTime: '',
  inputDate: '',
  debitCreditFlag: '',
  summary: '',
  remark: ''
})

// 认领表单
const claimForm = reactive({
  id: null,
  claimer: '',
  claimDate: ''
})

// 表单验证规则
const formRules: FormRules = {
  batchNo: [
    { required: true, message: '请输入批次号', trigger: 'blur' }
  ],
  lineNo: [
    { required: true, message: '请输入行号', trigger: 'blur' }
  ],
  processStatus: [
    { required: true, message: '请选择处理状态', trigger: 'change' }
  ],
  amount: [
    { required: true, message: '请输入金额', trigger: 'blur' }
  ]
}

// 认领验证规则
const claimRules: FormRules = {
  claimer: [
    { required: true, message: '请输入认领人', trigger: 'blur' }
  ],
  claimDate: [
    { required: true, message: '请选择认领日期', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建账款')

// 获取处理状态标签颜色
const getProcessStatusTag = (status: string) => {
  const tagMap: Record<string, string> = {
    'PENDING': 'warning',
    'PROCESSING': 'primary',
    'COMPLETED': 'success',
    'REJECTED': 'danger'
  }
  return tagMap[status] || 'info'
}

// 获取处理状态名称
const getProcessStatusName = (status: string) => {
  const nameMap: Record<string, string> = {
    'PENDING': '待处理',
    'PROCESSING': '处理中',
    'COMPLETED': '已完成',
    'REJECTED': '已拒绝'
  }
  return nameMap[status] || status
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  searchForm.batchNo = ''
  searchForm.processStatus = ''
  searchForm.tenantName = ''
  handleSearch()
}

// 新建
const handleAdd = () => {
  dialogTitle.value = '新建账款'
  resetForm()
  dialogVisible.value = true
}

// 查看
const handleView = (row: any) => {
  ElMessage.info('查看功能开发中...')
}

// 认领
const handleClaim = (row: ReceivableAccount) => {
  claimForm.id = row.id
  claimForm.claimer = ''
  claimForm.claimDate = ''
  claimDialogVisible.value = true
}

// 编辑
const handleEdit = (row: ReceivableAccount) => {
  dialogTitle.value = '编辑账款'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row: ReceivableAccount) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除批次号为"${row.batchNo}"的账款记录吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await receivableApi.deleteReceivable(row.id!)
    ElMessage.success('删除成功')
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  loadData()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  pagination.current = current
  loadData()
}

// 对话框关闭
const handleDialogClose = () => {
  resetForm()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const receivableData: ReceivableAccount = {
      batchNo: formData.batchNo,
      lineNo: formData.lineNo,
      processStatus: formData.processStatus as 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'REJECTED',
      company: formData.company || undefined,
      projectId: formData.projectId || 1, // 默认项目ID
      paymentMethod: formData.paymentMethod || undefined,
      payeeName: formData.payeeName || undefined,
      payeeAccount: formData.payeeAccount || undefined,
      payeeBank: formData.payeeBank || undefined,
      payee: formData.payee || undefined,
      payerName: formData.payerName || undefined,
      tenantName: formData.tenantName || undefined,
      contractNo: formData.contractNo || undefined,
      payerAccount: formData.payerAccount || undefined,
      payer: formData.payer || undefined,
      payerBankCode: formData.payerBankCode || undefined,
      transactionTime: formData.transactionTime || undefined,
      amount: formData.amount || undefined,
      pendingAmount: formData.pendingAmount || undefined,
      inputDate: formData.inputDate || undefined,
      debitCreditFlag: formData.debitCreditFlag as 'DEBIT' | 'CREDIT' || undefined,
      summary: formData.summary || undefined,
      remark: formData.remark || undefined
    }
    
    if (formData.id) {
      await receivableApi.updateReceivable(formData.id, receivableData)
      ElMessage.success('更新成功')
    } else {
      await receivableApi.createReceivable(receivableData)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 认领提交
const handleClaimSubmit = async () => {
  if (!claimFormRef.value) return
  
  try {
    await claimFormRef.value.validate()
    claimLoading.value = true
    
    const claimData = {
      claimer: claimForm.claimer,
      claimDate: claimForm.claimDate
    }
    
    await receivableApi.claimReceivable(claimForm.id!, claimData)
    ElMessage.success('认领成功')
    claimDialogVisible.value = false
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '认领失败')
  } finally {
    claimLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: null,
    batchNo: '',
    lineNo: '',
    processStatus: 'PENDING',
    company: '',
    projectId: null,
    paymentMethod: '',
    payeeName: '',
    payeeAccount: '',
    payeeBank: '',
    payee: '',
    payerName: '',
    payerAccount: '',
    payer: '',
    payerBankCode: '',
    tenantName: '',
    contractNo: '',
    amount: null,
    pendingAmount: null,
    transactionTime: '',
    inputDate: '',
    debitCreditFlag: '',
    summary: '',
    remark: ''
  })
  formRef.value?.resetFields()
}

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    
    const params = {
      current: pagination.current,
      size: pagination.size,
      batchNo: searchForm.batchNo || undefined,
      processStatus: searchForm.processStatus || undefined,
      tenantName: searchForm.tenantName || undefined
    }
    
    const response = await receivableApi.getReceivablePage(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error: any) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.receivable-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
}

.receivable-management :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  transition: all 0.3s ease;
}

.receivable-management :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.receivable-management :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.receivable-management :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  display: flex;
  align-items: center;
}

.card-header span::before {
  content: '';
  width: 4px;
  height: 20px;
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  border-radius: 2px;
  margin-right: 12px;
}

.card-header :deep(.el-button) {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(245, 158, 11, 0.2);
}

.card-header :deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(245, 158, 11, 0.3);
}

.search-area {
  margin-bottom: 24px;
  padding: 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 12px;
  flex-shrink: 0;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.search-area :deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

.search-area :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.search-area :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.search-area :deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

.search-area :deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.search-area :deep(.el-button--primary) {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(245, 158, 11, 0.3);
}

.search-area :deep(.el-button--primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.4);
}

.el-table {
  flex: 1;
  overflow: auto;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.el-table :deep(.el-table__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.el-table :deep(.el-table__header th) {
  background: transparent;
  color: #374151;
  font-weight: 600;
  border-bottom: 2px solid #e2e8f0;
  padding: 16px 12px;
}

.el-table :deep(.el-table__body tr) {
  transition: all 0.3s ease;
}

.el-table :deep(.el-table__body tr:hover) {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  transform: scale(1.01);
}

.el-table :deep(.el-table__body td) {
  padding: 16px 12px;
  border-bottom: 1px solid #f1f5f9;
}

.el-table :deep(.el-tag) {
  border-radius: 6px;
  font-weight: 500;
  padding: 4px 12px;
  border: none;
}

.el-table :deep(.el-tag--warning) {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
}

.el-table :deep(.el-tag--primary) {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: white;
}

.el-table :deep(.el-tag--success) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

.el-table :deep(.el-tag--danger) {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
}

.el-table :deep(.el-button--text) {
  font-weight: 500;
  border-radius: 6px;
  padding: 6px 12px;
  transition: all 0.3s ease;
}

.el-table :deep(.el-button--text:hover) {
  background: rgba(245, 158, 11, 0.1);
  color: #f59e0b;
  transform: translateY(-1px);
}

.pagination {
  margin-top: 24px;
  text-align: right;
  flex-shrink: 0;
}

.pagination :deep(.el-pagination) {
  justify-content: flex-end;
}

.pagination :deep(.el-pager li) {
  border-radius: 8px;
  margin: 0 2px;
  transition: all 0.3s ease;
}

.pagination :deep(.el-pager li:hover) {
  transform: translateY(-1px);
}

.pagination :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(245, 158, 11, 0.3);
}

.pagination :deep(.btn-prev),
.pagination :deep(.btn-next) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.pagination :deep(.btn-prev:hover),
.pagination :deep(.btn-next:hover) {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.amount-positive {
  color: #10b981;
  font-weight: 600;
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.1) 0%, rgba(5, 150, 105, 0.1) 100%);
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.amount-negative {
  color: #ef4444;
  font-weight: 600;
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.1) 0%, rgba(220, 38, 38, 0.1) 100%);
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid rgba(239, 68, 68, 0.2);
}

/* 添加加载动画 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.receivable-management {
  animation: fadeInUp 0.6s ease forwards;
}
</style>
