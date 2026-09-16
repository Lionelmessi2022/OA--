import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'employee',
        name: 'Employee',
        component: () => import('@/views/employee/ListView.vue'),
        meta: { title: '员工管理', icon: 'User', roles: ['admin'] }
      },
      {
        path: 'department',
        name: 'Department',
        component: () => import('@/views/department/ListView.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding', roles: ['admin'] }
      },
      {
        path: 'job',
        name: 'Job',
        component: () => import('@/views/job/ListView.vue'),
        meta: { title: '职位管理', icon: 'Briefcase', roles: ['admin'] }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/IndexView.vue'),
        meta: { title: '个人中心', icon: 'UserFilled' }
      },
      {
        path: 'ai-chat',
        name: 'AiChat',
        component: () => import('@/views/ai/ChatView.vue'),
        meta: { title: 'AI智能对话', icon: 'ChatDotRound' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('token')
  const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || 'null')

  if (to.path === '/login') {
    token ? next('/') : next()
    return
  }

  if (!token) {
    next('/login')
    return
  }

  // Check role-based access
  if (to.meta.roles && to.meta.roles.length > 0) {
    if (!userInfo || !to.meta.roles.includes(userInfo.role)) {
      next('/dashboard')
      return
    }
  }

  next()
})

export default router
