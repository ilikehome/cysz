<template>
  <div class="tenant-profile">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>租户画像</span>
          <el-button type="primary" @click="handleGenerate">
            <el-icon><DataAnalysis /></el-icon>
            生成画像
          </el-button>
        </div>
      </template>
      
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
          <el-form-item label="行业类型">
            <el-select
              v-model="searchForm.industry"
              placeholder="请选择行业类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="科技" value="科技" />
              <el-option label="贸易" value="贸易" />
              <el-option label="金融" value="金融" />
              <el-option label="服务" value="服务" />
            </el-select>
          </el-form-item>
          <el-form-item label="信用等级">
            <el-select
              v-model="searchForm.creditLevel"
              placeholder="请选择信用等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="AAA" value="AAA" />
              <el-option label="AA" value="AA" />
              <el-option label="A" value="A" />
              <el-option label="BBB" value="BBB" />
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
        <el-table-column prop="industry" label="行业类型" width="100" />
        <el-table-column prop="creditLevel" label="信用等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getCreditLevelType(row.creditLevel)">
              {{ row.creditLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="businessScale" label="企业规模" width="120" />
        <el-table-column prop="registeredCapital" label="注册资本(万元)" width="130" />
        <el-table-column prop="employeeCount" label="员工数量" width="100" />
        <el-table-column prop="profileScore" label="画像评分" width="100">
          <template #default="{ row }">
            <el-rate
              v-model="row.profileScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdate" label="更新时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleViewDetail(row)">详情</el-button>
            <el-button type="text" @click="handleRegenerate(row)">重新生成</el-button>
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
import { tenantApi } from '@/api/tenant'

// 响应式数据
const loading = ref(false)

// 搜索表单
const searchForm = reactive({
  keyword: '',
  industry: '',
  creditLevel: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 获取信用等级类型
const getCreditLevelType = (level: string) => {
  switch (level) {
    case 'AAA':
      return 'success'
    case 'AA':
      return 'success'
    case 'A':
      return 'warning'
    case 'BBB':
      return 'info'
    default:
      return 'danger'
  }
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    // 模拟画像数据
    const mockData = [
      {
        tenantCode: 'T001',
        tenantName: '北京科技有限公司',
        industry: '科技',
        creditLevel: 'AA',
        businessScale: '中型企业',
        registeredCapital: 5000,
        employeeCount: 150,
        profileScore: 4.2,
        lastUpdate: '2024-01-15 10:30:00'
      },
      {
        tenantCode: 'T002',
        tenantName: '上海贸易公司',
        industry: '贸易',
        creditLevel: 'A',
        businessScale: '小型企业',
        registeredCapital: 1000,
        employeeCount: 50,
        profileScore: 3.8,
        lastUpdate: '2024-01-12 14:20:00'
      },
      {
        tenantCode: 'T003',
        tenantName: '深圳金融服务',
        industry: '金融',
        creditLevel: 'AAA',
        businessScale: '大型企业',
        registeredCapital: 10000,
        employeeCount: 300,
        profileScore: 4.8,
        lastUpdate: '2024-01-10 16:45:00'
      },
      {
        tenantCode: 'T004',
        tenantName: '广州服务中心',
        industry: '服务',
        creditLevel: 'BBB',
        businessScale: '小型企业',
        registeredCapital: 500,
        employeeCount: 25,
        profileScore: 3.2,
        lastUpdate: '2024-01-08 09:15:00'
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
    if (searchForm.industry) {
      filteredData = filteredData.filter(item => item.industry === searchForm.industry)
    }
    if (searchForm.creditLevel) {
      filteredData = filteredData.filter(item => item.creditLevel === searchForm.creditLevel)
    }
    
    // 分页处理
    const start = (pagination.current - 1) * pagination.size
    const end = start + pagination.size
    tableData.value = filteredData.slice(start, end)
    pagination.total = filteredData.length
    
  } catch (error) {
    console.error('加载画像数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
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
  searchForm.industry = ''
  searchForm.creditLevel = ''
  pagination.current = 1
  loadData()
}

// 生成画像
const handleGenerate = () => {
  ElMessage.info('生成租户画像功能')
}

// 查看详情
const handleViewDetail = (row: any) => {
  ElMessage.info(`查看 ${row.tenantName} 的画像详情`)
}

// 重新生成
const handleRegenerate = (row: any) => {
  ElMessage.info(`重新生成 ${row.tenantName} 的画像`)
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
  loadData()
})
</script>

<style scoped>
.tenant-profile {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.tenant-profile :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.tenant-profile :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.tenant-profile :deep(.el-card__body) {
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
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
  border-radius: 2px;
  margin-right: 12px;
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

.tenant-profile {
  animation: fadeInUp 0.6s ease forwards;
}
</style>