<template>
  <div class="collection-overview">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card total-reminders">
          <div class="card-content">
            <div class="icon">
              <el-icon><Message /></el-icon>
            </div>
            <div class="info">
              <div class="value">{{ statistics.totalReminders }}</div>
              <div class="label">总催缴次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-rate">
          <div class="card-content">
            <div class="icon">
              <el-icon><SuccessFilled /></el-icon>
            </div>
            <div class="info">
              <div class="value">{{ statistics.successRate }}%</div>
              <div class="label">催缴成功率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card total-amount">
          <div class="card-content">
            <div class="icon">
              <el-icon><Money /></el-icon>
            </div>
            <div class="info">
              <div class="value">¥{{ formatAmount(statistics.totalAmount) }}</div>
              <div class="label">催缴总金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card collection-rate">
          <div class="card-content">
            <div class="icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="info">
              <div class="value">{{ statistics.collectionRate }}%</div>
              <div class="label">回收率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-section">
      <!-- 月度催缴趋势 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>月度催缴趋势</span>
          </template>
          <div ref="monthlyChartRef" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 催缴类型效果 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>催缴类型效果分析</span>
          </template>
          <div ref="typeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速操作 -->
    <el-row :gutter="20" class="quick-actions">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>快速操作</span>
          </template>
          <div class="action-buttons">
            <el-button type="primary" @click="handleManualReminder">
              <el-icon><Message /></el-icon>
              手动催缴
            </el-button>
            <el-button type="success" @click="handleBatchReminder">
              <el-icon><Promotion /></el-icon>
              批量催缴
            </el-button>
            <el-button type="warning" @click="handlePrioritySettings">
              <el-icon><Star /></el-icon>
              优先级设置
            </el-button>
            <el-button type="info" @click="handleExportRecords">
              <el-icon><Download /></el-icon>
              导出记录
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Message, SuccessFilled, Money, TrendCharts, Promotion, Star, Download } from '@element-plus/icons-vue'
import { collectionReminderApi } from '@/api/collectionReminder'
import { ElMessage } from 'element-plus'

// 图表引用
const monthlyChartRef = ref<HTMLElement>()
const typeChartRef = ref<HTMLElement>()

// 图表实例
let monthlyChart: echarts.ECharts | null = null
let typeChart: echarts.ECharts | null = null

// 统计数据
const statistics = ref({
  totalReminders: 0,
  successfulReminders: 0,
  successRate: 0,
  totalAmount: 0,
  collectedAmount: 0,
  collectionRate: 0,
  monthlyStats: [],
  typeStats: []
})

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const response = await collectionReminderApi.getStatistics()
    if (response.data.code === 200) {
      statistics.value = response.data.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 初始化月度趋势图
const initMonthlyChart = () => {
  if (!monthlyChartRef.value) return
  
  monthlyChart = echarts.init(monthlyChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['催缴次数', '成功次数']
    },
    xAxis: {
      type: 'category',
      data: statistics.value.monthlyStats.map((item: any) => item.month)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '催缴次数',
        type: 'bar',
        data: statistics.value.monthlyStats.map((item: any) => item.reminderCount),
        itemStyle: { color: '#409EFF' }
      },
      {
        name: '成功次数',
        type: 'bar',
        data: statistics.value.monthlyStats.map((item: any) => item.successCount),
        itemStyle: { color: '#67C23A' }
      }
    ]
  }
  
  monthlyChart.setOption(option)
}

// 初始化类型效果图
const initTypeChart = () => {
  if (!typeChartRef.value) return
  
  typeChart = echarts.init(typeChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '催缴类型',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: statistics.value.typeStats.map((item: any) => ({
          name: item.type,
          value: item.count
        }))
      }
    ]
  }
  
  typeChart.setOption(option)
}

// 格式化金额
const formatAmount = (amount: number) => {
  return (amount / 10000).toFixed(1) + '万'
}

// 快速操作处理函数
const handleManualReminder = () => {
  ElMessage.info('跳转到手动催缴页面')
}

const handleBatchReminder = () => {
  ElMessage.info('跳转到批量催缴页面')
}

const handlePrioritySettings = () => {
  ElMessage.info('跳转到优先级设置页面')
}

const handleExportRecords = () => {
  ElMessage.info('导出催缴记录')
}

// 窗口大小变化时重新调整图表
const handleResize = () => {
  monthlyChart?.resize()
  typeChart?.resize()
}

onMounted(async () => {
  await fetchStatistics()
  
  await nextTick()
  
  initMonthlyChart()
  initTypeChart()
  
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  monthlyChart?.dispose()
  typeChart?.dispose()
})
</script>

<style scoped>
.collection-overview {
  padding: 0;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-card .card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-card .icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 24px;
  color: white;
}

.total-reminders .icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.success-rate .icon {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
}

.total-amount .icon {
  background: linear-gradient(135deg, #E6A23C 0%, #f0a020 100%);
}

.collection-rate .icon {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
}

.stat-card .info .value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-card .info .label {
  font-size: 14px;
  color: #909399;
}

.chart-section {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

.quick-actions .action-buttons {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.quick-actions .el-button {
  flex: 1;
  min-width: 120px;
}
</style>