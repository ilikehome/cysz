<template>
  <div class="operation-dashboard">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>运营仪表盘</span>
          <div class="header-actions">
            <el-select v-model="selectedPeriod" style="width: 120px; margin-right: 12px;">
              <el-option label="今日" value="today" />
              <el-option label="本周" value="week" />
              <el-option label="本月" value="month" />
              <el-option label="本季度" value="quarter" />
              <el-option label="本年度" value="year" />
            </el-select>
            <el-button type="primary" @click="handleExport">
              <el-icon><Download /></el-icon>
              导出报告
            </el-button>
            <el-button @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 核心指标概览 -->
      <div class="metrics-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="metric-card projects">
              <div class="metric-icon">
                <el-icon><OfficeBuilding /></el-icon>
              </div>
              <div class="metric-content">
                <div class="metric-number">{{ metrics.totalProjects }}</div>
                <div class="metric-label">项目总数</div>
                <div class="metric-change positive">+{{ metrics.projectGrowth }}%</div>
                <div class="metric-detail">较上期增长</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-card tenants">
              <div class="metric-icon">
                <el-icon><Avatar /></el-icon>
              </div>
              <div class="metric-content">
                <div class="metric-number">{{ metrics.totalTenants }}</div>
                <div class="metric-label">租户总数</div>
                <div class="metric-change positive">+{{ metrics.tenantGrowth }}%</div>
                <div class="metric-detail">新增租户{{ metrics.newTenants }}家</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-card revenue">
              <div class="metric-icon">
                <el-icon><Money /></el-icon>
              </div>
              <div class="metric-content">
                <div class="metric-number">{{ metrics.totalRevenue }}</div>
                <div class="metric-label">月收入(万元)</div>
                <div class="metric-change positive">+{{ metrics.revenueGrowth }}%</div>
                <div class="metric-detail">收缴率{{ metrics.collectionRate }}%</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-card occupancy">
              <div class="metric-icon">
                <el-icon><House /></el-icon>
              </div>
              <div class="metric-content">
                <div class="metric-number">{{ metrics.occupancyRate }}%</div>
                <div class="metric-label">出租率</div>
                <div class="metric-change" :class="metrics.occupancyChange > 0 ? 'positive' : 'negative'">
                  {{ metrics.occupancyChange > 0 ? '+' : '' }}{{ metrics.occupancyChange }}%
                </div>
                <div class="metric-detail">空置单元{{ metrics.vacantUnits }}个</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 快速操作区域 -->
      <div class="quick-actions">
        <div class="section-title">
          <span>快速操作</span>
        </div>
        <el-row :gutter="16">
          <el-col :span="4">
            <div class="action-card" @click="handleQuickAction('tenant')">
              <el-icon><UserFilled /></el-icon>
              <span>新增租户</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="action-card" @click="handleQuickAction('contract')">
              <el-icon><Document /></el-icon>
              <span>签署合同</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="action-card" @click="handleQuickAction('payment')">
              <el-icon><CreditCard /></el-icon>
              <span>收款登记</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="action-card" @click="handleQuickAction('maintenance')">
              <el-icon><Tools /></el-icon>
              <span>维修申报</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="action-card" @click="handleQuickAction('inspection')">
              <el-icon><View /></el-icon>
              <span>巡检记录</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="action-card" @click="handleQuickAction('report')">
              <el-icon><Printer /></el-icon>
              <span>生成报表</span>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 图表分析区域 -->
      <div class="charts-section">
        <el-row :gutter="20">
          <!-- 收入趋势图 -->
          <el-col :span="12">
            <div class="chart-container">
              <div class="chart-title">收入趋势分析</div>
              <div ref="revenueTrendChart" class="chart"></div>
            </div>
          </el-col>
          
          <!-- 租户类型分布 -->
          <el-col :span="12">
            <div class="chart-container">
              <div class="chart-title">租户类型分布</div>
              <div ref="tenantDistributionChart" class="chart"></div>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px;">
          <!-- 项目收入对比 -->
          <el-col :span="12">
            <div class="chart-container">
              <div class="chart-title">项目收入对比</div>
              <div ref="projectComparisonChart" class="chart"></div>
            </div>
          </el-col>
          
          <!-- 出租率统计 -->
          <el-col :span="12">
            <div class="chart-container">
              <div class="chart-title">出租率统计</div>
              <div ref="occupancyStatsChart" class="chart"></div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 重要提醒和待办事项 -->
      <div class="alerts-section">
        <el-row :gutter="20">
          <!-- 即将到期合同 -->
          <el-col :span="8">
            <div class="alert-card contracts">
              <div class="alert-header">
                <el-icon><Warning /></el-icon>
                <span>即将到期合同</span>
                <el-badge :value="expiringContracts.length" class="alert-badge" />
              </div>
              <div class="alert-content">
                <div v-for="contract in expiringContracts.slice(0, 5)" :key="contract.id" class="alert-item">
                  <div class="item-info">
                    <div class="item-name">{{ contract.tenantName }}</div>
                    <div class="item-detail">{{ contract.projectName }} - {{ contract.unitCode }}</div>
                  </div>
                  <div class="item-time">{{ contract.daysLeft }}天</div>
                </div>
                <div v-if="expiringContracts.length > 5" class="more-link" @click="handleViewMore('contracts')">
                  查看更多 ({{ expiringContracts.length - 5 }}+)
                </div>
              </div>
            </div>
          </el-col>
          
          <!-- 逾期账款 -->
          <el-col :span="8">
            <div class="alert-card overdue">
              <div class="alert-header">
                <el-icon><CircleClose /></el-icon>
                <span>逾期账款</span>
                <el-badge :value="overduePayments.length" class="alert-badge" />
              </div>
              <div class="alert-content">
                <div v-for="payment in overduePayments.slice(0, 5)" :key="payment.id" class="alert-item">
                  <div class="item-info">
                    <div class="item-name">{{ payment.tenantName }}</div>
                    <div class="item-detail">应收金额: ¥{{ payment.amount.toLocaleString() }}</div>
                  </div>
                  <div class="item-time">{{ payment.overdueDays }}天</div>
                </div>
                <div v-if="overduePayments.length > 5" class="more-link" @click="handleViewMore('payments')">
                  查看更多 ({{ overduePayments.length - 5 }}+)
                </div>
              </div>
            </div>
          </el-col>
          
          <!-- 长期空置单元 -->
          <el-col :span="8">
            <div class="alert-card vacant">
              <div class="alert-header">
                <el-icon><House /></el-icon>
                <span>长期空置单元</span>
                <el-badge :value="vacantUnits.length" class="alert-badge" />
              </div>
              <div class="alert-content">
                <div v-for="unit in vacantUnits.slice(0, 5)" :key="unit.id" class="alert-item">
                  <div class="item-info">
                    <div class="item-name">{{ unit.projectName }}</div>
                    <div class="item-detail">{{ unit.buildingName }} - {{ unit.unitCode }}</div>
                  </div>
                  <div class="item-time">{{ unit.vacantDays }}天</div>
                </div>
                <div v-if="vacantUnits.length > 5" class="more-link" @click="handleViewMore('vacant')">
                  查看更多 ({{ vacantUnits.length - 5 }}+)
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 运营洞察 -->
      <div class="insights-section">
        <div class="section-title">
          <span>运营洞察</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="insight-card performance">
              <div class="insight-header">
                <el-icon><TrendCharts /></el-icon>
                <span>业绩表现</span>
              </div>
              <div class="insight-content">
                <p>本月收入较上月增长 <strong>{{ insights.revenueGrowth }}%</strong>，超额完成目标</p>
                <p>出租率达到 <strong>{{ insights.occupancyRate }}%</strong>，高于行业平均水平</p>
                <p>新增租户 <strong>{{ insights.newTenants }}</strong> 家，续租率 <strong>{{ insights.renewalRate }}%</strong></p>
                <p>客户满意度评分 <strong>{{ insights.satisfaction }}</strong> 分</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="insight-card trends">
              <div class="insight-header">
                <el-icon><DataAnalysis /></el-icon>
                <span>市场趋势</span>
              </div>
              <div class="insight-content">
                <p>商业地产需求持续增长，预计下季度收入将增长 <strong>8-12%</strong></p>
                <p>科技类租户占比上升至 <strong>35%</strong>，成为主要增长动力</p>
                <p>平均租金水平稳中有升，同比增长 <strong>{{ insights.rentGrowth }}%</strong></p>
                <p>新兴业态如共享办公、直播基地需求旺盛</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="insight-card recommendations">
              <div class="insight-header">
                <el-icon><Compass /></el-icon>
                <span>优化建议</span>
              </div>
              <div class="insight-content">
                <p>建议重点关注空置率较高的项目，制定针对性招商策略</p>
                <p>可考虑在热门区域增加科技类租户的配套设施</p>
                <p>建议优化租金结构，提升整体收益水平</p>
                <p>加强客户关系管理，提高租户粘性和续租率</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { statisticsApi } from '@/api'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const selectedPeriod = ref('month')

// 核心指标数据
const metrics = reactive({
  totalProjects: 15,
  projectGrowth: 8.5,
  totalTenants: 328,
  tenantGrowth: 12.3,
  newTenants: 28,
  totalRevenue: 1250.8,
  revenueGrowth: 15.7,
  collectionRate: 95.2,
  occupancyRate: 88.5,
  occupancyChange: 2.3,
  vacantUnits: 45
})

// 即将到期合同
const expiringContracts = ref([
  { id: 1, tenantName: '北京科技有限公司', projectName: '科技园A座', unitCode: 'A-1001', daysLeft: 7 },
  { id: 2, tenantName: '上海贸易公司', projectName: '商业中心B栋', unitCode: 'B-2005', daysLeft: 15 },
  { id: 3, tenantName: '深圳制造企业', projectName: '产业园C区', unitCode: 'C-3008', daysLeft: 22 },
  { id: 4, tenantName: '广州服务公司', projectName: '写字楼D座', unitCode: 'D-1205', daysLeft: 28 },
  { id: 5, tenantName: '杭州创新企业', projectName: '创业大厦E栋', unitCode: 'E-0801', daysLeft: 30 }
])

// 逾期账款
const overduePayments = ref([
  { id: 1, tenantName: '某贸易公司', amount: 85000, overdueDays: 15 },
  { id: 2, tenantName: '某科技企业', amount: 120000, overdueDays: 8 },
  { id: 3, tenantName: '某服务机构', amount: 65000, overdueDays: 25 },
  { id: 4, tenantName: '某制造企业', amount: 95000, overdueDays: 12 }
])

// 长期空置单元
const vacantUnits = ref([
  { id: 1, projectName: '科技园A座', buildingName: 'A栋', unitCode: 'A-1205', vacantDays: 120 },
  { id: 2, projectName: '商业中心B栋', buildingName: 'B栋', unitCode: 'B-0808', vacantDays: 95 },
  { id: 3, projectName: '产业园C区', buildingName: 'C栋', unitCode: 'C-2201', vacantDays: 78 },
  { id: 4, projectName: '写字楼D座', buildingName: 'D栋', unitCode: 'D-1501', vacantDays: 65 }
])

// 运营洞察
const insights = reactive({
  revenueGrowth: 15.7,
  occupancyRate: 88.5,
  newTenants: 28,
  renewalRate: 85.2,
  satisfaction: 4.6,
  rentGrowth: 6.8
})

// 图表引用
const revenueTrendChart = ref()
const tenantDistributionChart = ref()
const projectComparisonChart = ref()
const occupancyStatsChart = ref()

// 初始化收入趋势图表
const initRevenueTrendChart = () => {
  const chart = echarts.init(revenueTrendChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['月收入', '累计收入', '目标收入']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: {
      type: 'value',
      name: '收入(万元)'
    },
    series: [
      {
        name: '月收入',
        type: 'line',
        data: [1080, 1150, 1220, 1180, 1250, 1250.8],
        smooth: true,
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '累计收入',
        type: 'line',
        data: [1080, 2230, 3450, 4630, 5880, 7130.8],
        smooth: true,
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '目标收入',
        type: 'line',
        data: [1000, 2100, 3200, 4300, 5400, 6500],
        smooth: true,
        lineStyle: { type: 'dashed' },
        itemStyle: { color: '#fac858' }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化租户分布图表
const initTenantDistributionChart = () => {
  const chart = echarts.init(tenantDistributionChart.value)
  
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
        name: '租户类型',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 115, name: '科技企业', itemStyle: { color: '#5470c6' } },
          { value: 88, name: '贸易公司', itemStyle: { color: '#91cc75' } },
          { value: 65, name: '金融机构', itemStyle: { color: '#fac858' } },
          { value: 42, name: '服务业', itemStyle: { color: '#ee6666' } },
          { value: 18, name: '其他', itemStyle: { color: '#73c0de' } }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化项目对比图表
const initProjectComparisonChart = () => {
  const chart = echarts.init(projectComparisonChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['科技园A座', '商业中心B栋', '创业大厦C座', '金融中心D座', '产业园E区']
    },
    yAxis: {
      type: 'value',
      name: '收入(万元)'
    },
    series: [
      {
        name: '月收入',
        type: 'bar',
        data: [280.5, 195.2, 220.8, 385.6, 168.7],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 初始化出租率统计图表
const initOccupancyStatsChart = () => {
  const chart = echarts.init(occupancyStatsChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['科技园A座', '商业中心B栋', '创业大厦C座', '金融中心D座', '产业园E区']
    },
    yAxis: {
      type: 'value',
      name: '出租率(%)',
      max: 100
    },
    series: [
      {
        name: '出租率',
        type: 'bar',
        data: [90, 92, 86, 90, 84],
        itemStyle: {
          color: function(params: any) {
            const value = params.value
            if (value >= 90) return '#67c23a'
            if (value >= 85) return '#e6a23c'
            return '#f56c6c'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 快速操作
const handleQuickAction = (action: string) => {
  const actionMap: Record<string, string> = {
    tenant: '/tenant/info',
    contract: '/contract',
    payment: '/receivable',
    maintenance: '/maintenance',
    inspection: '/inspection',
    report: '/reports'
  }
  
  if (actionMap[action]) {
    router.push(actionMap[action])
  } else {
    ElMessage.info(`${action} 功能开发中...`)
  }
}

// 查看更多
const handleViewMore = (type: string) => {
  const typeMap: Record<string, string> = {
    contracts: '/contract',
    payments: '/receivable',
    vacant: '/asset/unit'
  }
  
  if (typeMap[type]) {
    router.push(typeMap[type])
  }
}

// 导出报告
const handleExport = () => {
  ElMessage.success('运营报告导出中...')
}

// 刷新数据
const handleRefresh = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('数据刷新完成')
  }, 1000)
}

// 加载数据
const loadData = async () => {
  try {
    // 这里可以调用实际的API
    // const response = await statisticsApi.getDashboard()
    // 暂时使用模拟数据
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  loadData()
  nextTick(() => {
    initRevenueTrendChart()
    initTenantDistributionChart()
    initProjectComparisonChart()
    initOccupancyStatsChart()
  })
})
</script>

<style scoped>
.operation-dashboard {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.operation-dashboard :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.operation-dashboard :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.operation-dashboard :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.metrics-overview {
  margin-bottom: 32px;
}

.metric-card {
  display: flex;
  align-items: center;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.metric-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--card-color), var(--card-color-light));
}

.metric-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.metric-card.projects {
  --card-color: #8b5cf6;
  --card-color-light: #a78bfa;
  background: linear-gradient(135deg, #faf5ff 0%, #f3e8ff 100%);
}

.metric-card.tenants {
  --card-color: #06b6d4;
  --card-color-light: #67e8f9;
  background: linear-gradient(135deg, #f0fdfa 0%, #ccfbf1 100%);
}

.metric-card.revenue {
  --card-color: #10b981;
  --card-color-light: #34d399;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
}

.metric-card.occupancy {
  --card-color: #f59e0b;
  --card-color-light: #fbbf24;
  background: linear-gradient(135deg, #fffbeb 0%, #fef3c7 100%);
}

.metric-icon {
  font-size: 36px;
  margin-right: 20px;
  color: var(--card-color);
}

.metric-content {
  flex: 1;
}

.metric-number {
  font-size: 32px;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 8px;
  color: #1f2937;
}

.metric-label {
  font-size: 16px;
  color: #6b7280;
  font-weight: 500;
  margin-bottom: 4px;
}

.metric-change {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
}

.metric-change.positive {
  color: #10b981;
}

.metric-change.negative {
  color: #ef4444;
}

.metric-detail {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 400;
}

.quick-actions {
  margin-bottom: 32px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f1f5f9;
}

.section-title span {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.action-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  border-radius: 12px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border: 1px solid #e2e8f0;
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100px;
}

.action-card:hover {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.3);
}

.action-card .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.action-card span {
  font-size: 14px;
  font-weight: 500;
}

.charts-section {
  margin-bottom: 32px;
}

.chart-container {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f1f5f9;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f1f5f9;
}

.chart {
  height: 300px;
}

.alerts-section {
  margin-bottom: 32px;
}

.alert-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f1f5f9;
  height: 100%;
}

.alert-card.contracts {
  border-left: 4px solid #f59e0b;
}

.alert-card.overdue {
  border-left: 4px solid #ef4444;
}

.alert-card.vacant {
  border-left: 4px solid #6b7280;
}

.alert-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  position: relative;
}

.alert-header .el-icon {
  margin-right: 8px;
  font-size: 18px;
}

.alert-badge {
  margin-left: auto;
}

.alert-content {
  max-height: 200px;
  overflow-y: auto;
}

.alert-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f1f5f9;
}

.alert-item:last-child {
  border-bottom: none;
}

.item-info {
  flex: 1;
}

.item-name {
  font-weight: 500;
  color: #374151;
  margin-bottom: 4px;
}

.item-detail {
  font-size: 12px;
  color: #6b7280;
}

.item-time {
  font-size: 12px;
  font-weight: 600;
  color: #ef4444;
  background: #fef2f2;
  padding: 2px 8px;
  border-radius: 4px;
}

.more-link {
  text-align: center;
  padding: 8px;
  color: #3b82f6;
  font-size: 12px;
  cursor: pointer;
  border-top: 1px solid #f1f5f9;
  margin-top: 8px;
}

.more-link:hover {
  background: #f8fafc;
}

.insights-section {
  margin-top: 32px;
}

.insight-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f1f5f9;
  height: 100%;
  transition: all 0.3s ease;
}

.insight-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.insight-card.performance {
  border-left: 4px solid #10b981;
}

.insight-card.trends {
  border-left: 4px solid #3b82f6;
}

.insight-card.recommendations {
  border-left: 4px solid #8b5cf6;
}

.insight-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 600;
  color: #374151;
}

.insight-header .el-icon {
  margin-right: 8px;
  font-size: 18px;
}

.insight-content {
  color: #6b7280;
  line-height: 1.6;
}

.insight-content p {
  margin-bottom: 8px;
}

.insight-content strong {
  color: #374151;
  font-weight: 600;
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

.operation-dashboard {
  animation: fadeInUp 0.6s ease forwards;
}

.metrics-overview .el-col {
  animation: fadeInUp 0.6s ease forwards;
}

.metrics-overview .el-col:nth-child(1) { animation-delay: 0.1s; }
.metrics-overview .el-col:nth-child(2) { animation-delay: 0.2s; }
.metrics-overview .el-col:nth-child(3) { animation-delay: 0.3s; }
.metrics-overview .el-col:nth-child(4) { animation-delay: 0.4s; }

/* 响应式设计 */
@media (max-width: 768px) {
  .metrics-overview .el-col {
    margin-bottom: 16px;
  }
  
  .quick-actions .el-col {
    margin-bottom: 12px;
  }
  
  .charts-section .el-col {
    margin-bottom: 20px;
  }
  
  .chart {
    height: 250px;
  }
  
  .alerts-section .el-col {
    margin-bottom: 20px;
  }
  
  .insights-section .el-col {
    margin-bottom: 20px;
  }
}
</style>
