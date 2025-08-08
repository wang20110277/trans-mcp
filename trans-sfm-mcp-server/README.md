# Trans SFM MCP Server

基于Spring AI的Model Control Protocol (MCP) Server实现。

## 简介

这是一个使用Spring AI框架构建的MCP（Model Control Protocol）服务器，提供了与AI模型交互的REST API接口。

## 功能特性

- 基于Spring Boot和Spring AI构建
- 支持OpenAI模型（可扩展支持其他模型）
- 提供RESTful API接口
- 支持会话历史记录管理
- 易于扩展和定制

## 项目结构

```
trans-sfm-mcp-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/trans/sfm/mcp/
│   │   │       ├── config/
│   │   │       │   └── AiConfig.java
│   │   │       ├── controller/
│   │   │       │   └── McpController.java
│   │   │       ├── dto/
│   │   │       │   ├── McpRequest.java
│   │   │       │   └── McpResponse.java
│   │   │       ├── entity/
│   │   │       │   ├── SfmTaProduct.java
│   │   │       │   └── SfmTaPrdDaily.java
│   │   │       ├── mapper/
│   │   │       │   ├── SfmTaProductMapper.java
│   │   │       │   └── SfmTaPrdDailyMapper.java
│   │   │       ├── service/
│   │   │       │   ├── SfmTaProductService.java
│   │   │       │   └── SfmTaPrdDailyService.java
│   │   │       └── McpServerApplication.java
│   │   └── resources/
│   │       └── application.yml
├── ddl/
│   └── trans-ta-npdb.sql
├── pom.xml
└── README.md
```

## API接口

### 1. 健康检查

```
GET /mcp/health
```

检查MCP服务器是否正常运行。

### 2. 聊天接口

```
POST /mcp/chat
```

发送单条消息给AI模型。

**请求示例:**
```json
{
  "message": "你好，介绍一下你自己"
}
```

**响应示例:**
```json
{
  "response": "我是基于Spring AI构建的MCP服务器...",
  "status": "success",
  "data": null
}
```

### 3. 对话补全接口

```
POST /mcp/chat/completion
```

发送包含历史对话的完整对话上下文。

**请求示例:**
```json
{
  "messages": [
    {
      "role": "user",
      "content": "你好"
    },
    {
      "role": "assistant", 
      "content": "你好！有什么我可以帮你的吗？"
    },
    {
      "role": "user",
      "content": "介绍一下Spring AI"
    }
  ]
}
```

### 4. 重置会话

```
POST /mcp/reset
```

清除当前会话历史记录。

### 5. 产品信息查询接口

```
GET /mcp/products
```

获取所有产品信息。

```
GET /mcp/products/{prdCode}
```

根据产品代码获取特定产品信息。

```
GET /mcp/products/search?prdName={prdName}
```

根据产品名称模糊搜索产品信息。

### 6. 产品行情查询接口

```
GET /mcp/prd-dailies
```

获取所有产品行情信息。

```
GET /mcp/prd-dailies/product/{prdCode}
```

根据产品代码获取该产品的行情信息。

```
GET /mcp/prd-dailies/date/{issDate}
```

根据净值日期获取该日期的产品行情信息。

```
GET /mcp/prd-dailies/product/{prdCode}/date/{issDate}
```

根据产品代码和净值日期获取特定的产品行情信息。

```
GET /mcp/prd-dailies/ta/{taCode}
```

根据TA代码获取该TA下的产品行情信息。

## 配置

在`application.yml`中配置以下参数：

```yaml
spring:
  ai:
    openai:
      api-key: your-openai-api-key
      chat:
        options:
          model: gpt-3.5-turbo
```

## 构建和运行

### 构建项目

```bash
mvn clean package
```

### 运行项目

```bash
mvn spring-boot:run
```

或者

```bash
java -jar target/trans-sfm-mcp-server-1.0.0.jar
```

## 依赖

- Spring Boot 3.2.0
- Spring AI 0.8.1
- OpenAI API
- MyBatis-Plus 3.5.5
- Lombok 1.18.30
- MySQL Connector 8.0.33

## 扩展

可以通过以下方式扩展MCP服务器：

1. 添加对其他AI模型的支持
2. 实现更复杂的对话管理逻辑
3. 添加认证和授权机制
4. 集成数据库存储对话历史