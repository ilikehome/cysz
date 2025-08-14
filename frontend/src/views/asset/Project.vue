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
              <el-option label="商业街区" value="COMMERCIAL_DISTRICT" />
              <el-option label="酒店" value="HOTEL" />
              <el-option label="公寓" value="APARTMENT" />
              <el-option label="写字楼" value="OFFICE" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
            >
              <el-option label="正常" :value="1" />
              <el-option label="关闭" :value="0" />
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
        <el-table-column prop="projectType" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getProjectTypeTag(row.projectType)">
              {{ getProjectTypeName(row.projectType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="managementOrg" label="管理组织" min-width="150" />
        <el-table-column prop="rentBillCompany" label="租金账单公司" min-width="150" />
        <el-table-column prop="rentBillBankAccount" label="租金账单银行账号" min-width="180" />
        <el-table-column prop="city" label="城市" width="100" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="projectManager" label="项目负责人" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '关闭' }}
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
    
    <!-- 查看项目详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="项目详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="项目名称">{{ viewData.projectName }}</el-descriptions-item>
        <el-descriptions-item label="项目类型">
          <el-tag :type="getProjectTypeTag(viewData.projectType)">
            {{ getProjectTypeName(viewData.projectType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="管理组织">{{ viewData.managementOrg }}</el-descriptions-item>
        <el-descriptions-item label="租金账单公司">{{ viewData.rentBillCompany }}</el-descriptions-item>
        <el-descriptions-item label="租金账单银行账号" :span="2">{{ viewData.rentBillBankAccount || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="城市">{{ viewData.city }}</el-descriptions-item>
        <el-descriptions-item label="项目负责人">{{ viewData.projectManager }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ viewData.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.status === 1 ? 'success' : 'danger'">
            {{ viewData.status === 1 ? '正常' : '关闭' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ viewData.address }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ viewData.updateTime }}</el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEditFromView">编辑</el-button>
      </template>
    </el-dialog>

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
            <el-form-item label="类型" prop="projectType">
              <el-select v-model="formData.projectType" placeholder="请选择项目类型" style="width: 100%">
                <el-option label="综合体" value="COMPLEX" />
                <el-option label="商业街区" value="COMMERCIAL_DISTRICT" />
                <el-option label="酒店" value="HOTEL" />
                <el-option label="公寓" value="APARTMENT" />
                <el-option label="写字楼" value="OFFICE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="管理组织" prop="managementOrg">
              <el-input v-model="formData.managementOrg" placeholder="请输入管理组织" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租金账单公司" prop="rentBillCompany">
              <el-input v-model="formData.rentBillCompany" placeholder="请输入租金账单公司" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="租金账单银行账号">
              <el-input v-model="formData.rentBillBankAccount" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-cascader
                v-model="formData.cityValue"
                :options="cityOptions"
                :props="{ expandTrigger: 'hover' }"
                placeholder="请选择省/市"
                style="width: 100%"
                @change="handleCityChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目负责人" prop="projectManager">
              <el-input v-model="formData.projectManager" placeholder="请输入项目负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="正常" :value="1" />
                <el-option label="关闭" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="地址" prop="address">
          <el-autocomplete
            v-model="formData.address"
            :fetch-suggestions="queryAddressSearch"
            placeholder="请输入地址关键字，支持智能搜索"
            style="width: 100%"
            @select="handleAddressSelect"
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
import { projectApi, type Project } from '@/api/project'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  keyword: '',
  projectType: '',
  status: null
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
const formData = reactive<{
  id: number | null
  projectName: string
  projectType: string
  managementOrg: string
  rentBillCompany: string
  rentBillBankAccount: string
  city: string
  cityValue: any[]
  address: string
  projectManager: string
  contactPhone: string
  status: number
}>({
  id: null,
  projectName: '',
  projectType: '',
  managementOrg: '',
  rentBillCompany: '',
  rentBillBankAccount: '',
  city: '',
  cityValue: [],
  address: '',
  projectManager: '',
  contactPhone: '',
  status: 1
})

// 查看数据
const viewData = reactive({
  id: null,
  projectName: '',
  projectType: '',
  managementOrg: '',
  rentBillCompany: '',
  rentBillBankAccount: '',
  city: '',
  address: '',
  projectManager: '',
  contactPhone: '',
  status: 1,
  createTime: '',
  updateTime: ''
})

// 表单验证规则
const formRules: FormRules = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' }
  ],
  projectType: [
    { required: true, message: '请选择项目类型', trigger: 'change' }
  ],
  managementOrg: [
    { required: true, message: '请输入管理组织', trigger: 'blur' }
  ],
  rentBillCompany: [
    { required: true, message: '请输入租金账单公司', trigger: 'blur' }
  ],
  city: [
    { required: true, message: '请选择城市', trigger: 'change' }
  ],
  projectManager: [
    { required: true, message: '请输入项目负责人', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建项目')

// 选中的行
const selectedRows = ref<Project[]>([])

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
    'COMMERCIAL_DISTRICT': '商业街区',
    'HOTEL': '酒店',
    'APARTMENT': '公寓',
    'OFFICE': '写字楼'
  }
  return nameMap[type] || type
}

// 城市选项数据
const cityOptions = ref([
  {
    value: 'beijing',
    label: '北京市',
    children: [
      { value: 'dongcheng', label: '东城区' },
      { value: 'xicheng', label: '西城区' },
      { value: 'chaoyang', label: '朝阳区' },
      { value: 'fengtai', label: '丰台区' },
      { value: 'shijingshan', label: '石景山区' },
      { value: 'haidian', label: '海淀区' },
      { value: 'mentougou', label: '门头沟区' },
      { value: 'fangshan', label: '房山区' },
      { value: 'tongzhou', label: '通州区' },
      { value: 'shunyi', label: '顺义区' },
      { value: 'changping', label: '昌平区' },
      { value: 'daxing', label: '大兴区' },
      { value: 'huairou', label: '怀柔区' },
      { value: 'pinggu', label: '平谷区' },
      { value: 'miyun', label: '密云区' },
      { value: 'yanqing', label: '延庆区' }
    ]
  },
  {
    value: 'shanghai',
    label: '上海市',
    children: [
      { value: 'huangpu', label: '黄浦区' },
      { value: 'xuhui', label: '徐汇区' },
      { value: 'changning', label: '长宁区' },
      { value: 'jingan', label: '静安区' },
      { value: 'putuo', label: '普陀区' },
      { value: 'hongkou', label: '虹口区' },
      { value: 'yangpu', label: '杨浦区' },
      { value: 'minhang', label: '闵行区' },
      { value: 'baoshan', label: '宝山区' },
      { value: 'jiading', label: '嘉定区' },
      { value: 'pudong', label: '浦东新区' },
      { value: 'jinshan', label: '金山区' },
      { value: 'songjiang', label: '松江区' },
      { value: 'qingpu', label: '青浦区' },
      { value: 'fengxian', label: '奉贤区' },
      { value: 'chongming', label: '崇明区' }
    ]
  },
  {
    value: 'guangdong',
    label: '广东省',
    children: [
      { value: 'guangzhou', label: '广州市' },
      { value: 'shenzhen', label: '深圳市' },
      { value: 'zhuhai', label: '珠海市' },
      { value: 'shantou', label: '汕头市' },
      { value: 'foshan', label: '佛山市' },
      { value: 'shaoguan', label: '韶关市' },
      { value: 'zhanjiang', label: '湛江市' },
      { value: 'zhaoqing', label: '肇庆市' },
      { value: 'jiangmen', label: '江门市' },
      { value: 'maoming', label: '茂名市' },
      { value: 'huizhou', label: '惠州市' },
      { value: 'meizhou', label: '梅州市' },
      { value: 'shanwei', label: '汕尾市' },
      { value: 'heyuan', label: '河源市' },
      { value: 'yangjiang', label: '阳江市' },
      { value: 'qingyuan', label: '清远市' },
      { value: 'dongguan', label: '东莞市' },
      { value: 'zhongshan', label: '中山市' },
      { value: 'chaozhou', label: '潮州市' },
      { value: 'jieyang', label: '揭阳市' },
      { value: 'yunfu', label: '云浮市' }
    ]
  }
])

// 城市变化处理
const handleCityChange = (value: any) => {
  if (value && value.length >= 2) {
    const province = cityOptions.value.find(p => p.value === value[0])
    const city = province?.children.find(c => c.value === value[1])
    formData.city = city ? city.label : ''
  } else {
    formData.city = ''
  }
}

// 地址搜索
const queryAddressSearch = async (queryString: string, callback: (suggestions: any[]) => void) => {
  if (!queryString || !formData.city) {
    callback([])
    return
  }
  
  try {
    const response = await fetch(`/api/address/search?city=${encodeURIComponent(formData.city)}&keyword=${encodeURIComponent(queryString)}`)
    const result = await response.json()
    
    if (result.code === 200 && result.data) {
      const suggestions = result.data.map((item: any) => ({
        value: item.address,
        district: item.district,
        location: item.location
      }))
      callback(suggestions)
    } else {
      // 如果API调用失败，使用本地模拟数据
      const suggestions = [
        { value: `${formData.city}${queryString}路1号` },
        { value: `${formData.city}${queryString}街2号` },
        { value: `${formData.city}${queryString}大道3号` },
        { value: `${formData.city}${queryString}广场4号` },
        { value: `${formData.city}${queryString}中心5号` }
      ]
      callback(suggestions)
    }
  } catch (error) {
    console.error('地址搜索失败:', error)
    // 出错时使用本地模拟数据
    const suggestions = [
      { value: `${formData.city}${queryString}路1号` },
      { value: `${formData.city}${queryString}街2号` },
      { value: `${formData.city}${queryString}大道3号` },
      { value: `${formData.city}${queryString}广场4号` },
      { value: `${formData.city}${queryString}中心5号` }
    ]
    callback(suggestions)
  }
}

// 地址选择处理
const handleAddressSelect = (item: any) => {
  formData.address = item.value
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
  searchForm.status = null
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
  Object.assign(viewData, row)
  viewDialogVisible.value = true
}

// 从查看对话框进入编辑
const handleEditFromView = () => {
  viewDialogVisible.value = false
  Object.assign(formData, viewData)
  
  // 处理城市级联选择器的值
  if (formData.city) {
    // 根据城市名称找到对应的省市值
    for (const province of cityOptions.value) {
      const city = province.children.find(c => c.label === formData.city)
      if (city) {
        formData.cityValue = [province.value, city.value]
        break
      }
    }
  }
  
  dialogTitle.value = '编辑项目'
  dialogVisible.value = true
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
          await projectApi.updateProject(formData.id, formData as Project)
          ElMessage.success('更新成功')
        } else {
          // 创建项目
          await projectApi.createProject(formData as Project)
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
    managementOrg: '',
    rentBillCompany: '',
    rentBillBankAccount: '',
    city: '',
    cityValue: [],
    address: '',
    projectManager: '',
    contactPhone: '',
    status: 1
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
      projectType: searchForm.projectType || undefined,
      status: searchForm.status !== null ? searchForm.status : undefined
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
