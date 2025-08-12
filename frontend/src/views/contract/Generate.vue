<template>
  <div class="contract-generate">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>合同生成</span>
          <div class="header-actions">
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </div>
        </div>
      </template>
      
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
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { 
  Refresh, 
  Document, 
  CircleCheck, 
  CircleClose, 
  Loading, 
  Download, 
  View 
} from '@element-plus/icons-vue'

const router = useRouter()

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

// 重置
const handleReset = () => {
  resetGenerateForm()
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.contract-generate {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.contract-generate :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.contract-generate :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.contract-generate :deep(.el-card__body) {
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
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: 2px;
  margin-right: 12px;
}

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

.contract-generate {
  animation: fadeInUp 0.6s ease forwards;
}
</style>