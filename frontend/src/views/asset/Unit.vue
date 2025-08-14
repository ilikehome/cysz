文件有ts检查异常的地方，请先分析一下什么原因，需不需要修改。等我确定你的方案之后你再修改。<template>
  <div class="unit-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>单元管理</span>
          <div class="header-buttons">
            <el-button type="success" @click="handleAddBuilding">
              <el-icon><OfficeBuilding /></el-icon>
              新建楼栋
            </el-button>
            <el-button type="warning" @click="handleAddFloor">
              <el-icon><Grid /></el-icon>
              新建楼层
            </el-button>
            <el-button type="primary" @click="handleAddUnit">
              <el-icon><Plus /></el-icon>
              新建单元
            </el-button>
            <el-dropdown @command="handleDeleteCommand">
              <el-button type="danger">
                <el-icon><Delete /></el-icon>
                删除管理
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="deleteBuilding">删除楼栋</el-dropdown-item>
                  <el-dropdown-item command="deleteFloor">删除楼层</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
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
          <el-form-item label="资产状态">
            <el-select
              v-model="searchForm.unitStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
            >
              <el-option label="可租" value="RENTABLE" />
              <el-option label="自用" value="SELF_USE" />
              <el-option label="公用" value="PUBLIC_USE" />
              <el-option label="返租" value="LEASE_BACK" />
              <el-option label="停用" value="DISABLED" />
              <el-option label="自持出租" value="SELF_RENTAL" />
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
        <el-table-column prop="unitName" label="单元名称" width="120" />
        <el-table-column prop="projectName" label="所属项目" width="120" />
        <el-table-column prop="buildingName" label="所属楼栋" width="120" />
        <el-table-column prop="floorName" label="所属楼层" width="120" />
        <el-table-column prop="unitStatus" label="资产状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getUnitStatusTag(row.unitStatus)">
              {{ getUnitStatusName(row.unitStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unitPurpose" label="用途" width="120" />
        <el-table-column prop="buildingArea" label="建筑面积(㎡)" width="120" />
        <el-table-column prop="rentArea" label="计租面积(㎡)" width="120" />
        <el-table-column prop="isMultiTenant" label="一位多租" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isMultiTenant ? 'success' : 'info'">
              {{ row.isMultiTenant ? '是' : '否' }}
            </el-tag>
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
    
    <!-- 新建楼栋对话框 -->
    <el-dialog
      v-model="buildingDialogVisible"
      title="新建楼栋"
      width="600px"
      @close="handleBuildingDialogClose"
    >
      <el-form
        ref="buildingFormRef"
        :model="buildingFormData"
        :rules="buildingFormRules"
        label-width="120px"
      >
        <el-form-item label="所属项目" prop="projectId">
          <el-select v-model="buildingFormData.projectId" placeholder="请选择所属项目" style="width: 100%">
            <el-option
              v-for="project in projectList"
              :key="project.id"
              :label="project.projectName"
              :value="project.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋名称" prop="buildingName">
          <el-input v-model="buildingFormData.buildingName" placeholder="请输入楼栋名称" />
        </el-form-item>
        <el-form-item label="楼栋编码" prop="buildingCode">
          <el-input v-model="buildingFormData.buildingCode" placeholder="请输入楼栋编码" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="buildingFormData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="buildingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBuildingSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 新建楼层对话框 -->
    <el-dialog
      v-model="floorDialogVisible"
      title="新建楼层"
      width="600px"
      @close="handleFloorDialogClose"
    >
      <el-form
        ref="floorFormRef"
        :model="floorFormData"
        :rules="floorFormRules"
        label-width="120px"
      >
        <el-form-item label="所属项目" prop="projectId">
          <el-select 
            v-model="floorFormData.projectId" 
            placeholder="请选择所属项目" 
            style="width: 100%"
            @change="handleFloorProjectChange"
          >
            <el-option
              v-for="project in projectList"
              :key="project.id"
              :label="project.projectName"
              :value="project.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属楼栋" prop="buildingId">
          <el-select 
            v-model="floorFormData.buildingId" 
            placeholder="请选择所属楼栋" 
            style="width: 100%"
            :disabled="!floorFormData.projectId"
          >
            <el-option
              v-for="building in floorBuildingOptions"
              :key="building.id"
              :label="`${building.buildingName} (${building.buildingCode})`"
              :value="building.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="楼层名称" prop="floorName">
          <el-input v-model="floorFormData.floorName" placeholder="请输入楼层名称" />
        </el-form-item>
        <el-form-item label="楼层编码" prop="floorCode">
          <el-input v-model="floorFormData.floorCode" placeholder="请输入楼层编码" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="floorFormData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="floorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleFloorSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 查看单元对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="查看单元详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="单元名称">{{ viewData.unitName }}</el-descriptions-item>
        <el-descriptions-item label="单元编码">{{ viewData.unitCode }}</el-descriptions-item>
        <el-descriptions-item label="所属项目">{{ viewData.projectName }}</el-descriptions-item>
        <el-descriptions-item label="所属楼栋">{{ viewData.buildingName }}</el-descriptions-item>
        <el-descriptions-item label="所属楼层">{{ viewData.floorName }}</el-descriptions-item>
        <el-descriptions-item label="资产状态">
          <el-tag :type="getUnitStatusTag(viewData.unitStatus)">
            {{ getUnitStatusName(viewData.unitStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="用途">{{ viewData.unitPurpose || '-' }}</el-descriptions-item>
        <el-descriptions-item label="建筑面积">{{ viewData.buildingArea ? viewData.buildingArea + '㎡' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="计租面积">{{ viewData.rentArea ? viewData.rentArea + '㎡' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="一位多租">
          <el-tag :type="viewData.isMultiTenant ? 'success' : 'info'">
            {{ viewData.isMultiTenant ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ viewData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEditFromView">编辑</el-button>
      </template>
    </el-dialog>

    <!-- 新建/编辑单元对话框 -->
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
        <el-form-item label="所属项目" prop="projectId">
          <el-select
            v-model="formData.projectId"
            placeholder="请选择所属项目"
            style="width: 100%"
            @change="handleUnitProjectChange"
          >
            <el-option
              v-for="project in projectList"
              :key="project.id"
              :label="project.projectName"
              :value="project.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="所属楼栋" prop="buildingId">
          <el-select
            v-model="formData.buildingId"
            placeholder="请选择所属楼栋"
            style="width: 100%"
            :disabled="!formData.projectId"
            @change="handleUnitBuildingChange"
          >
            <el-option
              v-for="building in unitBuildingOptions"
              :key="building.id"
              :label="`${building.buildingName} (${building.buildingCode})`"
              :value="building.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="所属楼层" prop="floorId">
          <el-select
            v-model="formData.floorId"
            placeholder="请选择所属楼层"
            style="width: 100%"
            :disabled="!formData.buildingId"
          >
            <el-option
              v-for="floor in unitFloorList"
              :key="floor.id"
              :label="`${floor.floorName} (${floor.floorCode})`"
              :value="floor.id"
            />
          </el-select>
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单元名称" prop="unitName">
              <el-input v-model="formData.unitName" placeholder="请输入单元名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单元编码" prop="unitCode">
              <el-input
                v-model="formData.unitCode"
                placeholder="请输入单元编码"
                :disabled="!!formData.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产状态" prop="unitStatus">
              <el-select v-model="formData.unitStatus" placeholder="请选择资产状态" style="width: 100%">
                <el-option label="可租" value="RENTABLE" />
                <el-option label="自用" value="SELF_USE" />
                <el-option label="公用" value="PUBLIC_USE" />
                <el-option label="返租" value="LEASE_BACK" />
                <el-option label="停用" value="DISABLED" />
                <el-option label="自持出租" value="SELF_RENTAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用途">
              <el-select v-model="formData.unitPurpose" placeholder="请选择单元用途" style="width: 100%">
                <el-option label="办公" value="OFFICE" />
                <el-option label="仓库" value="WAREHOUSE" />
                <el-option label="货运" value="FREIGHT" />
                <el-option label="商业" value="COMMERCIAL" />
                <el-option label="会议室" value="MEETING_ROOM" />
                <el-option label="营业房" value="BUSINESS_ROOM" />
                <el-option label="停车位" value="PARKING" />
                <el-option label="广告位" value="ADVERTISING" />
                <el-option label="公寓" value="APARTMENT" />
                <el-option label="多经点位" value="MULTI_BUSINESS" />
                <el-option label="推广场地" value="PROMOTION_VENUE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="建筑面积(㎡)">
              <el-input-number
                v-model="formData.buildingArea"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计租面积(㎡)">
              <el-input-number
                v-model="formData.rentArea"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否一位多租">
              <el-switch v-model="formData.isMultiTenant" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
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
import { reactive, ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { projectApi, type Project } from '@/api/project'
import { unitApi, buildingApi, floorApi, type Unit, type Building, type Floor } from '@/api/unit'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const buildingDialogVisible = ref(false)
const floorDialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const buildingFormRef = ref<FormInstance>()
const floorFormRef = ref<FormInstance>()

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
const allBuildingList = ref<Building[]>([])
const floorBuildingOptions = ref<Building[]>([])
const unitBuildingOptions = ref<Building[]>([])

// 楼层列表
const floorList = ref<any[]>([])
const unitFloorList = ref<any[]>([])

// 表格数据
const tableData = ref<Unit[]>([])

// 查看数据
const viewData = ref<Unit>({
  unitName: '',
  unitCode: '',
  floorId: 0,
  unitStatus: 'RENTABLE',
  unitPurpose: '',
  buildingArea: 0,
  rentArea: 0,
  isMultiTenant: false,
  remark: ''
})

// 楼栋表单数据
const buildingFormData = reactive({
  projectId: null,
  buildingName: '',
  buildingCode: '',
  remark: ''
})

// 楼层表单数据
const floorFormData = reactive({
  projectId: null,
  buildingId: null,
  floorName: '',
  floorCode: '',
  remark: ''
})

// 表单数据
const formData = reactive({
  id: null,
  unitName: '',
  unitCode: '',
  projectId: null,
  buildingId: null,
  floorId: null,
  unitStatus: 'RENTABLE',
  unitPurpose: '',
  buildingArea: null,
  rentArea: null,
  isMultiTenant: false,
  remark: ''
})

// 表单验证规则
const formRules: FormRules = {
  unitName: [
    { required: true, message: '请输入单元名称', trigger: 'blur' }
  ],
  unitCode: [
    { required: true, message: '请输入单元编码', trigger: 'blur' }
  ],
  projectId: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ],
  buildingId: [
    { required: true, message: '请选择所属楼栋', trigger: 'change' }
  ],
  floorId: [
    { required: true, message: '请选择所属楼层', trigger: 'change' }
  ],
  unitStatus: [
    { required: true, message: '请选择资产状态', trigger: 'change' }
  ]
}

// 楼栋表单验证规则
const buildingFormRules: FormRules = {
  projectId: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ],
  buildingName: [
    { required: true, message: '请输入楼栋名称', trigger: 'blur' }
  ],
  buildingCode: [
    { required: true, message: '请输入楼栋编码', trigger: 'blur' }
  ]
}

// 楼层表单验证规则
const floorFormRules: FormRules = {
  projectId: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ],
  buildingId: [
    { required: true, message: '请选择所属楼栋', trigger: 'change' }
  ],
  floorName: [
    { required: true, message: '请输入楼层名称', trigger: 'blur' }
  ],
  floorCode: [
    { required: true, message: '请输入楼层编码', trigger: 'blur' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建单元')

// 获取单元状态标签颜色
const getUnitStatusTag = (status: string) => {
  const tagMap: Record<string, string> = {
    'RENTABLE': 'success',
    'SELF_USE': 'primary',
    'PUBLIC_USE': 'info',
    'LEASE_BACK': 'warning',
    'DISABLED': 'danger',
    'SELF_RENTAL': 'success'
  }
  return tagMap[status] || 'info'
}

// 获取单元状态名称
const getUnitStatusName = (status: string) => {
  const nameMap: Record<string, string> = {
    'RENTABLE': '可租',
    'SELF_USE': '自用',
    'PUBLIC_USE': '公用',
    'LEASE_BACK': '返租',
    'DISABLED': '停用',
    'SELF_RENTAL': '自持出租'
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

// 楼层项目变化处理
const handleFloorProjectChange = async (projectId: number) => {
  // 重置楼栋选择
  floorFormData.buildingId = null
  // 根据项目ID加载对应的楼栋列表
  if (projectId) {
    try {
      const response = await buildingApi.getBuildingsByProject(projectId)
      floorBuildingOptions.value = response.data
    } catch (error: any) {
      ElMessage.error(error.message || '加载楼栋列表失败')
    }
  } else {
    floorBuildingOptions.value = []
  }
}

// 单元项目变化处理
const handleUnitProjectChange = async (projectId: number) => {
  // 重置楼栋和楼层选择
  formData.buildingId = null
  formData.floorId = null
  // 根据项目ID加载对应的楼栋列表
  if (projectId) {
    try {
      const response = await buildingApi.getBuildingsByProject(projectId)
      unitBuildingOptions.value = response.data
    } catch (error: any) {
      ElMessage.error(error.message || '加载楼栋列表失败')
    }
  } else {
    unitBuildingOptions.value = []
  }
  unitFloorList.value = []
}

// 单元楼栋变化处理
const handleUnitBuildingChange = async (buildingId: number) => {
  // 重置楼层选择
  formData.floorId = null
  // 根据楼栋ID加载对应的楼层列表
  if (buildingId) {
    try {
      const response = await floorApi.getFloorsByBuilding(buildingId)
      unitFloorList.value = response.data
    } catch (error: any) {
      ElMessage.error(error.message || '加载楼层列表失败')
    }
  } else {
    unitFloorList.value = []
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

// 新建楼栋
const handleAddBuilding = () => {
  resetBuildingForm()
  buildingDialogVisible.value = true
}

// 新建楼层
const handleAddFloor = () => {
  resetFloorForm()
  floorDialogVisible.value = true
}

// 新建单元
const handleAddUnit = () => {
  dialogTitle.value = '新建单元'
  resetForm()
  dialogVisible.value = true
}

// 删除命令处理
const handleDeleteCommand = (command: string) => {
  if (command === 'deleteBuilding') {
    ElMessage.info('删除楼栋功能开发中...')
  } else if (command === 'deleteFloor') {
    ElMessage.info('删除楼层功能开发中...')
  }
}

// 查看
const handleView = (row: Unit) => {
  Object.assign(viewData.value, row)
  viewDialogVisible.value = true
}

// 从查看对话框进入编辑
const handleEditFromView = () => {
  viewDialogVisible.value = false
  handleEdit(viewData.value)
}

// 编辑
const handleEdit = (row: Unit) => {
  dialogTitle.value = '编辑单元'
  Object.assign(formData, {
    id: row.id,
    unitName: row.unitName,
    unitCode: row.unitCode,
    projectId: row.projectId,
    buildingId: row.buildingId,
    floorId: row.floorId,
    unitStatus: row.unitStatus,
    unitPurpose: row.unitPurpose,
    buildingArea: row.buildingArea,
    rentArea: row.rentArea,
    isMultiTenant: row.isMultiTenant,
    remark: row.remark
  })
  // 根据项目加载楼栋列表
  if (row.projectId) {
    handleUnitProjectChange(row.projectId)
  }
  // 根据楼栋加载楼层列表
  if (row.buildingId) {
    setTimeout(() => {
      handleUnitBuildingChange(row.buildingId!)
    }, 100)
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
      unitName: formData.unitName,
      unitCode: formData.unitCode,
      floorId: formData.floorId!,
      unitStatus: formData.unitStatus as Unit['unitStatus'],
      unitPurpose: formData.unitPurpose || undefined,
      buildingArea: formData.buildingArea || undefined,
      rentArea: formData.rentArea || undefined,
      isMultiTenant: formData.isMultiTenant,
      remark: formData.remark || undefined
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
    unitName: '',
    projectId: null,
    buildingId: null,
    floorId: null,
    unitStatus: 'RENTABLE',
    unitPurpose: '',
    buildingArea: null,
    rentArea: null,
    isMultiTenant: false,
    remark: ''
  })
  unitBuildingOptions.value = []
  unitFloorList.value = []
  formRef.value?.resetFields()
}

// 重置楼栋表单
const resetBuildingForm = () => {
  Object.assign(buildingFormData, {
    projectId: null,
    buildingName: '',
    buildingCode: '',
    remark: ''
  })
  buildingFormRef.value?.resetFields()
}

// 重置楼层表单
const resetFloorForm = () => {
  Object.assign(floorFormData, {
    projectId: null,
    buildingId: null,
    floorName: '',
    floorCode: '',
    remark: ''
  })
  floorBuildingOptions.value = []
  floorFormRef.value?.resetFields()
}

// 楼栋对话框关闭
const handleBuildingDialogClose = () => {
  resetBuildingForm()
}

// 楼层对话框关闭
const handleFloorDialogClose = () => {
  resetFloorForm()
}

// 楼栋提交
const handleBuildingSubmit = async () => {
  if (!buildingFormRef.value) return
  
  try {
    await buildingFormRef.value.validate()
    submitLoading.value = true
    
    const buildingData: Building = {
      projectId: buildingFormData.projectId!,
      buildingName: buildingFormData.buildingName,
      buildingCode: buildingFormData.buildingCode,
      remark: buildingFormData.remark
    }
    
    await buildingApi.createBuilding(buildingData)
    ElMessage.success('楼栋创建成功')
    buildingDialogVisible.value = false
    loadAllBuildingList()
  } catch (error: any) {
    ElMessage.error(error.message || '创建失败')
  } finally {
    submitLoading.value = false
  }
}

// 楼层提交
const handleFloorSubmit = async () => {
  if (!floorFormRef.value) return
  
  try {
    await floorFormRef.value.validate()
    submitLoading.value = true
    
    const floorData: Floor = {
      buildingId: floorFormData.buildingId!,
      floorName: floorFormData.floorName,
      floorCode: floorFormData.floorCode,
      remark: floorFormData.remark
    }
    
    await floorApi.createFloor(floorData)
    ElMessage.success('楼层创建成功')
    floorDialogVisible.value = false
    loadFloorList()
  } catch (error: any) {
    ElMessage.error(error.message || '创建失败')
  } finally {
    submitLoading.value = false
  }
}

// 加载所有楼栋列表
const loadAllBuildingList = async () => {
  try {
    const response = await buildingApi.getBuildingPage({ current: 1, size: 1000 })
    allBuildingList.value = response.data.records
  } catch (error: any) {
    ElMessage.error(error.message || '加载楼栋列表失败')
  }
}

// 加载楼层列表
const loadFloorList = async () => {
  try {
    const response = await floorApi.getFloorPage({ current: 1, size: 1000 })
    floorList.value = response.data.records
  } catch (error: any) {
    ElMessage.error(error.message || '加载楼层列表失败')
  }
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
  loadFloorList()
  loadAllBuildingList()
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
