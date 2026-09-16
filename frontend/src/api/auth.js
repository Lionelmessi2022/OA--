import request from '@/utils/request'

// Login
export function login(data) {
  return request.post('/user/login', data)
}

// Logout
export function logout() {
  return request.post('/user/logout')
}

// Get current user info
export function getUserInfo() {
  return request.get('/user/info')
}

// Change password
export function changePassword(data) {
  return request.put('/user/password', data)
}

// Upload avatar
export function uploadAvatar(formData) {
  return request.post('/user/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
