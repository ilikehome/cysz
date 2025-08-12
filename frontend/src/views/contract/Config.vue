<template>
  <div class="contract-config">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>合同模板</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建模板
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="模板名称">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入模板名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="模板类型">
            <el-select
              v-model="searchForm.templateType"
              placeholder="请选择模板类型"
              clearable
              style="width: 150px"
            >
              <el-option label="租赁合同" value="LEASE" />
              <el-option label="服务合同" value="SERVICE" />
              <el-option label="销售合同" value="SALES" />
              <el-option label="其他合同" value="OTHER" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
            >
              <el-option label="启用" value="ACTIVE" />
              <el-option label="禁用" value="INACTIVE" />
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
        <el-table-column prop="templateName" label="模板名称" min-width="180" />
        <el-table-column prop="templateType" label="模板类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTemplateTypeTag(row.templateType)">
              {{ getTemplateTypeName(row.templateType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ row.status === 'ACTIVE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="usageCount" label="使用次数" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handlePreview(row)">预览</el-button>
            <el-button type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" @click="handleCopy(row)">复制</el-button>
            <el-button 
              type="text" 
              @click="handleToggleStatus(row)"
              :style="{ color: row.status === 'ACTIVE' ? '#f56c6c' : '#67c23a' }"
            >
              {{ row.status === 'ACTIVE' ? '禁用' : '启用' }}
            </el-button>
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

    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="模板预览"
      width="80%"
      :before-close="handlePreviewClose"
    >
      <div class="preview-container">
        <div class="preview-header">
          <h3>{{ currentTemplate?.templateName }}</h3>
          <div class="template-meta">
            <el-tag :type="getTemplateTypeTag(currentTemplate?.templateType)">
              {{ getTemplateTypeName(currentTemplate?.templateType) }}
            </el-tag>
            <span class="version">版本: {{ currentTemplate?.version }}</span>
          </div>
        </div>
        <div class="preview-content">
          <div class="template-content">
            <h4>合同模板内容</h4>
            <div class="content-text">
              {{ templateContent }}
            </div>
          </div>
          <div class="template-fields" v-if="templateFields.length > 0">
            <h4>模板字段</h4>
            <el-table :data="templateFields" style="width: 100%">
              <el-table-column prop="fieldName" label="字段名称" width="150" />
              <el-table-column prop="fieldType" label="字段类型" width="120" />
              <el-table-column prop="required" label="是否必填" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.required ? 'danger' : 'info'" size="small">
                    {{ row.required ? '必填' : '选填' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="字段描述" />
            </el-table>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="previewVisible = false">关闭</el-button>
          <el-button type="primary" @click="handleEditFromPreview">编辑模板</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="editVisible"
      :title="editForm.id ? '编辑模板' : '新建模板'"
      width="90%"
      :before-close="handleEditClose"
    >
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="editForm.templateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="editForm.templateType" placeholder="请选择模板类型" style="width: 100%">
                <el-option label="租赁合同" value="LEASE" />
                <el-option label="服务合同" value="SERVICE" />
                <el-option label="销售合同" value="SALES" />
                <el-option label="其他合同" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="版本号" prop="version">
              <el-input v-model="editForm.version" placeholder="请输入版本号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="editForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="启用" value="ACTIVE" />
                <el-option label="禁用" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="模板描述">
          <el-input
            v-model="editForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>

        <el-form-item label="模板内容" prop="content">
          <el-input
            v-model="editForm.content"
            type="textarea"
            :rows="10"
            placeholder="请输入模板内容，可使用变量如：{{甲方名称}}、{{乙方名称}}等"
          />
        </el-form-item>

        <el-form-item label="模板字段">
          <div class="fields-container">
            <div class="fields-header">
              <span>字段配置</span>
              <el-button type="primary" size="small" @click="addField">
                <el-icon><Plus /></el-icon>
                添加字段
              </el-button>
            </div>
            <el-table :data="editForm.fields" style="width: 100%">
              <el-table-column label="字段名称" width="150">
                <template #default="{ row, $index }">
                  <el-input v-model="row.fieldName" placeholder="字段名称" size="small" />
                </template>
              </el-table-column>
              <el-table-column label="字段类型" width="120">
                <template #default="{ row, $index }">
                  <el-select v-model="row.fieldType" placeholder="类型" size="small">
                    <el-option label="文本" value="text" />
                    <el-option label="数字" value="number" />
                    <el-option label="日期" value="date" />
                    <el-option label="选择" value="select" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="是否必填" width="100">
                <template #default="{ row, $index }">
                  <el-switch v-model="row.required" />
                </template>
              </el-table-column>
              <el-table-column label="字段描述">
                <template #default="{ row, $index }">
                  <el-input v-model="row.description" placeholder="字段描述" size="small" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row, $index }">
                  <el-button type="danger" size="small" @click="removeField($index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const previewVisible = ref(false)
const editVisible = ref(false)
const currentTemplate = ref<any>(null)
const editFormRef = ref()

// 模板内容和字段
const templateContent = ref('')
const templateFields = ref<any[]>([])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  templateType: '',
  status: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 编辑表单数据
const editForm = reactive({
  id: null,
  templateName: '',
  templateType: '',
  version: '',
  status: 'ACTIVE',
  description: '',
  content: '',
  fields: []
})

// 表单验证规则
const editRules = {
  templateName: [
    { required: true, message: '请输入模板名称', trigger: 'blur' }
  ],
  templateType: [
    { required: true, message: '请选择模板类型', trigger: 'change' }
  ],
  version: [
    { required: true, message: '请输入版本号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入模板内容', trigger: 'blur' }
  ]
}

// 表格数据 - 模拟数据
const tableData = ref([
  {
    id: 1,
    templateName: '标准租赁合同模板',
    templateType: 'LEASE',
    version: 'v1.0',
    status: 'ACTIVE',
    usageCount: 25,
    createTime: '2024-01-15 10:30:00',
    updateTime: '2024-01-20 14:20:00',
    description: '适用于标准房屋租赁业务的合同模板',
    content: '甲方：{{甲方名称}}\n乙方：{{乙方名称}}\n\n根据《中华人民共和国合同法》等相关法律法规，甲乙双方在平等、自愿、协商一致的基础上，就房屋租赁事宜达成如下协议：\n\n一、租赁房屋基本情况\n房屋地址：{{房屋地址}}\n建筑面积：{{建筑面积}}平方米\n\n二、租赁期限\n租赁期限自{{租赁开始日期}}至{{租赁结束日期}}止。\n\n三、租金及支付方式\n月租金为人民币{{月租金}}元。',
    fields: [
      { fieldName: '甲方名称', fieldType: 'text', required: true, description: '出租方名称' },
      { fieldName: '乙方名称', fieldType: 'text', required: true, description: '承租方名称' },
      { fieldName: '房屋地址', fieldType: 'text', required: true, description: '租赁房屋的详细地址' },
      { fieldName: '建筑面积', fieldType: 'number', required: true, description: '房屋建筑面积' },
      { fieldName: '租赁开始日期', fieldType: 'date', required: true, description: '租赁合同开始日期' },
      { fieldName: '租赁结束日期', fieldType: 'date', required: true, description: '租赁合同结束日期' },
      { fieldName: '月租金', fieldType: 'number', required: true, description: '每月租金金额' }
    ]
  },
  {
    id: 2,
    templateName: '服务外包合同模板',
    templateType: 'SERVICE',
    version: 'v2.1',
    status: 'ACTIVE',
    usageCount: 18,
    createTime: '2024-01-10 09:15:00',
    updateTime: '2024-01-25 16:45:00',
    description: '适用于各类服务外包业务的合同模板',
    content: '委托方：{{委托方名称}}\n承接方：{{承接方名称}}\n\n根据《中华人民共和国合同法》等相关法律法规，委托方与承接方在平等、自愿、协商一致的基础上，就服务外包事宜达成如下协议：\n\n一、服务内容\n服务项目：{{服务项目}}\n服务内容：{{服务内容描述}}\n\n二、服务期限\n服务期限自{{服务开始日期}}至{{服务结束日期}}止。\n\n三、服务费用\n服务费用总计人民币{{服务费用}}元。',
    fields: [
      { fieldName: '委托方名称', fieldType: 'text', required: true, description: '委托服务的一方名称' },
      { fieldName: '承接方名称', fieldType: 'text', required: true, description: '提供服务的一方名称' },
      { fieldName: '服务项目', fieldType: 'text', required: true, description: '服务项目名称' },
      { fieldName: '服务内容描述', fieldType: 'text', required: true, description: '详细的服务内容描述' },
      { fieldName: '服务开始日期', fieldType: 'date', required: true, description: '服务开始日期' },
      { fieldName: '服务结束日期', fieldType: 'date', required: true, description: '服务结束日期' },
      { fieldName: '服务费用', fieldType: 'number', required: true, description: '服务费用总额' }
    ]
  }
])

// 获取模板类型标签颜色
const getTemplateTypeTag = (type: string) => {
  const tagMap: Record<string, string> = {
    'LEASE': 'primary',
    'SERVICE': 'success',
    'SALES': 'warning',
    'OTHER': 'info'
  }
  return tagMap[type] || 'info'
}

// 获取模板类型名称
const getTemplateTypeName = (type: string) => {
  const nameMap: Record<string, string> = {
    'LEASE': '租赁合同',
    'SERVICE': '服务合同',
    'SALES': '销售合同',
    'OTHER': '其他合同'
  }
  return nameMap[type] || type
}

// 新建模板
const handleAdd = () => {
  resetEditForm()
  editVisible.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.templateType = ''
  searchForm.status = ''
  handleSearch()
}

// 编辑模板
const handleEdit = (row: any) => {
  currentTemplate.value = row
  editForm.id = row.id
  editForm.templateName = row.templateName
  editForm.templateType = row.templateType
  editForm.version = row.version
  editForm.status = row.status
  editForm.description = row.description || ''
  editForm.content = row.content || ''
  editForm.fields = row.fields ? [...row.fields] : []
  editVisible.value = true
}

// 复制模板
const handleCopy = async (row: any) => {
  try {
    const newTemplate = {
      ...row,
      id: Date.now(),
      templateName: row.templateName + ' - 副本',
      version: 'v1.0',
      createTime: new Date().toLocaleString(),
      updateTime: new Date().toLocaleString(),
      usageCount: 0
    }
    tableData.value.push(newTemplate)
    ElMessage.success('复制成功')
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '复制失败')
  }
}

// 预览模板
const handlePreview = (row: any) => {
  currentTemplate.value = row
  templateContent.value = row.content || '暂无模板内容'
  templateFields.value = row.fields || []
  previewVisible.value = true
}

// 从预览页面编辑
const handleEditFromPreview = () => {
  previewVisible.value = false
  handleEdit(currentTemplate.value)
}

// 关闭预览对话框
const handlePreviewClose = () => {
  previewVisible.value = false
  currentTemplate.value = null
  templateContent.value = ''
  templateFields.value = []
}

// 关闭编辑对话框
const handleEditClose = () => {
  editVisible.value = false
  resetEditForm()
}

// 重置编辑表单
const resetEditForm = () => {
  editForm.id = null
  editForm.templateName = ''
  editForm.templateType = ''
  editForm.version = ''
  editForm.status = 'ACTIVE'
  editForm.description = ''
  editForm.content = ''
  editForm.fields = []
  currentTemplate.value = null
}

// 添加字段
const addField = () => {
  editForm.fields.push({
    fieldName: '',
    fieldType: 'text',
    required: false,
    description: ''
  })
}

// 删除字段
const removeField = (index: number) => {
  editForm.fields.splice(index, 1)
}

// 保存模板
const handleSave = async () => {
  try {
    await editFormRef.value?.validate()
    saving.value = true
    
    // 模拟保存操作
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (editForm.id) {
      // 更新现有模板
      const index = tableData.value.findIndex(item => item.id === editForm.id)
      if (index !== -1) {
        tableData.value[index] = {
          ...tableData.value[index],
          templateName: editForm.templateName,
          templateType: editForm.templateType,
          version: editForm.version,
          status: editForm.status,
          description: editForm.description,
          content: editForm.content,
          fields: [...editForm.fields],
          updateTime: new Date().toLocaleString()
        }
      }
      ElMessage.success('模板更新成功')
    } else {
      // 新建模板
      const newTemplate = {
        id: Date.now(),
        templateName: editForm.templateName,
        templateType: editForm.templateType,
        version: editForm.version,
        status: editForm.status,
        description: editForm.description,
        content: editForm.content,
        fields: [...editForm.fields],
        usageCount: 0,
        createTime: new Date().toLocaleString(),
        updateTime: new Date().toLocaleString()
      }
      tableData.value.unshift(newTemplate)
      ElMessage.success('模板创建成功')
    }
    
    editVisible.value = false
    resetEditForm()
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '保存失败')
  } finally {
    saving.value = false
  }
}

// 切换状态
const handleToggleStatus = async (row: any) => {
  try {
    const action = row.status === 'ACTIVE' ? '禁用' : '启用'
    await ElMessageBox.confirm(
      `确定要${action}模板"${row.templateName}"吗？`,
      `确认${action}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    row.status = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
    row.updateTime = new Date().toLocaleString()
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

// 删除模板
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除模板"${row.templateName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const index = tableData.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      tableData.value.splice(index, 1)
    }
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

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    // 这里可以调用实际的API
    pagination.total = tableData.value.length
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
.contract-config {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.contract-config :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.contract-config :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.contract-config :deep(.el-card__body) {
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
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
  border-radius: 2px;
  margin-right: 12px;
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

.el-table {
  flex: 1;
  overflow: auto;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.pagination {
  margin-top: 24px;
  text-align: right;
  flex-shrink: 0;
}

/* 预览对话框样式 */
.preview-container {
  max-height: 70vh;
  overflow-y: auto;
}

.preview-header {
  padding: 20px 0;
  border-bottom: 1px solid #e5e7eb;
  margin-bottom: 20px;
}

.preview-header h3 {
  font-size: 20px;
  color: #1f2937;
  margin-bottom: 12px;
}

.template-meta {
  display: flex;
  align-items: center;
  gap: 16px;
}

.version {
  color: #6b7280;
  font-size: 14px;
}

.preview-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.template-content h4,
.template-fields h4 {
  font-size: 16px;
  color: #374151;
  margin-bottom: 12px;
  font-weight: 600;
}

.content-text {
  background: #f9fafb;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  white-space: pre-wrap;
  line-height: 1.6;
  color: #374151;
  font-family: 'Courier New', monospace;
}

/* 编辑对话框样式 */
.fields-container {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.fields-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
  font-weight: 600;
  color: #374151;
}

/* 对话框样式优化 */
:deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  padding: 20px 24px;
  border-bottom: 1px solid #e2e8f0;
}

:deep(.el-dialog__body) {
  padding: 24px;
  max-height: 70vh;
  overflow-y: auto;
}

:deep(.el-dialog__footer) {
  padding: 16px 24px;
  background: #f9fafb;
  border-top: 1px solid #e2e8f0;
}

/* 表单样式优化 */
:deep(.el-form-item__label) {
  font-weight: 600;
  color: #374151;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #3b82f6;
}

:deep(.el-textarea__inner) {
  border-radius: 8px;
  font-family: 'Courier New', monospace;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

/* 按钮样式优化 */
:deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 动画效果 */
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

.contract-config {
  animation: fadeInUp 0.6s ease forwards;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .contract-config {
    padding: 16px;
  }
  
  .search-area {
    padding: 16px;
  }
  
  :deep(.el-dialog) {
    width: 95% !important;
    margin: 0 auto;
  }
  
  .template-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>
