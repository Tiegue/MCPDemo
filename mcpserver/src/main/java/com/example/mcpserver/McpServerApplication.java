package com.example.mcpserver;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpServerApplication.class, args);
	}

//	@PostConstruct
//	public void init() {
//		System.out.println("🟢 MCP Server started.");
//	}
//	@Bean
//	public List<ToolCallback> danTools(CourseService courseService) {
//		return List.of(ToolCallbacks.from(courseService));
//	}
	@Bean
	public ToolCallbackProvider danToolsProvider(CourseService courseService) {
		return MethodToolCallbackProvider.builder().toolObjects(courseService).build();
	}
	@Bean
	public ToolCallbackProvider weatherToolsProvider(WeatherService weatherService) {
		return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
	}
}
