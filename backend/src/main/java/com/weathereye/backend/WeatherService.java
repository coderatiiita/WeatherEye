package com.weathereye.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Service
public class WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final StringRedisTemplate redisTemplate;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getWeather(String city) {
        String key = "weather:" + city.toLowerCase();
        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        String uri = UriComponentsBuilder.fromHttpUrl(apiUrl + "/" + city)
                .queryParam("unitGroup", "metric")
                .queryParam("key", apiKey)
                .queryParam("contentType", "json")
                .toUriString();
        String response = restTemplate.getForObject(uri, String.class);
        if (response != null) {
            redisTemplate.opsForValue().set(key, response, Duration.ofHours(12));
        }
        return response;
    }
}
