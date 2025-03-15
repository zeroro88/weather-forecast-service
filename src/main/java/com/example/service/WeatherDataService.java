package com.example.service;

import com.example.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherDataService {
    private WeatherData weatherData;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("weather_data.json");
        weatherData = objectMapper.readValue(resource.getInputStream(), WeatherData.class);
    }

    public Optional<String> getWeatherContext(String city) {
        if (!weatherData.getCities().containsKey(city)) {
            return Optional.empty();
        }

        WeatherData.CityData cityData = weatherData.getCities().get(city);
        String season = getCurrentSeason();
        WeatherData.SeasonData seasonData = cityData.getSeasons().get(season);

        return Optional.of(String.format(
            "参考数据：%s属于%s，%s季节的典型天气特征是%s，温度范围在%s℃。",
            city,
            cityData.getClimate(),
            season,
            String.join("、", seasonData.getTypicalWeather()),
            seasonData.getTempRange()
        ));
    }

    private String getCurrentSeason() {
        int month = LocalDateTime.now().getMonthValue();
        if (month >= 3 && month <= 5) return "spring";
        if (month >= 6 && month <= 8) return "summer";
        if (month >= 9 && month <= 11) return "autumn";
        return "winter";
    }
} 