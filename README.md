# 云联智管 - 地产资管系统

**Slogan：云联万物 智管未来**

## 项目简介

云联智管是一个现代化的地产资产管理系统，专为地产行业设计，提供全面的资产管理、租户管理、合同管理和应收账款管理功能。

## 技术栈

### 前端
- **Vue 3** - 渐进式JavaScript框架
- **TypeScript** - JavaScript的超集，提供类型安全
- **Element Plus** - Vue 3组件库
- **Vite** - 现代化构建工具
- **Pinia** - Vue状态管理
- **Vue Router** - 路由管理

### 后端
- **Java 17** - 编程语言
- **Spring Boot 3.x** - 应用框架
- **Spring Security** - 安全框架
- **MyBatis Plus** - ORM框架
- **MySQL 8** - 数据库

### 开发工具
- **Maven** - 项目构建工具
- **Git** - 版本控制

## 系统功能

### 1. 用户管理
- 用户注册、登录、权限管理
- 角色管理和权限分配
- 用户信息维护

### 2. 资产管理
- **项目管理**：项目信息维护，支持综合体、公寓、写字楼、商业等类型
- **楼栋管理**：楼栋信息管理，关联项目
- **单元管理**：单元信息管理，支持状态跟踪
- **单元合并拆分**：灵活的单元合并和拆分功能

### 3. 租户管理
- 租户信息维护
- 企业和个人租户分类管理
- 证件信息管理

### 4. 合同管理
- 合同全生命周期管理
- 合同状态跟踪
- 租金模式配置

### 5. 应收账款管理
- 账款批次管理
- 认领流程管理
- 收付款信息跟踪

## 项目结构

```
cysz/
├── frontend/                 # 前端项目
│   ├── src/
│   │   ├── api/             # API接口
│   │   ├── components/      # 公共组件
│   │   ├── views/           # 页面组件
│   │   ├── stores/          # 状态管理
│   │   ├── utils/           # 工具类
│   │   └── router/          # 路由配置
│   ├── package.json
│   └── vite.config.ts
├── backend/                  # 后端项目
│   ├── src/main/java/com/cysz/
│   │   ├── controller/      # 控制器
│   │   ├── service/         # 业务逻辑
│   │   ├── mapper/          # 数据访问
│   │   ├── entity/          # 实体类
│   │   └── common/          # 公共类
│   ├── src/main/resources/
│   └── pom.xml
├── database/                 # 数据库脚本
│   ├── schema.sql           # 数据库结构
│   └── init-data.sql        # 初始化数据
└── docs/                     # 项目文档
```

## 快速开始

### 环境要求

- Node.js 16+
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### 数据库初始化

1. 创建数据库：
```sql
CREATE DATABASE cysz CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行数据库脚本：
```bash
mysql -u root -p cysz < database/schema.sql
mysql -u root -p cysz < database/init-data.sql
```

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 安装依赖：
```bash
mvn clean install
```

3. 启动应用：
```bash
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 前端启动

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

前端应用将在 `http://localhost:3000` 启动

## 默认账户

系统提供以下默认账户用于测试：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | 123456 | 超级管理员 | 拥有所有权限 |
| asset_admin | 123456 | 资产管理员 | 负责资产相关功能 |
| business_admin | 123456 | 业务管理员 | 负责租户、合同、账款管理 |

## 系统特性

- **响应式设计**：适配不同屏幕尺寸
- **权限控制**：基于角色的访问控制(RBAC)
- **数据校验**：前后端双重数据校验
- **操作日志**：关键操作审计跟踪
- **枚举管理**：可扩展的枚举值配置

## 开发规范

### 前端规范
- 使用TypeScript进行类型检查
- 遵循Vue 3 Composition API规范
- 使用Element Plus组件库
- 统一的代码格式化配置

### 后端规范
- 遵循RESTful API设计原则
- 使用统一的响应格式
- 异常统一处理
- 日志规范记录

### 数据库规范
- 统一的命名规范
- 必要的索引优化
- 数据完整性约束
- 软删除机制

## 部署说明

### 生产环境部署

1. **数据库配置**：
   - 修改 `application-prod.yml` 中的数据库连接信息
   - 确保数据库性能优化

2. **前端构建**：
```bash
cd frontend
npm run build
```

3. **后端打包**：
```bash
cd backend
mvn clean package -Pprod
```

4. **服务器部署**：
   - 将前端构建产物部署到Web服务器
   - 将后端jar包部署到应用服务器
   - 配置反向代理和负载均衡

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目维护者：云联智管开发团队
- 邮箱：dev@cysz.com
- 项目地址：https://github.com/cysz/cysz-system

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 完成基础功能模块
- 用户管理、资产管理、租户管理、合同管理、应收账款管理

---

**云联智管 - 让地产管理更智能**