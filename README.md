# 个人博客系统

基于 Vue3 + Spring Boot + MyBatis + MySQL 的全栈个人博客系统

## 技术栈

- **前端**: Vue3 + Vite + Element Plus + Vue Router + Pinia + Axios
- **后端**: Spring Boot 2.7 + Spring Security + MyBatis-Plus + MySQL 8.0
- **构建工具**: Maven, Vite

## 项目结构

```
personal_blog/
├── backend/              # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/blog/personal_blog/
│   │   │   │   ├── common/      # 通用类
│   │   │   │   ├── config/      # 配置类
│   │   │   │   ├── controller/  # 控制器
│   │   │   │   ├── entity/      # 实体类
│   │   │   │   ├── exception/   # 异常处理
│   │   │   │   ├── mapper/      # MyBatis Mapper
│   │   │   │   ├── service/     # 服务层
│   │   │   │   └── utils/       # 工具类
│   │   │   └── resources/
│   │   │       ├── db/           # 数据库脚本
│   │   │       └── application.yml
│   └── pom.xml
├── frontend/             # 前端项目
│   ├── src/
│   │   ├── api/          # API 接口
│   │   ├── assets/       # 静态资源
│   │   ├── components/   # 组件
│   │   ├── router/       # 路由
│   │   ├── stores/       # 状态管理
│   │   ├── utils/        # 工具函数
│   │   ├── views/        # 页面
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
└── PROJECT_PLAN.md       # 项目规划文档
```

## 已完成功能

### 第一阶段：项目初始化与基础框架搭建
- ✅ 后端 Spring Boot 项目搭建
- ✅ 前端 Vue3 + Vite 项目搭建
- ✅ MySQL 数据库配置
- ✅ MyBatis-Plus 集成
- ✅ 统一响应结果类 R&lt;T&gt;
- ✅ 全局异常处理
- ✅ JWT 工具类
- ✅ Spring Security 配置
- ✅ 数据库初始化脚本

### 第二阶段：用户模块开发
- ✅ 用户实体类
- ✅ 用户注册接口
- ✅ 用户登录接口（JWT Token）
- ✅ 用户信息查询接口
- ✅ 用户信息更新接口
- ✅ 前端登录页面
- ✅ 前端注册页面
- ✅ Token 存储与管理
- ✅ 首页展示

### 第三阶段：文章管理模块开发
- ✅ 文章、分类、标签实体类
- ✅ 文章列表查询接口（分页）
- ✅ 文章详情查询接口
- ✅ 文章创建接口
- ✅ 文章编辑接口
- ✅ 文章删除接口
- ✅ 分类列表接口
- ✅ 标签列表接口
- ✅ 前端文章列表页面（分页）
- ✅ 前端文章详情页面
- ✅ 前端文章编辑页面
- ✅ 文章发布/草稿功能

### 第四阶段：评论系统开发
- ✅ 评论实体类
- ✅ 评论列表查询接口
- ✅ 评论添加接口
- ✅ 评论删除接口
- ✅ 前端评论展示区域
- ✅ 前端评论发布功能
- ✅ 前端评论删除功能（作者）
- ✅ 文章详情页集成评论功能

### 第五阶段：个人中心与管理后台
- ✅ 管理后台统计接口
- ✅ 管理后台用户管理接口
- ✅ 前端管理后台布局
- ✅ 前端仪表板页面（统计数据展示）
- ✅ 前端文章管理页面
- ✅ 前端评论管理页面
- ✅ 前端用户管理页面
- ✅ 前端个人中心页面
- ✅ 管理后台路由配置

## 快速开始

### 1. 数据库准备

确保已安装 MySQL 8.0+，然后执行初始化脚本：

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source d:/personal_blog/backend/src/main/resources/db/init.sql
```

或修改 `backend/src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/personal_blog?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: your_password
```

### 2. 后端启动

```bash
cd backend

# 使用 Maven 编译并运行
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 3. 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

## API 接口文档

### 用户相关接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| POST | /api/user/register | 用户注册 | 否 |
| POST | /api/user/login | 用户登录 | 否 |
| GET | /api/user/{id} | 获取用户信息 | 否 |
| PUT | /api/user/{id} | 更新用户信息 | 否 |

### 文章相关接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | /api/article/list | 文章列表（分页） | 否 |
| GET | /api/article/{id} | 文章详情 | 否 |
| POST | /api/article | 创建文章 | 是 |
| PUT | /api/article/{id} | 更新文章 | 是 |
| DELETE | /api/article/{id} | 删除文章 | 是 |

### 分类相关接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | /api/category/list | 分类列表 | 否 |
| POST | /api/category | 创建分类 | 否 |

### 标签相关接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | /api/tag/list | 标签列表 | 否 |
| POST | /api/tag | 创建标签 | 否 |

### 评论相关接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | /api/comment/article/{articleId} | 文章评论列表 | 否 |
| POST | /api/comment | 发表评论 | 是 |
| DELETE | /api/comment/{id} | 删除评论 | 是 |
| GET | /api/comment/list | 全部评论列表（分页） | 否 |

### 管理后台接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | /api/admin/stats | 获取统计数据 | 否 |
| GET | /api/admin/users | 用户列表（分页） | 否 |
| DELETE | /api/admin/user/{id} | 删除用户 | 否 |

## 待开发功能

根据 PROJECT_PLAN.md，后续可继续开发：

- [x] 文章管理模块（第三阶段）
- [x] 评论系统（第四阶段）
- [x] 个人中心与管理后台（第五阶段）
- [x] 服务层架构优化 - 接口与实现分离
- [ ] 高级功能（第六阶段）
- [ ] 性能优化与安全加固（第七阶段）
- [ ] 部署与上线（第八阶段）

## 许可证

MIT License
