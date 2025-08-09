<template>
  <div class="merge-split-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>单元合并拆分</span>
          <div>
            <el-button type="primary" @click="handleMerge">
              <el-icon><Connection /></el-icon>
              合并单元
            </el-button>
            <el-button type="warning" @click="handleSplit">
              <el-icon><Grid /></el-icon>
              拆分单元
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="操作类型">
            <el-select
              v-model="searchForm.operationType"
              placeholder="请选择操作类型"
              clearable
              style="width: 120px"
            >
              <el-option label="合并" value="MERGE" />
              <el-option label="拆分" value="SPLIT" />
            </el-select>
          </el-form-item>
          <el-form-item label="操作人">
            <el-input
              v-model="searchForm.operator"
              placeholder="请输入操作人"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="操作时间">
            <el-date-picker
              v-model="searchForm.operationDate"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
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
      
      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
      >
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.operationType === 'MERGE' ? 'primary' : 'warning'">
              {{ row.operationType === 'MERGE' ? '合并' : '拆分' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sourceUnits" label="源单元" min-width="200">
          <template #default="{ row }">
            <el-tag
              v-for="unit in row.sourceUnits"
              :key="unit"
              size="small"
              style="margin-right: 5px;"
            >
              {{ unit }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetUnits" label="目标单元" min-width="200">
          <template #default="{ row }">
            <el-tag
              v-for="unit in row.targetUnits"
              :key="unit"
              size="small"
              type="success"
              style="margin-right: 5px;"
            >
              {{ unit }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationReason" label="操作原因" min-width="200" />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="operationTime" label="操作时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看详情</el-button>
            <el-button type="text" @click="handleRevert(row)" style="color: #f56c6c">撤销操作</el-button>
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
    
    <!-- 合并单元对话框 -->
    <el-dialog
      v-model="mergeDialogVisible"
      title="合并单元"
      width="700px"
      @close="handleMergeDialogClose"
    >
      <el-form
        ref="mergeFormRef"
        :model="mergeFormData"
        :rules="mergeFormRules"
        label-width="120px"
      >
        <el-form-item label="选择源单元" prop="sourceUnitIds">
          <el-select
            v-model="mergeFormData.sourceUnitIds"
            multiple
            placeholder="请选择要合并的单元（至少选择2个）"
            style="width: 100%"
          >
            <el-option
              v-for="unit in availableUnits"
              :key="unit.id"
              :label="`${unit.unitCode} - ${unit.unitDescription}`"
              :value="unit.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="目标单元编码" prop="targetUnitCode">
          <el-input
            v-model="mergeFormData.targetUnitCode"
            placeholder="请输入合并后的单元编码"
          />
        </el-form-item>
        
        <el-form-item label="目标单元说明" prop="targetUnitDescription">
          <el-input
            v-model="mergeFormData.targetUnitDescription"
            placeholder="请输入合并后的单元说明"
          />
        </el-form-item>
        
        <el-form-item label="操作原因" prop="operationReason">
          <el-input
            v-model="mergeFormData.operationReason"
            type="textarea"
            :rows="3"
            placeholder="请输入合并原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="mergeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleMergeSubmit" :loading="mergeLoading">
          确定合并
        </el-button>
      </template>
    </el-dialog>
    
    <!-- 拆分单元对话框 -->
    <el-dialog
      v-model="splitDialogVisible"
      title="拆分单元"
      width="700px"
      @close="handleSplitDialogClose"
    >
      <el-form
        ref="splitFormRef"
        :model="splitFormData"
        :rules="splitFormRules"
        label-width="120px"
      >
        <el-form-item label="选择源单元" prop="sourceUnitId">
          <el-select
            v-model="splitFormData.sourceUnitId"
            placeholder="请选择要拆分的单元"
            style="width: 100%"
          >
            <el-option
              v-for="unit in availableUnits"
              :key="unit.id"
              :label="`${unit.unitCode} - ${unit.unitDescription}`"
              :value="unit.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="拆分后单元">
          <div class="split-units">
            <div
              v-for="(unit, index) in splitFormData.targetUnits"
              :key="index"
              class="split-unit-item"
            >
              <el-input
                v-model="unit.unitCode"
                placeholder="单元编码"
                style="width: 200px; margin-right: 10px;"
              />
              <el-input
                v-model="unit.unitDescription"
                placeholder="单元说明"
                style="width: 250px; margin-right: 10px;"
              />
              <el-button
                type="danger"
                size="small"
                @click="removeSplitUnit(index)"
                v-if="splitFormData.targetUnits.length > 2"
              >
                删除
              </el-button>
            </div>
            <el-button type="primary" size="small" @click="addSplitUnit">
              <el-icon><Plus /></el-icon>
              添加单元
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="操作原因" prop="operationReason">
          <el-input
            v-model="splitFormData.operationReason"
            type="textarea"
            :rows="3"
            placeholder="请输入拆分原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="splitDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSplitSubmit" :loading="splitLoading">
          确定拆分
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { unitApi, type Unit } from '@/api'

// 响应式数据
const loading = ref(false)
const mergeDialogVisible = ref(false)
const splitDialogVisible = ref(false)
const mergeLoading = ref(false)
const splitLoading = ref(false)
const mergeFormRef = ref<FormInstance>()
const splitFormRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  operationType: '',
  operator: '',
  operationDate: null
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 可用单元列表
const availableUnits = ref<Unit[]>([])

// 表格数据 - 由于后端没有专门的操作记录表，这里暂时使用模拟数据
const tableData = ref([
  {
    id: 1,
    operationType: 'MERGE',
    sourceUnits: ['A001-101', 'A001-102'],
    targetUnits: ['A001-101-102'],
    operationReason: '客户需要更大的办公空间',
    operator: '张三',
    operationTime: '2024-01-15 10:30:00'
  },
  {
    id: 2,
    operationType: 'SPLIT',
    sourceUnits: ['A001-201'],
    targetUnits: ['A001-201A', 'A001-201B'],
    operationReason: '单元面积过大，需要拆分出租',
    operator: '李四',
    operationTime: '2024-01-16 14:20:00'
  }
])

// 合并表单数据
const mergeFormData = reactive({
  sourceUnitIds: [],
  targetUnitCode: '',
  targetUnitDescription: '',
  operationReason: ''
})

// 拆分表单数据
const splitFormData = reactive({
  sourceUnitId: null,
  targetUnits: [
    { unitCode: '', unitDescription: '' },
    { unitCode: '', unitDescription: '' }
  ],
  operationReason: ''
})

// 合并表单验证规则
const mergeFormRules: FormRules = {
  sourceUnitIds: [
    { required: true, message: '请选择要合并的单元', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (value.length < 2) {
          callback(new Error('至少需要选择2个单元进行合并'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  targetUnitCode: [
    { required: true, message: '请输入目标单元编码', trigger: 'blur' }
  ],
  targetUnitDescription: [
    { required: true, message: '请输入目标单元说明', trigger: 'blur' }
  ],
  operationReason: [
    { required: true, message: '请输入操作原因', trigger: 'blur' }
  ]
}

// 拆分表单验证规则
const splitFormRules: FormRules = {
  sourceUnitId: [
    { required: true, message: '请选择要拆分的单元', trigger: 'change' }
  ],
  operationReason: [
    { required: true, message: '请输入操作原因', trigger: 'blur' }
  ]
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  searchForm.operationType = ''
  searchForm.operator = ''
  searchForm.operationDate = null
  handleSearch()
}

// 合并单元
const handleMerge = () => {
  resetMergeForm()
  mergeDialogVisible.value = true
}

// 拆分单元
const handleSplit = () => {
  resetSplitForm()
  splitDialogVisible.value = true
}

// 查看详情
const handleView = (row: any) => {
  ElMessage.info('查看详情功能开发中...')
}

// 撤销操作
const handleRevert = (row: any) => {
  ElMessageBox.confirm(
    `确定要撤销这次${row.operationType === 'MERGE' ? '合并' : '拆分'}操作吗？`,
    '确认撤销',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('撤销成功')
    loadData()
  })
}

// 添加拆分单元
const addSplitUnit = () => {
  splitFormData.targetUnits.push({ unitCode: '', unitDescription: '' })
}

// 删除拆分单元
const removeSplitUnit = (index: number) => {
  splitFormData.targetUnits.splice(index, 1)
}

// 合并对话框关闭
const handleMergeDialogClose = () => {
  resetMergeForm()
}

// 拆分对话框关闭
const handleSplitDialogClose = () => {
  resetSplitForm()
}

// 合并提交
const handleMergeSubmit = async () => {
  if (!mergeFormRef.value) return
  
  try {
    await mergeFormRef.value.validate()
    mergeLoading.value = true
    
    const mergeData = {
      sourceUnitIds: mergeFormData.sourceUnitIds,
      targetUnitCode: mergeFormData.targetUnitCode,
      targetUnitDescription: mergeFormData.targetUnitDescription,
      operationReason: mergeFormData.operationReason
    }
    
    await unitApi.mergeUnits(mergeData)
    ElMessage.success('单元合并成功')
    mergeDialogVisible.value = false
    loadData()
    loadAvailableUnits() // 重新加载可用单元列表
  } catch (error: any) {
    ElMessage.error(error.message || '合并失败')
  } finally {
    mergeLoading.value = false
  }
}

// 拆分提交
const handleSplitSubmit = async () => {
  if (!splitFormRef.value) return
  
  // 验证拆分后单元信息
  const hasEmptyUnit = splitFormData.targetUnits.some(unit => !unit.unitCode || !unit.unitDescription)
  if (hasEmptyUnit) {
    ElMessage.error('请完善所有拆分后单元的信息')
    return
  }
  
  try {
    await splitFormRef.value.validate()
    splitLoading.value = true
    
    const splitData = {
      sourceUnitId: splitFormData.sourceUnitId,
      targetUnits: splitFormData.targetUnits,
      operationReason: splitFormData.operationReason
    }
    
    await unitApi.splitUnit(splitData)
    ElMessage.success('单元拆分成功')
    splitDialogVisible.value = false
    loadData()
    loadAvailableUnits() // 重新加载可用单元列表
  } catch (error: any) {
    ElMessage.error(error.message || '拆分失败')
  } finally {
    splitLoading.value = false
  }
}

// 重置合并表单
const resetMergeForm = () => {
  Object.assign(mergeFormData, {
    sourceUnitIds: [],
    targetUnitCode: '',
    targetUnitDescription: '',
    operationReason: ''
  })
  mergeFormRef.value?.resetFields()
}

// 重置拆分表单
const resetSplitForm = () => {
  Object.assign(splitFormData, {
    sourceUnitId: null,
    targetUnits: [
      { unitCode: '', unitDescription: '' },
      { unitCode: '', unitDescription: '' }
    ],
    operationReason: ''
  })
  splitFormRef.value?.resetFields()
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

// 加载数据
const loadData = () => {
  loading.value = true
  
  // 由于后端没有专门的操作记录表，这里暂时使用模拟数据
  // 实际项目中应该有专门的操作记录表来存储合并拆分历史
  setTimeout(() => {
    loading.value = false
    pagination.total = 2
  }, 500)
}

// 加载可用单元列表
const loadAvailableUnits = async () => {
  try {
    // 获取状态为空置的单元，这些单元可以进行合并拆分操作
    const response = await unitApi.getUnitPage({
      current: 1,
      size: 1000, // 获取所有单元
      unitStatus: 'VACANT' // 只获取空置单元
    })
    availableUnits.value = response.data.records
  } catch (error: any) {
    ElMessage.error(error.message || '加载可用单元列表失败')
  }
}

onMounted(() => {
  loadData()
  loadAvailableUnits()
})
</script>

<style scoped>
.merge-split-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
}

.merge-split-management :deep(.el-card) {
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

.merge-split-management :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.merge-split-management :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.merge-split-management :deep(.el-card__body) {
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

.card-header div {
  display: flex;
  gap: 12px;
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

.card-header :deep(.el-button--warning) {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(245, 158, 11, 0.2);
}

.card-header :deep(.el-button--warning:hover) {
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
  margin-right: 5px;
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

.split-units {
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.split-unit-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  padding: 12px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.split-unit-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transform: translateY(-1px);
}

.split-unit-item:last-child {
  margin-bottom: 0;
}

.split-unit-item :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.split-unit-item :deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
}
</style>
