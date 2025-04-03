# Design Document: MCP Server Application

## Overview
This project, `mcpserver`, is part of the larger `mcpdemo` system. It is a Spring Boot application that exposes tools via the Spring AI Model Context Protocol (MCP) server. These tools provide AI agents (e.g., Claude, ChatGPT) with access to dynamic, real-world data including weather forecasts and Bitcoin price information through public APIs.

## Goals
- Expose functional tools via Spring AI's MCP Tooling system.
- Integrate public REST APIs (CoinGecko, Weather.gov).
- Package the app as an MCP-compliant HTTP server.

---

## Architecture
- **Framework**: Spring Boot (Java 22)
- **MCP Integration**: Via `spring-ai-mcp-server-webmvc-spring-boot-starter`
- **Tool Annotation System**: Via `@Tool` and `MethodToolCallbackProvider`
- **External APIs**:
    - [CoinGecko](https://www.coingecko.com/en/api) for Bitcoin price data
    - [weather.gov](https://www.weather.gov/documentation/services-web-api) for US weather forecasts and alerts

---

## Components

### 1. `McpServerApplication`
- The Spring Boot entry point.
- Registers two tool providers using `MethodToolCallbackProvider`: `WeatherService` and `BitcoinService`.
- Beans:
    - `weatherToolsProvider`
    - `bitcoinToolsProvider`

### 2. `WeatherService`
Annotated with `@Service` and `@Tool`. Provides two methods:

- `getWeatherForecastByLocation(double lat, double lon)`
    - Retrieves point-specific forecast data using a two-step API call.
    - Limits the output to the next 2 forecast periods.

- `getAlerts(String state)`
    - Fetches active weather alerts for a U.S. state (e.g., CA, NY).
    - Summarizes up to 2 alerts with severity, description, and instructions.

### 3. `BitcoinService`
Also a Spring `@Service` with `@Tool`-exposed methods:

- `getBitcoinPriceByCurrency(String currency)`
    - Calls CoinGecko's `/simple/price` endpoint.

- `getHistoricalBitcoinPrice(LocalDate date, String currency)`
    - Uses CoinGecko's `/coins/bitcoin/history` endpoint to fetch historical price for a given date.

- `getBitcoinPrice()`
    - Shorthand for fetching the current price in USD.

---

## Build & Dependencies

### Maven
- `spring-boot-starter-web`: Web application support
- `spring-ai-mcp-client-spring-boot-starter`: AI client tools
- `spring-ai-mcp-server-webmvc-spring-boot-starter`: Exposes tools over HTTP for MCP
- `spring-boot-starter-test`: Test support

### Version Control
- BOM import: `spring-ai-bom` for unified dependency management (version: `1.0.0-M6`)

---

## API Notes
- **CoinGecko**: No API key required, but subject to rate limits.
- **Weather.gov**: Requires `User-Agent` header for compliance. Uses a 2-step flow to resolve forecast URLs from lat/lon.

---

## Usage Scenario
Once running, the server exposes its tools to any client that speaks the MCP protocol (e.g., Claude). Tools such as:

```json
{
  "tool": "getWeatherForecastByLocation",
  "parameters": {
    "latitude": 37.7749,
    "longitude": -122.4194
  }
}
```

Or:

```json
{
  "tool": "getBitcoinPriceByCurrency",
  "parameters": {
    "currency": "EUR"
  }
}
```

Can be invoked to return live data.

---

## Future Improvements
- Add unit tests and mocks for external API responses.
- Implement caching or rate-limit handling.
- Add new financial or geolocation tools.
- Provide Docker image for deployment.

---

## Author
Tiegue Zhang  
Email: tiegue303@email.com
