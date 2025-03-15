package com.example.controller;

import com.example.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/check")
    public String checkWeather(@RequestParam("query") String query) {
        String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
        return weatherService.checkWeather(decodedQuery);
    }
} 