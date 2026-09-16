// 前端 Mock 数据（模拟后端数据库），后端接通后可删除本目录
export const departments = [
  { id: 1, name: '技术部', description: '负责产品研发与技术支持', createTime: '2023-03-01 09:00:00' },
  { id: 2, name: '人事部', description: '负责招聘、考勤与员工关系', createTime: '2023-03-02 09:00:00' },
  { id: 3, name: '财务部', description: '负责财务核算与资金管理', createTime: '2023-03-05 09:00:00' },
  { id: 4, name: '市场部', description: '负责市场推广与品牌运营', createTime: '2023-04-11 09:00:00' },
  { id: 5, name: '运营部', description: '负责产品运营与客户服务', createTime: '2023-06-18 09:00:00' },
  { id: 6, name: '行政部', description: '负责行政后勤与办公支持', createTime: '2024-01-09 09:00:00' }
]

export const jobs = [
  { id: 1, name: '前端工程师', sort: 1 },
  { id: 2, name: '后端工程师', sort: 2 },
  { id: 3, name: '测试工程师', sort: 3 },
  { id: 4, name: '产品经理', sort: 4 },
  { id: 5, name: 'UI设计师', sort: 5 },
  { id: 6, name: '人事专员', sort: 6 },
  { id: 7, name: '财务专员', sort: 7 },
  { id: 8, name: '市场专员', sort: 8 }
]

const names = ['张伟', '王芳', '李娜', '刘洋', '陈静', '杨勇', '黄敏', '周杰', '吴婷', '徐磊',
  '孙丽', '马超', '朱琳', '胡军', '郭涛', '何平', '高霞', '林峰', '罗丹', '梁宇',
  '宋佳', '唐亮', '许晴']

export const employees = names.map((name, i) => {
  const deptId = (i % 6) + 1
  const jobId = (i % 8) + 1
  return {
    id: i + 1,
    empNo: 'EMP' + String(i + 1).padStart(3, '0'),
    name,
    gender: i % 3 === 0 ? 0 : 1,
    phone: '138' + String(10000000 + i * 1377).slice(0, 8),
    email: 'user' + (i + 1) + '@oa.com',
    deptId,
    jobId,
    hireDate: '202' + (i % 4 + 1) + '-0' + (i % 9 + 1) + '-1' + (i % 9),
    status: i % 11 === 5 ? 0 : 1,
    username: 'user' + (i + 1),
    password: '123456',
    role: 'employee',
    avatar: ''
  }
})

// 登录账号：管理员 + 普通员工
export const accounts = [
  {
    id: 900, username: 'admin', password: '123456', role: 'admin', name: '系统管理员',
    gender: 1, phone: '13900000000', email: 'admin@oa.com',
    deptId: 1, jobId: 4, hireDate: '2022-01-01', avatar: '', status: 1
  },
  {
    id: 901, username: 'zhangsan', password: '123456', role: 'employee', name: '张三',
    gender: 1, phone: '13911112222', email: 'zhangsan@oa.com',
    deptId: 1, jobId: 1, hireDate: '2023-07-01', avatar: '', status: 1
  }
]
