package com.weather.demo.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private Main main;
    private List<Weather> weather;
    private Rain rain;
    private Sys sys;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private Double temp;
        private Double feels_like;
        private Double humidity;

        public Double getFeelsLike() {
            return feels_like;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String main;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        private long sunset;
    }

    // Define a Rain class to handle the "1h" key in the API response
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rain {
        private Double _1h;  // Map the "1h" key to _1h (or use @JsonProperty("1h"))

        // Getter and setter for "1h"
        public Double get1h() {
            return _1h;
        }

        public void set1h(Double _1h) {
            this._1h = _1h;
        }
    }
}
