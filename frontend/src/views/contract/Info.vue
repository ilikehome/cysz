<template>
  <div class="contract-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>合同档案</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建合同
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="合同编号">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入合同编号或名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="合同状态">
            <el-select
              v-model="searchForm.contractStatus"
              placeholder="请选择合同状态"
              clearable
              style="width: 150px"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="生效" value="ACTIVE" />
              <el-option label="过期" value="EXPIRED" />
              <el-option label="终止" value="TERMINATED" />
            </el-select>
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
        <el-table-column prop="contractNo" label="合同编号" width="150" />
        <el-table-column prop="contractName" label="合同名称" min-width="180" />
        <el-table-column prop="tenantName" label="租户名称" width="150" />
        <el-table-column prop="contractStatus" label="合同状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getContractStatusTag(row.contractStatus)">
              {{ getContractStatusName(row.contractStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="monthlyRent" label="月租金(元)" width="120">
          <template #default="{ row }">
            ¥{{ row.monthlyRent?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看</el-button>
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
            <el-form-item label="合同编号" prop="contractNo">
              <el-input
                v-model="formData.contractNo"
                placeholder="请输入合同编号"
                :disabled="!!formData.id"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="formData.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                type="date"
                placeholder="请选择开始日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                type="date"
                placeholder="请选择结束日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签订人">
              <el-input v-model="formData.signatory" placeholder="请输入签订人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同类型">
              <el-input v-model="formData.contractType" placeholder="请输入合同类型" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同状态" prop="contractStatus">
              <el-select v-model="formData.contractStatus" placeholder="请选择合同状态">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="生效" value="ACTIVE" />
                <el-option label="过期" value="EXPIRED" />
                <el-option label="终止" value="TERMINATED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租金模式">
              <el-select v-model="formData.rentMode" placeholder="请选择租金模式">
                <el-option label="月付" value="月付" />
                <el-option label="季付" value="季付" />
                <el-option label="半年付" value="半年付" />
                <el-option label="年付" value="年付" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="租金账单公司">
              <el-input v-model="formData.rentBillCompany" placeholder="请输入租金账单公司" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物业账单公司">
              <el-input v-model="formData.propertyBillCompany" placeholder="请输入物业账单公司" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="租赁号">
              <el-input v-model="formData.leaseNo" placeholder="请输入租赁号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租户名称" prop="tenantName">
              <el-input v-model="formData.tenantName" placeholder="请输入租户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="月租金(元)">
              <el-input-number
                v-model="formData.monthlyRent"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入月租金"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="押金(元)">
              <el-input-number
                v-model="formData.deposit"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入押金"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="单元说明">
          <el-input
            v-model="formData.unitDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入单元说明"
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
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { contractApi, type Contract } from '@/api'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  keyword: '',
  contractStatus: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<Contract[]>([])

// 表单数据
const formData = reactive({
  id: null,
  contractNo: '',
  contractName: '',
  startDate: '',
  endDate: '',
  signatory: '',
  contractType: '',
  contractStatus: 'DRAFT',
  rentBillCompany: '',
  propertyBillCompany: '',
  leaseNo: '',
  tenantName: '',
  unitDescription: '',
  rentMode: '',
  monthlyRent: null,
  deposit: null
})

// 表单验证规则
const formRules: FormRules = {
  contractNo: [
    { required: true, message: '请输入合同编号', trigger: 'blur' }
  ],
  contractName: [
    { required: true, message: '请输入合同名称', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束日期', trigger: 'change' }
  ],
  contractStatus: [
    { required: true, message: '请选择合同状态', trigger: 'change' }
  ],
  tenantName: [
    { required: true, message: '请输入租户名称', trigger: 'blur' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建合同')

// 获取合同状态标签颜色
const getContractStatusTag = (status: string) => {
  const tagMap: Record<string, string> = {
    'DRAFT': 'info',
    'ACTIVE': 'success',
    'EXPIRED': 'warning',
    'TERMINATED': 'danger'
  }
  return tagMap[status] || 'info'
}

// 获取合同状态名称
const getContractStatusName = (status: string) => {
  const nameMap: Record<string, string> = {
    'DRAFT': '草稿',
    'ACTIVE': '生效',
    'EXPIRED': '过期',
    'TERMINATED': '终止'
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
  searchForm.keyword = ''
  searchForm.contractStatus = ''
  handleSearch()
}

// 新建
const handleAdd = () => {
  dialogTitle.value = '新建合同'
  resetForm()
  dialogVisible.value = true
}

// 查看
const handleView = (row: any) => {
  ElMessage.info('查看功能开发中...')
}

// 编辑
const handleEdit = (row: Contract) => {
  dialogTitle.value = '编辑合同'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row: Contract) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除合同"${row.contractName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await contractApi.deleteContract(row.id!)
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
    
    const contractData: Contract = {
      contractNo: formData.contractNo,
      contractName: formData.contractName,
      projectId: formData.projectId || 1, // 默认项目ID
      startDate: formData.startDate,
      endDate: formData.endDate,
      signatory: formData.signatory || undefined,
      contractType: formData.contractType || undefined,
      contractStatus: formData.contractStatus as 'DRAFT' | 'ACTIVE' | 'EXPIRED' | 'TERMINATED',
      rentBillCompany: formData.rentBillCompany || undefined,
      propertyBillCompany: formData.propertyBillCompany || undefined,
      leaseNo: formData.leaseNo || undefined,
      tenantId: formData.tenantId || 1, // 默认租户ID
      tenantName: formData.tenantName,
      unitId: formData.unitId || 1, // 默认单元ID
      unitDescription: formData.unitDescription || undefined,
      rentMode: formData.rentMode || undefined,
      monthlyRent: formData.monthlyRent || undefined,
      deposit: formData.deposit || undefined
    }
    
    if (formData.id) {
      await contractApi.updateContract(formData.id, contractData)
      ElMessage.success('更新成功')
    } else {
      await contractApi.createContract(contractData)
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

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: null,
    contractNo: '',
    contractName: '',
    projectId: null,
    startDate: '',
    endDate: '',
    signatory: '',
    contractType: '',
    contractStatus: 'DRAFT',
    rentBillCompany: '',
    propertyBillCompany: '',
    leaseNo: '',
    tenantId: null,
    tenantName: '',
    unitId: null,
    unitDescription: '',
    rentMode: '',
    monthlyRent: null,
    deposit: null
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
      keyword: searchForm.keyword || undefined,
      contractStatus: searchForm.contractStatus || undefined
    }
    
    const response = await contractApi.getContractPage(params)
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
.contract-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
}

.contract-management :deep(.el-card) {
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

.contract-management :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.contract-management :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.contract-management :deep(.el-card__body) {
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
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: 2px;
  margin-right: 12px;
}

.card-header :deep(.el-button) {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.2);
}

.card-header :deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(16, 185, 129, 0.3);
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
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.search-area :deep(.el-button--primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
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

.el-table :deep(.el-tag--info) {
  background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
  color: white;
}

.el-table :deep(.el-tag--success) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

.el-table :deep(.el-tag--warning) {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
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
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
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
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
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

.contract-management {
  animation: fadeInUp 0.6s ease forwards;
}
</style>
