<template>
  <div class="contract-config">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>合同配置</span>
          <el-button type="primary" @click="handleAddTemplate">
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
    
    <!-- 新建/编辑模板对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="1200px"
      @close="handleDialogClose"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="formData.templateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="formData.templateType" placeholder="请选择模板类型" style="width: 100%">
                <el-option label="租赁合同" value="LEASE" />
                <el-option label="服务合同" value="SERVICE" />
                <el-option label="销售合同" value="SALES" />
                <el-option label="其他合同" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="版本号" prop="version">
              <el-input v-model="formData.version" placeholder="如：v1.0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="formData.status">
                <el-radio value="ACTIVE">启用</el-radio>
                <el-radio value="INACTIVE">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="模板描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>
        
        <!-- 合同字段配置 -->
        <el-form-item label="合同字段">
          <div class="field-config">
            <div class="field-header">
              <span>字段配置</span>
              <el-button type="primary" size="small" @click="handleAddField">
                <el-icon><Plus /></el-icon>
                添加字段
              </el-button>
            </div>
            <el-table :data="formData.fields" style="width: 100%; margin-top: 10px;">
              <el-table-column prop="fieldName" label="字段名称" width="150">
                <template #default="{ row, $index }">
                  <el-input v-model="row.fieldName" placeholder="字段名称" size="small" />
                </template>
              </el-table-column>
              <el-table-column prop="fieldLabel" label="字段标签" width="150">
                <template #default="{ row, $index }">
                  <el-input v-model="row.fieldLabel" placeholder="字段标签" size="small" />
                </template>
              </el-table-column>
              <el-table-column prop="fieldType" label="字段类型" width="120">
                <template #default="{ row, $index }">
                  <el-select v-model="row.fieldType" size="small" style="width: 100%">
                    <el-option label="文本" value="text" />
                    <el-option label="数字" value="number" />
                    <el-option label="日期" value="date" />
                    <el-option label="选择" value="select" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column prop="required" label="必填" width="80">
                <template #default="{ row, $index }">
                  <el-checkbox v-model="row.required" />
                </template>
              </el-table-column>
              <el-table-column prop="defaultValue" label="默认值" width="150">
                <template #default="{ row, $index }">
                  <el-input v-model="row.defaultValue" placeholder="默认值" size="small" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="{ row, $index }">
                  <el-button type="text" @click="handleRemoveField($index)" style="color: #f56c6c">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
        
        <!-- 合同模板内容 -->
        <el-form-item label="模板内容" prop="templateContent">
          <div class="template-editor">
            <div class="editor-toolbar">
              <el-button-group>
                <el-button size="small" @click="insertVariable('{{甲方名称}}')">甲方名称</el-button>
                <el-button size="small" @click="insertVariable('{{乙方名称}}')">乙方名称</el-button>
                <el-button size="small" @click="insertVariable('{{合同金额}}')">合同金额</el-button>
                <el-button size="small" @click="insertVariable('{{签订日期}}')">签订日期</el-button>
                <el-button size="small" @click="insertVariable('{{生效日期}}')">生效日期</el-button>
                <el-button size="small" @click="insertVariable('{{到期日期}}')">到期日期</el-button>
              </el-button-group>
            </div>
            <el-input
              ref="templateContentRef"
              v-model="formData.templateContent"
              type="textarea"
              :rows="15"
              placeholder="请输入合同模板内容，使用 {{变量名}} 格式插入动态变量"
              style="margin-top: 10px;"
            />
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handlePreviewTemplate" :loading="previewLoading">预览模板</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
    
    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="模板预览"
      width="900px"
    >
      <div class="preview-content">
        <div class="preview-toolbar">
          <el-button type="primary" @click="handleGeneratePDF">
            <el-icon><Download /></el-icon>
            生成PDF
          </el-button>
        </div>
        <div class="preview-body" v-html="previewContent"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { contractTemplateApi, type ContractTemplate, type ContractField } from '@/api'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const previewVisible = ref(false)
const submitLoading = ref(false)
const previewLoading = ref(false)
const formRef = ref<FormInstance>()
const templateContentRef = ref()

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

// 表格数据
const tableData = ref([])

// 表单数据
const formData = reactive({
  id: null,
  templateName: '',
  templateType: '',
  version: 'v1.0',
  status: 'ACTIVE',
  description: '',
  templateContent: '',
  fields: []
})

// 预览内容
const previewContent = ref('')

// 表单验证规则
const formRules: FormRules = {
  templateName: [
    { required: true, message: '请输入模板名称', trigger: 'blur' }
  ],
  templateType: [
    { required: true, message: '请选择模板类型', trigger: 'change' }
  ],
  version: [
    { required: true, message: '请输入版本号', trigger: 'blur' }
  ],
  templateContent: [
    { required: true, message: '请输入模板内容', trigger: 'blur' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建模板')

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

// 新建模板
const handleAddTemplate = () => {
  dialogTitle.value = '新建模板'
  resetForm()
  dialogVisible.value = true
}

// 编辑模板
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑模板'
  Object.assign(formData, {
    ...row,
    fields: row.fields || []
  })
  dialogVisible.value = true
}

// 复制模板
const handleCopy = async (row: any) => {
  try {
    const response = await contractTemplateApi.copyTemplate(row.id)
    ElMessage.success('复制成功')
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '复制失败')
  }
}

// 预览模板
const handlePreview = (row: any) => {
  previewContent.value = generatePreviewContent(row.templateContent)
  previewVisible.value = true
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
    
    await contractTemplateApi.toggleTemplateStatus(row.id)
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
    
    await contractTemplateApi.deleteTemplate(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 添加字段
const handleAddField = () => {
  formData.fields.push({
    fieldName: '',
    fieldLabel: '',
    fieldType: 'text',
    required: false,
    defaultValue: ''
  })
}

// 删除字段
const handleRemoveField = (index: number) => {
  formData.fields.splice(index, 1)
}

// 插入变量
const insertVariable = (variable: string) => {
  if (templateContentRef.value) {
    const textarea = templateContentRef.value.textarea
    const start = textarea.selectionStart
    const end = textarea.selectionEnd
    const text = formData.templateContent
    formData.templateContent = text.substring(0, start) + variable + text.substring(end)
    
    // 设置光标位置
    setTimeout(() => {
      textarea.selectionStart = textarea.selectionEnd = start + variable.length
      textarea.focus()
    }, 0)
  }
}

// 预览模板
const handlePreviewTemplate = async () => {
  if (!formData.templateContent) {
    ElMessage.warning('请先输入模板内容')
    return
  }
  
  try {
    previewLoading.value = true
    
    const response = await contractTemplateApi.previewTemplate({
      templateContent: formData.templateContent
    })
    
    previewContent.value = response.data.replace(/\n/g, '<br>')
    previewVisible.value = true
  } catch (error: any) {
    ElMessage.error(error.message || '预览失败')
  } finally {
    previewLoading.value = false
  }
}

// 生成预览内容（保留作为备用）
const generatePreviewContent = (content: string) => {
  // 替换变量为示例数据
  const sampleData = {
    '{{甲方名称}}': '北京科技有限公司',
    '{{乙方名称}}': '上海贸易有限公司',
    '{{合同金额}}': '100,000.00',
    '{{签订日期}}': '2024年1月15日',
    '{{生效日期}}': '2024年2月1日',
    '{{到期日期}}': '2025年1月31日'
  }
  
  let previewText = content
  Object.entries(sampleData).forEach(([key, value]) => {
    previewText = previewText.replace(new RegExp(key.replace(/[{}]/g, '\\$&'), 'g'), value)
  })
  
  // 转换为HTML格式
  return previewText.replace(/\n/g, '<br>')
}

// 生成PDF
const handleGeneratePDF = () => {
  ElMessage.info('PDF生成功能开发中...')
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
    
    const templateData: ContractTemplate = {
      templateName: formData.templateName,
      templateType: formData.templateType as 'LEASE' | 'SERVICE' | 'SALES' | 'OTHER',
      version: formData.version,
      status: formData.status as 'ACTIVE' | 'INACTIVE',
      description: formData.description,
      templateContent: formData.templateContent,
      fields: formData.fields
    }
    
    if (formData.id) {
      await contractTemplateApi.updateTemplate(formData.id, templateData)
      ElMessage.success('更新成功')
    } else {
      await contractTemplateApi.createTemplate(templateData)
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
    templateName: '',
    templateType: '',
    version: 'v1.0',
    status: 'ACTIVE',
    description: '',
    templateContent: '',
    fields: []
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
      templateType: searchForm.templateType || undefined,
      status: searchForm.status || undefined
    }
    
    const response = await contractTemplateApi.getTemplatePage(params)
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
.contract-config {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
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
  transition: all 0.3s ease;
}

.contract-config :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
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

.field-config {
  width: 100%;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  background: #f8fafc;
}

.field-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.field-header span {
  font-weight: 600;
  color: #374151;
}

.template-editor {
  width: 100%;
}

.editor-toolbar {
  margin-bottom: 12px;
  padding: 12px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.preview-content {
  max-height: 600px;
  overflow-y: auto;
}

.preview-toolbar {
  margin-bottom: 16px;
  text-align: right;
  padding-bottom: 12px;
  border-bottom: 1px solid #e2e8f0;
}

.preview-body {
  padding: 20px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  line-height: 1.8;
  font-size: 14px;
  color: #374151;
  white-space: pre-wrap;
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
</style>