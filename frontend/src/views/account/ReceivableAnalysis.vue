<template>
  <div class="receivable-analysis">
    <PageContainer title="收款进度" description="可视化展示应收、已收、未收账款数据，助力管理决策">
      <!-- 数据概览 -->
      <el-row :gutter="20" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card total-receivable">
          <div class="card-content">
            <div class="icon">
              <el-icon><Money /></el-icon>
            </div>
            <div class="info">
              <div class="value">¥{{ formatAmount(overviewData.totalReceivable) }}</div>
              <div class="label">应收总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card received">
          <div class="card-content">
            <div class="icon">
              <el-icon><Check /></el-icon>
            </div>
            <div class="info">
              <div class="value">¥{{ formatAmount(overviewData.totalReceived) }}</div>
              <div class="label">已收金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card pending">
          <div class="card-content">
            <div class="icon">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="info">
              <div class="value">¥{{ formatAmount(overviewData.totalPending) }}</div>
              <div class="label">未收金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card collection-rate">
          <div class="card-content">
            <div class="icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="info">
              <div class="value">{{ overviewData.collectionRate }}%</div>
              <div class="label">收款率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-section">
      <!-- 收款情况饼图 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>收款情况分布</span>
              <el-date-picker
                v-model="dateRange"
                type="monthrange"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份"
                format="YYYY-MM"
                value-format="YYYY-MM"
                @change="handleDateChange"
                size="small"
              />
            </div>
          </template>
          <div ref="pieChartRef" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 收款趋势图 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>收款趋势分析</span>
          </template>
          <div ref="trendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 项目收款对比 -->
    <el-row :gutter="20" class="chart-section">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>项目收款对比</span>
              <el-select v-model="selectedProjects" multiple placeholder="选择项目" size="small" style="width: 300px" @change="handleProjectChange">
                <el-option
                  v-for="project in projectList"
                  :key="project.id"
                  :label="project.name"
                  :value="project.id"
                />
              </el-select>
            </div>
          </template>
          <div ref="projectChartRef" class="chart-container-large"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 逾期预警 -->
    <el-row :gutter="20" class="warning-section">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span class="warning-title">
              <el-icon><Warning /></el-icon>
              逾期账款预警
            </span>
          </template>
          <div class="warning-list">
            <div v-for="item in overdueList" :key="item.id" class="warning-item overdue">
              <div class="warning-info">
                <div class="tenant-name">{{ item.tenantName }}</div>
                <div class="contract-info">合同: {{ item.contractNo }}</div>
                <div class="amount">¥{{ formatAmount(item.amount) }}</div>
              </div>
              <div class="warning-status">
                <el-tag type="danger">逾期{{ item.overdueDays }}天</el-tag>
              </div>
            </div>
            <el-empty v-if="overdueList.length === 0" description="暂无逾期账款" :image-size="80" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <span class="warning-title">
              <el-icon><Bell /></el-icon>
              即将到期提醒
            </span>
          </template>
          <div class="warning-list">
            <div v-for="item in upcomingList" :key="item.id" class="warning-item upcoming">
              <div class="warning-info">
                <div class="tenant-name">{{ item.tenantName }}</div>
                <div class="contract-info">合同: {{ item.contractNo }}</div>
                <div class="amount">¥{{ formatAmount(item.amount) }}</div>
              </div>
              <div class="warning-status">
                <el-tag type="warning">{{ item.daysUntilDue }}天后到期</el-tag>
              </div>
            </div>
            <el-empty v-if="upcomingList.length === 0" description="暂无即将到期账款" :image-size="80" />
          </div>
        </el-card>
      </el-col>
    </el-row>
    </PageContainer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Money, Check, Clock, TrendCharts, Warning, Bell } from '@element-plus/icons-vue'
import { receivableAnalysisApi } from '@/api/receivableAnalysis'
import { ElMessage } from 'element-plus'
import PageContainer from '@/components/PageContainer.vue'

// 响应式数据
const dateRange = ref<[string, string]>([])
const selectedProjects = ref<number[]>([])
const pieChartRef = ref<HTMLElement>()
const trendChartRef = ref<HTMLElement>()
const projectChartRef = ref<HTMLElement>()

// 图表实例
let pieChart: echarts.ECharts | null = null
let trendChart: echarts.ECharts | null = null
let projectChart: echarts.ECharts | null = null

// 数据
const overviewData = ref({
  totalReceivable: 0,
  totalReceived: 0,
  totalPending: 0,
  collectionRate: 0
})

const projectList = ref<Array<{id: number, name: string}>>([])
const overdueList = ref<Array<any>>([])
const upcomingList = ref<Array<any>>([])

// API调用函数
const fetchOverviewData = async () => {
  try {
    const response = await receivableAnalysisApi.getOverview()
    if (response.data.code === 200) {
      overviewData.value = response.data.data
    }
  } catch (error) {
    console.error('获取概览数据失败:', error)
    ElMessage.error('获取概览数据失败')
  }
}

const fetchProjectList = async () => {
  try {
    const response = await receivableAnalysisApi.getProjects()
    if (response.data.code === 200) {
      projectList.value = response.data.data
    }
  } catch (error) {
    console.error('获取项目列表失败:', error)
  }
}

const fetchWarningData = async () => {
  try {
    const [overdueResponse, upcomingResponse] = await Promise.all([
      receivableAnalysisApi.getOverdueWarning(),
      receivableAnalysisApi.getUpcomingWarning()
    ])
    
    if (overdueResponse.data.code === 200) {
      overdueList.value = overdueResponse.data.data
    }
    
    if (upcomingResponse.data.code === 200) {
      upcomingList.value = upcomingResponse.data.data
    }
  } catch (error) {
    console.error('获取预警数据失败:', error)
  }
}

// 初始化饼图
const initPieChart = async () => {
  if (!pieChartRef.value) return
  
  try {
    const response = await receivableAnalysisApi.getPieChartData(dateRange.value[0], dateRange.value[1])
    if (response.data.code === 200) {
      pieChart = echarts.init(pieChartRef.value)
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '收款情况',
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
            data: response.data.data
          }
        ]
      }
      
      pieChart.setOption(option)
    }
  } catch (error) {
    console.error('初始化饼图失败:', error)
  }
}

// 初始化趋势图
const initTrendChart = async () => {
  if (!trendChartRef.value) return
  
  try {
    const response = await receivableAnalysisApi.getTrendChartData()
    if (response.data.code === 200) {
      const data = response.data.data
      
      trendChart = echarts.init(trendChartRef.value)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['应收金额', '已收金额']
        },
        xAxis: {
          type: 'category',
          data: data.months
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '应收金额',
            type: 'line',
            data: data.receivable,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '已收金额',
            type: 'line',
            data: data.received,
            itemStyle: { color: '#67C23A' }
          }
        ]
      }
      
      trendChart.setOption(option)
    }
  } catch (error) {
    console.error('初始化趋势图失败:', error)
  }
}

// 初始化项目对比图
const initProjectChart = async () => {
  if (!projectChartRef.value) return
  
  try {
    const response = await receivableAnalysisApi.getProjectChartData(selectedProjects.value)
    if (response.data.code === 200) {
      const data = response.data.data
      
      projectChart = echarts.init(projectChartRef.value)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['应收金额', '已收金额', '未收金额']
        },
        xAxis: {
          type: 'category',
          data: data.projects
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '应收金额',
            type: 'bar',
            data: data.receivable,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '已收金额',
            type: 'bar',
            data: data.received,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '未收金额',
            type: 'bar',
            data: data.pending,
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      
      projectChart.setOption(option)
    }
  } catch (error) {
    console.error('初始化项目对比图失败:', error)
  }
}

// 格式化金额
const formatAmount = (amount: number) => {
  return (amount / 10000).toFixed(1) + '万'
}

// 处理日期变化
const handleDateChange = (value: [string, string]) => {
  console.log('日期范围变化:', value)
  initPieChart() // 重新加载饼图数据
}

// 处理项目选择变化
const handleProjectChange = () => {
  initProjectChart() // 重新加载项目对比图数据
}

// 窗口大小变化时重新调整图表
const handleResize = () => {
  pieChart?.resize()
  trendChart?.resize()
  projectChart?.resize()
}

onMounted(async () => {
  // 设置默认日期范围为最近6个月
  const now = new Date()
  const endMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  const startMonth = `${now.getFullYear()}-${String(now.getMonth() - 5).padStart(2, '0')}`
  dateRange.value = [startMonth, endMonth] as [string, string]
  
  // 加载基础数据
  await Promise.all([
    fetchOverviewData(),
    fetchProjectList(),
    fetchWarningData()
  ])
  
  // 默认选择所有项目
  selectedProjects.value = projectList.value.map(p => p.id)
  
  await nextTick()
  
  // 初始化图表
  await Promise.all([
    initPieChart(),
    initTrendChart(),
    initProjectChart()
  ])
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
})

// 组件卸载时清理
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  pieChart?.dispose()
  trendChart?.dispose()
  projectChart?.dispose()
})
</script>

<style scoped>
.receivable-analysis {
  padding: 0;
}

.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  height: 120px;
}

.overview-card .card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.overview-card .icon {
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

.total-receivable .icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.received .icon {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
}

.pending .icon {
  background: linear-gradient(135deg, #E6A23C 0%, #f0a020 100%);
}

.collection-rate .icon {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
}

.overview-card .info .value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.overview-card .info .label {
  font-size: 14px;
  color: #909399;
}

.chart-section {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

.chart-container-large {
  height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.warning-section .warning-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.warning-list {
  max-height: 300px;
  overflow-y: auto;
}

.warning-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  border-radius: 8px;
  border: 1px solid #EBEEF5;
}

.warning-item.overdue {
  background-color: #fef0f0;
  border-color: #fbc4c4;
}

.warning-item.upcoming {
  background-color: #fdf6ec;
  border-color: #f5dab1;
}

.warning-info .tenant-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.warning-info .contract-info {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.warning-info .amount {
  font-size: 14px;
  color: #E6A23C;
  font-weight: 600;
}
</style>