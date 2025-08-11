import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: {
        title: '登录',
        requiresAuth: false
      }
    },
    {
      path: '/test',
      name: 'Test',
      component: () => import('@/views/Test.vue'),
      meta: {
        title: '测试页面',
        requiresAuth: false
      }
    },
    {
      path: '/simple-login',
      name: 'SimpleLogin',
      component: () => import('@/views/SimpleLogin.vue'),
      meta: {
        title: '简单登录',
        requiresAuth: false
      }
    },
    {
      path: '/',
      name: 'Layout',
      component: () => import('@/views/Layout.vue'),
      redirect: '/dashboard',
      meta: {
        requiresAuth: true
      },
      children: [
        {
          path: '/dashboard',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue'),
          meta: {
            title: '运营仪表盘',
            icon: 'DataBoard'
          }
        },
        // 资产管理
        {
          path: '/asset',
          name: 'Asset',
          meta: {
            title: '资产管理',
            icon: 'OfficeBuilding'
          },
          children: [
            {
              path: '/asset/project',
              name: 'AssetProject',
              component: () => import('@/views/asset/Project.vue'),
              meta: {
                title: '项目管理',
                icon: 'House'
              }
            },
            {
              path: '/asset/building',
              name: 'AssetBuilding',
              component: () => import('@/views/asset/Building.vue'),
              meta: {
                title: '楼栋管理',
                icon: 'Building'
              }
            },
            {
              path: '/asset/unit',
              name: 'AssetUnit',
              component: () => import('@/views/asset/Unit.vue'),
              meta: {
                title: '单元管理',
                icon: 'Grid'
              }
            },
            {
              path: '/asset/merge-split',
              name: 'AssetMergeSplit',
              component: () => import('@/views/asset/MergeSplit.vue'),
              meta: {
                title: '单元合并拆分',
                icon: 'Connection'
              }
            }
          ]
        },
        // 租户管理
        {
          path: '/tenant',
          name: 'Tenant',
          redirect: '/tenant/info',
          meta: {
            title: '租户管理',
            icon: 'Avatar'
          },
          children: [
            {
              path: '/tenant/info',
              name: 'TenantInfo',
              component: () => import('@/views/tenant/Info.vue'),
              meta: {
                title: '租户信息',
                icon: 'User'
              }
            },
            {
              path: '/tenant/risk',
              name: 'TenantRisk',
              component: () => import('@/views/tenant/Risk.vue'),
              meta: {
                title: '风险管控',
                icon: 'Warning'
              }
            },
            {
              path: '/tenant/profile',
              name: 'TenantProfile',
              component: () => import('@/views/tenant/Profile.vue'),
              meta: {
                title: '租户画像',
                icon: 'DataAnalysis'
              }
            }
          ]
        },
        // 合同管理
        {
          path: '/contract',
          name: 'ContractManagement',
          redirect: '/contract/info',
          meta: {
            title: '合同管理',
            icon: 'Document'
          },
          children: [
            {
              path: '/contract/info',
              name: 'ContractInfo',
              component: () => import('@/views/contract/Info.vue'),
              meta: {
                title: '合同信息',
                icon: 'Document'
              }
            },
            {
              path: '/contract/business-analysis',
              name: 'ContractBusinessAnalysis',
              component: () => import('@/views/contract/BusinessAnalysis.vue'),
              meta: {
                title: '业态分析',
                icon: 'TrendCharts'
              }
            },
            {
              path: '/contract/config',
              name: 'ContractConfig',
              component: () => import('@/views/contract/Config.vue'),
              meta: {
                title: '合同配置',
                icon: 'Setting'
              }
            },
            {
              path: '/contract/generate',
              name: 'ContractGenerate',
              component: () => import('@/views/contract/Generate.vue'),
              meta: {
                title: '合同生成',
                icon: 'DocumentAdd'
              }
            }
          ]
        },
        // 账款管理
        {
          path: '/account',
          name: 'AccountManagement',
          redirect: '/account/receivable-received',
          meta: {
            title: '账款管理',
            icon: 'Money'
          },
          children: [
            {
              path: '/account/receivable-received',
              name: 'ReceivableReceived',
              component: () => import('@/views/account/ReceivableReceived.vue'),
              meta: {
                title: '应收已收',
                icon: 'Wallet'
              }
            },
            {
              path: '/account/receivable-analysis',
              name: 'ReceivableAnalysis',
              component: () => import('@/views/account/ReceivableAnalysis.vue'),
              meta: {
                title: '收款进度',
                icon: 'TrendCharts'
              }
            },
            {
              path: '/account/payment-claim',
              name: 'PaymentClaim',
              component: () => import('@/views/account/PaymentClaim.vue'),
              meta: {
                title: '手动认领',
                icon: 'CreditCard'
              }
            },
            {
              path: '/account/collection-reminder',
              name: 'CollectionReminder',
              component: () => import('@/views/account/CollectionReminder.vue'),
              meta: {
                title: '自动催缴',
                icon: 'Message'
              }
            }
          ]
        },
        // 系统管理
        {
          path: '/system',
          name: 'System',
          meta: {
            title: '系统管理',
            icon: 'Setting'
          },
          children: [
            {
              path: '/system/user',
              name: 'SystemUser',
              component: () => import('@/views/system/User.vue'),
              meta: {
                title: '用户管理',
                icon: 'User'
              }
            },
            {
              path: '/system/role',
              name: 'SystemRole',
              component: () => import('@/views/RoleManagement.vue'),
              meta: {
                title: '角色管理',
                icon: 'UserFilled'
              }
            }
          ]
        },
        // 个人中心
        {
          path: '/profile',
          name: 'Profile',
          component: () => import('@/views/Profile.vue'),
          meta: {
            title: '个人中心',
            icon: 'User'
          }
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/404.vue'),
      meta: {
        title: '页面不存在'
      }
    }
  ]
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 云联智管`
  }
  
  // 如果访问登录页或其他不需要认证的页面，直接放行
  if (to.meta.requiresAuth === false) {
    // 如果已经有token且访问登录页，重定向到首页
    if (to.path === '/login' && userStore.token) {
      next('/dashboard')
      return
    }
    next()
    return
  }
  
  // 需要认证的页面
  if (!userStore.token) {
    console.log('没有token，重定向到登录页')
    next('/login')
    return
  }
  
  // 如果有token但没有用户信息，尝试获取用户信息
  if (!userStore.userInfo) {
    try {
      console.log('路由守卫：尝试获取用户信息，当前token:', userStore.token)
      await userStore.getUserInfo()
      console.log('路由守卫：获取用户信息成功:', userStore.userInfo)
    } catch (error) {
      console.error('路由守卫：获取用户信息失败:', error)
      console.log('路由守卫：清除token并重定向到登录页')
      userStore.logout()
      next('/login')
      return
    }
  }
  
  next()
})

export default router