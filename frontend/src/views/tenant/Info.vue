<template>
  <div class="tenant-info">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>租户信息</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增租户
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-section">
        <el-form :model="searchForm" inline>
          <el-form-item label="租户名称">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入租户名称或编码"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          <el-form-item label="租户类别">
            <el-select
              v-model="searchForm.tenantCategory"
              placeholder="请选择租户类别"
              clearable
              style="width: 150px;"
            >
              <el-option label="企业" value="企业" />
              <el-option label="个体工商户" value="个体工商户" />
              <el-option label="个人" value="个人" />
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="tenantCode" label="租户编码" width="120" />
        <el-table-column prop="tenantName" label="租户名称" min-width="150" />
        <el-table-column prop="tenantCategory" label="租户类别" width="120" />
        <el-table-column prop="legalPerson" label="法定代表人" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="socialCreditCode" label="统一社会信用代码" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看</el-button>
            <el-button type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(row)" style="color: #f56c6c;">删除</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { tenantApi } from '@/api'

// 响应式数据
const loading = ref(false)

// 搜索表单
const searchForm = reactive({
  keyword: '',
  tenantCategory: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])
const selectedRows = ref([])

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      keyword: searchForm.keyword,
      tenantCategory: searchForm.tenantCategory
    }
    
    const response = await tenantApi.getTenantPage(params)
    if (response.data.code === 200) {
      tableData.value = response.data.data.records
      pagination.total = response.data.total
    }
  } catch (error) {
    console.error('加载租户数据失败:', error)
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
  searchForm.tenantCategory = ''
  pagination.current = 1
  loadData()
}

// 新增
const handleAdd = () => {
  ElMessage.info('新增租户功能')
}

// 编辑
const handleEdit = (row: any) => {
  ElMessage.info(`编辑租户: ${row.tenantName}`)
}

// 查看
const handleView = (row: any) => {
  ElMessage.info(`查看租户: ${row.tenantName}`)
}

// 删除
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除租户"${row.tenantName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await tenantApi.deleteTenant(row.id)
    if (response.data.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除租户失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
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
.tenant-info {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.tenant-info :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.tenant-info :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.tenant-info :deep(.el-card__body) {
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

.tenant-info {
  animation: fadeInUp 0.6s ease forwards;
}
</style>