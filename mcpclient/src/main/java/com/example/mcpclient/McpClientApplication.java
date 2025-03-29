package com.example.mcpclient;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class McpClientApplication {

	public static void main(String[] args) {
//		SpringApplication.run(McpClientApplication.class, args);

		// 👇 Mcp server path
		String serverJarPath = "/Users/tiegue/Dev/mcpdemo/mcpserver/target/mcpserver-0.0.1-SNAPSHOT.jar";

		// Build server parameters
		var stdioParams = ServerParameters.builder("java")
				.args("-jar", serverJarPath)
				.build();

		// Initialize client
		var stdioTransport = new StdioClientTransport(stdioParams);
		var mcpClient = McpClient.sync(stdioTransport).build();

		try {
			mcpClient.initialize();

			// List tools
			McpSchema.ListToolsResult toolsList = mcpClient.listTools();
			System.out.println("Available tools:");
			toolsList.tools().forEach(tool -> System.out.println(" - " + tool.name()));

			// Call weather forecast tool
			McpSchema.CallToolResult weather = mcpClient.callTool(
					new McpSchema.CallToolRequest("getWeatherForecastByLocation",
							Map.of("latitude", "47.6062", "longitude", "-122.3321")));
			System.out.println("🌤 Forecast Result:\n" + weather.content());

			// Call alert tool
			McpSchema.CallToolResult alert = mcpClient.callTool(
					new McpSchema.CallToolRequest("getAlerts", Map.of("state", "NY")));
			System.out.println("🚨 Alert Result:\n" + alert.content());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mcpClient.closeGracefully();
		}
	}


}
