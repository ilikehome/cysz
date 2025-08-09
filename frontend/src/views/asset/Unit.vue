<template>
  <div class="unit-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>单元管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建单元
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="单元编码">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入单元编码或说明"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="所属项目">
            <el-select
              v-model="searchForm.projectId"
              placeholder="请选择项目"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="project in projectList"
                :key="project.id"
                :label="project.projectName"
                :value="project.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="单元状态">
            <el-select
              v-model="searchForm.unitStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
            >
              <el-option label="空置" value="VACANT" />
              <el-option label="已租" value="OCCUPIED" />
              <el-option label="维修" value="MAINTENANCE" />
              <el-option label="预留" value="RESERVED" />
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
        <el-table-column prop="unitCode" label="单元编码" width="120" />
        <el-table-column prop="unitDescription" label="单元说明" min-width="150" />
        <el-table-column prop="projectName" label="所属项目" width="120" />
        <el-table-column prop="buildingName" label="所属楼栋" width="120" />
        <el-table-column prop="unitStatus" label="单元状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getUnitStatusTag(row.unitStatus)">
              {{ getUnitStatusName(row.unitStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unitPurpose" label="单元用途" width="120" />
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
            <el-form-item label="单元编码" prop="unitCode">
              <el-input
                v-model="formData.unitCode"
                placeholder="请输入单元编码"
                :disabled="!!formData.id"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单元用途">
              <el-input v-model="formData.unitPurpose" placeholder="请输入单元用途" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="单元说明">
          <el-input
            v-model="formData.unitDescription"
            type="textarea"
            :rows="2"
            placeholder="请输入单元说明"
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属项目" prop="projectId">
              <el-select
                v-model="formData.projectId"
                placeholder="请选择所属项目"
                style="width: 100%"
                @change="handleProjectChange"
              >
                <el-option
                  v-for="project in projectList"
                  :key="project.id"
                  :label="project.projectName"
                  :value="project.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属楼栋" prop="buildingId">
              <el-select
                v-model="formData.buildingId"
                placeholder="请选择所属楼栋"
                style="width: 100%"
              >
                <el-option
                  v-for="building in buildingList"
                  :key="building.id"
                  :label="building.buildingName"
                  :value="building.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="单元状态" prop="unitStatus">
          <el-select v-model="formData.unitStatus" placeholder="请选择单元状态">
            <el-option label="空置" value="VACANT" />
            <el-option label="已租" value="OCCUPIED" />
            <el-option label="维修" value="MAINTENANCE" />
            <el-option label="预留" value="RESERVED" />
          </el-select>
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
import { reactive, ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { unitApi, projectApi, buildingApi, type Unit, type Project, type Building } from '@/api'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  keyword: '',
  projectId: null,
  unitStatus: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 项目列表
const projectList = ref<Project[]>([])

// 楼栋列表
const buildingList = ref<Building[]>([])

// 表格数据
const tableData = ref<Unit[]>([])

// 表单数据
const formData = reactive({
  id: null,
  unitCode: '',
  unitDescription: '',
  projectId: null,
  buildingId: null,
  unitStatus: 'VACANT',
  unitPurpose: '',
  buildingArea: null,
  rentArea: null,
  propertyArea: null
})

// 表单验证规则
const formRules: FormRules = {
  unitCode: [
    { required: true, message: '请输入单元编码', trigger: 'blur' }
  ],
  projectId: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ],
  buildingId: [
    { required: true, message: '请选择所属楼栋', trigger: 'change' }
  ],
  unitStatus: [
    { required: true, message: '请选择单元状态', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建单元')

// 获取单元状态标签颜色
const getUnitStatusTag = (status: string) => {
  const tagMap: Record<string, string> = {
    'VACANT': 'info',
    'OCCUPIED': 'success',
    'MAINTENANCE': 'warning',
    'RESERVED': 'primary'
  }
  return tagMap[status] || 'info'
}

// 获取单元状态名称
const getUnitStatusName = (status: string) => {
  const nameMap: Record<string, string> = {
    'VACANT': '空置',
    'OCCUPIED': '已租',
    'MAINTENANCE': '维修',
    'RESERVED': '预留'
  }
  return nameMap[status] || status
}

// 项目变化处理
const handleProjectChange = async (projectId: number) => {
  // 重置楼栋选择
  formData.buildingId = null
  // 根据项目ID加载对应的楼栋列表
  if (projectId) {
    try {
      const response = await buildingApi.getBuildingsByProject(projectId)
      buildingList.value = response.data
    } catch (error: any) {
      ElMessage.error(error.message || '加载楼栋列表失败')
    }
  } else {
    buildingList.value = []
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.projectId = null
  searchForm.unitStatus = ''
  handleSearch()
}

// 新建
const handleAdd = () => {
  dialogTitle.value = '新建单元'
  resetForm()
  dialogVisible.value = true
}

// 查看
const handleView = (row: Unit) => {
  ElMessage.info('查看功能开发中...')
}

// 编辑
const handleEdit = (row: Unit) => {
  dialogTitle.value = '编辑单元'
  Object.assign(formData, row)
  // 加载对应项目的楼栋列表
  if (row.projectId) {
    handleProjectChange(row.projectId)
  }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row: Unit) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除单元"${row.unitCode}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await unitApi.deleteUnit(row.id!)
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
    
    const unitData: Unit = {
      unitCode: formData.unitCode,
      unitDescription: formData.unitDescription,
      projectId: formData.projectId!,
      buildingId: formData.buildingId!,
      unitStatus: formData.unitStatus as 'VACANT' | 'OCCUPIED' | 'MAINTENANCE' | 'RESERVED',
      unitPurpose: formData.unitPurpose || undefined,
      buildingArea: formData.buildingArea || undefined,
      rentArea: formData.rentArea || undefined,
      propertyArea: formData.propertyArea || undefined
    }
    
    if (formData.id) {
      await unitApi.updateUnit(formData.id, unitData)
      ElMessage.success('更新成功')
    } else {
      await unitApi.createUnit(unitData)
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
    unitCode: '',
    unitDescription: '',
    projectId: null,
    buildingId: null,
    unitStatus: 'VACANT',
    unitPurpose: '',
    buildingArea: null,
    rentArea: null,
    propertyArea: null
  })
  buildingList.value = []
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
      projectId: searchForm.projectId || undefined,
      unitStatus: searchForm.unitStatus || undefined
    }
    
    const response = await unitApi.getUnitPage(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error: any) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

// 加载项目列表
const loadProjectList = async () => {
  try {
    const response = await projectApi.getProjectList()
    projectList.value = response.data
  } catch (error: any) {
    ElMessage.error(error.message || '加载项目列表失败')
  }
}

onMounted(() => {
  loadData()
  loadProjectList()
})
</script>

<style scoped>
.unit-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
}

.unit-management :deep(.el-card) {
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

.unit-management :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.unit-management :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.unit-management :deep(.el-card__body) {
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
