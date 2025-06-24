package com.weathereye.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather")
    public ResponseEntity<String> getWeather(@RequestParam String city) {
        String result = weatherService.getWeather(city);
        if (result == null) {
            return ResponseEntity.internalServerError().body("Failed to get weather");
        }
        return ResponseEntity.ok(result);
    }
}
