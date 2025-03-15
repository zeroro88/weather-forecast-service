package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WeatherData {
    private Map<String, CityData> cities;

    @Data
    public static class CityData {
        private String climate;
        private Map<String, SeasonData> seasons;
    }

    @Data
    public static class SeasonData {
        @JsonProperty("temp_range")
        private String tempRange;
        @JsonProperty("typical_weather")
        private List<String> typicalWeather;
    }
} 