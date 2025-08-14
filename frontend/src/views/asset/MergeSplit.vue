<template>
  <div class="merge-split-container">
    <div class="page-header">
      <h1>合并拆分</h1>
      <p>单元可进行合并或拆分，变更之后所属项目、楼栋、楼层不变</p>
    </div>

    <el-card class="operation-card">
      <template #header>
        <div class="card-header">
          <span>操作类型</span>
        </div>
      </template>
      
      <el-radio-group v-model="operationType" @change="handleOperationChange">
        <el-radio-button label="merge">单元合并</el-radio-button>
        <el-radio-button label="split">单元拆分</el-radio-button>
        <el-radio-button label="history">操作历史</el-radio-button>
      </el-radio-group>
    </el-card>

    <!-- 操作历史 -->
    <el-card v-if="operationType === 'history'" class="history-card">
      <template #header>
        <div class="card-header">
          <span>操作历史记录</span>
          <el-button type="primary" @click="loadHistory">
            刷新记录
          </el-button>
        </div>
      </template>

      <div class="history-content">
        <div class="history-filters">
          <el-form :model="historyQuery" inline>
            <el-form-item label="操作类型">
              <el-select v-model="historyQuery.operationType" placeholder="全部" clearable>
                <el-option label="合并" value="merge" />
                <el-option label="拆分" value="split" />
              </el-select>
            </el-form-item>
            <el-form-item label="操作时间">
              <el-date-picker
                v-model="historyQuery.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item label="操作人">
              <el-input v-model="historyQuery.operator" placeholder="请输入操作人" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchHistory">查询</el-button>
              <el-button @click="resetHistoryQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="historyList" style="width: 100%" v-loading="historyLoading">
          <el-table-column prop="operationType" label="操作类型" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.operationType === 'merge' ? 'success' : 'warning'">
                {{ scope.row.operationType === 'merge' ? '合并' : '拆分' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="operationTime" label="操作时间" width="180" />
          <el-table-column prop="operator" label="操作人" width="120" />
          <el-table-column prop="sourceUnits" label="源单元" width="200">
            <template #default="scope">
              <div class="unit-list">
                <el-tag v-for="unit in scope.row.sourceUnits" :key="unit.id" size="small" style="margin: 2px;">
                  {{ unit.unitNumber }} - {{ unit.unitName }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="targetUnits" label="目标单元" width="200">
            <template #default="scope">
              <div class="unit-list">
                <el-tag v-for="unit in scope.row.targetUnits" :key="unit.id" size="small" type="success" style="margin: 2px;">
                  {{ unit.unitNumber }} - {{ unit.unitName }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="operationReason" label="操作原因" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="text" @click="viewHistoryDetail(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="historyQuery.current"
            v-model:page-size="historyQuery.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="historyTotal"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="loadHistory"
            @current-change="loadHistory"
          />
        </div>
      </div>
    </el-card>

    <!-- 单元合并 -->
    <el-card v-if="operationType === 'merge'" class="merge-card">
      <template #header>
        <div class="card-header">
          <span>单元合并</span>
          <el-button type="primary" @click="handleMerge" :disabled="selectedUnits.length < 2">
            执行合并
          </el-button>
        </div>
      </template>

      <div class="merge-content">
        <div class="unit-selection">
          <h3>选择要合并的单元（至少选择2个）</h3>
          <el-table
            :data="unitList"
            @selection-change="handleSelectionChange"
            style="width: 100%"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="unitNumber" label="单元编号" width="120" />
            <el-table-column prop="unitName" label="单元名称" width="150" />
            <el-table-column prop="projectName" label="项目名称" width="150" />
            <el-table-column prop="buildingName" label="楼栋名称" width="120" />
            <el-table-column prop="floorName" label="楼层名称" width="120" />
            <el-table-column prop="buildingArea" label="建筑面积(㎡)" width="120" />
            <el-table-column prop="rentArea" label="计租面积(㎡)" width="120" />
            <el-table-column prop="unitType" label="单元类型" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="selectedUnits.length >= 2" class="merge-preview">
          <h3>合并预览</h3>
          <el-form :model="mergeForm" label-width="120px">
            <el-form-item label="新单元编号">
              <el-input v-model="mergeForm.newUnitNumber" placeholder="请输入新单元编号" />
            </el-form-item>
            <el-form-item label="新单元名称">
              <el-input v-model="mergeForm.newUnitName" placeholder="请输入新单元名称" />
            </el-form-item>
            <el-form-item label="合并后建筑面积">
              <el-input v-model="mergeForm.totalBuildingArea" placeholder="请输入建筑面积">
                <template #suffix>㎡</template>
              </el-input>
            </el-form-item>
            <el-form-item label="合并后计租面积">
              <el-input v-model="mergeForm.totalRentArea" placeholder="请输入计租面积">
                <template #suffix>㎡</template>
              </el-input>
            </el-form-item>
            <el-form-item label="单元类型">
              <el-select v-model="mergeForm.unitType" placeholder="请选择单元类型">
                <el-option label="住宅" value="residential" />
                <el-option label="商业" value="commercial" />
                <el-option label="办公" value="office" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="mergeForm.remark" type="textarea" rows="3" />
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>

    <!-- 单元拆分 -->
    <el-card v-if="operationType === 'split'" class="split-card">
      <template #header>
        <div class="card-header">
          <span>单元拆分</span>
          <el-button type="primary" @click="handleSplit" :disabled="!selectedUnit || splitUnits.length < 2">
            执行拆分
          </el-button>
        </div>
      </template>

      <div class="split-content">
        <div class="unit-selection">
          <h3>选择要拆分的单元</h3>
          <el-table
            :data="unitList"
            @current-change="handleCurrentChange"
            highlight-current-row
            style="width: 100%"
          >
            <el-table-column prop="unitNumber" label="单元编号" width="120" />
            <el-table-column prop="unitName" label="单元名称" width="150" />
            <el-table-column prop="projectName" label="项目名称" width="150" />
            <el-table-column prop="buildingName" label="楼栋名称" width="120" />
            <el-table-column prop="floorName" label="楼层名称" width="120" />
            <el-table-column prop="buildingArea" label="建筑面积(㎡)" width="120" />
            <el-table-column prop="rentArea" label="计租面积(㎡)" width="120" />
            <el-table-column prop="unitType" label="单元类型" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="selectedUnit" class="split-preview">
          <h3>拆分配置</h3>
          <div class="split-info">
            <p><strong>原单元：</strong>{{ selectedUnit.unitNumber }} - {{ selectedUnit.unitName }}</p>
            <p><strong>建筑面积：</strong>{{ selectedUnit.buildingArea }}㎡ | <strong>计租面积：</strong>{{ selectedUnit.rentArea }}㎡</p>
            <el-button type="success" @click="addSplitUnit" style="margin-bottom: 16px">
              添加拆分单元
            </el-button>
          </div>

          <div class="split-units">
            <div v-for="(unit, index) in splitUnits" :key="index" class="split-unit-item">
              <el-card>
                <template #header>
                  <div class="split-unit-header">
                    <span>拆分单元 {{ index + 1 }}</span>
                    <el-button type="danger" size="small" @click="removeSplitUnit(index)">
                      删除
                    </el-button>
                  </div>
                </template>
                <el-form :model="unit" label-width="100px" size="small">
                  <el-form-item label="单元编号">
                    <el-input v-model="unit.unitNumber" placeholder="请输入单元编号" />
                  </el-form-item>
                  <el-form-item label="单元名称">
                    <el-input v-model="unit.unitName" placeholder="请输入单元名称" />
                  </el-form-item>
                  <el-form-item label="建筑面积(㎡)">
                    <el-input v-model="unit.buildingArea" type="number" placeholder="请输入建筑面积" />
                  </el-form-item>
                  <el-form-item label="计租面积(㎡)">
                    <el-input v-model="unit.rentArea" type="number" placeholder="请输入计租面积" />
                  </el-form-item>
                  <el-form-item label="单元类型">
                    <el-select v-model="unit.unitType" placeholder="请选择单元类型">
                      <el-option label="住宅" value="residential" />
                      <el-option label="商业" value="commercial" />
                      <el-option label="办公" value="office" />
                      <el-option label="其他" value="other" />
                    </el-select>
                  </el-form-item>
                </el-form>
              </el-card>
            </div>
          </div>

          <div class="area-summary">
            <div class="area-row">
              <p><strong>原单元建筑面积：</strong>{{ selectedUnit.buildingArea }}㎡</p>
              <p><strong>拆分后建筑面积总计：</strong>{{ totalSplitBuildingArea }}㎡</p>
              <p v-if="buildingAreaDifference !== 0" :class="buildingAreaDifference > 0 ? 'area-excess' : 'area-shortage'">
                <strong>建筑面积差异：</strong>{{ buildingAreaDifference > 0 ? '+' : '' }}{{ buildingAreaDifference }}㎡
              </p>
            </div>
            <div class="area-row">
              <p><strong>原单元计租面积：</strong>{{ selectedUnit.rentArea }}㎡</p>
              <p><strong>拆分后计租面积总计：</strong>{{ totalSplitRentArea }}㎡</p>
              <p v-if="rentAreaDifference !== 0" :class="rentAreaDifference > 0 ? 'area-excess' : 'area-shortage'">
                <strong>计租面积差异：</strong>{{ rentAreaDifference > 0 ? '+' : '' }}{{ rentAreaDifference }}㎡
              </p>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 历史详情对话框 -->
    <el-dialog v-model="historyDetailVisible" title="操作详情" width="800px">
      <div v-if="currentHistoryDetail" class="history-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="操作类型">
            <el-tag :type="currentHistoryDetail.operationType === 'merge' ? 'success' : 'warning'">
              {{ currentHistoryDetail.operationType === 'merge' ? '合并' : '拆分' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作时间">{{ currentHistoryDetail.operationTime }}</el-descriptions-item>
          <el-descriptions-item label="操作人">{{ currentHistoryDetail.operator }}</el-descriptions-item>
          <el-descriptions-item label="操作原因">{{ currentHistoryDetail.operationReason }}</el-descriptions-item>
        </el-descriptions>

        <div class="detail-section">
          <h4>源单元信息</h4>
          <el-table :data="currentHistoryDetail.sourceUnits" style="width: 100%">
            <el-table-column prop="unitNumber" label="单元编号" />
            <el-table-column prop="unitName" label="单元名称" />
            <el-table-column prop="buildingArea" label="建筑面积(㎡)" />
            <el-table-column prop="rentArea" label="计租面积(㎡)" />
            <el-table-column prop="unitType" label="单元类型" />
          </el-table>
        </div>

        <div class="detail-section">
          <h4>目标单元信息</h4>
          <el-table :data="currentHistoryDetail.targetUnits" style="width: 100%">
            <el-table-column prop="unitNumber" label="单元编号" />
            <el-table-column prop="unitName" label="单元名称" />
            <el-table-column prop="buildingArea" label="建筑面积(㎡)" />
            <el-table-column prop="rentArea" label="计租面积(㎡)" />
            <el-table-column prop="unitType" label="单元类型" />
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { unitApi } from '@/api/unit'

// 操作类型
const operationType = ref<'merge' | 'split' | 'history'>('merge')

// 单元列表
const unitList = ref<any[]>([])

// 合并相关
const selectedUnits = ref<any[]>([])
const mergeForm = ref({
  newUnitNumber: '',
  newUnitName: '',
  totalBuildingArea: '',
  totalRentArea: '',
  unitType: '',
  remark: ''
})

// 拆分相关
const selectedUnit = ref<any>(null)
const splitUnits = ref<any[]>([])

// 历史记录相关
const historyList = ref<any[]>([])
const historyLoading = ref(false)
const historyTotal = ref(0)
const historyQuery = ref({
  current: 1,
  size: 20,
  operationType: '',
  dateRange: null as any,
  operator: ''
})
const historyDetailVisible = ref(false)
const currentHistoryDetail = ref<any>(null)

// 计算属性
const totalSplitBuildingArea = computed(() => {
  return splitUnits.value.reduce((total, unit) => total + (parseFloat(unit.buildingArea) || 0), 0)
})

const totalSplitRentArea = computed(() => {
  return splitUnits.value.reduce((total, unit) => total + (parseFloat(unit.rentArea) || 0), 0)
})

const buildingAreaDifference = computed(() => {
  if (!selectedUnit.value) return 0
  return totalSplitBuildingArea.value - parseFloat(selectedUnit.value.buildingArea || 0)
})

const rentAreaDifference = computed(() => {
  if (!selectedUnit.value) return 0
  return totalSplitRentArea.value - parseFloat(selectedUnit.value.rentArea || 0)
})

// 方法
const handleOperationChange = () => {
  selectedUnits.value = []
  selectedUnit.value = null
  splitUnits.value = []
  resetForms()
  
  if (operationType.value === 'history') {
    loadHistory()
  }
}

const resetForms = () => {
  mergeForm.value = {
    newUnitNumber: '',
    newUnitName: '',
    totalBuildingArea: '',
    totalRentArea: '',
    unitType: '',
    remark: ''
  }
}

const handleSelectionChange = (selection: any[]) => {
  selectedUnits.value = selection
  if (selection.length >= 2) {
    // 预计算合并后的面积
    const totalBuildingArea = selection.reduce((sum, unit) => sum + parseFloat(unit.buildingArea || 0), 0)
    const totalRentArea = selection.reduce((sum, unit) => sum + parseFloat(unit.rentArea || 0), 0)
    mergeForm.value.totalBuildingArea = totalBuildingArea.toString()
    mergeForm.value.totalRentArea = totalRentArea.toString()
  } else {
    mergeForm.value.totalBuildingArea = ''
    mergeForm.value.totalRentArea = ''
  }
}

const handleCurrentChange = (current: any) => {
  selectedUnit.value = current
  if (current) {
    splitUnits.value = [
      {
        unitNumber: '',
        unitName: '',
        buildingArea: '',
        rentArea: '',
        unitType: current.unitType
      },
      {
        unitNumber: '',
        unitName: '',
        buildingArea: '',
        rentArea: '',
        unitType: current.unitType
      }
    ]
  }
}

const addSplitUnit = () => {
  splitUnits.value.push({
    unitNumber: '',
    unitName: '',
    buildingArea: '',
    rentArea: '',
    unitType: selectedUnit.value?.unitType || ''
  })
}

const removeSplitUnit = (index: number) => {
  if (splitUnits.value.length > 2) {
    splitUnits.value.splice(index, 1)
  } else {
    ElMessage.warning('至少需要保留2个拆分单元')
  }
}

const handleMerge = async () => {
  if (selectedUnits.value.length < 2) {
    ElMessage.warning('请至少选择2个单元进行合并')
    return
  }

  if (!mergeForm.value.newUnitNumber) {
    ElMessage.warning('请输入新单元编号')
    return
  }

  if (!mergeForm.value.newUnitName) {
    ElMessage.warning('请输入新单元名称')
    return
  }

  if (!mergeForm.value.totalBuildingArea) {
    ElMessage.warning('请输入合并后建筑面积')
    return
  }

  if (!mergeForm.value.totalRentArea) {
    ElMessage.warning('请输入合并后计租面积')
    return
  }

  try {
    await ElMessageBox.confirm('确认执行单元合并操作？', '确认操作', {
      type: 'warning'
    })

    const mergeData = {
      unitIds: selectedUnits.value.map(unit => unit.id),
      newUnitNumber: mergeForm.value.newUnitNumber,
      newUnitName: mergeForm.value.newUnitName,
      totalBuildingArea: mergeForm.value.totalBuildingArea,
      totalRentArea: mergeForm.value.totalRentArea,
      unitType: mergeForm.value.unitType,
      remark: mergeForm.value.remark
    }

    // 调用合并接口
    await unitApi.mergeUnits({
      sourceUnitIds: mergeData.unitIds,
      targetUnitCode: mergeData.newUnitNumber,
      targetUnitDescription: `${mergeData.newUnitName} - 建筑面积: ${mergeData.totalBuildingArea}㎡, 计租面积: ${mergeData.totalRentArea}㎡`,
      operationReason: mergeData.remark || '单元合并操作'
    })
    
    ElMessage.success('单元合并成功')
    await loadUnits()
    selectedUnits.value = []
    resetForms()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('合并操作失败')
    }
  }
}

const handleSplit = async () => {
  if (!selectedUnit.value) {
    ElMessage.warning('请选择要拆分的单元')
    return
  }

  if (splitUnits.value.length < 2) {
    ElMessage.warning('至少需要拆分为2个单元')
    return
  }

  // 验证拆分单元信息
  for (let i = 0; i < splitUnits.value.length; i++) {
    const unit = splitUnits.value[i]
    if (!unit.unitNumber) {
      ElMessage.warning(`请填写拆分单元${i + 1}的编号`)
      return
    }
    if (!unit.unitName) {
      ElMessage.warning(`请填写拆分单元${i + 1}的名称`)
      return
    }
    if (!unit.buildingArea || parseFloat(unit.buildingArea) <= 0) {
      ElMessage.warning(`请填写拆分单元${i + 1}的有效建筑面积`)
      return
    }
    if (!unit.rentArea || parseFloat(unit.rentArea) <= 0) {
      ElMessage.warning(`请填写拆分单元${i + 1}的有效计租面积`)
      return
    }
  }

  try {
    await ElMessageBox.confirm('确认执行单元拆分操作？', '确认操作', {
      type: 'warning'
    })

    const splitData = {
      originalUnitId: selectedUnit.value.id,
      splitUnits: splitUnits.value
    }

    // 调用拆分接口
    await unitApi.splitUnit({
      sourceUnitId: splitData.originalUnitId,
      targetUnits: splitData.splitUnits.map((unit: any) => ({
        unitCode: unit.unitNumber,
        unitDescription: `${unit.unitName} - 建筑面积: ${unit.buildingArea}㎡, 计租面积: ${unit.rentArea}㎡, 类型: ${unit.unitType}`
      })),
      operationReason: '单元拆分操作'
    })
    
    ElMessage.success('单元拆分成功')
    await loadUnits()
    selectedUnit.value = null
    splitUnits.value = []
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('拆分操作失败')
    }
  }
}

// 历史记录相关方法
const loadHistory = async () => {
  historyLoading.value = true
  try {
    const params = {
      ...historyQuery.value,
      startDate: historyQuery.value.dateRange?.[0],
      endDate: historyQuery.value.dateRange?.[1]
    }
    delete params.dateRange
    
    // 这里应该调用实际的历史记录API
    // const response = await unitApi.getOperationHistory(params)
    
    // 模拟数据
    const mockData = {
      records: [
        {
          id: 1,
          operationType: 'merge',
          operationTime: '2024-01-15 10:30:00',
          operator: '张三',
          operationReason: '业务需要合并相邻单元',
          sourceUnits: [
            { id: 1, unitNumber: 'A101', unitName: '办公室A', buildingArea: 50, rentArea: 45, unitType: 'office' },
            { id: 2, unitNumber: 'A102', unitName: '办公室B', buildingArea: 60, rentArea: 55, unitType: 'office' }
          ],
          targetUnits: [
            { id: 3, unitNumber: 'A101-102', unitName: '大办公室', buildingArea: 110, rentArea: 100, unitType: 'office' }
          ]
        },
        {
          id: 2,
          operationType: 'split',
          operationTime: '2024-01-14 14:20:00',
          operator: '李四',
          operationReason: '租户需求拆分为小单元',
          sourceUnits: [
            { id: 4, unitNumber: 'B201', unitName: '大会议室', buildingArea: 120, rentArea: 110, unitType: 'office' }
          ],
          targetUnits: [
            { id: 5, unitNumber: 'B201-1', unitName: '会议室1', buildingArea: 60, rentArea: 55, unitType: 'office' },
            { id: 6, unitNumber: 'B201-2', unitName: '会议室2', buildingArea: 60, rentArea: 55, unitType: 'office' }
          ]
        }
      ],
      total: 2
    }
    
    historyList.value = mockData.records
    historyTotal.value = mockData.total
  } catch (error) {
    console.error('加载历史记录失败:', error)
    ElMessage.error('加载历史记录失败')
  } finally {
    historyLoading.value = false
  }
}

const searchHistory = () => {
  historyQuery.value.current = 1
  loadHistory()
}

const resetHistoryQuery = () => {
  historyQuery.value = {
    current: 1,
    size: 20,
    operationType: '',
    dateRange: null,
    operator: ''
  }
  loadHistory()
}

const viewHistoryDetail = (row: any) => {
  currentHistoryDetail.value = row
  historyDetailVisible.value = true
}

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    'available': 'success',
    'occupied': 'warning',
    'maintenance': 'danger',
    'reserved': 'info'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'available': '可用',
    'occupied': '已占用',
    'maintenance': '维护中',
    'reserved': '预留'
  }
  return statusMap[status] || '未知'
}

const loadUnits = async () => {
  try {
    const response = await unitApi.getUnitPage({ current: 1, size: 1000 })
    unitList.value = response.data?.records || []
  } catch (error) {
    console.error('加载单元列表失败:', error)
    ElMessage.error('加载单元列表失败')
  }
}

onMounted(() => {
  loadUnits()
})
</script>

<style scoped>
.merge-split-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.operation-card,
.merge-card,
.split-card,
.history-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.merge-content,
.split-content,
.history-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.unit-selection h3 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.merge-preview,
.split-preview {
  border-top: 1px solid #ebeef5;
  padding-top: 24px;
}

.merge-preview h3,
.split-preview h3 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.split-info {
  margin-bottom: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 4px;
}

.split-units {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.split-unit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.area-summary {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.area-row {
  margin-bottom: 16px;
}

.area-row:last-child {
  margin-bottom: 0;
}

.area-summary p {
  margin: 8px 0;
  font-size: 14px;
}

.area-excess {
  color: #f56c6c;
}

.area-shortage {
  color: #e6a23c;
}

/* 历史记录样式 */
.history-filters {
  margin-bottom: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 4px;
}

.unit-list {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.history-detail {
  padding: 16px 0;
}

.detail-section {
  margin-top: 24px;
}

.detail-section h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-radio-button__inner) {
  padding: 12px 20px;
}

:deep(.el-descriptions) {
  margin-bottom: 24px;
}
</style>
