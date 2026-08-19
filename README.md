# 立冷科技 LIQUICOOL · 数据中心液冷解决方案系统

Vue3 + Element Plus + Spring Boot + MySQL

## 快速演示（给领导看）

### 1. 导入数据库

双击根目录 **`导入数据库.bat`**  
（MySQL 账号 `root` / 密码 `123456`，会自动创建库 `liquicool` 并写入演示数据）

或命令行：

```bat
mysql -uroot -p123456 --default-character-set=utf8mb4 < database\init.sql
```

### 2. 启动后端

```bat
cd backend
mvn spring-boot:run
```

### 3. 启动前端

```bat
cd frontend
npm install
npm run dev
```

### 4. 访问地址

| 说明 | 地址 |
|------|------|
| 门户首页（无需登录） | http://localhost:5173/ |
| 门户网站 | http://localhost:5173/portal/home |
| 管理员登录 | http://localhost:5173/admin/login |
| 管理后台 | http://localhost:5173/admin/dashboard |
| 用户登录（收藏/咨询） | http://localhost:5173/login |
| **接口文档** | http://localhost:8080/swagger-ui.html |

### 默认账号

- 管理员：`admin` / `123456`（登录页可点快捷登录）
- 普通用户：`user1` / `123456`

## 目录结构

```
backend/     Spring Boot 后端
frontend/    门户 + 后台（同一 Vue 工程）
database/    init.sql
uploads/     上传/演示图片
导入数据库.bat
```

## 说明

- 密码 MD5 加密
- 图片统一走文件上传，演示图已放在 `uploads/`
- 后台菜单二级归类、默认折叠、面包屑 + 卡片标签导航
- 登录页左下角「代码」可定位到当前页源码（需本机 VS Code）
