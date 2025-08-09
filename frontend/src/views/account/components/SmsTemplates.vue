<template>
  <div class="sms-templates">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="left-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增模板
        </el-button>
        <el-button type="success" @click="handleImport">
          <el-icon><Upload /></el-icon>
          导入模板
        </el-button>
      </div>
      <div class="right-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索模板名称"
          style="width: 200px"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 模板列表 -->
    <div class="template-list">
      <el-row :gutter="20">
        <el-col :span="8" v-for="template in templateList" :key="template.id">
          <el-card class="template-card" :class="{ disabled: !template.enabled }">
            <template #header>
              <div class="card-header">
                <div class="template-info">
                  <span class="template-name">{{ template.name }}</span>
                  <el-tag :type="getTypeTagType(template.type)" size="small">
                    {{ getTypeLabel(template.type) }}
                  </el-tag>
                </div>
                <div class="template-actions">
                  <el-switch
                    v-model="template.enabled"
                    size="small"
                    @change="handleToggleEnabled(template)"
                  />
                  <el-dropdown @command="handleCommand">
                    <el-button type="text" size="small">
                      <el-icon><MoreFilled /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item :command="`edit-${template.id}`">编辑</el-dropdown-item>
                        <el-dropdown-item :command="`copy-${template.id}`">复制</el-dropdown-item>
                        <el-dropdown-item :command="`preview-${template.id}`">预览</el-dropdown-item>
                        <el-dropdown-item :command="`delete-${template.id}`" divided>删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </template>
            
            <div class="template-content">
              <div class="content-preview">
                {{ template.content }}
              </div>
              
              <div class="template-stats">
                <div class="stat-item">
                  <span class="stat-label">使用次数:</span>
                  <span class="stat-value">{{ template.usageCount || 0 }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">成功率:</span>
                  <span class="stat-value">{{ template.successRate || 0 }}%</span>
                </div>
              </div>
              
              <div class="template-variables">
                <span class="variables-label">可用变量:</span>
                <el-tag v-for="variable in getAvailableVariables()" :key="variable" size="small" class="variable-tag">
                  {{ variable }}
                </el-tag>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 编辑模板对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="isEditing ? '编辑模板' : '新增模板'"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        ref="templateFormRef"
        :model="templateForm"
        :rules="templateRules"
        label-width="100px"
      >
        <el-form-item label="模板名称" prop="name">
          <el-input
            v-model="templateForm.name"
            placeholder="请输入模板名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="模板类型" prop="type">
          <el-select v-model="templateForm.type" placeholder="请选择模板类型" style="width: 100%">
            <el-option label="首次催缴" value="first" />
            <el-option label="二次催缴" value="second" />
            <el-option label="最终催缴" value="final" />
            <el-option label="自定义" value="custom" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="短信内容" prop="content">
          <el-input
            v-model="templateForm.content"
            type="textarea"
            :rows="6"
            placeholder="请输入短信内容，可使用变量：{tenantName}、{amount}、{days}、{phone}"
            maxlength="500"
            show-word-limit
          />
          <div class="content-tips">
            <p>可用变量说明：</p>
            <ul>
              <li><code>{tenantName}</code> - 租户名称</li>
              <li><code>{amount}</code> - 应收金额</li>
              <li><code>{days}</code> - 逾期天数</li>
              <li><code>{phone}</code> - 联系电话</li>
              <li><code>{contractNo}</code> - 合同编号</li>
              <li><code>{dueDate}</code> - 到期日期</li>
            </ul>
          </div>
        </el-form-item>
        
        <el-form-item label="模板预览">
          <div class="template-preview">
            {{ getTemplatePreview() }}
          </div>
        </el-form-item>
        
        <el-form-item label="启用状态">
          <el-switch
            v-model="templateForm.enabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTemplate" :loading="saving">
          {{ isEditing ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog v-model="previewDialogVisible" title="模板预览" width="600px">
      <div v-if="previewTemplate" class="preview-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="模板名称">{{ previewTemplate.name }}</el-descriptions-item>
          <el-descriptions-item label="模板类型">
            <el-tag :type="getTypeTagType(previewTemplate.type)">
              {{ getTypeLabel(previewTemplate.type) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="启用状态">
            <el-tag :type="previewTemplate.enabled ? 'success' : 'danger'">
              {{ previewTemplate.enabled ? '已启用' : '已禁用' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="preview-section">
          <h4>原始内容</h4>
          <div class="original-content">
            {{ previewTemplate.content }}
          </div>
        </div>
        
        <div class="preview-section">
          <h4>效果预览</h4>
          <div class="effect-preview">
            {{ getEffectPreview(previewTemplate.content) }}
          </div>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEditFromPreview">编辑模板</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus, Upload, Search, MoreFilled } from '@element-plus/icons-vue'
import { collectionReminderApi } from '@/api/collectionReminder'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 响应式数据
const searchKeyword = ref('')
const templateList = ref<any[]>([])
const editDialogVisible = ref(false)
const previewDialogVisible = ref(false)
const isEditing = ref(false)
const saving = ref(false)
const previewTemplate = ref<any>(null)

// 表单引用和数据
const templateFormRef = ref<FormInstance>()
const templateForm = ref({
  id: null,
  name: '',
  type: '',
  content: '',
  enabled: true
})

// 表单验证规则
const templateRules: FormRules = {
  name: [
    { required: true, message: '请输入模板名称', trigger: 'blur' },
    { min: 2, max: 50, message: '模板名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择模板类型', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入短信内容', trigger: 'blur' },
    { min: 10, max: 500, message: '短信内容长度在 10 到 500 个字符', trigger: 'blur' }
  ]
}

// 获取模板列表
const fetchTemplates = async () => {
  try {
    const response = await collectionReminderApi.getSmsTemplates()
    if (response.data.code === 200) {
      templateList.value = response.data.data.map((template: any) => ({
        ...template,
        usageCount: Math.floor(Math.random() * 100),
        successRate: Math.floor(Math.random() * 40) + 60
      }))
    }
  } catch (error) {
    console.error('获取模板列表失败:', error)
    ElMessage.error('获取模板列表失败')
  }
}

// 获取类型标签类型
const getTypeTagType = (type: string) => {
  switch (type) {
    case 'first': return 'success'
    case 'second': return 'warning'
    case 'final': return 'danger'
    default: return 'info'
  }
}

// 获取类型标签文本
const getTypeLabel = (type: string) => {
  switch (type) {
    case 'first': return '首次催缴'
    case 'second': return '二次催缴'
    case 'final': return '最终催缴'
    case 'custom': return '自定义'
    default: return '未知'
  }
}

// 获取可用变量
const getAvailableVariables = () => {
  return ['{tenantName}', '{amount}', '{days}', '{phone}']
}

// 获取模板预览
const getTemplatePreview = () => {
  if (!templateForm.value.content) return '请输入短信内容'
  
  return templateForm.value.content
    .replace(/{tenantName}/g, '张三')
    .replace(/{amount}/g, '5000')
    .replace(/{days}/g, '15')
    .replace(/{phone}/g, '138****1234')
    .replace(/{contractNo}/g, 'HT001')
    .replace(/{dueDate}/g, '2025-07-25')
}

// 获取效果预览
const getEffectPreview = (content: string) => {
  return content
    .replace(/{tenantName}/g, '张三')
    .replace(/{amount}/g, '5000')
    .replace(/{days}/g, '15')
    .replace(/{phone}/g, '138****1234')
    .replace(/{contractNo}/g, 'HT001')
    .replace(/{dueDate}/g, '2025-07-25')
}

// 事件处理函数
const handleSearch = () => {
  // 实现搜索逻辑
  console.log('搜索:', searchKeyword.value)
}

const handleAdd = () => {
  isEditing.value = false
  templateForm.value = {
    id: null,
    name: '',
    type: '',
    content: '',
    enabled: true
  }
  editDialogVisible.value = true
}

const handleImport = () => {
  ElMessage.info('导入功能开发中...')
}

const handleToggleEnabled = async (template: any) => {
  try {
    // 这里应该调用API更新启用状态
    ElMessage.success(`模板已${template.enabled ? '启用' : '禁用'}`)
  } catch (error) {
    template.enabled = !template.enabled // 回滚状态
    ElMessage.error('操作失败，请重试')
  }
}

const handleCommand = (command: string) => {
  const [action, id] = command.split('-')
  const template = templateList.value.find(t => t.id == id)
  
  if (!template) return
  
  switch (action) {
    case 'edit':
      handleEdit(template)
      break
    case 'copy':
      handleCopy(template)
      break
    case 'preview':
      handlePreview(template)
      break
    case 'delete':
      handleDelete(template)
      break
  }
}

const handleEdit = (template: any) => {
  isEditing.value = true
  templateForm.value = { ...template }
  editDialogVisible.value = true
}

const handleCopy = (template: any) => {
  isEditing.value = false
  templateForm.value = {
    ...template,
    id: null,
    name: template.name + ' (副本)'
  }
  editDialogVisible.value = true
}

const handlePreview = (template: any) => {
  previewTemplate.value = template
  previewDialogVisible.value = true
}

const handleDelete = async (template: any) => {
  try {
    await ElMessageBox.confirm(`确认删除模板"${template.name}"？`, '确认删除', {
      type: 'warning'
    })
    
    // 这里应该调用删除API
    ElMessage.success('模板删除成功')
    fetchTemplates()
  } catch (error) {
    // 用户取消
  }
}

const handleSaveTemplate = async () => {
  if (!templateFormRef.value) return
  
  try {
    await templateFormRef.value.validate()
    
    saving.value = true
    
    await collectionReminderApi.saveSmsTemplate(templateForm.value)
    
    ElMessage.success(isEditing.value ? '模板更新成功' : '模板创建成功')
    editDialogVisible.value = false
    fetchTemplates()
  } catch (error) {
    if (error !== false) {
      console.error('保存模板失败:', error)
      ElMessage.error('保存模板失败')
    }
  } finally {
    saving.value = false
  }
}

const handleDialogClose = () => {
  templateFormRef.value?.resetFields()
}

const handleEditFromPreview = () => {
  previewDialogVisible.value = false
  handleEdit(previewTemplate.value)
}

onMounted(() => {
  fetchTemplates()
})
</script>

<style scoped>
.sms-templates {
  padding: 0;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.left-actions {
  display: flex;
  gap: 12px;
}

.template-list {
  min-height: 400px;
}

.template-card {
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.template-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.template-card.disabled {
  opacity: 0.6;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.template-name {
  font-weight: 600;
  color: #303133;
}

.template-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.template-content {
  padding: 0;
}

.content-preview {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 6px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 16px;
  min-height: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.template-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
  padding: 8px 0;
  border-top: 1px solid #EBEEF5;
  border-bottom: 1px solid #EBEEF5;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.template-variables {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.variables-label {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
}

.variable-tag {
  font-size: 11px;
}

.content-tips {
  margin-top: 8px;
  padding: 12px;
  background: #f0f9ff;
  border-radius: 6px;
  font-size: 12px;
  color: #606266;
}

.content-tips p {
  margin: 0 0 8px 0;
  font-weight: 600;
}

.content-tips ul {
  margin: 0;
  padding-left: 16px;
}

.content-tips li {
  margin-bottom: 4px;
}

.content-tips code {
  background: #e1f5fe;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
}

.template-preview {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  color: #606266;
  line-height: 1.6;
  min-height: 60px;
}

.preview-content {
  padding: 0;
}

.preview-section {
  margin-top: 20px;
}

.preview-section h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.original-content {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  color: #606266;
  line-height: 1.6;
  font-family: 'Courier New', monospace;
}

.effect-preview {
  padding: 12px;
  background: #f0f9ff;
  border-radius: 6px;
  color: #303133;
  line-height: 1.6;
  border: 1px solid #b3d8ff;
}
</style>