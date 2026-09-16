import request from '@/utils/request'

export function getJobList(params) {
  return request.get('/job/page', { params })
}

export function getAllJobs() {
  return request.get('/job/list')
}

export function addJob(data) {
  return request.post('/job', data)
}

export function updateJob(data) {
  return request.put('/job', data)
}

export function deleteJob(id) {
  return request.delete(`/job/${id}`)
}
