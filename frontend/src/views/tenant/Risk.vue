<template>
  <div class="tenant-risk">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>风险管控</span>
          <el-button type="primary" @click="handleAssess">
            <el-icon><Warning /></el-icon>
            风险评估
          </el-button>
        </div>
      </template>
      
      <!-- 风险统计概览 -->
      <div class="risk-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="risk-card high-risk">
              <div class="risk-icon">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="risk-content">
                <div class="risk-number">{{ riskStats.highRisk }}</div>
                <div class="risk-label">高风险</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="risk-card medium-risk">
              <div class="risk-icon">
                <el-icon><InfoFilled /></el-icon>
              </div>
              <div class="risk-content">
                <div class="risk-number">{{ riskStats.mediumRisk }}</div>
                <div class="risk-label">中风险</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="risk-card low-risk">
              <div class="risk-icon">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="risk-content">
                <div class="risk-number">{{ riskStats.lowRisk }}</div>
                <div class="risk-label">低风险</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="risk-card total">
              <div class="risk-icon">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="risk-content">
                <div class="risk-number">{{ riskStats.total }}</div>
                <div class="risk-label">总计</div>
              </div>
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
          <el-form-item label="风险等级">
            <el-select
              v-model="searchForm.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 150px;"
            >
              <el-option label="高风险" value="高风险" />
              <el-option label="中风险" value="中风险" />
              <el-option label="低风险" value="低风险" />
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
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getRiskLevelType(row.riskLevel)"
              effect="dark"
            >
              {{ row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险评分" width="100" />
        <el-table-column prop="riskTypes" label="风险类型" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="type in row.riskTypes"
              :key="type"
              size="small"
              style="margin-right: 5px;"
            >
              {{ type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastAssessment" label="最近评估时间" width="160" />
        <el-table-column prop="overdueDays" label="逾期天数" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleViewDetail(row)">详情</el-button>
            <el-button type="text" @click="handleReassess(row)">重新评估</el-button>
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
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { tenantApi } from '@/api'

// 响应式数据
const loading = ref(false)

// 风险统计数据
const riskStats = reactive({
  highRisk: 12,
  mediumRisk: 35,
  lowRisk: 128,
  total: 175
})

// 搜索表单
const searchForm = reactive({
  keyword: '',
  riskLevel: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 获取风险等级类型
const getRiskLevelType = (level: string) => {
  switch (level) {
    case '高风险':
      return 'danger'
    case '中风险':
      return 'warning'
    case '低风险':
      return 'success'
    default:
      return ''
  }
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    // 模拟风险数据
    const mockData = [
      {
        tenantCode: 'T001',
        tenantName: '北京科技有限公司',
        riskLevel: '高风险',
        riskScore: 85,
        riskTypes: ['信用风险', '财务风险'],
        lastAssessment: '2024-01-15 10:30:00',
        overdueDays: 15
      },
      {
        tenantCode: 'T002',
        tenantName: '上海贸易公司',
        riskLevel: '中风险',
        riskScore: 65,
        riskTypes: ['经营风险'],
        lastAssessment: '2024-01-12 14:20:00',
        overdueDays: 5
      },
      {
        tenantCode: 'T003',
        tenantName: '深圳服务公司',
        riskLevel: '低风险',
        riskScore: 35,
        riskTypes: ['合规风险'],
        lastAssessment: '2024-01-10 16:45:00',
        overdueDays: 0
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
    if (searchForm.riskLevel) {
      filteredData = filteredData.filter(item => item.riskLevel === searchForm.riskLevel)
    }
    
    // 分页处理
    const start = (pagination.current - 1) * pagination.size
    const end = start + pagination.size
    tableData.value = filteredData.slice(start, end)
    pagination.total = filteredData.length
    
  } catch (error) {
    console.error('加载风险数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 加载风险统计
const loadRiskStats = async () => {
  try {
    // 使用模拟数据，避免API调用失败
    riskStats.highRisk = 12
    riskStats.mediumRisk = 35
    riskStats.lowRisk = 128
    riskStats.total = 175
  } catch (error) {
    console.error('加载风险统计失败:', error)
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
  searchForm.riskLevel = ''
  pagination.current = 1
  loadData()
}

// 风险评估
const handleAssess = () => {
  ElMessage.info('风险评估功能')
}

// 查看详情
const handleViewDetail = (row: any) => {
  ElMessage.info(`查看 ${row.tenantName} 的风险详情`)
}

// 重新评估
const handleReassess = (row: any) => {
  ElMessage.info(`重新评估 ${row.tenantName} 的风险`)
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
  loadRiskStats()
  loadData()
})
</script>

<style scoped>
.tenant-risk {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.tenant-risk :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.tenant-risk :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.tenant-risk :deep(.el-card__body) {
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
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  border-radius: 2px;
  margin-right: 12px;
}

.risk-overview {
  margin-bottom: 32px;
}

.risk-card {
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

.risk-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--card-color), var(--card-color-light));
}

.risk-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.risk-card.high-risk {
  --card-color: #ef4444;
  --card-color-light: #f87171;
  background: linear-gradient(135deg, #fef2f2 0%, #fee2e2 100%);
}

.risk-card.medium-risk {
  --card-color: #f59e0b;
  --card-color-light: #fbbf24;
  background: linear-gradient(135deg, #fffbeb 0%, #fef3c7 100%);
}

.risk-card.low-risk {
  --card-color: #10b981;
  --card-color-light: #34d399;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
}

.risk-card.total {
  --card-color: #6366f1;
  --card-color-light: #818cf8;
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
}

.risk-icon {
  font-size: 36px;
  margin-right: 20px;
  color: var(--card-color);
}

.risk-content {
  flex: 1;
}

.risk-number {
  font-size: 32px;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 8px;
  color: #1f2937;
}

.risk-label {
  font-size: 16px;
  color: #6b7280;
  font-weight: 500;
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

.tenant-risk {
  animation: fadeInUp 0.6s ease forwards;
}
</style>