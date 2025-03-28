package com.example.mcpserver;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
public class WeatherService {

    private final RestClient restClient;

    public WeatherService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.weather.gov")
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (tiegue303@email.com)")
                .build();
    }

    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(
            double latitude,   // Latitude coordinate
            double longitude   // Longitude coordinate
    ) {
        try {
            // Step 1: Get forecast URL for the point
            Map<String, Object> response = restClient.get()
                    .uri("/point/{lat}, {lon}", latitude, longitude)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});

            String forecastUrl = (String) ((Map<String, Object>) pointData.get("properties")).get("forecast");

            // Step 2: Get forecast data


        } catch (RestClientException | NullPointerException e) {
            return "Failed to get weather forecast: " + e.getMessage();
        }

    }

    @Tool(description = "Get weather alerts for a US state")
    public String getAlerts(
            @ToolParam(description = "Two-letter US state code (e.g. CA, NY") String state)
   {
        // Returns active alerts including:
        // - Event type
        // - Affected area
        // - Severity
        // - Description
        // - Safety instructions
        return "getAlerts";
    }

    // ......
}