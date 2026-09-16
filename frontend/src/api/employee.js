import request from '@/utils/request'

// Paged employee list with search
export function getEmployeeList(params) {
  return request.get('/emp/page', { params })
}

// Get single employee
export function getEmployee(id) {
  return request.get(`/emp/${id}`)
}

// Add employee
export function addEmployee(data) {
  return request.post('/emp', data)
}

// Update employee
export function updateEmployee(data) {
  return request.put('/emp', data)
}

// Delete employee
export function deleteEmployee(id) {
  return request.delete(`/emp/${id}`)
}

// Batch delete
export function batchDeleteEmployees(ids) {
  return request.delete('/emp/batch', { data: ids })
}

// Toggle employee status (enable/disable)
export function toggleEmployeeStatus(id, status) {
  return request.put(`/emp/status/${id}`, null, { params: { status } })
}
