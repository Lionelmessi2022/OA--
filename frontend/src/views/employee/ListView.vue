<template>
  <div>
    <!-- Search Bar -->
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="searchForm.deptId" placeholder="全部部门" clearable style="width: 150px">
            <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="职位">
          <el-select v-model="searchForm.jobId" placeholder="全部职位" clearable style="width: 150px">
            <el-option v-for="j in jobList" :key="j.id" :label="j.name" :value="j.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="loadData">搜索</el-button>
          <el-button icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- Table -->
    <div class="table-card">
      <div class="page-header">
        <h2>员工管理</h2>
        <div>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增员工</el-button>
          <el-button type="danger" icon="Delete" :disabled="!selectedIds.length" @click="handleBatchDelete">
            批量删除
          </el-button>
        </div>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe @selection-change="onSelectionChange" row-key="id">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="empNo" label="员工编号" width="110" />
        <el-table-column prop="name" label="姓名" width="90" />
        <el-table-column prop="gender" label="性别" width="70">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="deptName" label="部门" min-width="100" />
        <el-table-column prop="jobName" label="职位" min-width="100" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="160" />
        <el-table-column prop="hireDate" label="入职时间" width="110" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button :type="row.status === 1 ? 'warning' : 'success'" link
              :icon="row.status === 1 ? 'Lock' : 'Unlock'"
              @click="handleToggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-popconfirm title="确定删除该员工？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button type="danger" link icon="Delete">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <!-- Add/Edit Dialog -->
    <FormDialog
      v-model:visible="dialogVisible"
      :form-data="editForm"
      :dept-list="deptList"
      :job-list="jobList"
      :is-edit="isEdit"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getEmployeeList, addEmployee, updateEmployee, deleteEmployee, batchDeleteEmployees, toggleEmployeeStatus } from '@/api/employee'
import { getAllDepts } from '@/api/department'
import { getAllJobs } from '@/api/job'
import { ElMessage } from 'element-plus'
import FormDialog from './FormDialog.vue'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])
const deptList = ref([])
const jobList = ref([])

const searchForm = reactive({
  name: '',
  deptId: '',
  jobId: '',
  dateRange: null
})

const dialogVisible = ref(false)
const isEdit = ref(false)
const editForm = reactive({
  id: null,
  name: '',
  gender: 1,
  phone: '',
  email: '',
  deptId: '',
  jobId: '',
  hireDate: ''
})

onMounted(() => {
  loadData()
  loadDepts()
  loadJobs()
})

async function loadData() {
  loading.value = true
  try {
    const params = {
      page: page.value,
      pageSize: pageSize.value,
      name: searchForm.name || undefined,
      deptId: searchForm.deptId || undefined,
      jobId: searchForm.jobId || undefined,
      startDate: searchForm.dateRange?.[0] || undefined,
      endDate: searchForm.dateRange?.[1] || undefined
    }
    const res = await getEmployeeList(params)
    tableData.value = res.data?.records || res.data?.list || []
    total.value = res.data?.total || 0
  } catch (e) {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

async function loadDepts() {
  try {
    const res = await getAllDepts()
    deptList.value = res.data || []
  } catch (e) {}
}

async function loadJobs() {
  try {
    const res = await getAllJobs()
    jobList.value = res.data || []
  } catch (e) {}
}

function resetSearch() {
  searchForm.name = ''
  searchForm.deptId = ''
  searchForm.jobId = ''
  searchForm.dateRange = null
  page.value = 1
  loadData()
}

function onSelectionChange(rows) {
  selectedIds.value = rows.map(r => r.id)
}

function handleAdd() {
  isEdit.value = false
  Object.assign(editForm, { id: null, name: '', gender: 1, phone: '', email: '', deptId: '', jobId: '', hireDate: '' })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  Object.assign(editForm, {
    id: row.id,
    name: row.name,
    gender: row.gender,
    phone: row.phone,
    email: row.email,
    deptId: row.deptId,
    jobId: row.jobId,
    hireDate: row.hireDate
  })
  dialogVisible.value = true
}

async function handleSubmit(formData) {
  try {
    if (isEdit.value) {
      await updateEmployee(formData)
      ElMessage.success('修改成功')
    } else {
      await addEmployee(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {}
}

async function handleDelete(id) {
  try {
    await deleteEmployee(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {}
}

async function handleBatchDelete() {
  try {
    await batchDeleteEmployees(selectedIds.value)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (e) {}
}

async function handleToggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 1 ? '启用' : '禁用'
  try {
    await toggleEmployeeStatus(row.id, newStatus)
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (e) {}
}
</script>
