<template>
  <div class="business-analysis">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>业态分析</span>
          <el-button type="primary" @click="handleAnalyze">
            <el-icon><TrendCharts /></el-icon>
            生成分析
          </el-button>
        </div>
      </template>
      
      <!-- 业态概览 -->
      <div class="business-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="overview-card retail">
              <div class="overview-icon">
                <el-icon><Shop /></el-icon>
              </div>
              <div class="overview-content">
                <div class="overview-number">{{ businessStats.retail }}</div>
                <div class="overview-label">零售业态</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card catering">
              <div class="overview-icon">
                <el-icon><Food /></el-icon>
              </div>
              <div class="overview-content">
                <div class="overview-number">{{ businessStats.catering }}</div>
                <div class="overview-label">餐饮业态</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card entertainment">
              <div class="overview-icon">
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="overview-content">
                <div class="overview-number">{{ businessStats.entertainment }}</div>
                <div class="overview-label">娱乐业态</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-card service">
              <div class="overview-icon">
                <el-icon><Service /></el-icon>
              </div>
              <div class="overview-content">
                <div class="overview-number">{{ businessStats.service }}</div>
                <div class="overview-label">服务业态</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="chart-card">
              <div class="chart-title">业态分布</div>
              <div ref="pieChartRef" class="chart-container"></div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="chart-card">
              <div class="chart-title">收入趋势</div>
              <div ref="lineChartRef" class="chart-container"></div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="24">
            <div class="chart-card">
              <div class="chart-title">各业态经营指标对比</div>
              <div ref="barChartRef" class="chart-container" style="height: 300px;"></div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 搜索区域 -->
      <div class="search-section">
        <el-form :model="searchForm" inline>
          <el-form-item label="租户名称">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入租户名称"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          <el-form-item label="业态类型">
            <el-select
              v-model="searchForm.businessType"
              placeholder="请选择业态类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="零售" value="零售" />
              <el-option label="餐饮" value="餐饮" />
              <el-option label="娱乐" value="娱乐" />
              <el-option label="服务" value="服务" />
            </el-select>
          </el-form-item>
          <el-form-item label="经营状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择经营状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="正常" />
              <el-option label="预警" value="预警" />
              <el-option label="异常" value="异常" />
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
        <el-table-column prop="tenantCode" label="租户编码" width="120" />
        <el-table-column prop="tenantName" label="租户名称" min-width="150" />
        <el-table-column prop="businessType" label="业态类型" width="100" />
        <el-table-column prop="area" label="经营面积(㎡)" width="120" />
        <el-table-column prop="monthlyRevenue" label="月营业额(万元)" width="130" />
        <el-table-column prop="customerFlow" label="日客流量" width="100" />
        <el-table-column prop="profitMargin" label="利润率(%)" width="100">
          <template #default="{ row }">
            <span :class="getProfitMarginClass(row.profitMargin)">
              {{ row.profitMargin }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="经营状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastAnalysis" label="最近分析时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleViewDetail(row)">详情</el-button>
            <el-button type="text" @click="handleReanalyze(row)">重新分析</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-section">
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
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { tenantApi } from '@/api'
import * as echarts from 'echarts'

// 响应式数据
const loading = ref(false)

// 图表引用
const pieChartRef = ref<HTMLElement>()
const lineChartRef = ref<HTMLElement>()
const barChartRef = ref<HTMLElement>()

// 业态统计数据
const businessStats = reactive({
  retail: 45,
  catering: 32,
  entertainment: 18,
  service: 25
})

// 搜索表单
const searchForm = reactive({
  keyword: '',
  businessType: '',
  status: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 获取经营状态类型
const getStatusType = (status: string) => {
  switch (status) {
    case '正常':
      return 'success'
    case '预警':
      return 'warning'
    case '异常':
      return 'danger'
    default:
      return ''
  }
}

// 获取利润率样式类
const getProfitMarginClass = (margin: number) => {
  if (margin >= 20) return 'profit-high'
  if (margin >= 10) return 'profit-medium'
  return 'profit-low'
}

// 初始化饼图
const initPieChart = () => {
  if (!pieChartRef.value) return
  
  const chart = echarts.init(pieChartRef.value)
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
        name: '业态分布',
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
        data: [
          { value: businessStats.retail, name: '零售业态', itemStyle: { color: '#5470c6' } },
          { value: businessStats.catering, name: '餐饮业态', itemStyle: { color: '#91cc75' } },
          { value: businessStats.entertainment, name: '娱乐业态', itemStyle: { color: '#fac858' } },
          { value: businessStats.service, name: '服务业态', itemStyle: { color: '#ee6666' } }
        ]
      }
    ]
  }
  chart.setOption(option)
}

// 初始化折线图
const initLineChart = () => {
  if (!lineChartRef.value) return
  
  const chart = echarts.init(lineChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['零售', '餐饮', '娱乐', '服务']
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
      type: 'value'
    },
    series: [
      {
        name: '零售',
        type: 'line',
        stack: 'Total',
        data: [120, 132, 101, 134, 90, 230],
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '餐饮',
        type: 'line',
        stack: 'Total',
        data: [220, 182, 191, 234, 290, 330],
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '娱乐',
        type: 'line',
        stack: 'Total',
        data: [150, 232, 201, 154, 190, 330],
        itemStyle: { color: '#fac858' }
      },
      {
        name: '服务',
        type: 'line',
        stack: 'Total',
        data: [320, 332, 301, 334, 390, 330],
        itemStyle: { color: '#ee6666' }
      }
    ]
  }
  chart.setOption(option)
}

// 初始化柱状图
const initBarChart = () => {
  if (!barChartRef.value) return
  
  const chart = echarts.init(barChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['营业额', '客流量', '利润率']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: ['零售', '餐饮', '娱乐', '服务'],
        axisTick: {
          alignWithLabel: true
        }
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: '营业额(万元)',
        position: 'left'
      },
      {
        type: 'value',
        name: '利润率(%)',
        position: 'right'
      }
    ],
    series: [
      {
        name: '营业额',
        type: 'bar',
        data: [320, 280, 150, 200],
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '客流量',
        type: 'bar',
        data: [450, 380, 220, 300],
        itemStyle: { color: '#91cc75' }
      },
      {
        name: '利润率',
        type: 'line',
        yAxisIndex: 1,
        data: [15, 22, 18, 12],
        itemStyle: { color: '#ee6666' }
      }
    ]
  }
  chart.setOption(option)
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    // 模拟业态分析数据
    const mockData = [
      {
        tenantCode: 'T001',
        tenantName: '北京科技有限公司',
        businessType: '零售',
        area: 120,
        monthlyRevenue: 25.8,
        customerFlow: 180,
        profitMargin: 15.2,
        status: '正常',
        lastAnalysis: '2024-01-15 10:30:00'
      },
      {
        tenantCode: 'T002',
        tenantName: '上海贸易公司',
        businessType: '餐饮',
        area: 200,
        monthlyRevenue: 45.6,
        customerFlow: 220,
        profitMargin: 22.1,
        status: '正常',
        lastAnalysis: '2024-01-12 14:20:00'
      },
      {
        tenantCode: 'T003',
        tenantName: '深圳娱乐中心',
        businessType: '娱乐',
        area: 300,
        monthlyRevenue: 38.9,
        customerFlow: 150,
        profitMargin: 8.5,
        status: '预警',
        lastAnalysis: '2024-01-10 16:45:00'
      }
    ]
    
    // 过滤数据
    let filteredData = mockData
    if (searchForm.keyword) {
      filteredData = filteredData.filter(item => 
        item.tenantName.includes(searchForm.keyword) || 
        item.tenantCode.includes(searchForm.keyword)
      )
    }
    if (searchForm.businessType) {
      filteredData = filteredData.filter(item => item.businessType === searchForm.businessType)
    }
    if (searchForm.status) {
      filteredData = filteredData.filter(item => item.status === searchForm.status)
    }
    
    // 分页处理
    const start = (pagination.current - 1) * pagination.size
    const end = start + pagination.size
    tableData.value = filteredData.slice(start, end)
    pagination.total = filteredData.length
    
  } catch (error) {
    console.error('加载业态分析数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 加载业态统计
const loadBusinessStats = async () => {
  try {
    // 使用模拟数据，避免API调用失败
    businessStats.retail = 45
    businessStats.catering = 32
    businessStats.entertainment = 18
    businessStats.service = 25
    
    // 重新初始化图表
    nextTick(() => {
      initPieChart()
      initLineChart()
      initBarChart()
    })
  } catch (error) {
    console.error('加载业态统计失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.businessType = ''
  searchForm.status = ''
  pagination.current = 1
  loadData()
}

// 生成分析
const handleAnalyze = () => {
  ElMessage.info('生成业态分析功能')
}

// 查看详情
const handleViewDetail = (row: any) => {
  ElMessage.info(`查看 ${row.tenantName} 的业态分析详情`)
}

// 重新分析
const handleReanalyze = (row: any) => {
  ElMessage.info(`重新分析 ${row.tenantName} 的业态`)
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  loadData()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  pagination.current = current
  loadData()
}

onMounted(() => {
  loadBusinessStats()
  loadData()
  
  nextTick(() => {
    initPieChart()
    initLineChart()
    initBarChart()
  })
})
</script>

<style scoped>
.business-analysis {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.business-analysis :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.business-analysis :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.business-analysis :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: auto;
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

.business-overview {
  margin-bottom: 32px;
}

.overview-card {
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

.overview-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--card-color), var(--card-color-light));
}

.overview-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.overview-card.retail {
  --card-color: #5470c6;
  --card-color-light: #7b9ce6;
  background: linear-gradient(135deg, #f0f4ff 0%, #e6f0ff 100%);
}

.overview-card.catering {
  --card-color: #91cc75;
  --card-color-light: #b3d99b;
  background: linear-gradient(135deg, #f0fff4 0%, #e6ffe6 100%);
}

.overview-card.entertainment {
  --card-color: #fac858;
  --card-color-light: #fbd478;
  background: linear-gradient(135deg, #fffbf0 0%, #fff7e6 100%);
}

.overview-card.service {
  --card-color: #ee6666;
  --card-color-light: #f28888;
  background: linear-gradient(135deg, #fff0f0 0%, #ffe6e6 100%);
}

.overview-icon {
  font-size: 36px;
  margin-right: 20px;
  color: var(--card-color);
}

.overview-content {
  flex: 1;
}

.overview-number {
  font-size: 32px;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 8px;
  color: #1f2937;
}

.overview-label {
  font-size: 16px;
  color: #6b7280;
  font-weight: 500;
}

.charts-section {
  margin-bottom: 32px;
}

.chart-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #e2e8f0;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #e2e8f0;
}

.chart-container {
  height: 250px;
}

.search-section {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-table {
  flex: 1;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.el-table :deep(.el-table__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.el-table :deep(.el-table__header th) {
  background: transparent;
  color: #374151;
  font-weight: 600;
  border-bottom: 2px solid #e2e8f0;
  padding: 16px 12px;
}

.el-table :deep(.el-table__body tr) {
  transition: all 0.3s ease;
}

.el-table :deep(.el-table__body tr:hover) {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.el-table :deep(.el-table__body td) {
  padding: 16px 12px;
  border-bottom: 1px solid #f1f5f9;
}

.el-table :deep(.el-button--text) {
  font-weight: 500;
  border-radius: 6px;
  padding: 6px 12px;
  transition: all 0.3s ease;
}

.el-table :deep(.el-button--text:hover) {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  transform: translateY(-1px);
}

.profit-high {
  color: #10b981;
  font-weight: 600;
}

.profit-medium {
  color: #f59e0b;
  font-weight: 600;
}

.profit-low {
  color: #ef4444;
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

.business-analysis {
  animation: fadeInUp 0.6s ease forwards;
}
</style>