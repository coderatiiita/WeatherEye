package com.weathereye.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import jakarta.annotation.PostConstruct;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.time.Duration;
import java.io.IOException;

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final StringRedisTemplate redisTemplate;
    private WeatherApi weatherApi;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.cache.ttl}")
    private long cacheTtlSeconds;

    public WeatherService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl.endsWith("/") ? apiUrl : apiUrl + "/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        weatherApi = retrofit.create(WeatherApi.class);
    }

    public String getWeather(String city) {
        String key = "weather:" + city.toLowerCase();
        String cached = null;
        try {
            cached = redisTemplate.opsForValue().get(key);
        } catch (RedisConnectionFailureException e) {
            logger.warn("Redis unavailable, continuing without cache", e);
        }
        if (cached != null) {
            return cached;
        }

        Call<String> call = weatherApi.getWeather(city, "metric", apiKey, "json");
        try {
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                String body = response.body();
                if (body != null) {
                    try {
                        redisTemplate.opsForValue().set(key, body, Duration.ofSeconds(cacheTtlSeconds));
                    } catch (RedisConnectionFailureException e) {
                        logger.warn("Failed to cache weather data", e);
                    }
                }
                return body;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
