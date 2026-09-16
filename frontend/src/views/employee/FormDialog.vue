<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑员工' : '新增员工'"
    width="560px"
    @close="$emit('update:visible', false)"
  >
    <el-form ref="formRef" :model="localForm" :rules="rules" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="localForm.name" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="localForm.gender">
          <el-radio :value="1">男</el-radio>
          <el-radio :value="0">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="localForm.phone" placeholder="请输入手机号" maxlength="11" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="localForm.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="部门" prop="deptId">
        <el-select v-model="localForm.deptId" placeholder="请选择部门" style="width: 100%">
          <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="职位" prop="jobId">
        <el-select v-model="localForm.jobId" placeholder="请选择职位" style="width: 100%">
          <el-option v-for="j in jobList" :key="j.id" :label="j.name" :value="j.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="入职时间" prop="hireDate">
        <el-date-picker v-model="localForm.hireDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, reactive } from 'vue'

const props = defineProps({
  visible: Boolean,
  formData: Object,
  deptList: Array,
  jobList: Array,
  isEdit: Boolean
})

const emit = defineEmits(['update:visible', 'submit'])
const formRef = ref()
const submitting = ref(false)
const localForm = reactive({ ...props.formData })

watch(() => props.visible, (val) => {
  if (val) {
    Object.assign(localForm, props.formData)
  }
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  jobId: [{ required: true, message: '请选择职位', trigger: 'change' }],
  hireDate: [{ required: true, message: '请选择入职时间', trigger: 'change' }]
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    emit('submit', { ...localForm })
  } finally {
    submitting.value = false
  }
}
</script>
