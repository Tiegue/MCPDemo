package com.example.mcpserver;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

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
            Map<String, Object> pointData = restClient.get()
                    .uri("/points/{lat},{lon}", latitude, longitude)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .getBody();


            // Step 2: Use deep-safe casting via Java Map APIs
            assert pointData != null;
            Object propsObj = pointData.get("properties");
            if (!(propsObj instanceof Map)) {
                return "Invalid response: no properties found";
            }
            Map<String, Object> props = (Map<String, Object>) propsObj;

            // Step 3: Extract forecast URL
            Object forecastObj = props.get("forecast");
            if (!(forecastObj instanceof String)) {
                return "No forecast URL found in properties";
            }
            String forecastUrl = (String) forecastObj;

            // Step 2: Get forecast data
            Map<String, Object> forecastData = restClient.get()
                    .uri(forecastUrl)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .getBody();

            var periods = (Iterable<Map<String, Object>>) ((Map<String, Object>) forecastData.get("properties")).get("periods");


            var sb = new StringBuilder();
            int count = 0;
            for (Map<String, Object> period : periods) {
                sb.append(period.get("name")).append(": ")
                        .append(period.get("detailedForecast")).append("\n");
                if (++count >= 2) break; // Show only 2 periods
            }

            return sb.toString();


        } catch (RestClientException | NullPointerException e) {
            return "Failed to get weather forecast: " + e.getMessage();
        }

    }

    @Tool(description = "Get weather alerts for a US state")
    public String getAlerts(
            @ToolParam(description = "Two-letter US state code (e.g. CA, NY") String state)
    {
        try {
            String uri = UriComponentsBuilder.fromPath("/alerts/active/area/{state}")
                    .buildAndExpand(state.toUpperCase())
                    .toUriString();

            Map<String, Object> alertData = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .getBody();

            var features = (Iterable<Map<String, Object>>) alertData.get("features");

            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Map<String, Object> alert : features) {
                var props = (Map<String, Object>) alert.get("properties");
                sb.append("⚠️ ").append(props.get("event")).append("\n")
                        .append("Area: ").append(props.get("areaDesc")).append("\n")
                        .append("Severity: ").append(props.get("severity")).append("\n")
                        .append("Description: ").append(props.get("description")).append("\n")
                        .append("Instructions: ").append(props.get("instruction")).append("\n\n");
                if (++count >= 2) break; // limit to first 2 alerts
            }

            return count == 0 ? "No active alerts for " + state.toUpperCase() : sb.toString();

        } catch (RestClientException | NullPointerException e) {
            return "Failed to fetch alerts: " + e.getMessage();
        }
    }

    // ......
}