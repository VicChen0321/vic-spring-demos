# Spring Boot Primary-Replica Database Demo

這是一個展示 Spring Boot 自定義 Primary-Replica 資料庫連線路由的範例專案。

## 功能特色

- 🔄 **自動路由**: 根據 `@Transactional` 的 `readOnly` 屬性自動路由到不同資料庫
- 📊 **讀寫分離**: 寫入操作使用 Primary DB，讀取操作使用 Replica DB
- ⚖️ **負載均衡**: Replica 資料庫使用 Round-robin 負載均衡
- 📝 **詳細日誌**: 完整的資料庫路由和連線日誌追蹤
- 🐳 **Docker 支援**: 完整的 Docker Compose 環境

## 架構說明

```
┌─────────────────┐    ┌──────────────────┐
│   Spring Boot   │    │   PostgreSQL     │
│   Application   │    │   Primary DB     │
│                 │◄──►│   (Write)        │
│  ┌─────────────┐│    └──────────────────┘
│  │   Routing   ││           │
│  │ DataSource  ││           │ Replication
│  └─────────────┘│           ▼
│                 │    ┌──────────────────┐
│                 │◄──►│   PostgreSQL     │
│                 │    │   Replica1 DB    │
│                 │    │   (Read)         │
│                 │    └──────────────────┘
│                 │           
│                 │    ┌──────────────────┐
│                 │◄──►│   PostgreSQL     │
│                 │    │   Replica2 DB    │
│                 │    │   (Read)         │
└─────────────────┘    └──────────────────┘
```

## 核心組件

### 1. 資料庫路由配置
- `DataSourceConfig`: 配置 Primary 和 Replica 資料源
- `RoutingDataSource`: 自定義路由邏輯，支援 Round-robin
- `DataSourceContextHolder`: ThreadLocal 上下文管理

### 2. AOP 切面
- `TransactionalRoutingAspect`: 攔截 `@Transactional` 註解，自動設定路由策略

### 3. 路由規則
- `@Transactional(readOnly = true)` → Replica DB (Round-robin)
- `@Transactional` 或 `@Transactional(readOnly = false)` → Primary DB

## 快速開始

### 1. 啟動環境

```bash
# 啟動 PostgreSQL 集群和應用程式
make up

# 查看服務狀態
make status

# 查看應用程式日誌
make logs
```

### 2. 測試 API

```bash
# 建立使用者 (寫入 → Primary DB)
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '"John Doe"'

# 查詢使用者 (讀取 → Replica DB)
curl http://localhost:8080/api/users
```

### 3. 觀察日誌

應用程式會輸出詳細的路由日誌：

```
🚀 [API] POST /api/users - Creating user: John Doe
✏️ [TRANSACTION] UserService.createUser(..) -> WRITE -> Route to PRIMARY
✅ [API] POST /api/users - User created with ID: 1
🧹 [TRANSACTION] UserService.createUser(..) -> Context cleared
```

```
🚀 [API] GET /api/users - Fetching all users
📖 [TRANSACTION] UserService.findAllUsers(..) -> READ-ONLY -> Route to REPLICA
✅ [API] GET /api/users - Found 1 users
🧹 [TRANSACTION] UserService.findAllUsers(..) -> Context cleared
```

## 專案結構

```
src/main/java/com/vicchen/spring/boot/primary/replica/
├── App.java                          # 主程式入口
├── controller/
│   └── UserController.java           # REST API 控制器
├── service/
│   └── UserService.java              # 業務邏輯層
├── repository/
│   └── UserRepository.java           # 資料存取層
├── entity/
│   └── User.java                     # 實體類別
├── config/
│   ├── DataSourceConfig.java         # 資料源配置
│   ├── RoutingDataSource.java        # 路由資料源
│   ├── DataSourceContextHolder.java  # 上下文管理
│   └── DataSourceType.java           # 資料源類型
└── aspect/
    └── TransactionalRoutingAspect.java # AOP 切面
```

## 配置說明

### application-docker.yml
```yaml
spring:
  datasource:
    primary:
      url: jdbc:postgresql://postgres-primary:5432/mydb
      hikari:
        pool-name: PrimaryHikariPool
    replica1:
      url: jdbc:postgresql://postgres-replica1:5432/mydb
      hikari:
        pool-name: Replica1HikariPool
    replica2:
      url: jdbc:postgresql://postgres-replica2:5432/mydb
      hikari:
        pool-name: Replica2HikariPool
```

## 資料庫設定

### PostgreSQL Replication
- Primary DB: 支援讀寫操作
- Replica DB: 從 Primary 同步資料，僅支援讀取
- 使用 PostgreSQL 內建的 Streaming Replication

### 連線池配置
- 每個資料源使用獨立的 HikariCP 連線池
- 可透過 `hikari` 配置調整連線池參數

## 開發工具

### Makefile 命令

```bash
# 啟動所有服務
make up

# 停止所有服務
make down

# 清理所有資料 (包含 volumes)
make clean

# 重新建置並啟動
make rebuild

# 查看服務狀態
make status

# 查看應用程式日誌
make logs

# 查看所有服務日誌
make logs-all
```

### 測試腳本

```bash
# 查看所有使用者
./scripts/list-users.sh

# 建立測試使用者
./scripts/create-user.sh "Test User"
```

### 本地開發

```bash
# 本地建置
make build-local

# 本地執行
make run-local
```

## 技術棧

- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring AOP**
- **PostgreSQL 18**
- **HikariCP**
- **Docker & Docker Compose**
- **Lombok**

## 注意事項

1. **資料一致性**: Replica 可能有輕微的延遲，請考慮讀寫一致性需求
2. **事務管理**: 確保在同一事務中不要混用 Primary 和 Replica
3. **連線池監控**: 生產環境建議監控各資料源的連線池狀態
4. **故障轉移**: 目前未實作 Replica 故障轉移機制

## 擴展建議

- 實作 Replica 健康檢查和故障轉移
- 加入資料庫連線池監控
- 支援動態資料源配置
- 實作讀寫分離的效能監控

## 授權

MIT License
