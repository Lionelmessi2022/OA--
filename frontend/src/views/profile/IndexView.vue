<template>
  <div>
    <div class="table-card">
      <div class="page-header">
        <h2>个人中心</h2>
      </div>

      <el-tabs v-model="activeTab">
        <!-- Personal Info Tab -->
        <el-tab-pane label="个人信息" name="info">
          <el-descriptions :column="2" border style="max-width: 700px; margin-top: 16px;">
            <el-descriptions-item label="账号">{{ userStore.userInfo?.username }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ userStore.userInfo?.name }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ userStore.userInfo?.gender === 1 ? '男' : '女' }}</el-descriptions-item>
            <el-descriptions-item label="角色">
              <el-tag :type="userStore.isAdmin ? 'danger' : 'info'">{{ userStore.roleName }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="部门">{{ userStore.userInfo?.deptName }}</el-descriptions-item>
            <el-descriptions-item label="职位">{{ userStore.userInfo?.jobName }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ userStore.userInfo?.phone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userStore.userInfo?.email }}</el-descriptions-item>
            <el-descriptions-item label="入职时间">{{ userStore.userInfo?.hireDate }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- Change Password Tab -->
        <el-tab-pane label="修改密码" name="password">
          <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px" style="max-width: 450px; margin-top: 16px;">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请确认新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="pwdLoading" @click="handleChangePwd">确认修改</el-button>
              <el-button @click="resetPwdForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- Avatar Upload Tab -->
        <el-tab-pane label="头像修改" name="avatar">
          <div class="avatar-section">
            <div class="current-avatar">
              <el-avatar :size="120" :src="userStore.userInfo?.avatar || ''">
                {{ userStore.userInfo?.name?.charAt(0) || 'U' }}
              </el-avatar>
              <p style="margin-top: 12px; color: #909399;">当前头像</p>
            </div>
            <div class="upload-area">
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :before-upload="beforeUpload"
                :http-request="customUpload"
              >
                <el-icon :size="40" class="upload-icon"><Plus /></el-icon>
                <div class="upload-text">点击上传新头像</div>
              </el-upload>
              <p style="margin-top: 8px; color: #909399; font-size: 12px;">支持 jpg、png 格式，不超过 2MB</p>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { changePassword, uploadAvatar } from '@/api/auth'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('info')

// Password change
const pwdFormRef = ref()
const pwdLoading = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

const validateConfirm = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('两次密码输入不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

async function handleChangePwd() {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return
  pwdLoading.value = true
  try {
    await changePassword({ oldPassword: pwdForm.oldPassword, newPassword: pwdForm.newPassword })
    ElMessage.success('密码修改成功，请重新登录')
    userStore.logout()
    window.location.href = '/login'
  } catch (e) {} finally {
    pwdLoading.value = false
  }
}

function resetPwdForm() {
  pwdForm.oldPassword = ''
  pwdForm.newPassword = ''
  pwdForm.confirmPassword = ''
  pwdFormRef.value?.resetFields()
}

// Avatar upload
function beforeUpload(file) {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) { ElMessage.error('只能上传 jpg/png 格式的图片'); return false }
  if (!isLt2M) { ElMessage.error('图片大小不能超过 2MB'); return false }
  return true
}

async function customUpload(options) {
  const fd = new FormData()
  fd.append('file', options.file)
  try {
    const res = await uploadAvatar(fd)
    userStore.updateUserInfo({ avatar: res.data })
    ElMessage.success('头像修改成功')
  } catch (e) {
    // Error handled by interceptor
  }
}
</script>

<style scoped>
.avatar-section {
  display: flex;
  gap: 60px;
  align-items: center;
  margin-top: 20px;
}

.current-avatar {
  text-align: center;
}

.upload-area {
  text-align: center;
}

.avatar-uploader {
  display: inline-block;
}

.avatar-uploader :deep(.el-upload) {
  width: 120px;
  height: 120px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: border-color 0.3s;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.upload-icon {
  color: #8c939d;
  margin-bottom: 4px;
}

.upload-text {
  font-size: 12px;
  color: #8c939d;
}
</style>