import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import mockAdapter from '@/mock'

// Mock 开关：true = 使用前端模拟数据（无需后端即可登录体验）；后端接通后改为 false
export const USE_MOCK = false

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
  withCredentials: true,
  adapter: USE_MOCK ? mockAdapter : undefined
})

// Request interceptor
request.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error)
)

// Response interceptor
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200 || res.code === 201) {
      return res
    }
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg || '请求失败'))
  },
  (error) => {
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        router.push('/login')
      } else if (status === 403) {
        ElMessage.error('没有权限访问')
      } else if (status === 500) {
        ElMessage.error('服务器内部错误')
      } else {
        ElMessage.error(error.response.data?.msg || '请求失败')
      }
    } else {
      ElMessage.error('网络异常，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request
