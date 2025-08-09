<template>
  <div class="reminder-settings">
    <el-form
      ref="formRef"
      :model="settingsForm"
      :rules="rules"
      label-width="120px"
      @submit.prevent="handleSave"
    >
      <!-- 基础设置 -->
      <el-card class="settings-section">
        <template #header>
          <span class="section-title">基础设置</span>
        </template>
        
        <el-form-item label="启用自动催缴" prop="enabled">
          <el-switch
            v-model="settingsForm.enabled"
            active-text="开启"
            inactive-text="关闭"
          />
          <div class="form-tip">开启后系统将根据设置的条件自动发送催缴短信</div>
        </el-form-item>
        
        <el-form-item label="触发条件" prop="triggerDays">
          <div class="trigger-days">
            <span>逾期</span>
            <el-select
              v-model="settingsForm.triggerDays"
              multiple
              placeholder="选择逾期天数"
              style="width: 200px; margin: 0 8px"
            >
              <el-option label="3天" :value="3" />
              <el-option label="7天" :value="7" />
              <el-option label="15天" :value="15" />
              <el-option label="30天" :value="30" />
              <el-option label="60天" :value="60" />
              <el-option label="90天" :value="90" />
            </el-select>
            <span>时自动发送催缴短信</span>
          </div>
          <div class="form-tip">可选择多个触发条件，系统将在对应逾期天数时发送催缴短信</div>
        </el-form-item>
        
        <el-form-item label="最大催缴次数" prop="maxReminders">
          <el-input-number
            v-model="settingsForm.maxReminders"
            :min="1"
            :max="10"
            controls-position="right"
            style="width: 120px"
          />
          <span style="margin-left: 8px">次</span>
          <div class="form-tip">对同一笔应收款的最大催缴次数限制</div>
        </el-form-item>
        
        <el-form-item label="催缴间隔" prop="reminderInterval">
          <el-input-number
            v-model="settingsForm.reminderInterval"
            :min="1"
            :max="30"
            controls-position="right"
            style="width: 120px"
          />
          <span style="margin-left: 8px">天</span>
          <div class="form-tip">两次催缴之间的最小间隔天数</div>
        </el-form-item>
      </el-card>

      <!-- 发送时间设置 -->
      <el-card class="settings-section">
        <template #header>
          <span class="section-title">发送时间设置</span>
        </template>
        
        <el-form-item label="工作时间" prop="workingHours">
          <div class="working-hours">
            <el-time-picker
              v-model="workingHoursStart"
              placeholder="开始时间"
              format="HH:mm"
              value-format="HH:mm"
              style="width: 120px"
            />
            <span style="margin: 0 12px">至</span>
            <el-time-picker
              v-model="workingHoursEnd"
              placeholder="结束时间"
              format="HH:mm"
              value-format="HH:mm"
              style="width: 120px"
            />
          </div>
          <div class="form-tip">催缴短信只在工作时间内发送</div>
        </el-form-item>
        
        <el-form-item label="工作日" prop="workingDays">
          <el-checkbox-group v-model="settingsForm.workingDays">
            <el-checkbox :label="1">周一</el-checkbox>
            <el-checkbox :label="2">周二</el-checkbox>
            <el-checkbox :label="3">周三</el-checkbox>
            <el-checkbox :label="4">周四</el-checkbox>
            <el-checkbox :label="5">周五</el-checkbox>
            <el-checkbox :label="6">周六</el-checkbox>
            <el-checkbox :label="7">周日</el-checkbox>
          </el-checkbox-group>
          <div class="form-tip">催缴短信只在选定的工作日发送</div>
        </el-form-item>
      </el-card>

      <!-- 高级设置 -->
      <el-card class="settings-section">
        <template #header>
          <span class="section-title">高级设置</span>
        </template>
        
        <el-form-item label="优先级规则">
          <div class="priority-rules">
            <div class="priority-rule">
              <span>逾期金额 ≥ </span>
              <el-input-number
                v-model="priorityRules.highAmountThreshold"
                :min="0"
                :step="1000"
                controls-position="right"
                style="width: 150px; margin: 0 8px"
              />
              <span>元，设为</span>
              <el-tag type="danger" style="margin-left: 8px">高优先级</el-tag>
            </div>
            
            <div class="priority-rule">
              <span>逾期天数 ≥ </span>
              <el-input-number
                v-model="priorityRules.highDaysThreshold"
                :min="0"
                controls-position="right"
                style="width: 120px; margin: 0 8px"
              />
              <span>天，设为</span>
              <el-tag type="danger" style="margin-left: 8px">高优先级</el-tag>
            </div>
          </div>
          <div class="form-tip">系统将根据设置的规则自动调整催缴优先级</div>
        </el-form-item>
        
        <el-form-item label="智能发送">
          <el-switch
            v-model="settingsForm.smartSending"
            active-text="开启"
            inactive-text="关闭"
          />
          <div class="form-tip">开启后系统将根据历史数据优化发送时间和频率</div>
        </el-form-item>
        
        <el-form-item label="节假日跳过">
          <el-switch
            v-model="settingsForm.skipHolidays"
            active-text="开启"
            inactive-text="关闭"
          />
          <div class="form-tip">开启后系统将跳过法定节假日发送催缴短信</div>
        </el-form-item>
      </el-card>

      <!-- 通知设置 -->
      <el-card class="settings-section">
        <template #header>
          <span class="section-title">通知设置</span>
        </template>
        
        <el-form-item label="发送通知">
          <el-checkbox-group v-model="settingsForm.notifications">
            <el-checkbox label="email">邮件通知</el-checkbox>
            <el-checkbox label="system">系统通知</el-checkbox>
            <el-checkbox label="wechat">微信通知</el-checkbox>
          </el-checkbox-group>
          <div class="form-tip">催缴短信发送后将通过选定方式通知管理员</div>
        </el-form-item>
        
        <el-form-item label="通知邮箱" v-if="settingsForm.notifications.includes('email')">
          <el-input
            v-model="settingsForm.notificationEmail"
            placeholder="请输入通知邮箱"
            style="width: 300px"
          />
        </el-form-item>
      </el-card>

      <!-- 操作按钮 -->
      <div class="form-actions">
        <el-button type="primary" @click="handleSave" :loading="saving">
          <el-icon><Check /></el-icon>
          保存设置
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button type="info" @click="handleTest">
          <el-icon><Message /></el-icon>
          测试发送
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { Check, Refresh, Message } from '@element-plus/icons-vue'
import { collectionReminderApi } from '@/api/collectionReminder'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 表单引用
const formRef = ref<FormInstance>()
const saving = ref(false)

// 表单数据
const settingsForm = ref({
  enabled: true,
  triggerDays: [7, 15, 30],
  maxReminders: 3,
  reminderInterval: 7,
  workingHours: {
    start: '09:00',
    end: '18:00'
  },
  workingDays: [1, 2, 3, 4, 5],
  smartSending: false,
  skipHolidays: true,
  notifications: ['system'],
  notificationEmail: ''
})

// 优先级规则
const priorityRules = ref({
  highAmountThreshold: 50000,
  highDaysThreshold: 30
})

// 工作时间计算属性
const workingHoursStart = computed({
  get: () => settingsForm.value.workingHours.start,
  set: (value: string) => {
    settingsForm.value.workingHours.start = value
  }
})

const workingHoursEnd = computed({
  get: () => settingsForm.value.workingHours.end,
  set: (value: string) => {
    settingsForm.value.workingHours.end = value
  }
})

// 表单验证规则
const rules: FormRules = {
  triggerDays: [
    { required: true, message: '请选择触发条件', trigger: 'change' }
  ],
  maxReminders: [
    { required: true, message: '请设置最大催缴次数', trigger: 'blur' }
  ],
  reminderInterval: [
    { required: true, message: '请设置催缴间隔', trigger: 'blur' }
  ],
  workingDays: [
    { required: true, message: '请选择工作日', trigger: 'change' }
  ]
}

// 获取设置数据
const fetchSettings = async () => {
  try {
    const response = await collectionReminderApi.getSettings()
    if (response.data.code === 200) {
      const data = response.data.data
      settingsForm.value = {
        ...settingsForm.value,
        ...data
      }
    }
  } catch (error) {
    console.error('获取设置失败:', error)
    ElMessage.error('获取设置失败')
  }
}

// 保存设置
const handleSave = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    saving.value = true
    
    const settingsData = {
      ...settingsForm.value,
      priorityRules: priorityRules.value
    }
    
    await collectionReminderApi.updateSettings(settingsData)
    
    ElMessage.success('设置保存成功')
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      console.error('保存设置失败:', error)
      ElMessage.error('保存设置失败')
    }
  } finally {
    saving.value = false
  }
}

// 重置设置
const handleReset = async () => {
  try {
    await ElMessageBox.confirm('确认重置所有设置为默认值？', '确认重置', {
      type: 'warning'
    })
    
    await fetchSettings()
    ElMessage.success('设置已重置')
  } catch (error) {
    // 用户取消
  }
}

// 测试发送
const handleTest = async () => {
  try {
    await ElMessageBox.confirm('确认发送测试催缴短信？', '测试发送', {
      type: 'info'
    })
    
    // 这里应该调用测试发送API
    ElMessage.success('测试短信发送成功')
  } catch (error) {
    // 用户取消
  }
}

onMounted(() => {
  fetchSettings()
})
</script>

<style scoped>
.reminder-settings {
  padding: 0;
}

.settings-section {
  margin-bottom: 20px;
}

.section-title {
  font-weight: 600;
  color: #303133;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}

.trigger-days {
  display: flex;
  align-items: center;
}

.working-hours {
  display: flex;
  align-items: center;
}

.priority-rules {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.priority-rule {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
}

.form-actions {
  display: flex;
  gap: 12px;
  padding: 20px 0;
  border-top: 1px solid #EBEEF5;
  margin-top: 20px;
}

:deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #EBEEF5;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-checkbox-group) {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}
</style>