<template>
  <div class="dashboard">
    <div class="welcome">
      <h1>欢迎回来，{{ userStore.userInfo?.name || '用户' }}</h1>
      <p>OA员工Ai管理系统 — 让管理更高效，让沟通更智能</p>
    </div>

    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6" v-for="card in statCards" :key="card.title">
        <el-card shadow="hover" class="stat-card" :style="{ borderTop: `3px solid ${card.color}` }">
          <div class="stat-card-body">
            <div class="stat-info">
              <div class="stat-title">{{ card.title }}</div>
              <div class="stat-value">{{ card.value }}</div>
            </div>
            <el-icon :size="48" :style="{ color: card.color }">
              <component :is="card.icon" />
            </el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span style="font-weight: 600;">快捷入口</span>
          </template>
          <div class="quick-links">
            <el-button v-for="link in quickLinks" :key="link.path" @click="$router.push(link.path)" :icon="link.icon" size="large">
              {{ link.title }}
            </el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span style="font-weight: 600;">系统信息</span>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="系统名称">OA员工Ai管理系统</el-descriptions-item>
            <el-descriptions-item label="技术栈">SpringBoot + MyBatis + Vue3</el-descriptions-item>
            <el-descriptions-item label="AI引擎">Spring AI</el-descriptions-item>
            <el-descriptions-item label="当前用户">
              <el-tag>{{ userStore.userInfo?.name }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="角色">
              <el-tag :type="userStore.isAdmin ? 'danger' : 'info'">{{ userStore.roleName }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getEmployeeList } from '@/api/employee'
import { getDeptList } from '@/api/department'
import{ getJobList } from '@/api/job'
const userStore = useUserStore()
const totalEmployees = ref(0)
const totalDepts = ref(0)
const totalJobs = ref(0)

onMounted(async () => {
  const res1 = await getEmployeeList({ page: 1, pageSize: 1 })
  totalEmployees.value = res1.data.total
  const res2 = await getDeptList({ page: 1, pageSize: 1 })
  totalDepts.value = res2.data.total
  const res3 = await getJobList({ page: 1, pageSize: 1 })
  totalJobs.value = res3.data.total
})
const statCards = computed(() => [
  { title: '员工总数', value: totalEmployees.value, icon: 'User', color: '#409eff' },
  { title: '部门数量', value: totalDepts.value, icon: 'OfficeBuilding', color: '#67c23a' },
  { title: '职位数量', value: totalJobs.value, icon: 'Briefcase', color: '#e6a23c' },
  { title: '在线用户', value: '19', icon: 'ChatDotRound', color: '#f56c6c' }
])

const quickLinks = [
  { title: '员工管理', path: '/employee', icon: 'User' },
  { title: '部门管理', path: '/department', icon: 'OfficeBuilding' },
  { title: '职位管理', path: '/job', icon: 'Briefcase' },
  { title: 'AI对话', path: '/ai-chat', icon: 'ChatDotRound' }
]
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
}

.welcome {
  margin-bottom: 24px;
}
.welcome h1 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 8px;
}
.welcome p {
  color: #909399;
  font-size: 14px;
}

.stat-card-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.quick-links {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
</style>
