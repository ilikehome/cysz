<template>
  <div class="project-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建项目
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="项目名称">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入项目名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="项目类型">
            <el-select
              v-model="searchForm.projectType"
              placeholder="请选择项目类型"
              clearable
              style="width: 150px"
            >
              <el-option label="综合体" value="COMPLEX" />
              <el-option label="公寓" value="APARTMENT" />
              <el-option label="写字楼" value="OFFICE" />
              <el-option label="商业" value="COMMERCIAL" />
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="projectName" label="项目名称" min-width="150" />
        <el-table-column prop="projectType" label="项目类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getProjectTypeTag(row.projectType)">
              {{ getProjectTypeName(row.projectType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="companyName" label="所属公司" min-width="150" />
        <el-table-column prop="city" label="城市" width="100" />
        <el-table-column prop="buildingArea" label="建筑面积(㎡)" width="120" />
        <el-table-column prop="rentArea" label="计租面积(㎡)" width="120" />
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
      width="800px"
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
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="formData.projectName" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目类型" prop="projectType">
              <el-select v-model="formData.projectType" placeholder="请选择项目类型">
                <el-option label="综合体" value="COMPLEX" />
                <el-option label="公寓" value="APARTMENT" />
                <el-option label="写字楼" value="OFFICE" />
                <el-option label="商业" value="COMMERCIAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属公司" prop="companyName">
              <el-input v-model="formData.companyName" placeholder="请输入所属公司" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input v-model="formData.city" placeholder="请输入城市" />
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
        
        <el-form-item label="产权公司">
          <el-input v-model="formData.propertyRightCompany" placeholder="请输入产权公司" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="建筑面积(㎡)">
              <el-input-number
                v-model="formData.buildingArea"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计租面积(㎡)">
              <el-input-number
                v-model="formData.rentArea"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产权面积(㎡)">
              <el-input-number
                v-model="formData.propertyArea"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="地址">
          <el-input
            v-model="formData.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
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
import { projectApi, type Project } from '@/api'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  keyword: '',
  projectType: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<Project[]>([])

// 表单数据
const formData = reactive({
  id: null,
  projectName: '',
  projectType: '',
  companyName: '',
  city: '',
  rentBillCompany: '',
  propertyBillCompany: '',
  propertyRightCompany: '',
  buildingArea: null,
  rentArea: null,
  propertyArea: null,
  address: ''
})

// 表单验证规则
const formRules: FormRules = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' }
  ],
  projectType: [
    { required: true, message: '请选择项目类型', trigger: 'change' }
  ],
  companyName: [
    { required: true, message: '请输入所属公司', trigger: 'blur' }
  ],
  city: [
    { required: true, message: '请输入城市', trigger: 'blur' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建项目')

// 选中的行
const selectedRows = ref([])

// 获取项目类型标签颜色
const getProjectTypeTag = (type: string) => {
  const tagMap: Record<string, string> = {
    'COMPLEX': 'primary',
    'APARTMENT': 'success',
    'OFFICE': 'warning',
    'COMMERCIAL': 'info'
  }
  return tagMap[type] || 'info'
}

// 获取项目类型名称
const getProjectTypeName = (type: string) => {
  const nameMap: Record<string, string> = {
    'COMPLEX': '综合体',
    'APARTMENT': '公寓',
    'OFFICE': '写字楼',
    'COMMERCIAL': '商业'
  }
  return nameMap[type] || type
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.projectType = ''
  handleSearch()
}

// 新建
const handleAdd = () => {
  dialogTitle.value = '新建项目'
  resetForm()
  dialogVisible.value = true
}

// 查看
const handleView = (row: any) => {
  ElMessage.info('查看功能开发中...')
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑项目'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除项目"${row.projectName}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await projectApi.deleteProject(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error: any) {
      console.error('删除失败:', error)
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  })
}

// 表格选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
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
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      
      try {
        if (formData.id) {
          // 更新项目
          await projectApi.updateProject(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          // 创建项目
          await projectApi.createProject(formData)
          ElMessage.success('创建成功')
        }
        
        dialogVisible.value = false
        loadData()
      } catch (error: any) {
        console.error('提交失败:', error)
        ElMessage.error(error.response?.data?.message || '操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: null,
    projectName: '',
    projectType: '',
    companyName: '',
    city: '',
    rentBillCompany: '',
    propertyBillCompany: '',
    propertyRightCompany: '',
    buildingArea: null,
    rentArea: null,
    propertyArea: null,
    address: ''
  })
  formRef.value?.resetFields()
}

// 加载数据
const loadData = async () => {
  loading.value = true
  
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      keyword: searchForm.keyword || undefined,
      projectType: searchForm.projectType || undefined
    }
    
    const response = await projectApi.getProjectPage(params)
    const { records, total } = response.data
    
    tableData.value = records
    pagination.total = total
  } catch (error: any) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.project-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
}

.project-management :deep(.el-card) {
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

.project-management :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.project-management :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.project-management :deep(.el-card__body) {
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
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border-radius: 2px;
  margin-right: 12px;
}

.card-header :deep(.el-button) {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.2);
}

.card-header :deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.3);
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
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

.search-area :deep(.el-button--primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
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

.el-table :deep(.el-button--text) {
  font-weight: 500;
  border-radius: 6px;
  padding: 6px 12px;
  transition: all 0.3s ease;
}

.el-table :deep(.el-button--text:hover) {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
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
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
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
</style>
