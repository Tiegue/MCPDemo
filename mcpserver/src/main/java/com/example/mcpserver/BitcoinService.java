package com.example.mcpserver;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class BitcoinService {

    private static final String BASE_URL= "https://api.coingecko.com/api/v3";
    private  final RestTemplate restTemplate = new RestTemplate();
    private final RestClient restClient;

    public BitcoinService() {
        this.restClient = RestClient.builder().baseUrl(BASE_URL).build();
    }

    @Tool(description = "Get Bitcoin price shows currency")
    public Integer getBitcoinPriceByCurrency(String currency) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/simple/price")
                .queryParam("ids", "bitcoin")
                .queryParam("vs_currencies", currency.toLowerCase())
                .toUriString();

        Map<String, Map<String, Integer>> response = restTemplate.getForObject(url, Map.class);
        if (response != null && response.containsKey("bitcoin")) {
            return response.get("bitcoin").get(currency.toLowerCase());
        }
        return null;
    }

    @Tool(description = "Get Bitcoin price from date till now.")
    public Integer getHistoricalBitcoinPrice(LocalDate date, String currency) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/coins/bitcoin/history")
                .queryParam("date", formattedDate)
                .queryParam("localization", "false")
                .toUriString();
        System.out.println("url: " + url);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response != null && response.containsKey("market_data")) {
            Map<String, Object> marketData = (Map<String, Object>) response.get("market_data");
            Map<String, Double> currentPrice = (Map<String, Double>) marketData.get("current_price");
            Object priceObj = currentPrice.get(currency.toLowerCase());
            if (priceObj instanceof Number) {
                return ((Number) priceObj).intValue(); // Convert Double → Integer safely
            } else {
                System.err.println("Currency value is not a number: " + priceObj);
            }
        }

        return null;
    }

    public Integer getBitcoinPrice() {
        return getBitcoinPriceByCurrency("USD");
    }
}
