import { employees, departments, jobs, accounts } from './data'

// Mock 开关：在 utils/request.js 中引用
let currentUser = null
let nextEmpId = employees.length + 1
let nextDeptId = departments.length + 1
let nextJobId = jobs.length + 1

const delay = (ms) => new Promise((r) => setTimeout(r, ms))

function ok(data, msg = '操作成功') {
  return { code: 200, msg, data }
}
function fail(msg, code = 400) {
  return { code, msg, data: null }
}

function parseBody(config) {
  if (config.data == null) return null
  if (typeof config.data === 'string') {
    try { return JSON.parse(config.data) } catch (e) { return config.data }
  }
  return config.data
}

function paginate(list, params = {}) {
  const page = Number(params.page) || 1
  const pageSize = Number(params.pageSize) || 10
  const start = (page - 1) * pageSize
  return { records: list.slice(start, start + pageSize), list: list.slice(start, start + pageSize), total: list.length, page, pageSize }
}

function withNames(emp) {
  const dept = departments.find((d) => d.id === emp.deptId)
  const job = jobs.find((j) => j.id === emp.jobId)
  return { ...emp, deptName: dept ? dept.name : '', jobName: job ? job.name : '' }
}

function now() {
  const d = new Date()
  const p = (n) => String(n).padStart(2, '0')
  return d.getFullYear() + '-' + p(d.getMonth() + 1) + '-' + p(d.getDate()) + ' ' + p(d.getHours()) + ':' + p(d.getMinutes()) + ':' + p(d.getSeconds())
}

function buildAiReply(msg) {
  if (msg.includes('考勤')) return '本月考勤统计：应出勤22天，全员平均出勤21.6天，迟到3人次，请假5人次，整体出勤率98.2%。建议关注技术部周三的迟到情况，可在员工管理模块查看明细。'
  if (msg.includes('周报')) return '本周工作周报摘要：\n1. 完成员工管理模块前后端联调；\n2. 修复部门删除时的关联校验问题；\n3. 优化登录页加载速度与交互体验。\n下周计划：推进 AI 对话模块接入 Spring AI，完善权限控制。'
  if (msg.includes('请假')) return '公司请假制度：年假5-15天（按工龄）、事假需提前1天申请、病假需提供医院证明、婚假3天、产假/陪产假按国家规定执行。请假流程：OA 系统提交申请 → 部门主管审批 → 人事备案。'
  if (msg.includes('效率')) return '提升工作效率的建议：1) 使用番茄工作法保持专注；2) 每天列出 TOP3 任务优先完成；3) 会议控制在一小时以内；4) 善用 AI 工具处理重复性工作，比如让我帮你写周报。'
  return '已收到你的问题："' + msg + '"。我是 OA 智能助手（基于 Spring AI 构建），可以回答考勤统计、公司制度、周报生成等办公问题，当前为前端 Mock 模式，后端接通后将调用真实大模型回复。'
}

// 自定义 axios adapter：拦截所有请求并返回 Mock 数据
export default async function mockAdapter(config) {
  await delay(200 + Math.random() * 300)
  const method = (config.method || 'get').toLowerCase()
  const url = (config.url || '').split('?')[0]
  const params = config.params || {}
  const body = parseBody(config)
  let result
  let m

  // ---------- 登录与个人中心 ----------
  if (url === '/user/login' && method === 'post') {
    const acc = accounts.find((a) => a.username === body.username && a.password === body.password)
    if (acc) {
      currentUser = acc
      result = ok({ ...withNames(acc), token: 'mock-token-' + Date.now() }, '登录成功')
    } else {
      result = fail('账号或密码错误')
    }
  } else if (url === '/user/logout' && method === 'post') {
    currentUser = null
    result = ok(null, '退出成功')
  } else if (url === '/user/info' && method === 'get') {
    result = currentUser ? ok(withNames(currentUser)) : fail('未登录', 401)
  } else if (url === '/user/password' && method === 'put') {
    if (!currentUser) result = fail('未登录', 401)
    else if (body.oldPassword !== currentUser.password) result = fail('原密码错误')
    else { currentUser.password = body.newPassword; result = ok(null, '密码修改成功') }
  } else if (url === '/user/avatar' && method === 'post') {
    result = ok('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', '头像上传成功')

  // ---------- 员工管理 ----------
  } else if (url === '/emp/page' && method === 'get') {
    let list = employees.map(withNames)
    if (params.name) list = list.filter((e) => e.name.includes(params.name))
    if (params.deptId) list = list.filter((e) => e.deptId === Number(params.deptId))
    if (params.jobId) list = list.filter((e) => e.jobId === Number(params.jobId))
    if (params.startDate) list = list.filter((e) => e.hireDate >= params.startDate)
    if (params.endDate) list = list.filter((e) => e.hireDate <= params.endDate)
    result = ok(paginate(list, params))
  } else if (url === '/emp/batch' && method === 'delete') {
    const ids = Array.isArray(body) ? body : []
    for (let i = employees.length - 1; i >= 0; i--) {
      if (ids.includes(employees[i].id)) employees.splice(i, 1)
    }
    result = ok(null, '批量删除成功')
  } else if ((m = url.match(/^\/emp\/status\/(\d+)$/)) && method === 'put') {
    const emp = employees.find((e) => e.id === Number(m[1]))
    if (!emp) result = fail('员工不存在')
    else { emp.status = Number(params.status); result = ok(null, '状态修改成功') }
  } else if ((m = url.match(/^\/emp\/(\d+)$/)) && method === 'get') {
    const emp = employees.find((e) => e.id === Number(m[1]))
    result = emp ? ok(withNames(emp)) : fail('员工不存在')
  } else if ((m = url.match(/^\/emp\/(\d+)$/)) && method === 'delete') {
    const idx = employees.findIndex((e) => e.id === Number(m[1]))
    if (idx < 0) result = fail('员工不存在')
    else { employees.splice(idx, 1); result = ok(null, '删除成功') }
  } else if (url === '/emp' && method === 'post') {
    const emp = {
      ...body,
      id: nextEmpId++,
      empNo: 'EMP' + String(nextEmpId).padStart(3, '0'),
      status: 1,
      password: '123456',
      role: 'employee',
      avatar: ''
    }
    employees.push(emp)
    result = ok(withNames(emp), '新增成功，初始密码为 123456')
  } else if (url === '/emp' && method === 'put') {
    const emp = employees.find((e) => e.id === body.id)
    if (!emp) result = fail('员工不存在')
    else { Object.assign(emp, body); result = ok(withNames(emp), '修改成功') }

  // ---------- 部门管理 ----------
  } else if (url === '/dept/page' && method === 'get') {
    let list = departments
    if (params.name) list = list.filter((d) => d.name.includes(params.name))
    result = ok(paginate(list, params))
  } else if (url === '/dept/list' && method === 'get') {
    result = ok(departments)
  } else if (url === '/dept' && method === 'post') {
    const dept = { id: nextDeptId++, name: body.name, description: body.description || '', createTime: now() }
    departments.push(dept)
    result = ok(dept, '新增成功')
  } else if (url === '/dept' && method === 'put') {
    const dept = departments.find((d) => d.id === body.id)
    if (!dept) result = fail('部门不存在')
    else { Object.assign(dept, body); result = ok(dept, '修改成功') }
  } else if ((m = url.match(/^\/dept\/(\d+)$/)) && method === 'delete') {
    const id = Number(m[1])
    if (employees.some((e) => e.deptId === id)) result = fail('该部门下存在员工，禁止删除')
    else {
      const idx = departments.findIndex((d) => d.id === id)
      if (idx < 0) result = fail('部门不存在')
      else { departments.splice(idx, 1); result = ok(null, '删除成功') }
    }

  // ---------- 职位管理 ----------
  } else if (url === '/job/page' && method === 'get') {
    let list = jobs
    if (params.name) list = list.filter((j) => j.name.includes(params.name))
    result = ok(paginate(list, params))
  } else if (url === '/job/list' && method === 'get') {
    result = ok(jobs)
  } else if (url === '/job' && method === 'post') {
    const job = { id: nextJobId++, name: body.name, sort: body.sort || 0 }
    jobs.push(job)
    result = ok(job, '新增成功')
  } else if (url === '/job' && method === 'put') {
    const job = jobs.find((j) => j.id === body.id)
    if (!job) result = fail('职位不存在')
    else { Object.assign(job, body); result = ok(job, '修改成功') }
  } else if ((m = url.match(/^\/job\/(\d+)$/)) && method === 'delete') {
    const id = Number(m[1])
    if (employees.some((e) => e.jobId === id)) result = fail('该职位下存在员工，禁止删除')
    else {
      const idx = jobs.findIndex((j) => j.id === id)
      if (idx < 0) result = fail('职位不存在')
      else { jobs.splice(idx, 1); result = ok(null, '删除成功') }
    }

  // ---------- AI 对话 ----------
  } else if (url === '/ai/chat' && method === 'post') {
    result = ok({ reply: buildAiReply(body.message || '') })
  } else if (url === '/ai/history' && method === 'get') {
    result = ok([])
  } else if (url === '/ai/history' && method === 'delete') {
    result = ok(null, '聊天记录已清空')

  // ---------- 未匹配接口 ----------
  } else {
    result = fail('Mock 接口不存在: ' + method + ' ' + url, 404)
  }

  return {
    data: result,
    status: 200,
    statusText: 'OK',
    headers: {},
    config,
    request: {}
  }
}
