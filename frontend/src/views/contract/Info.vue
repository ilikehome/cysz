<template>
  <div class="contract-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>合同档案</span>
          <div class="header-buttons">
            <el-button type="success" @click="handleUploadContract">
              <el-icon><Upload /></el-icon>
              上传盖章合同
            </el-button>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新建合同
            </el-button>
          </div>
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
              <el-option label="未盖章生效" value="UNSIGNED_EFFECTIVE" />
              <el-option label="已盖章生效" value="SIGNED_EFFECTIVE" />
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
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">查看</el-button>
            <el-button type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" @click="handleDownloadPDF(row)">
              <el-icon><Download /></el-icon>
              下载PDF
            </el-button>
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
    
    <!-- 上传盖章合同对话框 -->
    <el-dialog
      v-model="uploadDialogVisible"
      title="上传盖章合同"
      width="800px"
      @close="handleUploadDialogClose"
    >
      <el-form
        ref="uploadFormRef"
        :model="uploadFormData"
        :rules="uploadFormRules"
        label-width="120px"
      >
        <el-form-item label="上传合同文件" prop="contractFile">
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :limit="1"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            accept=".pdf,.doc,.docx"
            drag
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                只能上传 PDF/DOC/DOCX 文件，且不超过 10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="是否关联合同">
          <el-radio-group v-model="uploadFormData.linkType" @change="handleLinkTypeChange">
            <el-radio label="new">新建合同</el-radio>
            <el-radio label="existing">关联已有合同</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <!-- 关联已有合同 -->
        <div v-if="uploadFormData.linkType === 'existing'">
          <el-form-item label="选择合同" prop="existingContractId">
            <el-select
              v-model="uploadFormData.existingContractId"
              placeholder="请选择要关联的合同"
              style="width: 100%"
              filterable
            >
              <el-option
                v-for="contract in unsignedContracts"
                :key="contract.id"
                :label="`${contract.contractNo} - ${contract.contractName}`"
                :value="contract.id"
              />
            </el-select>
          </el-form-item>
        </div>
        
        <!-- 新建合同信息 -->
        <div v-if="uploadFormData.linkType === 'new'">
          <el-divider content-position="left">合同基本信息</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="合同编号" prop="contractNo">
                <el-input
                  v-model="uploadFormData.contractNo"
                  placeholder="请输入合同编号"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合同名称" prop="contractName">
                <el-input
                  v-model="uploadFormData.contractName"
                  placeholder="请输入合同名称"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="合同类型" prop="contractType">
                <el-select v-model="uploadFormData.contractType" placeholder="请选择合同类型" style="width: 100%">
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
            <el-col :span="12">
              <el-form-item label="租户名称" prop="tenantName">
                <el-input
                  v-model="uploadFormData.tenantName"
                  placeholder="请输入租户名称"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="签订日期" prop="signDate">
                <el-date-picker
                  v-model="uploadFormData.signDate"
                  type="date"
                  placeholder="请选择签订日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属项目" prop="projectId">
                <el-select v-model="uploadFormData.projectId" placeholder="请选择所属项目" style="width: 100%">
                  <el-option
                    v-for="project in projectOptions"
                    :key="project.id"
                    :label="project.projectName"
                    :value="project.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
      
      <template #footer>
        <el-button @click="uploadDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUploadSubmit" :loading="uploadLoading">
          确定上传
        </el-button>
      </template>
    </el-dialog>

    <!-- 合同预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      title="合同预览"
      width="1400px"
      @close="handlePreviewDialogClose"
    >
      <div class="preview-container">
        <!-- 步骤指示器 -->
        <el-steps :active="previewStep" align-center class="preview-steps">
          <el-step title="选择模板" description="选择合同模板" />
          <el-step title="合同预览" description="预览最终合同" />
          <el-step title="完成" description="保存或下载" />
        </el-steps>

        <!-- 步骤1：选择模板 -->
        <div v-if="previewStep === 0" class="step-panel">
          <h3>选择合同模板</h3>
          <div class="template-grid">
            <div
              v-for="template in contractTemplates"
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
                <el-tag :type="getTemplateTypeTag(template.templateType)" size="small">
                  {{ getTemplateTypeName(template.templateType) }}
                </el-tag>
                <div class="template-stats">
                  <span>使用次数: {{ template.usageCount }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 步骤2：合同预览 -->
        <div v-if="previewStep === 1" class="step-panel">
          <h3>合同预览</h3>
          <div class="preview-content">
            <div class="contract-info-panel">
              <h4>合同信息</h4>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="合同编号">{{ formData.contractNo }}</el-descriptions-item>
                <el-descriptions-item label="合同名称">{{ formData.contractName }}</el-descriptions-item>
                <el-descriptions-item label="合同类型">{{ formData.contractType }}</el-descriptions-item>
                <el-descriptions-item label="租户名称">{{ formData.tenantName }}</el-descriptions-item>
                <el-descriptions-item label="签订日期">{{ formData.signDate }}</el-descriptions-item>
                <el-descriptions-item label="开始时间">{{ formData.startDate }}</el-descriptions-item>
                <el-descriptions-item label="结束时间">{{ formData.endDate }}</el-descriptions-item>
                <el-descriptions-item label="租金模式">{{ formData.rentMode }}</el-descriptions-item>
              </el-descriptions>
            </div>
            
            <div class="contract-template-panel">
              <h4>合同内容预览</h4>
              <div class="contract-content">
                {{ mergedContractContent }}
              </div>
            </div>
          </div>
        </div>

        <!-- 步骤3：完成 -->
        <div v-if="previewStep === 2" class="step-panel">
          <h3>操作完成</h3>
          <div class="completion-panel">
            <el-result icon="success" title="合同预览完成">
              <template #sub-title>
                <p>您可以选择下载合同PDF或保存合同信息到合同档案</p>
              </template>
              <template #extra>
                <div class="action-buttons">
                  <el-button type="primary" @click="downloadContractPDF" :loading="downloadLoading">
                    <el-icon><Download /></el-icon>
                    下载合同PDF
                  </el-button>
                  <el-button type="success" @click="saveContract" :loading="saveLoading">
                    <el-icon><DocumentAdd /></el-icon>
                    保存到合同档案
                  </el-button>
                </div>
              </template>
            </el-result>
          </div>
        </div>
      </div>
      
      <template #footer>
        <div class="preview-footer">
          <el-button v-if="previewStep > 0" @click="prevStep">
            <el-icon><ArrowLeft /></el-icon>
            上一步
          </el-button>
          <el-button @click="previewDialogVisible = false">取消</el-button>
          <el-button 
            v-if="previewStep < 2" 
            type="primary" 
            @click="nextStep"
            :disabled="previewStep === 0 && !selectedTemplate"
          >
            下一步
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
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
        <el-button type="primary" @click="handlePreview" :loading="submitLoading">
          预览
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules, type UploadInstance, type UploadFile } from 'element-plus'
import { Plus, Search, Refresh, Upload, UploadFilled, Document, Download, DocumentAdd, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { contractApi, type Contract } from '@/api/contract'
import { projectApi } from '@/api/project'
import { buildingApi, floorApi, unitApi } from '@/api/unit'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const uploadDialogVisible = ref(false)
const previewDialogVisible = ref(false)
const submitLoading = ref(false)
const uploadLoading = ref(false)
const downloadLoading = ref(false)
const saveLoading = ref(false)
const formRef = ref<FormInstance>()
const uploadFormRef = ref<FormInstance>()
const uploadRef = ref<UploadInstance>()
const viewData = ref<any>(null)

// 预览相关数据
const previewStep = ref(0)
const selectedTemplate = ref<any>(null)
const mergedContractContent = ref('')

// 未盖章生效的合同列表
const unsignedContracts = ref<Contract[]>([])

// 合同模板列表
const contractTemplates = ref<any[]>([])

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
  
  contractStatus: 'UNSIGNED_EFFECTIVE'
})

// 上传表单数据
const uploadFormData = reactive({
  contractFile: null as File | null,
  linkType: 'new' as 'new' | 'existing',
  existingContractId: null as number | null,
  // 新建合同时的基本信息
  contractNo: '',
  contractName: '',
  contractType: '',
  tenantName: '',
  signDate: '',
  projectId: null as number | null
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

// 上传表单验证规则
const uploadFormRules: FormRules = {
  contractFile: [
    { required: true, message: '请上传合同文件', trigger: 'change' }
  ],
  existingContractId: [
    { 
      required: true, 
      message: '请选择要关联的合同', 
      trigger: 'change',
      validator: (rule: any, value: any, callback: any) => {
        if (uploadFormData.linkType === 'existing' && !value) {
          callback(new Error('请选择要关联的合同'))
        } else {
          callback()
        }
      }
    }
  ],
  contractNo: [
    { 
      required: true, 
      message: '请输入合同编号', 
      trigger: 'blur',
      validator: (rule: any, value: any, callback: any) => {
        if (uploadFormData.linkType === 'new' && !value) {
          callback(new Error('请输入合同编号'))
        } else {
          callback()
        }
      }
    }
  ],
  contractName: [
    { 
      required: true, 
      message: '请输入合同名称', 
      trigger: 'blur',
      validator: (rule: any, value: any, callback: any) => {
        if (uploadFormData.linkType === 'new' && !value) {
          callback(new Error('请输入合同名称'))
        } else {
          callback()
        }
      }
    }
  ],
  contractType: [
    { 
      required: true, 
      message: '请选择合同类型', 
      trigger: 'change',
      validator: (rule: any, value: any, callback: any) => {
        if (uploadFormData.linkType === 'new' && !value) {
          callback(new Error('请选择合同类型'))
        } else {
          callback()
        }
      }
    }
  ],
  tenantName: [
    { 
      required: true, 
      message: '请输入租户名称', 
      trigger: 'blur',
      validator: (rule: any, value: any, callback: any) => {
        if (uploadFormData.linkType === 'new' && !value) {
          callback(new Error('请输入租户名称'))
        } else {
          callback()
        }
      }
    }
  ],
  projectId: [
    { 
      required: true, 
      message: '请选择所属项目', 
      trigger: 'change',
      validator: (rule: any, value: any, callback: any) => {
        if (uploadFormData.linkType === 'new' && !value) {
          callback(new Error('请选择所属项目'))
        } else {
          callback()
        }
      }
    }
  ]
}

// 对话框标题
const dialogTitle = ref('新建合同')

// 获取合同状态标签颜色
const getContractStatusTag = (status: string) => {
  const tagMap: Record<string, string> = {
    'UNSIGNED_EFFECTIVE': 'warning',
    'SIGNED_EFFECTIVE': 'success',
    'TERMINATED': 'danger'
  }
  return tagMap[status] || 'info'
}

// 获取合同状态名称
const getContractStatusName = (status: string) => {
  const nameMap: Record<string, string> = {
    'UNSIGNED_EFFECTIVE': '未盖章生效',
    'SIGNED_EFFECTIVE': '已盖章生效',
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
  
  // 楼层变化时不需要重新加载单元，因为单元已经按楼栋加载了
  // 这里可以添加楼层相关的逻辑处理
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

// 根据楼栋加载单元
const loadUnitsByBuildings = async (buildingIds: number[]) => {
  try {
    if (buildingIds.length === 0) {
      unitOptions.value = []
      return
    }
    
    // 获取所有选中楼栋的单元
    const allUnits: any[] = []
    for (const buildingId of buildingIds) {
      const response = await unitApi.getUnitList({ buildingId })
      if (response.data && Array.isArray(response.data)) {
        allUnits.push(...response.data)
      }
    }
    
    unitOptions.value = allUnits
  } catch (error) {
    console.error('加载单元选项失败:', error)
    ElMessage.error('加载单元选项失败')
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

// 预览合同
const handlePreview = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    // 加载合同模板
    await loadContractTemplates()
    // 重置预览状态
    previewStep.value = 0
    selectedTemplate.value = null
    mergedContractContent.value = ''
    // 显示预览对话框
    previewDialogVisible.value = true
    dialogVisible.value = false
  } catch (error: any) {
    ElMessage.error('请完善合同信息')
  }
}

// 选择模板
const selectTemplate = (template: any) => {
  selectedTemplate.value = template
}

// 下一步
const nextStep = () => {
  if (previewStep.value === 0 && selectedTemplate.value) {
    // 合并合同信息和模板内容
    mergeContractContent()
    previewStep.value = 1
  } else if (previewStep.value === 1) {
    previewStep.value = 2
  }
}

// 上一步
const prevStep = () => {
  if (previewStep.value > 0) {
    previewStep.value--
  }
}

// 合并合同内容
const mergeContractContent = () => {
  if (!selectedTemplate.value) return
  
  let content = selectedTemplate.value.content || ''
  
  // 替换模板变量
  const replacements: Record<string, string> = {
    '{{甲方名称}}': '出租方', // 这里可以从项目信息中获取
    '{{乙方名称}}': formData.tenantName || '',
    '{{合同编号}}': formData.contractNo || '',
    '{{合同名称}}': formData.contractName || '',
    '{{房屋地址}}': '', // 从单元信息中获取
    '{{建筑面积}}': formData.buildingArea?.toString() || '',
    '{{计租面积}}': formData.rentableArea?.toString() || '',
    '{{签约面积}}': formData.contractArea?.toString() || '',
    '{{租赁开始日期}}': formData.startDate || '',
    '{{租赁结束日期}}': formData.endDate || '',
    '{{签订日期}}': formData.signDate || '',
    '{{月租金}}': formData.periodRent?.toString() || '',
    '{{首期租金}}': formData.firstPeriodRent?.toString() || '',
    '{{保证金}}': formData.depositAmount?.toString() || '',
    '{{租金模式}}': formData.rentMode || '',
    '{{付款频率}}': formData.paymentFrequency || '',
    '{{业态}}': formData.businessFormat || '',
    '{{经营品牌}}': formData.businessBrand || ''
  }
  
  // 执行替换
  Object.entries(replacements).forEach(([key, value]) => {
    content = content.replace(new RegExp(key, 'g'), value)
  })
  
  mergedContractContent.value = content
}

// 下载合同PDF
const downloadContractPDF = async () => {
  try {
    downloadLoading.value = true
    
    // 这里应该调用后端API生成PDF
    // const response = await contractApi.generateContractPDF({
    //   contractData: formData,
    //   templateId: selectedTemplate.value.id,
    //   content: mergedContractContent.value
    // })
    
    // 模拟下载
    const blob = new Blob([mergedContractContent.value], { type: 'text/plain' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `${formData.contractName || '合同'}.txt`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('合同PDF下载成功')
  } catch (error: any) {
    ElMessage.error(error.message || '下载失败')
  } finally {
    downloadLoading.value = false
  }
}

// 保存合同
const saveContract = async () => {
  try {
    saveLoading.value = true
    
    const contractData: any = {
      ...formData,
      templateId: selectedTemplate.value?.id,
      contractContent: mergedContractContent.value,
      contractStatus: 'UNSIGNED_EFFECTIVE', // 未盖章生效
      // 处理日期格式
      signDate: formData.signDate || undefined,
      startDate: formData.startDate || undefined,
      endDate: formData.endDate || undefined,
      depositLatestDate: formData.depositLatestDate || undefined,
      firstPaymentLatestDate: formData.firstPaymentLatestDate || undefined
    }
    
    await contractApi.createContract(contractData)
    ElMessage.success('合同保存成功')
    
    // 关闭预览对话框并刷新数据
    previewDialogVisible.value = false
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '保存失败')
  } finally {
    saveLoading.value = false
  }
}

// 关闭预览对话框
const handlePreviewDialogClose = () => {
  previewDialogVisible.value = false
  previewStep.value = 0
  selectedTemplate.value = null
  mergedContractContent.value = ''
}

// 加载合同模板
const loadContractTemplates = async () => {
  try {
    // 这里应该调用实际的API
    // const response = await contractApi.getTemplateList({ status: 'ACTIVE' })
    // contractTemplates.value = response.data
    
    // 模拟数据
    contractTemplates.value = [
      {
        id: 1,
        templateName: '标准租赁合同模板',
        templateType: 'LEASE',
        description: '适用于标准房屋租赁业务的合同模板',
        usageCount: 25,
        content: `合同编号：{{合同编号}}
合同名称：{{合同名称}}

甲方（出租方）：{{甲方名称}}
乙方（承租方）：{{乙方名称}}

根据《中华人民共和国合同法》等相关法律法规，甲乙双方在平等、自愿、协商一致的基础上，就房屋租赁事宜达成如下协议：

一、租赁房屋基本情况
房屋地址：{{房屋地址}}
建筑面积：{{建筑面积}}平方米
计租面积：{{计租面积}}平方米
签约面积：{{签约面积}}平方米

二、租赁期限
租赁期限自{{租赁开始日期}}至{{租赁结束日期}}止。

三、租金及支付方式
租金模式：{{租金模式}}
月租金：{{月租金}}元
首期租金：{{首期租金}}元
付款频率：{{付款频率}}

四、保证金
保证金金额：{{保证金}}元

五、其他约定
业态：{{业态}}
经营品牌：{{经营品牌}}

本合同一式两份，甲乙双方各执一份，具有同等法律效力。

甲方签字：________________    日期：{{签订日期}}
乙方签字：________________    日期：{{签订日期}}`
      },
      {
        id: 2,
        templateName: '商业租赁合同模板',
        templateType: 'LEASE',
        description: '适用于商业用途的租赁合同',
        usageCount: 18,
        content: `商业租赁合同

合同编号：{{合同编号}}
合同名称：{{合同名称}}

出租方（甲方）：{{甲方名称}}
承租方（乙方）：{{乙方名称}}

经甲乙双方友好协商，就商业用房租赁事宜达成如下协议：

第一条 租赁物业
物业地址：{{房屋地址}}
租赁面积：{{签约面积}}平方米
用途：{{业态}}

第二条 租赁期限
租期：{{租赁开始日期}} 至 {{租赁结束日期}}

第三条 租金条款
租金标准：{{租金模式}}
租金金额：{{月租金}}元/月
支付方式：{{付款频率}}

第四条 保证金
金额：{{保证金}}元

甲方：________________
乙方：________________
签约日期：{{签订日期}}`
      }
    ]
  } catch (error: any) {
    console.error('加载合同模板失败:', error)
    ElMessage.error('加载合同模板失败')
  }
}

// 获取模板类型标签颜色
const getTemplateTypeTag = (type: string) => {
  const tagMap: Record<string, string> = {
    'LEASE': 'primary',
    'SERVICE': 'success',
    'SALES': 'warning',
    'OTHER': 'info'
  }
  return tagMap[type] || 'info'
}

// 获取模板类型名称
const getTemplateTypeName = (type: string) => {
  const nameMap: Record<string, string> = {
    'LEASE': '租赁合同',
    'SERVICE': '服务合同',
    'SALES': '销售合同',
    'OTHER': '其他合同'
  }
  return nameMap[type] || type
}

// 上传盖章合同
const handleUploadContract = () => {
  resetUploadForm()
  loadUnsignedContracts()
  uploadDialogVisible.value = true
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

// 下载合同PDF
const handleDownloadPDF = async (row: Contract) => {
  try {
    downloadLoading.value = true
    
    // 这里应该调用后端API下载合同PDF
    // const response = await contractApi.downloadContractPDF(row.id!)
    
    // 模拟下载
    const contractContent = `合同编号：${row.contractNo}
合同名称：${row.contractName}
租户名称：${row.tenantName}
合同类型：${row.contractType}
签订日期：${row.signDate}
开始时间：${row.startDate}
结束时间：${row.endDate}
合同状态：${getContractStatusName(row.contractStatus)}

这是一个模拟的合同内容，实际应该从后端获取完整的合同PDF文件。`
    
    const blob = new Blob([contractContent], { type: 'text/plain' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `${row.contractName || '合同'}_${row.contractNo}.txt`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('合同PDF下载成功')
  } catch (error: any) {
    ElMessage.error(error.message || '下载失败')
  } finally {
    downloadLoading.value = false
  }
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

// 文件上传处理
const handleFileChange = (file: UploadFile) => {
  if (file.raw) {
    // 检查文件大小（10MB）
    if (file.raw.size > 10 * 1024 * 1024) {
      ElMessage.error('文件大小不能超过 10MB')
      return false
    }
    
    // 检查文件类型
    const allowedTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
    if (!allowedTypes.includes(file.raw.type)) {
      ElMessage.error('只能上传 PDF、DOC、DOCX 格式的文件')
      return false
    }
    
    uploadFormData.contractFile = file.raw
  }
}

// 文件移除处理
const handleFileRemove = () => {
  uploadFormData.contractFile = null
}

// 关联类型变化处理
const handleLinkTypeChange = (type: string) => {
  if (type === 'existing') {
    // 清空新建合同的表单数据
    uploadFormData.contractNo = ''
    uploadFormData.contractName = ''
    uploadFormData.contractType = ''
    uploadFormData.tenantName = ''
    uploadFormData.signDate = ''
    uploadFormData.projectId = null
  } else {
    // 清空关联合同选择
    uploadFormData.existingContractId = null
  }
}

// 加载未盖章生效的合同
const loadUnsignedContracts = async () => {
  try {
    const response = await contractApi.getContractPage({
      current: 1,
      size: 1000,
      contractStatus: 'UNSIGNED_EFFECTIVE'
    })
    unsignedContracts.value = response.data.records
  } catch (error: any) {
    console.error('加载未盖章合同失败:', error)
  }
}

// 上传对话框关闭
const handleUploadDialogClose = () => {
  resetUploadForm()
}

// 上传提交
const handleUploadSubmit = async () => {
  if (!uploadFormRef.value) return
  
  try {
    await uploadFormRef.value.validate()
    uploadLoading.value = true
    
    if (uploadFormData.linkType === 'existing') {
      // 关联已有合同，更新合同状态为已盖章生效
      // 只更新合同状态，使用 Partial 类型
      await contractApi.updateContract(uploadFormData.existingContractId!, {
        contractStatus: 'SIGNED_EFFECTIVE'
      } as any)
      
      // TODO: 上传文件到服务器并关联到合同
      // await contractApi.uploadContractFile(uploadFormData.existingContractId!, uploadFormData.contractFile!)
      
      ElMessage.success('合同状态已更新为已盖章生效')
    } else {
      // 新建合同，状态为已盖章生效
      const contractData = {
        contractNo: uploadFormData.contractNo,
        contractName: uploadFormData.contractName,
        contractType: uploadFormData.contractType,
        tenantName: uploadFormData.tenantName,
        signDate: uploadFormData.signDate,
        projectId: uploadFormData.projectId || 1, // 默认项目ID
        startDate: uploadFormData.signDate, // 使用签订日期作为开始日期
        endDate: new Date(new Date(uploadFormData.signDate).getTime() + 365 * 24 * 60 * 60 * 1000).toISOString().split('T')[0], // 默认一年后
        tenantId: 1, // 默认租户ID
        unitId: 1, // 默认单元ID
        contractStatus: 'SIGNED_EFFECTIVE'
      }
      
      const response = await contractApi.createContract(contractData)
      
      // TODO: 上传文件到服务器并关联到新建的合同
      // await contractApi.uploadContractFile(response.data.id, uploadFormData.contractFile!)
      
      ElMessage.success('合同创建成功，状态为已盖章生效')
    }
    
    uploadDialogVisible.value = false
    loadData()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    uploadLoading.value = false
  }
}

// 重置上传表单
const resetUploadForm = () => {
  Object.assign(uploadFormData, {
    contractFile: null,
    linkType: 'new',
    existingContractId: null,
    contractNo: '',
    contractName: '',
    contractType: '',
    tenantName: '',
    signDate: '',
    projectId: null
  })
  uploadFormRef.value?.resetFields()
  uploadRef.value?.clearFiles()
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
    
    contractStatus: 'UNSIGNED_EFFECTIVE'
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

.header-buttons {
  display: flex;
  gap: 12px;
}

.card-header :deep(.el-button) {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  border: none;
}

.card-header :deep(.el-button--primary) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.2);
}

.card-header :deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(16, 185, 129, 0.3);
}

.card-header :deep(.el-button--success) {
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
  box-shadow: 0 2px 8px rgba(5, 150, 105, 0.2);
}

.card-header :deep(.el-button--success:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(5, 150, 105, 0.3);
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

/* 上传对话框样式 */
:deep(.el-upload-dragger) {
  border-radius: 12px;
  border: 2px dashed #d1d5db;
  background: #f9fafb;
  transition: all 0.3s ease;
}

:deep(.el-upload-dragger:hover) {
  border-color: #10b981;
  background: #f0fdf4;
}

:deep(.el-upload-dragger .el-icon--upload) {
  font-size: 48px;
  color: #10b981;
  margin-bottom: 16px;
}

:deep(.el-upload__text) {
  color: #6b7280;
  font-size: 14px;
}

:deep(.el-upload__text em) {
  color: #10b981;
  font-weight: 500;
}

:deep(.el-upload__tip) {
  color: #9ca3af;
  font-size: 12px;
  margin-top: 8px;
}

:deep(.el-radio-group) {
  display: flex;
  gap: 24px;
}

:deep(.el-radio) {
  margin-right: 0;
}

:deep(.el-radio__label) {
  font-weight: 500;
  color: #374151;
}

/* 预览对话框样式 */
.preview-container {
  max-height: 75vh;
  overflow-y: auto;
}

.preview-steps {
  margin-bottom: 32px;
}

.step-panel {
  padding: 24px 0;
}

.step-panel h3 {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 24px;
  text-align: center;
}

/* 模板选择样式 */
.template-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
  margin-top: 24px;
}

.template-card {
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #ffffff;
}

.template-card:hover {
  border-color: #10b981;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.15);
  transform: translateY(-2px);
}

.template-card.active {
  border-color: #10b981;
  background: #f0fdf4;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
}

.template-icon {
  text-align: center;
  margin-bottom: 16px;
}

.template-icon .el-icon {
  font-size: 48px;
  color: #10b981;
}

.template-info h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 8px;
}

.template-info p {
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 12px;
  line-height: 1.5;
}

.template-stats {
  margin-top: 12px;
  font-size: 12px;
  color: #9ca3af;
}

/* 预览内容样式 */
.preview-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.contract-info-panel,
.contract-template-panel {
  background: #f9fafb;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e5e7eb;
}

.contract-info-panel h4,
.contract-template-panel h4 {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
}

.contract-content {
  background: #ffffff;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 20px;
  white-space: pre-wrap;
  line-height: 1.8;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  color: #374151;
  max-height: 400px;
  overflow-y: auto;
}

/* 完成页面样式 */
.completion-panel {
  text-align: center;
  padding: 40px 20px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 24px;
}

.action-buttons .el-button {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
}

/* 预览对话框底部 */
.preview-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-footer .el-button {
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .template-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .template-card {
    padding: 20px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 12px;
  }
  
  .action-buttons .el-button {
    width: 100%;
  }
  
  .preview-footer {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
