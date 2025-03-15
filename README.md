# Weather Forecast Service

一个基于 Spring Boot 和 LangChain4j 的天气预报服务。

## 功能特点

- 支持自然语言查询天气
- 集成大语言模型进行天气预测
- RESTful API 接口
- UTF-8 编码支持

## 快速开始

1. 克隆项目：
```bash
git clone https://github.com/你的用户名/weather-forecast-service.git
```

2. 配置 API Key：
   复制 `src/main/resources/application.properties.example` 到 `src/main/resources/application.properties`，
   并设置你的 API Key。

3. 运行项目：
```bash
mvn spring-boot:run
```

4. 测试接口：
```bash
curl "http://localhost:8080/api/weather/check?query=上海明天会不会下雨"
```

## 配置说明

在 `application.properties` 中配置：

```properties
server.port=8080
deepseek.api.key=your-api-key-here
```

## API 文档

### 查询天气
- 端点：`GET /api/weather/check`
- 参数：`query` - 天气查询问题
- 示例：`/api/weather/check?query=上海明天下不下雨`

## 技术栈

- Spring Boot
- LangChain4j
- OkHttp
- Jackson
- Maven

## 许可证

MIT License 