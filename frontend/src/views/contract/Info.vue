<template>
  <div class="contract-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>合同档案</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建合同
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="合同编号">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入合同编号或名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="合同状态">
            <el-select
              v-model="searchForm.contractStatus"
              placeholder="请选择合同状态"
              clearable
              style="width: 150px"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="生效" value="ACTIVE" />
              <el-option label="过期" value="EXPIRED" />
              <el-option label="终止" value="TERMINATED" />
            </el-select>
          </el-form-item>
          <el-form-item label="合同类型">
            <el-select
              v-model="searchForm.contractType"
              placeholder="请选择合同类型"
              clearable
              style="width: 150px"
            >
              <el-option label="商铺" value="商铺" />
              <el-option label="办公" value="办公" />
              <el-option label="公寓" value="公寓" />
              <el-option label="酒店" value="酒店" />
              <el-option label="车位" value="车位" />
              <el-option label="广告" value="广告" />
              <el-option label="场地" value="场地" />
              <el-option label="多经点位" value="多经点位" />
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
        <!-- 基本信息 -->
        <el-table-column prop="contractNo" label="合同编号" width="150" fixed="left" />
        <el-table-column prop="contractName" label="合同名称" min-width="180" />
        <el-table-column prop="contractType" label="合同类型" width="100" />
        <el-table-column prop="projectName" label="所属项目" width="150" />
        <el-table-column prop="signatory" label="签订人" width="120" />
        <el-table-column prop="signatoryPhone" label="签订人手机号" width="130" />
        <el-table-column prop="tenantName" label="租户名称" width="150" />
        <el-table-column prop="businessBrand" label="经营品牌" width="120" />
        <el-table-column prop="shopSign" label="店招" width="120" />
        <el-table-column prop="businessFormat" label="业态" width="120" />
        <el-table-column prop="signDate" label="签订日期" width="120" />
        <el-table-column prop="startDate" label="开始时间" width="120" />
        <el-table-column prop="endDate" label="结束时间" width="120" />
        
        <!-- 签约资产 -->
        <el-table-column prop="buildingNames" label="楼栋" width="150" show-overflow-tooltip />
        <el-table-column prop="floorNames" label="楼层" width="150" show-overflow-tooltip />
        <el-table-column prop="unitNames" label="单元" width="150" show-overflow-tooltip />
        <el-table-column prop="buildingArea" label="建筑面积(㎡)" width="120">
          <template #default="{ row }">
            {{ row.buildingArea || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="rentableArea" label="计租面积(㎡)" width="120">
          <template #default="{ row }">
            {{ row.rentableArea || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="contractArea" label="签约面积(㎡)" width="120">
          <template #default="{ row }">
            {{ row.contractArea || '-' }}
          </template>
        </el-table-column>
        
        <!-- 保证金 -->
        <el-table-column prop="depositAmount" label="保证金含税金额(元)" width="150">
          <template #default="{ row }">
            {{ row.depositAmount ? `¥${row.depositAmount.toLocaleString()}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="depositLatestDate" label="保证金最晚收取日期" width="160" />
        
        <!-- 租金 -->
        <el-table-column prop="feeCompany" label="费项公司" width="120" />
        <el-table-column prop="rentMode" label="租金模式" width="100">
          <template #default="{ row }">
            <el-tag :type="row.rentMode === '固定租金' ? 'success' : 'warning'">
              {{ row.rentMode || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rentPeriodSetting" label="租金期间设定" width="150" />
        <el-table-column prop="lateFeeRule" label="滞纳金规则" width="120" />
        <el-table-column prop="paymentAccount" label="付款账户" width="150" show-overflow-tooltip />
        <el-table-column prop="paymentFrequency" label="付款频率" width="100" />
        <el-table-column prop="latestPaymentDay" label="最晚缴款日" width="120">
          <template #default="{ row }">
            {{ row.latestPaymentDay ? `${row.latestPaymentDay}日` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="firstPaymentLatestDate" label="首期最晚缴款日" width="150" />
        <el-table-column prop="firstPeriodRent" label="首期租金(元)" width="120">
          <template #default="{ row }">
            {{ row.firstPeriodRent ? `¥${row.firstPeriodRent.toLocaleString()}` : '-' }}
          </template>
        </el-table-column>
        
        <!-- 固定租金模式字段 -->
        <el-table-column prop="periodRent" label="每期租金(元)" width="120">
          <template #default="{ row }">
            {{ row.periodRent ? `¥${row.periodRent.toLocaleString()}` : '-' }}
          </template>
        </el-table-column>
        
        <!-- 提成租金模式字段 -->
        <el-table-column prop="commissionRules" label="提成规则" width="200">
          <template #default="{ row }">
            <div v-if="row.commissionRules && row.commissionRules.length > 0">
              <div
                v-for="(rule, index) in row.commissionRules"
                :key="index"
                class="commission-rule-display"
              >
                {{ rule.minAmount || 0 }}-{{ rule.maxAmount || '∞' }}万: {{ rule.commissionRate || 0 }}%
              </div>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        
        <!-- 状态和操作 -->
        <el-table-column prop="contractStatus" label="合同状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getContractStatusTag(row.contractStatus)">
              {{ getContractStatusName(row.contractStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看</el-button>
            <el-button type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(row)" style="color: #f56c6c">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
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
    
    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="合同详情"
      width="1200px"
    >
      <div v-if="viewData" class="contract-detail">
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="合同编号">{{ viewData.contractNo }}</el-descriptions-item>
          <el-descriptions-item label="合同名称">{{ viewData.contractName }}</el-descriptions-item>
          <el-descriptions-item label="合同类型">{{ viewData.contractType || '-' }}</el-descriptions-item>
          <el-descriptions-item label="所属项目">{{ viewData.projectName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="签订人">{{ viewData.signatory || '-' }}</el-descriptions-item>
          <el-descriptions-item label="签订人手机号">{{ viewData.signatoryPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="租户名称">{{ viewData.tenantName }}</el-descriptions-item>
          <el-descriptions-item label="经营品牌">{{ viewData.businessBrand || '-' }}</el-descriptions-item>
          <el-descriptions-item label="店招">{{ viewData.shopSign || '-' }}</el-descriptions-item>
          <el-descriptions-item label="业态">{{ viewData.businessFormat || '-' }}</el-descriptions-item>
          <el-descriptions-item label="签订日期">{{ viewData.signDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ viewData.startDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="结束时间">{{ viewData.endDate || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 签约资产 -->
        <el-divider content-position="left">签约资产</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="楼栋">{{ viewData.buildingNames || '-' }}</el-descriptions-item>
          <el-descriptions-item label="楼层">{{ viewData.floorNames || '-' }}</el-descriptions-item>
          <el-descriptions-item label="单元">{{ viewData.unitNames || '-' }}</el-descriptions-item>
          <el-descriptions-item label="建筑面积(㎡)">{{ viewData.buildingArea || '-' }}</el-descriptions-item>
          <el-descriptions-item label="计租面积(㎡)">{{ viewData.rentableArea || '-' }}</el-descriptions-item>
          <el-descriptions-item label="签约面积(㎡)">{{ viewData.contractArea || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 保证金 -->
        <el-divider content-position="left">保证金</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="含税金额(元)">
            {{ viewData.depositAmount ? `¥${viewData.depositAmount.toLocaleString()}` : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="最晚收取日期">{{ viewData.depositLatestDate || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 租金 -->
        <el-divider content-position="left">租金</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="费项公司">{{ viewData.feeCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="租金模式">
            <el-tag :type="viewData.rentMode === '固定租金' ? 'success' : 'warning'">
              {{ viewData.rentMode || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="租金期间设定">{{ viewData.rentPeriodSetting || '-' }}</el-descriptions-item>
          <el-descriptions-item label="滞纳金规则">{{ viewData.lateFeeRule || '-' }}</el-descriptions-item>
          <el-descriptions-item label="付款账户">{{ viewData.paymentAccount || '-' }}</el-descriptions-item>
          <el-descriptions-item label="付款频率">{{ viewData.paymentFrequency || '-' }}</el-descriptions-item>
          <el-descriptions-item label="最晚缴款日">
            {{ viewData.latestPaymentDay ? `${viewData.latestPaymentDay}日` : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="首期最晚缴款日">{{ viewData.firstPaymentLatestDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="首期租金(元)">
            {{ viewData.firstPeriodRent ? `¥${viewData.firstPeriodRent.toLocaleString()}` : '-' }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- 固定租金模式 -->
        <div v-if="viewData.rentMode === '固定租金'">
          <el-divider content-position="left">固定租金</el-divider>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="每期租金(元)">
              {{ viewData.periodRent ? `¥${viewData.periodRent.toLocaleString()}` : '-' }}
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 提成租金模式 -->
        <div v-if="viewData.rentMode === '提成租金'">
          <el-divider content-position="left">提成租金</el-divider>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="提成规则">
              <div v-if="viewData.commissionRules && viewData.commissionRules.length > 0">
                <div
                  v-for="(rule, index) in viewData.commissionRules"
                  :key="index"
                  class="commission-rule-view"
                >
                  <el-tag type="info" size="small">
                    营业额 {{ rule.minAmount || 0 }}万 - {{ rule.maxAmount || '∞' }}万，提成比例 {{ rule.commissionRate || 0 }}%
                  </el-tag>
                </div>
              </div>
              <span v-else>-</span>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 合同状态 -->
        <el-divider content-position="left">其他信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="合同状态">
            <el-tag :type="getContractStatusTag(viewData.contractStatus)">
              {{ getContractStatusName(viewData.contractStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ viewData.createTime || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
    
    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="1200px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input
                v-model="formData.contractNo"
                placeholder="请输入合同编号"
                :disabled="!!formData.id"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="formData.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同类型" prop="contractType">
              <el-select v-model="formData.contractType" placeholder="请选择合同类型" style="width: 100%">
                <el-option label="商铺" value="商铺" />
                <el-option label="办公" value="办公" />
                <el-option label="公寓" value="公寓" />
                <el-option label="酒店" value="酒店" />
                <el-option label="车位" value="车位" />
                <el-option label="广告" value="广告" />
                <el-option label="场地" value="场地" />
                <el-option label="多经点位" value="多经点位" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="所属项目" prop="projectId">
              <el-select v-model="formData.projectId" placeholder="请选择所属项目" style="width: 100%">
                <el-option
                  v-for="project in projectOptions"
                  :key="project.id"
                  :label="project.projectName"
                  :value="project.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签订人" prop="signatory">
              <el-input v-model="formData.signatory" placeholder="请输入签订人" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签订人手机号" prop="signatoryPhone">
              <el-input v-model="formData.signatoryPhone" placeholder="请输入签订人手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="租户名称" prop="tenantName">
              <el-input v-model="formData.tenantName" placeholder="请输入租户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经营品牌">
              <el-input v-model="formData.businessBrand" placeholder="请输入经营品牌" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="店招">
              <el-input v-model="formData.shopSign" placeholder="请输入店招" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="业态" prop="businessFormat">
              <el-select v-model="formData.businessFormat" placeholder="请选择业态" style="width: 100%">
                <!-- 零售业态 -->
                <el-option-group label="零售业态">
                  <el-option label="百货商场" value="百货商场" />
                  <el-option label="购物中心" value="购物中心" />
                  <el-option label="超市" value="超市" />
                  <el-option label="便利店" value="便利店" />
                  <el-option label="专卖店" value="专卖店" />
                  <el-option label="品牌店" value="品牌店" />
                  <el-option label="折扣店" value="折扣店" />
                  <el-option label="免税店" value="免税店" />
                </el-option-group>
                
                <!-- 餐饮业态 -->
                <el-option-group label="餐饮业态">
                  <el-option label="正餐" value="正餐" />
                  <el-option label="快餐" value="快餐" />
                  <el-option label="休闲餐饮" value="休闲餐饮" />
                  <el-option label="咖啡厅" value="咖啡厅" />
                  <el-option label="茶饮店" value="茶饮店" />
                  <el-option label="酒吧" value="酒吧" />
                  <el-option label="烘焙店" value="烘焙店" />
                  <el-option label="甜品店" value="甜品店" />
                </el-option-group>
                
                <!-- 娱乐业态 -->
                <el-option-group label="娱乐业态">
                  <el-option label="电影院" value="电影院" />
                  <el-option label="KTV" value="KTV" />
                  <el-option label="游戏厅" value="游戏厅" />
                  <el-option label="健身房" value="健身房" />
                  <el-option label="美容美发" value="美容美发" />
                  <el-option label="SPA" value="SPA" />
                  <el-option label="儿童乐园" value="儿童乐园" />
                  <el-option label="密室逃脱" value="密室逃脱" />
                </el-option-group>
                
                <!-- 服务业态 -->
                <el-option-group label="服务业态">
                  <el-option label="银行" value="银行" />
                  <el-option label="保险" value="保险" />
                  <el-option label="通讯营业厅" value="通讯营业厅" />
                  <el-option label="快递服务" value="快递服务" />
                  <el-option label="洗衣店" value="洗衣店" />
                  <el-option label="维修服务" value="维修服务" />
                  <el-option label="教育培训" value="教育培训" />
                  <el-option label="医疗诊所" value="医疗诊所" />
                </el-option-group>
                
                <!-- 办公业态 -->
                <el-option-group label="办公业态">
                  <el-option label="写字楼" value="写字楼" />
                  <el-option label="联合办公" value="联合办公" />
                  <el-option label="创业孵化器" value="创业孵化器" />
                  <el-option label="会议中心" value="会议中心" />
                  <el-option label="展示厅" value="展示厅" />
                </el-option-group>
                
                <!-- 住宿业态 -->
                <el-option-group label="住宿业态">
                  <el-option label="酒店" value="酒店" />
                  <el-option label="民宿" value="民宿" />
                  <el-option label="青年旅社" value="青年旅社" />
                  <el-option label="公寓式酒店" value="公寓式酒店" />
                </el-option-group>
                
                <!-- 其他业态 -->
                <el-option-group label="其他业态">
                  <el-option label="仓储物流" value="仓储物流" />
                  <el-option label="汽车服务" value="汽车服务" />
                  <el-option label="宠物服务" value="宠物服务" />
                  <el-option label="文化艺术" value="文化艺术" />
                  <el-option label="其他" value="其他" />
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签订日期" prop="signDate">
              <el-date-picker
                v-model="formData.signDate"
                type="date"
                placeholder="请选择签订日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="开始时间" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                type="date"
                placeholder="请选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="结束时间" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                type="date"
                placeholder="请选择结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 签约资产 -->
        <el-divider content-position="left">签约资产</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="楼栋" prop="buildingIds">
              <el-select
                v-model="formData.buildingIds"
                multiple
                placeholder="请选择楼栋"
                style="width: 100%"
                @change="handleBuildingChange"
              >
                <el-option
                  v-for="building in buildingOptions"
                  :key="building.id"
                  :label="building.buildingName"
                  :value="building.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="楼层" prop="floorIds">
              <el-select
                v-model="formData.floorIds"
                multiple
                placeholder="请选择楼层"
                style="width: 100%"
                @change="handleFloorChange"
              >
                <el-option
                  v-for="floor in floorOptions"
                  :key="floor.id"
                  :label="floor.floorName"
                  :value="floor.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单元" prop="unitIds">
              <el-select
                v-model="formData.unitIds"
                multiple
                placeholder="请选择单元"
                style="width: 100%"
                @change="handleUnitChange"
              >
                <el-option
                  v-for="unit in unitOptions"
                  :key="unit.id"
                  :label="unit.unitName"
                  :value="unit.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="建筑面积(㎡)">
              <el-input-number
                v-model="formData.buildingArea"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入建筑面积"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计租面积(㎡)">
              <el-input-number
                v-model="formData.rentableArea"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入计租面积"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签约面积(㎡)">
              <el-input-number
                v-model="formData.contractArea"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入签约面积"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 保证金 -->
        <el-divider content-position="left">保证金</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="含税金额(元)">
              <el-input-number
                v-model="formData.depositAmount"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入保证金含税金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最晚收取日期">
              <el-date-picker
                v-model="formData.depositLatestDate"
                type="date"
                placeholder="请选择最晚收取日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 租金 -->
        <el-divider content-position="left">租金</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="费项公司">
              <el-input v-model="formData.feeCompany" placeholder="请输入费项公司" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="租金模式" prop="rentMode">
              <el-select v-model="formData.rentMode" placeholder="请选择租金模式" style="width: 100%" @change="handleRentModeChange">
                <el-option label="固定租金" value="固定租金" />
                <el-option label="提成租金" value="提成租金" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="租金期间设定" prop="rentPeriodSetting">
              <el-select v-model="formData.rentPeriodSetting" placeholder="请选择租金期间设定" style="width: 100%">
                <el-option label="月末截止计租周期" value="月末截止计租周期" />
                <el-option label="起租日滚动周期" value="起租日滚动周期" />
                <el-option label="指定日期滚动周期" value="指定日期滚动周期" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="滞纳金规则">
              <el-input v-model="formData.lateFeeRule" placeholder="请输入滞纳金规则(‰/日)" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="付款账户">
              <el-input v-model="formData.paymentAccount" placeholder="请输入付款账户" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="付款频率" prop="paymentFrequency">
              <el-select v-model="formData.paymentFrequency" placeholder="请选择付款频率" style="width: 100%">
                <el-option label="月" value="月" />
                <el-option label="双月" value="双月" />
                <el-option label="季度" value="季度" />
                <el-option label="半年" value="半年" />
                <el-option label="年" value="年" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="最晚缴款日">
              <el-input-number
                v-model="formData.latestPaymentDay"
                :min="1"
                :max="31"
                style="width: 100%"
                placeholder="请输入最晚缴款日"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="首期最晚缴款日">
              <el-date-picker
                v-model="formData.firstPaymentLatestDate"
                type="date"
                placeholder="请选择首期最晚缴款日"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="首期租金(元)">
              <el-input-number
                v-model="formData.firstPeriodRent"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入首期租金"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 固定租金模式 -->
        <div v-if="formData.rentMode === '固定租金'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="每期租金(元)">
                <el-input-number
                  v-model="formData.periodRent"
                  :precision="2"
                  :min="0"
                  style="width: 100%"
                  placeholder="请输入每期租金"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 提成租金模式 -->
        <div v-if="formData.rentMode === '提成租金'">
          <el-form-item label="提成规则">
            <div class="commission-rules">
              <div
                v-for="(rule, index) in formData.commissionRules"
                :key="index"
                class="commission-rule-item"
              >
                <el-row :gutter="10">
                  <el-col :span="6">
                    <el-input-number
                      v-model="rule.minAmount"
                      :precision="2"
                      :min="0"
                      placeholder="最小营业额"
                      style="width: 100%"
                    />
                  </el-col>
                  <el-col :span="2" class="rule-separator">至</el-col>
                  <el-col :span="6">
                    <el-input-number
                      v-model="rule.maxAmount"
                      :precision="2"
                      :min="0"
                      placeholder="最大营业额"
                      style="width: 100%"
                    />
                  </el-col>
                  <el-col :span="2" class="rule-separator">提成</el-col>
                  <el-col :span="4">
                    <el-input-number
                      v-model="rule.commissionRate"
                      :precision="2"
                      :min="0"
                      :max="100"
                      placeholder="提成比例"
                      style="width: 100%"
                    />
                  </el-col>
                  <el-col :span="1" class="rule-separator">%</el-col>
                  <el-col :span="3">
                    <el-button
                      type="danger"
                      size="small"
                      @click="removeCommissionRule(index)"
                      :disabled="formData.commissionRules.length <= 1"
                    >
                      删除
                    </el-button>
                  </el-col>
                </el-row>
              </div>
              <el-button type="primary" size="small" @click="addCommissionRule">
                添加提成规则
              </el-button>
            </div>
          </el-form-item>
        </div>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { contractApi, projectApi, buildingApi, floorApi, unitApi, type Contract } from '@/api'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const viewData = ref<any>(null)

// 选项数据
const projectOptions = ref<any[]>([])
const buildingOptions = ref<any[]>([])
const floorOptions = ref<any[]>([])
const unitOptions = ref<any[]>([])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  contractStatus: '',
  contractType: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<Contract[]>([])

// 提成规则接口
interface CommissionRule {
  minAmount: number | null
  maxAmount: number | null
  commissionRate: number | null
}

// 表单数据
const formData = reactive({
  id: null as number | null,
  // 基本信息
  contractNo: '',
  contractName: '',
  contractType: '',
  projectId: null as number | null,
  signatory: '',
  signatoryPhone: '',
  tenantName: '',
  businessBrand: '',
  shopSign: '',
  businessFormat: '',
  signDate: '',
  startDate: '',
  endDate: '',
  
  // 签约资产
  buildingIds: [] as number[],
  floorIds: [] as number[],
  unitIds: [] as number[],
  buildingArea: null as number | null,
  rentableArea: null as number | null,
  contractArea: null as number | null,
  
  // 保证金
  depositAmount: null as number | null,
  depositLatestDate: '',
  
  // 租金
  feeCompany: '',
  rentMode: '',
  rentPeriodSetting: '',
  lateFeeRule: '',
  paymentAccount: '',
  paymentFrequency: '',
  latestPaymentDay: null as number | null,
  firstPaymentLatestDate: '',
  firstPeriodRent: null as number | null,
  
  // 固定租金
  periodRent: null as number | null,
  
  // 提成租金
  commissionRules: [
    { minAmount: null, maxAmount: null, commissionRate: null }
  ] as CommissionRule[],
  
  contractStatus: 'DRAFT'
})

// 表单验证规则
const formRules: FormRules = {
  contractNo: [
    { required: true, message: '请输入合同编号', trigger: 'blur' }
  ],
  contractName: [
    { required: true, message: '请输入合同名称', trigger: 'blur' }
  ],
  contractType: [
    { required: true, message: '请选择合同类型', trigger: 'change' }
  ],
  projectId: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ],
  signatory: [
    { required: true, message: '请输入签订人', trigger: 'blur' }
  ],
  signatoryPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  tenantName: [
    { required: true, message: '请输入租户名称', trigger: 'blur' }
  ],
  businessFormat: [
    { required: true, message: '请选择业态', trigger: 'change' }
  ],
  signDate: [
    { required: true, message: '请选择签订日期', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  buildingIds: [
    { required: true, message: '请选择楼栋', trigger: 'change' }
  ],
  unitIds: [
    { required: true, message: '请选择单元', trigger: 'change' }
  ],
  rentMode: [
    { required: true, message: '请选择租金模式', trigger: 'change' }
  ],
  rentPeriodSetting: [
    { required: true, message: '请选择租金期间设定', trigger: 'change' }
  ],
  paymentFrequency: [
    { required: true, message: '请选择付款频率', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建合同')

// 获取合同状态标签颜色
const getContractStatusTag = (status: string) => {
  const tagMap: Record<string, string> = {
    'DRAFT': 'info',
    'ACTIVE': 'success',
    'EXPIRED': 'warning',
    'TERMINATED': 'danger'
  }
  return tagMap[status] || 'info'
}

// 获取合同状态名称
const getContractStatusName = (status: string) => {
  const nameMap: Record<string, string> = {
    'DRAFT': '草稿',
    'ACTIVE': '生效',
    'EXPIRED': '过期',
    'TERMINATED': '终止'
  }
  return nameMap[status] || status
}

// 处理楼栋变化
const handleBuildingChange = (buildingIds: number[]) => {
  // 清空楼层和单元选择
  formData.floorIds = []
  formData.unitIds = []
  floorOptions.value = []
  unitOptions.value = []
  
  // 加载对应的楼层数据
  if (buildingIds.length > 0) {
    loadFloorsByBuildings(buildingIds)
  }
}

// 处理楼层变化
const handleFloorChange = (floorIds: number[]) => {
  // 清空单元选择
  formData.unitIds = []
  unitOptions.value = []
  
  // 加载对应的单元数据
  if (floorIds.length > 0) {
    loadUnitsByFloors(floorIds)
  }
}

// 处理单元变化
const handleUnitChange = (unitIds: number[]) => {
  // 自动计算面积
  calculateAreas(unitIds)
}

// 处理租金模式变化
const handleRentModeChange = (mode: string) => {
  if (mode === '提成租金') {
    // 初始化提成规则
    if (formData.commissionRules.length === 0) {
      formData.commissionRules = [
        { minAmount: null, maxAmount: null, commissionRate: null }
      ]
    }
  }
}

// 添加提成规则
const addCommissionRule = () => {
  formData.commissionRules.push({
    minAmount: null,
    maxAmount: null,
    commissionRate: null
  })
}

// 删除提成规则
const removeCommissionRule = (index: number) => {
  if (formData.commissionRules.length > 1) {
    formData.commissionRules.splice(index, 1)
  }
}

// 计算面积
const calculateAreas = async (unitIds: number[]) => {
  if (unitIds.length === 0) return
  
  try {
    // 这里应该调用API获取单元面积信息
    // const response = await unitApi.getUnitAreas(unitIds)
    // formData.buildingArea = response.data.totalBuildingArea
    // formData.rentableArea = response.data.totalRentableArea
    // formData.contractArea = response.data.totalContractArea
  } catch (error) {
    console.error('计算面积失败:', error)
  }
}

// 加载项目选项
const loadProjectOptions = async () => {
  try {
    const response = await projectApi.getProjectList()
    projectOptions.value = response.data
  } catch (error) {
    console.error('加载项目选项失败:', error)
  }
}

// 加载楼栋选项
const loadBuildingOptions = async () => {
  try {
    // 使用分页接口获取所有楼栋
    const response = await buildingApi.getBuildingPage({ current: 1, size: 1000 })
    buildingOptions.value = response.data.records
  } catch (error) {
    console.error('加载楼栋选项失败:', error)
  }
}

// 根据楼栋加载楼层
const loadFloorsByBuildings = async (buildingIds: number[]) => {
  try {
    // 为每个楼栋获取楼层，然后合并结果
    const allFloors: any[] = []
    for (const buildingId of buildingIds) {
      const response = await floorApi.getFloorsByBuilding(buildingId)
      allFloors.push(...response.data)
    }
    floorOptions.value = allFloors
  } catch (error) {
    console.error('加载楼层选项失败:', error)
  }
}

// 根据楼层加载单元
const loadUnitsByFloors = async (floorIds: number[]) => {
  try {
    // 使用分页接口获取单元，这里需要根据实际API调整
    const response = await unitApi.getUnitPage({ current: 1, size: 1000 })
    // 过滤出指定楼层的单元
    const filteredUnits = response.data.records.filter((unit: any) => 
      floorIds.includes(unit.floorId)
    )
    unitOptions.value = filteredUnits
  } catch (error) {
    console.error('加载单元选项失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.contractStatus = ''
  searchForm.contractType = ''
  handleSearch()
}

// 新建
const handleAdd = () => {
  dialogTitle.value = '新建合同'
  resetForm()
  dialogVisible.value = true
}

// 查看
const handleView = (row: any) => {
  viewData.value = { ...row }
  viewDialogVisible.value = true
}

// 编辑
const handleEdit = (row: Contract) => {
  dialogTitle.value = '编辑合同'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row: Contract) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除合同"${row.contractName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await contractApi.deleteContract(row.id!)
    ElMessage.success('删除成功')
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  loadData()
}

// 当前页变化
const handleCurrentChange = (current: number) => {
  pagination.current = current
  loadData()
}

// 对话框关闭
const handleDialogClose = () => {
  resetForm()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const contractData: any = {
      ...formData,
      // 处理日期格式
      signDate: formData.signDate || undefined,
      startDate: formData.startDate || undefined,
      endDate: formData.endDate || undefined,
      depositLatestDate: formData.depositLatestDate || undefined,
      firstPaymentLatestDate: formData.firstPaymentLatestDate || undefined
    }
    
    if (formData.id) {
      await contractApi.updateContract(formData.id, contractData)
      ElMessage.success('更新成功')
    } else {
      await contractApi.createContract(contractData)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: null,
    // 基本信息
    contractNo: '',
    contractName: '',
    contractType: '',
    projectId: null,
    signatory: '',
    signatoryPhone: '',
    tenantName: '',
    businessBrand: '',
    shopSign: '',
    businessFormat: '',
    signDate: '',
    startDate: '',
    endDate: '',
    
    // 签约资产
    buildingIds: [],
    floorIds: [],
    unitIds: [],
    buildingArea: null,
    rentableArea: null,
    contractArea: null,
    
    // 保证金
    depositAmount: null,
    depositLatestDate: '',
    
    // 租金
    feeCompany: '',
    rentMode: '',
    rentPeriodSetting: '',
    lateFeeRule: '',
    paymentAccount: '',
    paymentFrequency: '',
    latestPaymentDay: null,
    firstPaymentLatestDate: '',
    firstPeriodRent: null,
    
    // 固定租金
    periodRent: null,
    
    // 提成租金
    commissionRules: [
      { minAmount: null, maxAmount: null, commissionRate: null }
    ],
    
    contractStatus: 'DRAFT'
  })
  formRef.value?.resetFields()
}

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    
    const params = {
      current: pagination.current,
      size: pagination.size,
      keyword: searchForm.keyword || undefined,
      contractStatus: searchForm.contractStatus || undefined,
      contractType: searchForm.contractType || undefined
    }
    
    const response = await contractApi.getContractPage(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error: any) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
  loadProjectOptions()
  loadBuildingOptions()
})
</script>

<style scoped>
.contract-management {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
  position: relative;
}

.contract-management :deep(.el-card) {
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

.contract-management :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.contract-management :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  padding: 20px 24px;
}

.contract-management :deep(.el-card__body) {
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

.card-header :deep(.el-button) {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.2);
}

.card-header :deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(16, 185, 129, 0.3);
}

.search-area {
  margin-bottom: 24px;
  padding: 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 12px;
  flex-shrink: 0;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.search-area :deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

.search-area :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.search-area :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.search-area :deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

.search-area :deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.search-area :deep(.el-button--primary) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.search-area :deep(.el-button--primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}

.el-table {
  flex: 1;
  overflow: auto;
  border-radius: 12px;
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
  transform: scale(1.01);
}

.el-table :deep(.el-table__body td) {
  padding: 16px 12px;
  border-bottom: 1px solid #f1f5f9;
}

.el-table :deep(.el-tag) {
  border-radius: 6px;
  font-weight: 500;
  padding: 4px 12px;
  border: none;
}

.el-table :deep(.el-tag--info) {
  background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
  color: white;
}

.el-table :deep(.el-tag--success) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

.el-table :deep(.el-tag--warning) {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
}

.el-table :deep(.el-tag--danger) {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
}

.el-table :deep(.el-button--text) {
  font-weight: 500;
  border-radius: 6px;
  padding: 6px 12px;
  transition: all 0.3s ease;
}

.el-table :deep(.el-button--text:hover) {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
  transform: translateY(-1px);
}

.pagination {
  margin-top: 24px;
  text-align: right;
  flex-shrink: 0;
}

.pagination :deep(.el-pagination) {
  justify-content: flex-end;
}

.pagination :deep(.el-pager li) {
  border-radius: 8px;
  margin: 0 2px;
  transition: all 0.3s ease;
}

.pagination :deep(.el-pager li:hover) {
  transform: translateY(-1px);
}

.pagination :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.pagination :deep(.btn-prev),
.pagination :deep(.btn-next) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.pagination :deep(.btn-prev:hover),
.pagination :deep(.btn-next:hover) {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 对话框样式 */
:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  padding: 20px 24px;
  border-bottom: 1px solid #e2e8f0;
}

:deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

:deep(.el-dialog__body) {
  padding: 24px;
  max-height: 70vh;
  overflow-y: auto;
}

:deep(.el-divider) {
  margin: 24px 0 16px 0;
}

:deep(.el-divider__text) {
  font-weight: 600;
  color: #374151;
  background: #f8fafc;
  padding: 0 16px;
}

/* 表单样式 */
:deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

/* 提成规则样式 */
.commission-rules {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  background: #f8fafc;
}

.commission-rule-item {
  margin-bottom: 12px;
}

.commission-rule-item:last-child {
  margin-bottom: 16px;
}

.rule-separator {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  color: #6b7280;
}

/* 提成规则显示样式 */
.commission-rule-display {
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 2px;
  padding: 2px 6px;
  background: #f3f4f6;
  border-radius: 4px;
  display: inline-block;
  margin-right: 4px;
}

.commission-rule-display:last-child {
  margin-bottom: 0;
}

/* 查看详情中的提成规则样式 */
.commission-rule-view {
  margin-bottom: 8px;
}

.commission-rule-view:last-child {
  margin-bottom: 0;
}

/* 合同详情样式 */
.contract-detail :deep(.el-descriptions) {
  margin-bottom: 24px;
}

.contract-detail :deep(.el-descriptions__label) {
  font-weight: 600;
  color: #374151;
  background: #f8fafc;
}

.contract-detail :deep(.el-descriptions__content) {
  color: #6b7280;
}

/* 添加加载动画 */
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

.contract-management {
  animation: fadeInUp 0.6s ease forwards;
}
</style>
