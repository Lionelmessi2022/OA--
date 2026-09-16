# OA 系统（前后端同仓）

OA 办公自动化系统，采用前后端分离架构，前后端代码统一存放在本仓库中。

## 目录结构

```
.
├── backend/     # 后端：Spring Boot 3 + MyBatis + MySQL
└── frontend/    # 前端：Vue 3 + Vite + Element Plus + Pinia
```

## 技术栈

### 后端 backend/
- Java 21
- Spring Boot 3.4.5
- MyBatis 3.0.4
- MySQL
- Maven

### 前端 frontend/
- Vue 3.5
- Vite 6
- Element Plus 2.9
- Pinia 2.3
- Vue Router 4.5
- Axios 1.7

## 快速开始

### 启动后端

```bash
cd backend
# 修改 src/main/resources/application.yml 中的数据库连接
./mvnw spring-boot:run
```

后端默认监听端口：`8083`

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端开发服务器端口：`8082`，已配置将 `/api` 代理到后端 `http://localhost:8083`。

## 说明

- 前端 `node_modules/`、`dist/` 与后端 `target/`、`.idea/` 均已通过 `.gitignore` 忽略。
- 前端项目在 IntelliJ / VS Code 中请单独打开 `frontend/` 目录；后端 Maven 项目请打开 `backend/pom.xml`。
