package com.weather.module.weather.service;

import com.weather.database.mysql.entity.City;
import com.weather.database.mysql.entity.Forecast;
import com.weather.database.mysql.entity.User;
import com.weather.database.mysql.repository.CityRepository;
import com.weather.database.mysql.repository.ForecastRepository;
import com.weather.database.mysql.repository.UserRepository;
import com.weather.module.weather.dto.WeatherDTO;
import com.weather.shared.util.ApiConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final UserRepository userRepository;
    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;



    public List<Forecast> getForecastForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return forecastRepository.findByCityIn(user.getFavoriteCities());
    }

    public List<Forecast> getHistoricalDataForCity(Long cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(() -> new IllegalArgumentException("City not found"));
        return forecastRepository.findByCity(city);
    }

    public Forecast getLatestForecastForCity(City city) {
        List<Forecast> forecasts = forecastRepository.findByCityOrderByTimestampDesc(city, PageRequest.of(0, 1));
        if (forecasts.isEmpty()) {
            throw new IllegalArgumentException("No forecast found for the provided city");
        }
        return forecasts.get(0);
    }

    public ResponseEntity<?> getMainCityWithForecast(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        City mainCity = user.getMainCity();
        if (mainCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Main city not set for the user");
        }

        Forecast latestForecast = getLatestForecastForCity(mainCity);
        Map<String, Object> response = new HashMap<>();
        response.put("city", mainCity);
        response.put("forecast", latestForecast);
        return ResponseEntity.ok(response);
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void fetchWeatherData() {
        userRepository.findAll().forEach(this::fetchWeatherDataForUser);
    }

    private void fetchWeatherDataForUser(User user) {
        user.getFavoriteCities().forEach(this::fetchWeatherDataForCity);
    }

    private void fetchWeatherDataForCity(City city) {
        String url = String.format(ApiConstant.WEATHER_API_URL,
                city.getLatitude(), city.getLongitude(), apiKey);
        WeatherDTO response = restTemplate.getForObject(url, WeatherDTO.class);
        if (response != null) {
            saveForecast(city, response);
        }
    }

    private void saveForecast(City city, WeatherDTO response) {
        Forecast forecast = new Forecast();
        forecast.setCity(city);
        forecast.setTemperature(response.getMain().getTemp() - 273.15);
        forecast.setHumidity(response.getMain().getHumidity());
        forecast.setFeelsLikeTemperature(response.getMain().getFeelsLike() - 273.15);
        forecast.setMainWeather(response.getWeather().get(0).getMain());
        forecast.setRainChance(response.getRain() != null && response.getRain().get1h() != null ? response.getRain().get1h().toString() : "0");
        forecast.setSunset(LocalDateTime.ofEpochSecond(response.getSys().getSunset(), 0, ZoneOffset.UTC));
        forecast.setTimestamp(LocalDateTime.now());
        forecastRepository.save(forecast);
    }
}