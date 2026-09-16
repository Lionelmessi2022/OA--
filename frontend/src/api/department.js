import request from '@/utils/request'

export function getDeptList(params) {
  return request.get('/dept/page', { params })
}

export function getAllDepts() {
  return request.get('/dept/list')
}

export function addDept(data) {
  return request.post('/dept', data)
}

export function updateDept(data) {
  return request.put('/dept', data)
}

export function deleteDept(id) {
  return request.delete(`/dept/${id}`)
}
