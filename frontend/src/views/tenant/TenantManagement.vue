<template>
  <div class="tenant-info">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>租户信息</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建租户
          </el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchForm" inline>
          <el-form-item label="租户名称">
            <el-input
              v-model="searchForm.keyword"
              placeholder="请输入租户名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="租户性质">
            <el-select
              v-model="searchForm.tenantNature"
              placeholder="请选择租户性质"
              clearable
              style="width: 150px"
            >
              <el-option label="个人" value="个人" />
              <el-option label="公司" value="公司" />
              <el-option label="政府机构" value="政府机构" />
            </el-select>
          </el-form-item>
          <el-form-item label="业态">
            <el-select
              v-model="searchForm.businessFormat"
              placeholder="请选择业态"
              clearable
              style="width: 150px"
            >
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
        <el-table-column prop="tenantName" label="租户名称" min-width="150" />
        <el-table-column prop="tenantNature" label="租户性质" width="100">
          <template #default="{ row }">
            <el-tag :type="getTenantNatureTag(row.tenantNature)">
              {{ row.tenantNature }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enterpriseNature" label="企业性质" width="180" show-overflow-tooltip />
        <el-table-column prop="socialCreditCode" label="社会信用代码" width="180" show-overflow-tooltip />
        <el-table-column prop="taxpayerId" label="纳税人识别号" width="150" show-overflow-tooltip />
        <el-table-column prop="businessRegistrationNumber" label="工商注册号" width="150" show-overflow-tooltip />
        <el-table-column prop="individualLicenseNumber" label="个体户证件号" width="150" show-overflow-tooltip />
        <el-table-column prop="brand" label="品牌" width="120" />
        <el-table-column prop="brandQualification" label="品牌资质" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.brandQualification" :type="getBrandQualificationTag(row.brandQualification)" size="small">
              {{ row.brandQualification }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="businessFormat" label="业态" width="120" />
        <el-table-column prop="businessScope" label="经营范围" width="200" show-overflow-tooltip />
        <el-table-column prop="legalPersonName" label="法人姓名" width="120" />
        <el-table-column prop="legalPersonPhone" label="法人手机号" width="130" />
        <el-table-column prop="legalPersonIdCard" label="法人身份证" width="180" show-overflow-tooltip />
        <el-table-column prop="financeContact" label="财务联系人" width="120" />
        <el-table-column prop="financePhone" label="财务电话" width="130" />
        <el-table-column prop="payerName" label="付款人名称" width="120" />
        <el-table-column prop="paymentAccount" label="付款账号" width="180" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" width="150" show-overflow-tooltip />
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
      title="租户详情"
      width="1000px"
    >
      <div v-if="viewData" class="tenant-detail">
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="租户名称">{{ viewData.tenantName }}</el-descriptions-item>
          <el-descriptions-item label="租户性质">
            <el-tag :type="getTenantNatureTag(viewData.tenantNature)">
              {{ viewData.tenantNature }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="品牌">{{ viewData.brand || '-' }}</el-descriptions-item>
          <el-descriptions-item label="品牌资质">
            <el-tag v-if="viewData.brandQualification" :type="getBrandQualificationTag(viewData.brandQualification)" size="small">
              {{ viewData.brandQualification }}
            </el-tag>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="业态">{{ viewData.businessFormat || '-' }}</el-descriptions-item>
          <el-descriptions-item label="企业性质">{{ viewData.enterpriseNature || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 证件信息 -->
        <el-divider content-position="left">证件信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="社会信用代码">{{ viewData.socialCreditCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="纳税人识别号">{{ viewData.taxpayerId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="工商注册号">{{ viewData.businessRegistrationNumber || '-' }}</el-descriptions-item>
          <el-descriptions-item label="个体户证件号">{{ viewData.individualLicenseNumber || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 经营信息 -->
        <el-divider content-position="left">经营信息</el-divider>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="经营范围">{{ viewData.businessScope || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 法人信息 -->
        <el-divider content-position="left">法人信息</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="法人姓名">{{ viewData.legalPersonName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="法人手机号">{{ viewData.legalPersonPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="法人身份证">{{ viewData.legalPersonIdCard || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 财务信息 -->
        <el-divider content-position="left">财务信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="财务联系人">{{ viewData.financeContact || '-' }}</el-descriptions-item>
          <el-descriptions-item label="财务电话">{{ viewData.financePhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="付款人名称">{{ viewData.payerName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="付款账号">{{ viewData.paymentAccount || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 其他信息 -->
        <el-divider content-position="left">其他信息</el-divider>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="备注">{{ viewData.remark || '-' }}</el-descriptions-item>
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
      width="1000px"
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
          <el-col :span="12">
            <el-form-item label="租户名称" prop="tenantName">
              <el-input v-model="formData.tenantName" placeholder="请输入租户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租户性质" prop="tenantNature">
              <el-select v-model="formData.tenantNature" placeholder="请选择租户性质" style="width: 100%">
                <el-option 
                  v-for="option in TENANT_NATURE_OPTIONS" 
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="品牌">
              <el-input v-model="formData.brand" placeholder="请输入品牌名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="品牌资质">
              <el-select v-model="formData.brandQualification" placeholder="请选择品牌资质" style="width: 100%">
                <el-option 
                  v-for="option in BRAND_QUALIFICATION_OPTIONS" 
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="业态">
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
        </el-row>

        <!-- 证件信息 -->
        <el-divider content-position="left">证件信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="社会信用代码">
              <el-input v-model="formData.socialCreditCode" placeholder="请输入社会信用代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纳税人识别号">
              <el-input v-model="formData.taxpayerId" placeholder="请输入纳税人识别号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工商注册号">
              <el-input v-model="formData.businessRegistrationNumber" placeholder="请输入工商注册号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="个体户证件号">
              <el-input v-model="formData.individualLicenseNumber" placeholder="请输入个体户证件号" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 企业性质 -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="企业性质">
              <el-select v-model="formData.enterpriseNature" placeholder="请选择企业性质" style="width: 100%">
                <el-option label="内资企业" value="内资企业" />
                <el-option label="港澳台投资企业" value="港澳台投资企业" />
                <el-option label="外商投资企业" value="外商投资企业" />
                <el-option label="个体经营户" value="个体经营户" />
                <el-option label="有限责任公司（自然人独资）" value="有限责任公司（自然人独资）" />
                <el-option label="有限责任公司分公司（外商投资企业法人独资）" value="有限责任公司分公司（外商投资企业法人独资）" />
                <el-option label="有限责任公司（自然人投资或控股）" value="有限责任公司（自然人投资或控股）" />
                <el-option label="有限责任公司分公司(自然人投资或控股)" value="有限责任公司分公司(自然人投资或控股)" />
                <el-option label="有限责任公司分公司（非自然人投资或控股的法）" value="有限责任公司分公司（非自然人投资或控股的法）" />
                <el-option label="有限责任公司（自然人投资或控股的法人独资）" value="有限责任公司（自然人投资或控股的法人独资）" />
                <el-option label="其他有限责任公司" value="其他有限责任公司" />
                <el-option label="其他股份有限公司（非上市）" value="其他股份有限公司（非上市）" />
                <el-option label="有限责任公司（外商投资企业法人独资）" value="有限责任公司（外商投资企业法人独资）" />
                <el-option label="中外合资" value="中外合资" />
                <el-option label="外国法人独资" value="外国法人独资" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 经营信息 -->
        <el-divider content-position="left">经营信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="经营范围">
              <el-input
                v-model="formData.businessScope"
                type="textarea"
                :rows="3"
                placeholder="请输入经营范围"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 法人信息 -->
        <el-divider content-position="left">法人信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="法人姓名">
              <el-input v-model="formData.legalPersonName" placeholder="请输入法人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="法人手机号" prop="legalPersonPhone">
              <el-input v-model="formData.legalPersonPhone" placeholder="请输入法人手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="法人身份证">
              <el-input v-model="formData.legalPersonIdCard" placeholder="请输入法人身份证号" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 财务信息 -->
        <el-divider content-position="left">财务信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="财务联系人">
              <el-input v-model="formData.financeContact" placeholder="请输入财务联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="财务电话">
              <el-input v-model="formData.financePhone" placeholder="请输入财务电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="付款人名称">
              <el-input v-model="formData.payerName" placeholder="请输入付款人名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款账号">
              <el-input v-model="formData.paymentAccount" placeholder="请输入付款账号" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 备注 -->
        <el-divider content-position="left">其他信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="formData.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
        </el-row>
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
import { tenantApi, type Tenant } from '@/api/tenant'
import { TENANT_NATURE_OPTIONS, getTenantNatureTagColor } from '@/constants/tenantNature'
import { BRAND_QUALIFICATION_OPTIONS, getBrandQualificationTagColor } from '@/constants/brandQualification'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const viewData = ref<any>(null)

// 搜索表单
const searchForm = reactive({
  keyword: '',
  tenantNature: '',
  businessFormat: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<Tenant[]>([])

// 表单数据
const formData = reactive({
  id: undefined as number | undefined,
  tenantName: '',
  tenantNature: '',
  enterpriseNature: '',
  socialCreditCode: '',
  taxpayerId: '',
  businessRegistrationNumber: '',
  individualLicenseNumber: '',
  brand: '',
  brandQualification: '',
  businessFormat: '',
  businessScope: '',
  legalPersonName: '',
  legalPersonPhone: '',
  legalPersonIdCard: '',
  financeContact: '',
  financePhone: '',
  payerName: '',
  paymentAccount: '',
  remark: ''
})

// 表单验证规则
const formRules: FormRules = {
  tenantName: [
    { required: true, message: '请输入租户名称', trigger: 'blur' }
  ],
  tenantNature: [
    { required: true, message: '请选择租户性质', trigger: 'change' }
  ],
  legalPersonPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

// 对话框标题
const dialogTitle = ref('新建租户')

// 获取租户性质标签颜色（使用常量文件中的函数）
const getTenantNatureTag = getTenantNatureTagColor

// 获取品牌资质标签颜色（使用常量文件中的函数）
const getBrandQualificationTag = getBrandQualificationTagColor

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.tenantNature = ''
  searchForm.businessFormat = ''
  handleSearch()
}

// 新建
const handleAdd = () => {
  dialogTitle.value = '新建租户'
  resetForm()
  dialogVisible.value = true
}

// 查看
const handleView = (row: any) => {
  viewData.value = { ...row }
  viewDialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑租户'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除租户"${row.tenantName}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await tenantApi.deleteTenant(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error: any) {
      console.error('删除失败:', error)
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  })
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
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      
      try {
        if (formData.id) {
          // 更新租户
          await tenantApi.updateTenant(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          // 创建租户
          await tenantApi.createTenant(formData)
          ElMessage.success('创建成功')
        }
        
        dialogVisible.value = false
        loadData()
      } catch (error: any) {
        console.error('提交失败:', error)
        ElMessage.error(error.response?.data?.message || '操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: undefined,
    tenantName: '',
    tenantNature: '',
    enterpriseNature: '',
    socialCreditCode: '',
    taxpayerId: '',
    businessRegistrationNumber: '',
    individualLicenseNumber: '',
    brand: '',
    brandQualification: '',
    businessFormat: '',
    businessScope: '',
    legalPersonName: '',
    legalPersonPhone: '',
    legalPersonIdCard: '',
    financeContact: '',
    financePhone: '',
    payerName: '',
    paymentAccount: '',
    remark: ''
  })
  formRef.value?.resetFields()
}

// 加载数据
const loadData = async () => {
  loading.value = true
  
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      keyword: searchForm.keyword || undefined,
      tenantNature: searchForm.tenantNature || undefined,
      businessFormat: searchForm.businessFormat || undefined
    }
    
    const response = await tenantApi.getTenantPage(params)
    const { records, total } = response.data
    
    tableData.value = records
    pagination.total = total
  } catch (error: any) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
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
  position: relative;
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
  transition: all 0.3s ease;
}

.tenant-info :deep(.el-card):hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
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

.card-header :deep(.el-button) {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #06b6d4 0%, #0891b2 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(6, 182, 212, 0.2);
}

.card-header :deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(6, 182, 212, 0.3);
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
  background: linear-gradient(135deg, #06b6d4 0%, #0891b2 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(6, 182, 212, 0.3);
}

.search-area :deep(.el-button--primary:hover) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(6, 182, 212, 0.4);
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

.el-table :deep(.el-tag--primary) {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
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

.el-table :deep(.el-button--text) {
  font-weight: 500;
  border-radius: 6px;
  padding: 6px 12px;
  transition: all 0.3s ease;
}

.el-table :deep(.el-button--text:hover) {
  background: rgba(6, 182, 212, 0.1);
  color: #06b6d4;
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
  background: linear-gradient(135deg, #06b6d4 0%, #0891b2 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(6, 182, 212, 0.3);
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

/* 对话框样式优化 */
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

/* 表单样式优化 */
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

:deep(.el-textarea__inner) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-textarea__inner:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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

.tenant-info {
  animation: fadeInUp 0.6s ease forwards;
}
</style>
