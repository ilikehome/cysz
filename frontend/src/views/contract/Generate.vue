<template>
  <div class="contract-generate">
    <PageContainer title="合同生成" subtitle="根据合同模板生成PDF合同文件">
      <div class="generate-content">
        <!-- 步骤条 -->
        <el-steps :active="currentStep" finish-status="success" align-center class="steps">
          <el-step title="选择模板" description="选择合同模板"></el-step>
          <el-step title="填写内容" description="填写合同具体信息"></el-step>
          <el-step title="预览确认" description="预览合同内容"></el-step>
          <el-step title="生成下载" description="生成PDF并下载"></el-step>
        </el-steps>

        <!-- 步骤1: 选择模板 -->
        <div v-if="currentStep === 0" class="step-content">
          <el-card class="template-selection">
            <template #header>
              <div class="card-header">
                <span>选择合同模板</span>
                <el-button type="primary" @click="refreshTemplates">
                  <el-icon><Refresh /></el-icon>
                  刷新
                </el-button>
              </div>
            </template>
            
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
                  <div class="template-meta">
                    <el-tag size="small" :type="getTemplateTypeColor(template.templateType)">
                      {{ template.templateType }}
                    </el-tag>
                    <span class="usage-count">使用次数: {{ template.usageCount }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="step-actions">
              <el-button type="primary" :disabled="!selectedTemplate" @click="nextStep">
                下一步
              </el-button>
            </div>
          </el-card>
        </div>

        <!-- 步骤2: 填写内容 -->
        <div v-if="currentStep === 1" class="step-content">
          <el-card class="form-content">
            <template #header>
              <div class="card-header">
                <span>填写合同信息</span>
                <el-tag>{{ selectedTemplate?.templateName }}</el-tag>
              </div>
            </template>
            
            <el-form :model="contractForm" :rules="formRules" ref="contractFormRef" label-width="120px">
              <!-- 基本信息 -->
              <div class="form-section">
                <h3>基本信息</h3>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="合同编号" prop="contractNo">
                      <el-input v-model="contractForm.contractNo" placeholder="请输入合同编号" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="合同名称" prop="contractName">
                      <el-input v-model="contractForm.contractName" placeholder="请输入合同名称" />
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="签约日期" prop="signDate">
                      <el-date-picker
                        v-model="contractForm.signDate"
                        type="date"
                        placeholder="选择签约日期"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="生效日期" prop="effectiveDate">
                      <el-date-picker
                        v-model="contractForm.effectiveDate"
                        type="date"
                        placeholder="选择生效日期"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>

              <!-- 动态字段 -->
              <div class="form-section" v-if="selectedTemplate?.fields?.length">
                <h3>模板字段</h3>
                <div v-for="field in selectedTemplate.fields" :key="field.fieldName" class="dynamic-field">
                  <el-form-item 
                    :label="field.fieldLabel" 
                    :prop="`dynamicFields.${field.fieldName}`"
                    :rules="field.required ? [{ required: true, message: `请填写${field.fieldLabel}` }] : []"
                  >
                    <!-- 文本输入 -->
                    <el-input 
                      v-if="field.fieldType === 'text'"
                      v-model="contractForm.dynamicFields[field.fieldName]"
                      :placeholder="`请输入${field.fieldLabel}`"
                    />
                    <!-- 数字输入 -->
                    <el-input-number 
                      v-else-if="field.fieldType === 'number'"
                      v-model="contractForm.dynamicFields[field.fieldName]"
                      :placeholder="`请输入${field.fieldLabel}`"
                      style="width: 100%"
                    />
                    <!-- 日期选择 -->
                    <el-date-picker 
                      v-else-if="field.fieldType === 'date'"
                      v-model="contractForm.dynamicFields[field.fieldName]"
                      type="date"
                      :placeholder="`请选择${field.fieldLabel}`"
                      style="width: 100%"
                    />
                    <!-- 文本域 -->
                    <el-input 
                      v-else-if="field.fieldType === 'textarea'"
                      v-model="contractForm.dynamicFields[field.fieldName]"
                      type="textarea"
                      :rows="3"
                      :placeholder="`请输入${field.fieldLabel}`"
                    />
                    <!-- 默认文本输入 -->
                    <el-input 
                      v-else
                      v-model="contractForm.dynamicFields[field.fieldName]"
                      :placeholder="`请输入${field.fieldLabel}`"
                    />
                  </el-form-item>
                </div>
              </div>
            </el-form>
            
            <div class="step-actions">
              <el-button @click="prevStep">上一步</el-button>
              <el-button type="primary" @click="validateAndNext">下一步</el-button>
            </div>
          </el-card>
        </div>

        <!-- 步骤3: 预览确认 -->
        <div v-if="currentStep === 2" class="step-content">
          <el-card class="preview-content">
            <template #header>
              <div class="card-header">
                <span>合同预览</span>
                <el-button @click="editContent">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
              </div>
            </template>
            
            <div class="contract-preview" v-html="previewContent"></div>
            
            <div class="step-actions">
              <el-button @click="prevStep">上一步</el-button>
              <el-button type="primary" @click="nextStep">确认生成</el-button>
            </div>
          </el-card>
        </div>

        <!-- 步骤4: 生成下载 -->
        <div v-if="currentStep === 3" class="step-content">
          <el-card class="generate-result">
            <template #header>
              <span>合同生成</span>
            </template>
            
            <div class="result-content">
              <div v-if="generating" class="generating">
                <el-icon class="is-loading"><Loading /></el-icon>
                <p>正在生成PDF合同，请稍候...</p>
                <el-progress :percentage="generateProgress" />
              </div>
              
              <div v-else-if="generateSuccess" class="success">
                <el-icon class="success-icon"><CircleCheck /></el-icon>
                <h3>合同生成成功！</h3>
                <p>合同编号：{{ contractForm.contractNo }}</p>
                <p>生成时间：{{ new Date().toLocaleString() }}</p>
                
                <div class="download-actions">
                  <el-button type="primary" size="large" @click="downloadPDF">
                    <el-icon><Download /></el-icon>
                    下载PDF合同
                  </el-button>
                  <el-button @click="previewPDF">
                    <el-icon><View /></el-icon>
                    预览PDF
                  </el-button>
                </div>
              </div>
              
              <div v-else class="error">
                <el-icon class="error-icon"><CircleClose /></el-icon>
                <h3>生成失败</h3>
                <p>{{ errorMessage }}</p>
                <el-button type="primary" @click="retryGenerate">重试</el-button>
              </div>
            </div>
            
            <div class="step-actions" v-if="generateSuccess">
              <el-button @click="resetForm">重新生成</el-button>
              <el-button type="primary" @click="goToContractList">查看合同列表</el-button>
            </div>
          </el-card>
        </div>
      </div>
    </PageContainer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { 
  Document, 
  Refresh, 
  Edit, 
  Loading, 
  CircleCheck, 
  CircleClose, 
  Download, 
  View 
} from '@element-plus/icons-vue'
import PageContainer from '@/components/PageContainer.vue'
import { getContractTemplates } from '@/api/contractTemplate'
import { 
  generateContractPDF, 
  downloadContractPDF, 
  previewContractPDF,
  validateContractData 
} from '@/api/contractGenerate'

const router = useRouter()

// 当前步骤
const currentStep = ref(0)

// 模板列表
const templates = ref([])
const selectedTemplate = ref(null)

// 表单数据
const contractForm = reactive({
  contractNo: '',
  contractName: '',
  signDate: '',
  effectiveDate: '',
  dynamicFields: {}
})

// 表单验证规则
const formRules = {
  contractNo: [{ required: true, message: '请输入合同编号', trigger: 'blur' }],
  contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
  signDate: [{ required: true, message: '请选择签约日期', trigger: 'change' }],
  effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }]
}

const contractFormRef = ref()

// 生成状态
const generating = ref(false)
const generateSuccess = ref(false)
const generateProgress = ref(0)
const errorMessage = ref('')

// 预览内容
const previewContent = computed(() => {
  if (!selectedTemplate.value) return ''
  
  let content = selectedTemplate.value.templateContent || ''
  
  // 替换基本字段
  content = content.replace(/\{\{contractNo\}\}/g, contractForm.contractNo)
  content = content.replace(/\{\{contractName\}\}/g, contractForm.contractName)
  content = content.replace(/\{\{signDate\}\}/g, contractForm.signDate)
  content = content.replace(/\{\{effectiveDate\}\}/g, contractForm.effectiveDate)
  
  // 替换动态字段
  if (selectedTemplate.value.fields) {
    selectedTemplate.value.fields.forEach(field => {
      const value = contractForm.dynamicFields[field.fieldName] || field.defaultValue || ''
      content = content.replace(new RegExp(`\\{\\{${field.fieldName}\\}\\}`, 'g'), value)
    })
  }
  
  return content
})

// 获取模板列表
const loadTemplates = async () => {
  try {
    const response = await getContractTemplates()
    templates.value = response.data || []
  } catch (error) {
    ElMessage.error('获取模板列表失败')
  }
}

// 刷新模板
const refreshTemplates = () => {
  loadTemplates()
}

// 选择模板
const selectTemplate = (template) => {
  selectedTemplate.value = template
  // 初始化动态字段
  if (template.fields) {
    template.fields.forEach(field => {
      contractForm.dynamicFields[field.fieldName] = field.defaultValue || ''
    })
  }
}

// 获取模板类型颜色
const getTemplateTypeColor = (type) => {
  const colors = {
    '租赁合同': 'primary',
    '服务合同': 'success',
    '采购合同': 'warning',
    '其他': 'info'
  }
  return colors[type] || 'info'
}

// 下一步
const nextStep = () => {
  if (currentStep.value === 2) {
    generateContract()
  } else {
    currentStep.value++
  }
}

// 上一步
const prevStep = () => {
  currentStep.value--
}

// 验证并下一步
const validateAndNext = async () => {
  try {
    await contractFormRef.value.validate()
    nextStep()
  } catch (error) {
    ElMessage.error('请完善必填信息')
  }
}

// 编辑内容
const editContent = () => {
  currentStep.value = 1
}

// 生成合同
const generateContract = async () => {
  currentStep.value = 3
  generating.value = true
  generateProgress.value = 0
  generateSuccess.value = false
  errorMessage.value = ''
  
  try {
    // 准备合同数据
    const contractData = {
      templateId: selectedTemplate.value?.id,
      contractNo: contractForm.contractNo,
      contractName: contractForm.contractName,
      signDate: contractForm.signDate,
      effectiveDate: contractForm.effectiveDate,
      dynamicFields: contractForm.dynamicFields
    }
    
    // 模拟生成进度
    const progressInterval = setInterval(() => {
      generateProgress.value += 20
      if (generateProgress.value >= 80) {
        clearInterval(progressInterval)
      }
    }, 300)
    
    // 调用后端API生成合同
    const response = await generateContractPDF(contractData)
    
    generateProgress.value = 100
    clearInterval(progressInterval)
    generating.value = false
    generateSuccess.value = true
    
    ElMessage.success('合同生成成功！')
  } catch (error) {
    generating.value = false
    generateSuccess.value = false
    errorMessage.value = error.message || '生成失败，请重试'
    ElMessage.error('合同生成失败')
  }
}

// 重试生成
const retryGenerate = () => {
  generateContract()
}

// 下载PDF
const downloadPDF = async () => {
  try {
    // 直接使用后端API下载
    const link = document.createElement('a')
    link.href = `http://localhost:8080/api/contract-generate/download/${contractForm.contractNo}`
    link.download = `${contractForm.contractNo}_合同.pdf`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    
    ElMessage.success('PDF下载已开始')
  } catch (error) {
    ElMessage.error('下载失败')
  }
}

// 预览PDF
const previewPDF = async () => {
  try {
    // 打开预览窗口
    const url = `http://localhost:8080/api/contract-generate/preview/${contractForm.contractNo}`
    window.open(url, '_blank')
    
    ElMessage.success('PDF预览已打开')
  } catch (error) {
    ElMessage.error('预览失败')
  }
}

// 重置表单
const resetForm = () => {
  currentStep.value = 0
  selectedTemplate.value = null
  Object.assign(contractForm, {
    contractNo: '',
    contractName: '',
    signDate: '',
    effectiveDate: '',
    dynamicFields: {}
  })
  generateSuccess.value = false
}

// 跳转到合同列表
const goToContractList = () => {
  router.push('/contract/info')
}

onMounted(() => {
  loadTemplates()
})
</script>

<script lang="ts">
import { defineComponent } from 'vue'
import { 
  Document, 
  Refresh, 
  Edit, 
  Loading, 
  CircleCheck, 
  CircleClose, 
  Download, 
  View 
} from '@element-plus/icons-vue'

export default defineComponent({
  components: {
    Document,
    Refresh,
    Edit,
    Loading,
    CircleCheck,
    CircleClose,
    Download,
    View
  }
})
</script>

<style scoped>
.contract-generate {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
}

.generate-content {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.steps {
  margin-bottom: 30px;
  background: rgba(255, 255, 255, 0.95);
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  flex-shrink: 0;
}

.steps :deep(.el-steps) {
  background: transparent;
}

.step-content {
  min-height: 500px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.step-content :deep(.el-card) {
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

.step-content :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.step-content :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.step-content :deep(.el-card__body) {
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

/* 模板选择样式 */
.template-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
  flex: 1;
  overflow-y: auto;
}

.template-card {
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.template-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.15);
  transform: translateY(-4px);
}

.template-card.active {
  border-color: #3b82f6;
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.2);
}

.template-icon {
  text-align: center;
  margin-bottom: 20px;
}

.template-icon .el-icon {
  font-size: 48px;
  color: #3b82f6;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.template-info h4 {
  margin: 0 0 12px 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.template-info p {
  margin: 0 0 16px 0;
  color: #6b7280;
  font-size: 14px;
  line-height: 1.5;
}

.template-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-meta :deep(.el-tag) {
  border-radius: 6px;
  font-weight: 500;
  border: none;
}

.usage-count {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 500;
}

/* 表单样式 */
.form-section {
  margin-bottom: 32px;
}

.form-section h3 {
  margin: 0 0 24px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #e2e8f0;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
  position: relative;
}

.form-section h3::before {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 40px;
  height: 2px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border-radius: 1px;
}

.dynamic-field {
  margin-bottom: 20px;
}

.form-content :deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

.form-content :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.form-content :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* 预览样式 */
.contract-preview {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  min-height: 400px;
  line-height: 1.8;
  font-size: 14px;
  margin-bottom: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  flex: 1;
  overflow-y: auto;
}

/* 生成结果样式 */
.result-content {
  text-align: center;
  padding: 40px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.generating {
  color: #3b82f6;
}

.generating .el-icon {
  font-size: 64px;
  margin-bottom: 24px;
}

.generating p {
  font-size: 16px;
  margin-bottom: 24px;
  color: #6b7280;
}

.success .success-icon {
  font-size: 80px;
  color: #10b981;
  margin-bottom: 24px;
}

.success h3 {
  font-size: 24px;
  color: #1f2937;
  margin-bottom: 16px;
}

.success p {
  color: #6b7280;
  margin-bottom: 8px;
}

.error .error-icon {
  font-size: 80px;
  color: #ef4444;
  margin-bottom: 24px;
}

.error h3 {
  font-size: 24px;
  color: #1f2937;
  margin-bottom: 16px;
}

.download-actions {
  margin: 32px 0;
}

.download-actions .el-button {
  margin: 0 12px;
  border-radius: 10px;
  padding: 12px 24px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.download-actions .el-button--primary {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.3);
}

.download-actions .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.4);
}

/* 步骤操作按钮 */
.step-actions {
  text-align: center;
  padding-top: 24px;
  border-top: 1px solid #e2e8f0;
  flex-shrink: 0;
}

.step-actions .el-button {
  margin: 0 12px;
  min-width: 120px;
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.step-actions .el-button--primary {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

.step-actions .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}
</style>
