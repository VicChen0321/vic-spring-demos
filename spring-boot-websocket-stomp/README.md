# WebSocket Chat Room Demo

基於 Spring Boot 和 WebSocket STOMP 協議的即時聊天室演示應用程式。

## 專案特色

這是一個完整的 WebSocket 聊天室實作範例，展示了：

- **即時雙向通訊** - 使用 WebSocket + STOMP 協議
- **多用戶聊天** - 支援多個用戶同時在線聊天
- **連接狀態管理** - 顯示用戶上線/離線狀態
- **容器化部署** - 使用 Docker 快速部署
- **現代化前端** - 簡潔的 HTML/CSS/JavaScript 界面

## 快速體驗

### 方式一：Docker 一鍵啟動（推薦）

```bash
# 構建並啟動容器
make start

# 在瀏覽器中打開
open http://localhost:8080
```

### 方式二：本地開發模式

```bash
# 啟動應用程式
./gradlew bootRun

# 訪問 http://localhost:8080
```

## 使用說明

1. **開啟聊天室**：在瀏覽器中訪問 `http://localhost:8080`
2. **輸入用戶名**：在輸入框中輸入你的用戶名
3. **開始聊天**：點擊連接後即可開始發送訊息
4. **多用戶測試**：開啟多個瀏覽器分頁模擬多用戶聊天

## Docker 管理命令

```bash
make build     # 構建 Docker 映像
make run       # 運行容器
make stop      # 停止並移除容器
make logs      # 查看容器日誌
make restart   # 重啟容器
make clean     # 清理映像和容器
make status    # 檢查容器狀態
make help      # 顯示所有可用命令
```

## 技術架構

### 技術棧
- **Spring Boot 3.5.7** - 主框架
- **Spring WebSocket** - WebSocket 支援
- **STOMP Protocol** - 訊息傳輸協議
- **Java 21** - 程式語言
- **Gradle** - 構建工具

## 專案結構

```
src/main/java/com/vicchen/spring/boot/websocket/stomp/
├── SpringBootWebsocketApplication.java    # 主應用程式類
├── config/                                # WebSocket 配置
├── controller/                            # 訊息控制器
├── model/                                 # 訊息模型
└── listener/                              # 事件監聽器

src/main/resources/
├── static/                                # 靜態資源
│   ├── index.html                         # 聊天室頁面
│   ├── css/                               # 樣式文件
│   └── js/                                # JavaScript 文件
└── application.yml                        # 應用程式配置
```

## WebSocket 端點

- **連接端點**：`/ws` (支援 SockJS)
- **發送訊息**：`/app/sendMessage`
- **用戶加入**：`/app/addUser`
- **廣播頻道**：`/topic/public`

## 開發說明

### 本地開發環境要求
- Java 21+
- Gradle 8.9+
- Docker（可選）

### 修改代碼後重新部署
```bash
# 停止現有容器
make stop

# 重新構建並啟動
make start
```

### 查看應用程式日誌
```bash
# 查看容器日誌
make logs

# 或者本地開發模式下直接查看控制台輸出
```

## 演示場景

1. **基本聊天功能**：多個用戶同時發送訊息
2. **用戶狀態管理**：用戶加入/離開聊天室的通知
3. **即時性展示**：訊息即時傳遞，無需刷新頁面
4. **容器化部署**：展示現代化的應用程式部署方式

## 擴展建議

這個 Demo 可以進一步擴展為：
- 添加用戶認證
- 實作聊天室分組
- 添加訊息持久化
- 支援文件傳輸
- 添加表情符號支援
