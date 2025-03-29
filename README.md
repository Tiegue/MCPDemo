This demo is my hands-on project.
https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html
# McpServer

## Denpendencies

## Beans

## Tools

## porperties
spring.application.name=mcpServer
spring.main.web-application-type=none

spring.ai.mcp.server.name=dan-vega-mcp1111 // the name will show in claude desktop No mater what name in config file.
spring.ai.mcp.server.version=0.0.0.1

// Very important
spring.main.banner-mode=off
logging.pattern.console=

## Claude_desktop_config.json
idea ~/Library/Application\ Support/Claude/claude_desktop_config.json
"spring-ai-mcp-weather": {
"command": "java",
"args": [
"-Dspring.ai.mcp.server.stdio=true",
That tells Spring AI MCP to connect to Claude Desktop using STDIN/STDOUT — a pipe for communication. Without that flag, your server doesn't act like a proper MCP tool provider, so it just exits quietly.
"-jar",
"/Users/tiegue/Dev/mcpdemo/mcpserver/target/mcpserver-0.0.1-SNAPSHOT.jar"
]

    }

# McpClient
## porperties
spring.application.name=mcpClient
#spring.ai.mcp.client.stdio.servers-configuration=/Users/tiegue/Library/Application Support/Claude/claude_desktop_config.json
it tell MCP clients automatically load the claude_desktop_config.json.


