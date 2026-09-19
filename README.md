# OA 员工管理系统（前后端同仓）

一个基于 **Spring Boot 3 + Vue 3** 的企业内部 OA 员工管理系统，前后端代码统一存放在本仓库。系统支持员工/部门/职位管理、登录鉴权、角色权限、头像上传，并集成了 **Spring AI（小米 MiMo）智能对话**——可以用自然语言查询公司组织架构、员工档案、入职统计等。

---

## 目录结构

```
OA-Employee-Management/
├── backend/                  后端：Spring Boot 3 + MyBatis + MySQL
│   ├── pom.xml
│   ├── src/main/java/com/test/oabackend/
│   │   ├── OaBackendApplication.java
│   │   ├── common/           统一返回 Result、分页 PageResult、全局异常
│   │   ├── config/            WebConfig（静态资源/跨域/拦截器注册）
│   │   ├── controller/        EmpController / DeptController / JobController / UserController / AiController
│   │   ├── dao/              EmpMapper / DeptMapper / JobMapper
│   │   ├── domain/           Emp / Dept / Job / LoginUser / EmpQuery
│   │   ├── interceptor/      LoginInterceptor（登录态校验）
│   │   ├── service/          业务接口 + impl 实现
│   │   └── utils/            AIQueryTools（AI Tool Calling）、Md5Util
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── mapper/           EmpMapper.xml / DeptMapper.xml / JobMapper.xml
│   └── uploads/              头像上传目录
│
├── frontend/                 前端：Vue 3 + Vite + Element Plus + Pinia
│   ├── package.json
│   ├── vite.config.js        端口 8082，/api 代理到后端 8083
│   └── src/
│       ├── api/              ai / auth / department / employee / job / stat
│       ├── layout/           MainLayout.vue
│       ├── router/           路由表 + 登录/角色守卫
│       ├── stores/           Pinia：user.js
│       ├── views/            Login / Dashboard / employee / department / job / profile / ai
│       ├── mock/             Mock 数据
│       └── utils/            request.js（axios 封装）
│
└── sql/
    └── oa_db.sql             MySQL 建库建表 + 初始数据（dept / emp / job）
```

---

## 技术栈

### 后端

| 分类 | 技术 |
|---|---|
| 基础框架 | Spring Boot 3.4.5 |
| JDK | 21 |
| 持久层 | MyBatis 3.0.4、MySQL 8 |
| AI | Spring AI 1.0.1（OpenAI 兼容接口接入小米 MiMo） |
| 鉴权 | 自定义拦截器 + MD5 密码哈希 + session token |
| 构建 | Maven（mvnw  wrapper） |
| 端口 | 8083 |

### 前端

| 分类 | 技术 |
|---|---|
| 框架 | Vue 3.5 + Vue Router 4.5 |
| 状态管理 | Pinia 2.3 |
| UI | Element Plus 2.9 + @element-plus/icons-vue |
| HTTP | Axios 1.7 |
| 构建 | Vite 6 |
| 工具 | dayjs、unplugin-auto-import、unplugin-vue-components |
| 端口 | 8082 |

---

## 功能模块

### 普通员工功能

- **登录 / 退出**：账号密码登录，密码 MD5 存储
- **首页 Dashboard**：公司数据概览
- **个人中心**：查看和编辑个人信息、上传头像
- **AI 智能对话**：用自然语言查询公司组织架构和员工信息

### 管理员功能（role = admin）

- **员工管理**：分页查询、按姓名/部门/在职状态筛选、新增/编辑/删除员工、启停用
- **部门管理**：部门增删改查
- **职位管理**：职位增删改查、排序
- **数据统计**：部门人数、入职趋势等

### AI 智能对话

后端通过 **Spring AI Tool Calling** 暴露了以下只读查询工具，模型在对话中自主决定调用：

| 工具 | 作用 |
|---|---|
| `listDepartments` | 查询全部部门列表 |
| `listJobs` | 查询全部职位列表 |
| `deptHeadcount` | 各部门人数分布（一次聚合返回） |
| `countEmployees` | 统计员工数，可按部门名过滤 |
| `countNewHires` | 统计指定日期区间新入职人数 |
| `searchNewHires` | 查询指定日期区间新入职员工名单 |
| `searchEmployees` | 按姓名/部门模糊查询员工 |
| `getEmployeeDetail` | 查询某员工完整档案（工号、部门、职位、联系方式等） |

示例提问：
- "技术部现在有多少人？"
- "查一下张三的手机号和邮箱"
- "今年新入职了哪些员工？"
- "帮我统计各部门的人数分布"

AI 模型默认使用 `mimo-v2.5-pro`，通过 OpenAI 兼容协议对接小米 MiMo。

---

## 数据库设计

数据库名 `oa_db`，共三张表（见 `sql/oa_db.sql`，已含初始数据）：

| 表 | 说明 | 主要字段 |
|---|---|---|
| `dept` | 部门表 | id、name、description、create_time |
| `job` | 职位表 | id、name、sort |
| `emp` | 员工表 | id、emp_no（唯一）、username（唯一）、password（MD5）、name、gender、phone、email、avatar、dept_id、job_id、hire_date、role（admin/employee）、status |

初始数据包含 14 个部门、16 个职位、47 名员工。

---

## 快速开始

### 1. 环境要求

- JDK 21
- Maven 3.6+（或使用项目自带 `mvnw`）
- Node.js 18+
- MySQL 8

### 2. 初始化数据库

```bash
mysql -u root -p < sql/oa_db.sql
```

这会创建 `oa_db` 数据库及三张表并插入初始数据。

### 3. 配置后端

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/oa_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8
    username: root
    password: 你的MySQL密码        # ← 修改

  ai:
    openai:
      api-key: ${MIMO_API_KEY}     # ← 通过环境变量注入，不要硬编码
      base-url: https://api.xiaomimimo.com

file:
  upload-dir: D:/IDEAProject/oa-backend/uploads   # ← 改成你本机的上传目录
```

**MIMO_API_KEY 环境变量**（Windows PowerShell）：

```powershell
$env:MIMO_API_KEY = "你的MiMo API Key"
```

或在 IDEA 的 Run Configuration → Environment variables 中添加。

### 4. 启动后端

```bash
cd backend
./mvnw spring-boot:run
```

后端运行在 http://localhost:8083。

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 http://localhost:8082，已配置 `/api` 代理到 `http://localhost:8083`。

### 默认账号

| 账号 | 密码 | 角色 |
|---|---|---|
| `admin` | `123456` | 管理员 |
| `zhangsan` | `123` | 普通员工 |
| `user3` ~ `user47` | `123456` | 普通员工 |

> 密码均以 MD5 存储于数据库。

---

## 前后端交互

```
浏览器 :8082
   │  /api/xxx（Vite devServer proxy）
   ▼
后端 Spring Boot :8083
   ├── /api/auth/*       登录/登nie/当前用户
   ├── /api/emp/*        员工 CRUD、分页、导出
   ├── /api/dept/*       部门 CRUD
   ├── /api/job/*        职位 CRUD
   ├── /api/ai/chat      AI 对话（SSE 流式）
   └── /uploads/**       头像静态资源
```

- 前端 `src/utils/request.js` 统一封装 Axios，自动携带 token、处理 401 跳转登录。
- 后端 `LoginInterceptor` 拦截除登录/静态资源外的请求，校验 token。
- 路由守卫在前端根据 `role` 控制菜单可见性（员工管理/部门管理/职位管理仅 admin 可见）。

---

## 说明

- 前端 `node_modules/`、`dist/` 与后端 `target/`、`.idea/` 已通过 `.gitignore` 忽略。
- API Key 不入库，通过环境变量 `MIMO_API_KEY` 注入。
- 前端请单独用 VS Code 打开 `frontend/` 目录；后端用 IDEA 打开 `backend/pom.xml`。
