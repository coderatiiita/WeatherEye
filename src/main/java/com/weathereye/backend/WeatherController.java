package com.weathereye.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather")
    @RateLimiter(name = "weatherApi")
    public ResponseEntity<String> getWeather(@RequestParam String city) {
        String result = weatherService.getWeather(city);
        if (result == null) {
            return ResponseEntity.internalServerError().body("Failed to get weather");
        }
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> rateLimitExceeded() {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests");
    }
}
