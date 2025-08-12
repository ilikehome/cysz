<template>
  <div class="contract-auto">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>自动合同</span>
          <div class="header-actions">
            <el-button type="primary" @click="handleAddTemplate" v-if="activeTab === 'template'">
              <el-icon><Plus /></el-icon>
              新建模板
            </el-button>
            <el-button type="success" @click="handleStartGenerate" v-if="activeTab === 'generate'">
              <el-icon><DocumentAdd /></el-icon>
              开始生成
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 标签页切换 -->
      <el-tabs v-model="activeTab" class="auto-tabs">
        <!-- 合同模板 -->
        <el-tab-pane label="合同模板" name="template">
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
          
          <!-- 模板表格 -->
          <el-table
            v-loading="templateLoading"
            :data="templateData"
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
            <el-table-column label="操作" width="320" fixed="right">
              <template #default="{ row }">
                <el-button type="text" @click="handlePreview(row)">预览</el-button>
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
                <el-button type="text" @click="handleCopy(row)">复制</el-button>
                <el-button type="text" @click="handleGenerate(row)" style="color: #67c23a">生成</el-button>
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
          
          <!-- 模板分页 -->
          <div class="pagination">
            <el-pagination
              v-model:current-page="templatePagination.current"
              v-model:page-size="templatePagination.size"
              :page-sizes="[10, 20, 50, 100]"
              :total="templatePagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleTemplateSizeChange"
              @current-change="handleTemplateCurrentChange"
            />
          </div>
        </el-tab-pane>
        
        <!-- 合同生成 -->
        <el-tab-pane label="合同生成" name="generate">
          <!-- 步骤条 -->
          <div class="steps-container">
            <el-steps :active="currentStep" align-center>
              <el-step title="选择模板" description="选择合同模板" />
              <el-step title="填写信息" description="填写合同信息" />
              <el-step title="预览确认" description="预览并确认" />
              <el-step title="生成完成" description="下载合同文件" />
            </el-steps>
          </div>
          
          <!-- 步骤内容 -->
          <div class="step-content">
            <!-- 步骤1：选择模板 -->
            <div v-if="currentStep === 0" class="step-panel">
              <h3>选择合同模板</h3>
              <div class="template-grid">
                <div 
                  v-for="template in templates" 
                  :key="template.id"
                  class="template-card"
                  :class="{ active: selectedTemplate?.id === template.id }"
                  @click="selectTemplate(template)"
                >
                  <div class="template-icon">
                    <el-icon><Document /></el-icon>
                  </div>
                  <div class="template-info">
                    <h4>{{ template.templateName }}</h4>
                    <p>{{ template.description }}</p>
                    <el-tag :type="getTemplateTypeColor(template.templateType)" size="small">
                      {{ template.templateType }}
                    </el-tag>
                    <div class="template-stats">
                      <span>使用次数: {{ template.usageCount }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="step-actions">
                <el-button type="primary" :disabled="!selectedTemplate" @click="nextStep">
                  下一步
                </el-button>
              </div>
            </div>
            
            <!-- 步骤2：填写信息 -->
            <div v-if="currentStep === 1" class="step-panel">
              <h3>填写合同信息</h3>
              <div class="form-container">
                <el-form :model="contractForm" label-width="120px">
                  <el-row :gutter="24">
                    <el-col :span="12">
                      <el-form-item label="合同名称" required>
                        <el-input v-model="contractForm.contractName" placeholder="请输入合同名称" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="合同编号">
                        <el-input v-model="contractForm.contractNo" placeholder="自动生成" readonly />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  
                  <el-row :gutter="24">
                    <el-col :span="12">
                      <el-form-item label="甲方名称" required>
                        <el-input v-model="contractForm.partyA" placeholder="请输入甲方名称" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="乙方名称" required>
                        <el-input v-model="contractForm.partyB" placeholder="请输入乙方名称" />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  
                  <el-row :gutter="24">
                    <el-col :span="12">
                      <el-form-item label="签订日期" required>
                        <el-date-picker
                          v-model="contractForm.signDate"
                          type="date"
                          placeholder="请选择签订日期"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="生效日期" required>
                        <el-date-picker
                          v-model="contractForm.effectiveDate"
                          type="date"
                          placeholder="请选择生效日期"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  
                  <el-row :gutter="24">
                    <el-col :span="12">
                      <el-form-item label="合同金额">
                        <el-input v-model="contractForm.amount" placeholder="请输入合同金额">
                          <template #append>元</template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="合同期限">
                        <el-input v-model="contractForm.duration" placeholder="请输入合同期限">
                          <template #append>月</template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  
                  <el-form-item label="备注">
                    <el-input
                      v-model="contractForm.remark"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入备注信息"
                    />
                  </el-form-item>
                </el-form>
              </div>
              <div class="step-actions">
                <el-button @click="prevStep">上一步</el-button>
                <el-button type="primary" @click="validateAndNext">下一步</el-button>
              </div>
            </div>
            
            <!-- 步骤3：预览确认 -->
            <div v-if="currentStep === 2" class="step-panel">
              <h3>预览合同内容</h3>
              <div class="preview-container">
                <div class="preview-toolbar">
                  <el-button @click="editContent">编辑内容</el-button>
                </div>
                <div class="preview-content">
                  <div class="contract-preview">
                    <h2>{{ contractForm.contractName }}</h2>
                    <div class="contract-info">
                      <p><strong>合同编号：</strong>{{ contractForm.contractNo }}</p>
                      <p><strong>甲方：</strong>{{ contractForm.partyA }}</p>
                      <p><strong>乙方：</strong>{{ contractForm.partyB }}</p>
                      <p><strong>签订日期：</strong>{{ contractForm.signDate }}</p>
                      <p><strong>生效日期：</strong>{{ contractForm.effectiveDate }}</p>
                      <p><strong>合同金额：</strong>{{ contractForm.amount }}元</p>
                      <p><strong>合同期限：</strong>{{ contractForm.duration }}月</p>
                    </div>
                    <div class="contract-content">
                      <p>{{ previewContent }}</p>
                    </div>
                  </div>
                </div>
              </div>
              <div class="step-actions">
                <el-button @click="prevStep">上一步</el-button>
                <el-button type="primary" @click="nextStep">确认生成</el-button>
              </div>
            </div>
            
            <!-- 步骤4：生成完成 -->
            <div v-if="currentStep === 3" class="step-panel">
              <div class="result-container">
                <div v-if="generateStatus === 'success'" class="success-result">
                  <el-icon class="success-icon"><CircleCheck /></el-icon>
                  <h3>合同生成成功！</h3>
                  <p>合同已成功生成，您可以下载PDF文件或预览合同内容。</p>
                  <div class="result-actions">
                    <el-button type="primary" @click="downloadPDF">
                      <el-icon><Download /></el-icon>
                      下载PDF
                    </el-button>
                    <el-button @click="previewPDF">
                      <el-icon><View /></el-icon>
                      预览合同
                    </el-button>
                  </div>
                </div>
                
                <div v-else-if="generateStatus === 'error'" class="error-result">
                  <el-icon class="error-icon"><CircleClose /></el-icon>
                  <h3>生成失败</h3>
                  <p>合同生成过程中出现错误，请重试。</p>
                  <div class="result-actions">
                    <el-button type="primary" @click="retryGenerate">重新生成</el-button>
                  </div>
                </div>
                
                <div v-else class="loading-result">
                  <el-icon class="loading-icon"><Loading /></el-icon>
                  <h3>正在生成合同...</h3>
                  <p>请稍候，系统正在为您生成合同文件。</p>
                </div>
              </div>
              
              <div class="step-actions" v-if="generateStatus === 'success'">
                <el-button @click="resetGenerateForm">重新生成</el-button>
                <el-button type="primary" @click="goToContractList">查看合同列表</el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
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

    <!-- 生成合同对话框 -->
    <el-dialog
      v-model="generateVisible"
      title="生成合同"
      width="70%"
      :before-close="handleGenerateClose"
    >
      <el-form :model="generateForm" :rules="generateRules" ref="generateFormRef" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="generateForm.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同编号">
              <el-input v-model="generateForm.contractNo" placeholder="自动生成" readonly />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="甲方名称" prop="partyA">
              <el-input v-model="generateForm.partyA" placeholder="请输入甲方名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="乙方名称" prop="partyB">
              <el-input v-model="generateForm.partyB" placeholder="请输入乙方名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="签订日期" prop="signDate">
              <el-date-picker
                v-model="generateForm.signDate"
                type="date"
                placeholder="请选择签订日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="generateForm.effectiveDate"
                type="date"
                placeholder="请选择生效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="合同金额">
              <el-input v-model="generateForm.amount" placeholder="请输入合同金额">
                <template #append>元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同期限">
              <el-input v-model="generateForm.duration" placeholder="请输入合同期限">
                <template #append>月</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注">
          <el-input
            v-model="generateForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="generateVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirmGenerate" :loading="generating">生成合同</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { 
  Plus, 
  Search, 
  Refresh, 
  DocumentAdd,
  Document,
  CircleCheck,
  CircleClose,
  Loading,
  Download,
  View
} from '@element-plus/icons-vue'

const router = useRouter()

// 当前激活的标签页
const activeTab = ref('template')

// 当前步骤
const currentStep = ref(0)

// 生成状态
const generateStatus = ref('loading') // loading, success, error

// 选中的模板
const selectedTemplate = ref<any>(null)

// 合同表单数据
const contractForm = reactive({
  contractName: '',
  contractNo: 'HT' + Date.now(),
  partyA: '',
  partyB: '',
  signDate: '',
  effectiveDate: '',
  amount: '',
  duration: '',
  remark: ''
})

// 预览内容
const previewContent = ref('这里是合同的详细内容预览...')

// 模板列表 - 模拟数据
const templates = ref([
  {
    id: 1,
    templateName: '标准租赁合同模板',
    templateType: '租赁合同',
    description: '适用于标准的房屋租赁业务',
    usageCount: 25
  },
  {
    id: 2,
    templateName: '服务外包合同模板',
    templateType: '服务合同',
    description: '适用于各类服务外包业务',
    usageCount: 18
  }
])

// 响应式数据
const templateLoading = ref(false)
const generateLoading = ref(false)
const saving = ref(false)
const generating = ref(false)
const previewVisible = ref(false)
const editVisible = ref(false)
const generateVisible = ref(false)
const currentTemplate = ref<any>(null)
const editFormRef = ref()
const generateFormRef = ref()

// 模板内容和字段
const templateContent = ref('')
const templateFields = ref<any[]>([])

// 模板搜索表单
const searchForm = reactive({
  keyword: '',
  templateType: '',
  status: ''
})

// 生成记录搜索表单
const generateSearchForm = reactive({
  contractName: '',
  status: '',
  dateRange: null
})

// 模板分页数据
const templatePagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 生成记录分页数据
const generatePagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 字段类型定义
interface TemplateField {
  fieldName: string
  fieldType: string
  required: boolean
  description: string
}

// 编辑表单数据
const editForm = reactive({
  id: null as number | null,
  templateName: '',
  templateType: '',
  version: '',
  status: 'ACTIVE',
  description: '',
  content: '',
  fields: [] as TemplateField[]
})

// 生成表单数据
const generateForm = reactive({
  templateId: null as number | null,
  templateName: '',
  contractName: '',
  contractNo: 'HT' + Date.now(),
  partyA: '',
  partyB: '',
  signDate: '',
  effectiveDate: '',
  amount: '',
  duration: '',
  remark: ''
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

const generateRules = {
  contractName: [
    { required: true, message: '请输入合同名称', trigger: 'blur' }
  ],
  partyA: [
    { required: true, message: '请输入甲方名称', trigger: 'blur' }
  ],
  partyB: [
    { required: true, message: '请输入乙方名称', trigger: 'blur' }
  ],
  signDate: [
    { required: true, message: '请选择签订日期', trigger: 'change' }
  ],
  effectiveDate: [
    { required: true, message: '请选择生效日期', trigger: 'change' }
  ]
}

// 模板数据 - 模拟数据
const templateData = ref([
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

// 生成记录数据 - 模拟数据
const generateData = ref([
  {
    id: 1,
    contractName: '万达广场租赁合同',
    templateName: '标准租赁合同模板',
    partyA: '万达集团',
    partyB: '张三商贸有限公司',
    status: 'SUCCESS',
    generateTime: '2024-01-20 15:30:00',
    fileSize: '2.5MB'
  },
  {
    id: 2,
    contractName: '物业服务外包合同',
    templateName: '服务外包合同模板',
    partyA: '万达集团',
    partyB: '清洁服务公司',
    status: 'SUCCESS',
    generateTime: '2024-01-18 14:20:00',
    fileSize: '1.8MB'
  },
  {
    id: 3,
    contractName: '设备采购合同',
    templateName: '销售合同模板',
    partyA: '万达集团',
    partyB: '设备供应商',
    status: 'GENERATING',
    generateTime: '2024-01-22 10:15:00',
    fileSize: '-'
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

// 获取生成状态标签颜色
const getGenerateStatusTag = (status: string) => {
  const tagMap: Record<string, string> = {
    'GENERATING': 'warning',
    'SUCCESS': 'success',
    'FAILED': 'danger'
  }
  return tagMap[status] || 'info'
}

// 获取生成状态名称
const getGenerateStatusName = (status: string) => {
  const nameMap: Record<string, string> = {
    'GENERATING': '生成中',
    'SUCCESS': '生成成功',
    'FAILED': '生成失败'
  }
  return nameMap[status] || status
}

// 获取模板类型颜色
const getTemplateTypeColor = (type: string) => {
  const colorMap: Record<string, string> = {
    '租赁合同': 'primary',
    '服务合同': 'success',
    '销售合同': 'warning',
    '其他合同': 'info'
  }
  return colorMap[type] || 'info'
}

// 选择模板
const selectTemplate = (template: any) => {
  selectedTemplate.value = template
}

// 下一步
const nextStep = () => {
  if (currentStep.value < 3) {
    if (currentStep.value === 2) {
      // 开始生成合同
      generateContract()
    }
    currentStep.value++
  }
}

// 上一步
const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

// 验证并下一步
const validateAndNext = () => {
  if (!contractForm.contractName) {
    ElMessage.warning('请输入合同名称')
    return
  }
  if (!contractForm.partyA) {
    ElMessage.warning('请输入甲方名称')
    return
  }
  if (!contractForm.partyB) {
    ElMessage.warning('请输入乙方名称')
    return
  }
  nextStep()
}

// 编辑内容
const editContent = () => {
  ElMessage.info('编辑功能开发中...')
}

// 生成合同
const generateContract = async () => {
  generateStatus.value = 'loading'
  
  // 模拟生成过程
  setTimeout(() => {
    generateStatus.value = 'success'
  }, 2000)
}

// 下载PDF
const downloadPDF = () => {
  ElMessage.success('PDF下载功能开发中...')
}

// 预览PDF
const previewPDF = () => {
  ElMessage.info('PDF预览功能开发中...')
}

// 重试生成
const retryGenerate = () => {
  generateContract()
}

// 重置表单
const resetGenerateForm = () => {
  currentStep.value = 0
  selectedTemplate.value = null
  generateStatus.value = 'loading'
  Object.assign(contractForm, {
    contractName: '',
    contractNo: 'HT' + Date.now(),
    partyA: '',
    partyB: '',
    signDate: '',
    effectiveDate: '',
    amount: '',
    duration: '',
    remark: ''
  })
}

// 跳转到合同列表
const goToContractList = () => {
  router.push('/contract/info')
}

// 新建模板
const handleAddTemplate = () => {
  resetEditForm()
  editVisible.value = true
}

// 开始生成
const handleStartGenerate = () => {
  activeTab.value = 'template'
  ElMessage.info('请先选择一个模板进行生成')
}

// 模板搜索
const handleSearch = () => {
  templatePagination.current = 1
  loadTemplateData()
}

// 重置模板搜索
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.templateType = ''
  searchForm.status = ''
  handleSearch()
}

// 生成记录搜索
const handleGenerateSearch = () => {
  generatePagination.current = 1
  loadGenerateData()
}

// 重置生成记录搜索
const handleGenerateReset = () => {
  generateSearchForm.contractName = ''
  generateSearchForm.status = ''
  generateSearchForm.dateRange = null
  handleGenerateSearch()
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
    templateData.value.push(newTemplate)
    ElMessage.success('复制成功')
    loadTemplateData()
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

// 生成合同（从模板表格）
const handleGenerate = (row: any) => {
  currentTemplate.value = row
  generateForm.templateId = row.id
  generateForm.templateName = row.templateName
  generateForm.contractName = ''
  generateForm.contractNo = 'HT' + Date.now()
  generateForm.partyA = ''
  generateForm.partyB = ''
  generateForm.signDate = ''
  generateForm.effectiveDate = ''
  generateForm.amount = ''
  generateForm.duration = ''
  generateForm.remark = ''
  generateVisible.value = true
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

// 关闭生成对话框
const handleGenerateClose = () => {
  generateVisible.value = false
  resetGenerateForm()
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
      const index = templateData.value.findIndex(item => item.id === editForm.id)
      if (index !== -1) {
        templateData.value[index] = {
          ...templateData.value[index],
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
      templateData.value.unshift(newTemplate)
      ElMessage.success('模板创建成功')
    }
    
    editVisible.value = false
    resetEditForm()
    loadTemplateData()
  } catch (error: any) {
    ElMessage.error(error.message || '保存失败')
  } finally {
    saving.value = false
  }
}

// 确认生成合同
const handleConfirmGenerate = async () => {
  try {
    await generateFormRef.value?.validate()
    generating.value = true
    
    // 模拟生成操作
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    const newGenerate = {
      id: Date.now(),
      contractName: generateForm.contractName,
      templateName: generateForm.templateName,
      partyA: generateForm.partyA,
      partyB: generateForm.partyB,
      status: 'SUCCESS',
      generateTime: new Date().toLocaleString(),
      fileSize: '2.1MB'
    }
    
    generateData.value.unshift(newGenerate)
    
    // 更新模板使用次数
    const templateIndex = templateData.value.findIndex(item => item.id === generateForm.templateId)
    if (templateIndex !== -1) {
      templateData.value[templateIndex].usageCount++
    }
    
    ElMessage.success('合同生成成功')
    generateVisible.value = false
    resetGenerateForm()
    activeTab.value = 'generate'
    loadGenerateData()
  } catch (error: any) {
    ElMessage.error(error.message || '生成失败')
  } finally {
    generating.value = false
  }
}

// 切换模板状态
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
    loadTemplateData()
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
    
    const index = templateData.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      templateData.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
    loadTemplateData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 预览合同
const handlePreviewContract = (row: any) => {
  ElMessage.info(`预览合同: ${row.contractName}`)
}

// 下载合同
const handleDownload = (row: any) => {
  ElMessage.success(`下载合同: ${row.contractName}`)
}

// 重新生成
const handleRegenerate = (row: any) => {
  ElMessage.info(`重新生成合同: ${row.contractName}`)
}

// 删除生成记录
const handleDeleteGenerate = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除生成记录"${row.contractName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const index = generateData.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      generateData.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
    loadGenerateData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 模板分页大小变化
const handleTemplateSizeChange = (size: number) => {
  templatePagination.size = size
  loadTemplateData()
}

// 模板当前页变化
const handleTemplateCurrentChange = (current: number) => {
  templatePagination.current = current
  loadTemplateData()
}

// 生成记录分页大小变化
const handleGenerateSizeChange = (size: number) => {
  generatePagination.size = size
  loadGenerateData()
}

// 生成记录当前页变化
const handleGenerateCurrentChange = (current: number) => {
  generatePagination.current = current
  loadGenerateData()
}

// 加载模板数据
const loadTemplateData = async () => {
  try {
    templateLoading.value = true
    // 这里可以调用实际的API
    templatePagination.total = templateData.value.length
  } catch (error: any) {
    ElMessage.error(error.message || '加载模板数据失败')
  } finally {
    templateLoading.value = false
  }
}

// 加载生成记录数据
const loadGenerateData = async () => {
  try {
    generateLoading.value = true
    // 这里可以调用实际的API
    generatePagination.total = generateData.value.length
  } catch (error: any) {
    ElMessage.error(error.message || '加载生成记录失败')
  } finally {
    generateLoading.value = false
  }
}

onMounted(() => {
  loadTemplateData()
  loadGenerateData()
})
</script>

<style scoped>
.contract-auto {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.contract-auto :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.contract-auto :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.contract-auto :deep(.el-card__body) {
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
  background: linear-gradient(135deg, #06b6d4 0%, #0891b2 100%);
  border-radius: 2px;
  margin-right: 12px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.auto-tabs {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.auto-tabs :deep(.el-tabs__content) {
  flex: 1;
  overflow: hidden;
}

.auto-tabs :deep(.el-tab-pane) {
  height: 100%;
  display: flex;
  flex-direction: column;
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

.contract-auto {
  animation: fadeInUp 0.6s ease forwards;
}

/* 合同生成相关样式 */
.steps-container {
  margin-bottom: 32px;
  padding: 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.step-content {
  flex: 1;
  overflow-y: auto;
}

.step-panel {
  padding: 24px;
}

.step-panel h3 {
  font-size: 20px;
  color: #1f2937;
  margin-bottom: 24px;
  font-weight: 600;
}

.template-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.template-card {
  padding: 24px;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fff;
}

.template-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
  transform: translateY(-2px);
}

.template-card.active {
  border-color: #3b82f6;
  background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

.template-icon {
  text-align: center;
  margin-bottom: 16px;
}

.template-icon .el-icon {
  font-size: 48px;
  color: #3b82f6;
}

.template-info h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 8px;
}

.template-info p {
  color: #6b7280;
  margin-bottom: 12px;
  font-size: 14px;
}

.template-stats {
  margin-top: 12px;
  font-size: 12px;
  color: #9ca3af;
}

.form-container {
  background: #fff;
  padding: 32px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  margin-bottom: 32px;
}

.preview-container {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  margin-bottom: 32px;
  overflow: hidden;
}

.preview-toolbar {
  padding: 16px 24px;
  background: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
}

.preview-content {
  padding: 32px;
  max-height: 400px;
  overflow-y: auto;
}

.contract-preview h2 {
  text-align: center;
  color: #1f2937;
  margin-bottom: 24px;
}

.contract-info {
  margin-bottom: 24px;
}

.contract-info p {
  margin-bottom: 8px;
  color: #374151;
}

.contract-content {
  color: #4b5563;
  line-height: 1.6;
}

.result-container {
  text-align: center;
  padding: 48px 24px;
}

.success-result .success-icon {
  font-size: 64px;
  color: #10b981;
  margin-bottom: 24px;
}

.error-result .error-icon {
  font-size: 64px;
  color: #ef4444;
  margin-bottom: 24px;
}

.loading-result .loading-icon {
  font-size: 64px;
  color: #3b82f6;
  margin-bottom: 24px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.result-container h3 {
  font-size: 24px;
  color: #1f2937;
  margin-bottom: 16px;
}

.result-container p {
  color: #6b7280;
  margin-bottom: 32px;
}

.result-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.step-actions {
  text-align: center;
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;
}

.step-actions .el-button {
  margin: 0 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .contract-auto {
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
