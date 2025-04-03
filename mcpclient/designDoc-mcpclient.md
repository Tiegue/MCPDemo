# Design Document: MCP Client Demo

## Overview
The `mcpclient` project is a minimal and functional demonstration of an AI tool client built using the **Model Context Protocol (MCP)**. It launches and interacts with a locally hosted MCP-compliant tool server (i.e. the `mcpserver` project), retrieves available tools, and calls them using structured MCP schema requests.

---

## Goals
- Demonstrate how to connect to an MCP server using the official Java SDK
- Execute AI tools exposed by an MCP server using `callTool`
- Show synchronous request/response pattern using `StdioClientTransport`

---

## Architecture
- **Language**: Java 22
- **MCP SDK**: `io.modelcontextprotocol.client` (official MCP Java client)
- **Transport**: Standard Input/Output (Stdio) communication with the MCP server
- **Launch Type**: Embedded invocation of Spring Boot-based MCP server `.jar`

---

## Workflow

### 1. Launch Server via `ServerParameters`
```java
var stdioParams = ServerParameters.builder("java")
    .args("-jar", serverJarPath)
    .build();
```
This builds a local process command to launch `mcpserver-0.0.1-SNAPSHOT.jar` using the standard JVM command.

### 2. Connect via `StdioClientTransport`
```java
var stdioTransport = new StdioClientTransport(stdioParams);
var mcpClient = McpClient.sync(stdioTransport).build();
```
The client communicates with the server process using JSON-RPC messages over `stdin/stdout`.

### 3. Initialize & Interact
```java
mcpClient.initialize();
```
Performs handshake and schema introspection.

---

## Functional Demos

### ✔️ List Available Tools
```java
McpSchema.ListToolsResult toolsList = mcpClient.listTools();
toolsList.tools().forEach(tool -> System.out.println(" - " + tool.name()));
```
Fetches metadata for tools exposed by the server.

### ✔️ Call Weather Forecast Tool
```java
McpSchema.CallToolResult weather = mcpClient.callTool(
    new McpSchema.CallToolRequest("getWeatherForecastByLocation",
        Map.of("latitude", "47.6062", "longitude", "-122.3321")));
```
Invokes a remote tool with parameters and prints the forecast.

### ✔️ Call Weather Alert Tool
```java
McpSchema.CallToolResult alert = mcpClient.callTool(
    new McpSchema.CallToolRequest("getAlerts", Map.of("state", "NY")));
```
Retrieves active weather alerts for a given U.S. state.

---

## Design Highlights
- **Self-contained**: The server JAR is launched from within the client process, no need for pre-running a server.
- **Stdio Transport**: Best for CLI toolchains and test environments.
- **Graceful Exit**: Client ensures cleanup with `closeGracefully()`.

---

## Future Improvements
- Replace hardcoded paths with environment variables or CLI args
- Add support for multiple transports (e.g., WebSocket, HTTP)
- Support non-blocking/async mode with `McpClient.async()`
- Extract client calls to a dedicated service class for better modularity

---

## Author
Tiegue Zhang  
Email: tiegue303@email.com
