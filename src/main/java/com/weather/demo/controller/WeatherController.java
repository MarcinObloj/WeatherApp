package com.weather.demo.controller;

import com.weather.demo.entity.City;
import com.weather.demo.entity.Forecast;
import com.weather.demo.entity.User;
import com.weather.demo.service.UserService;
import com.weather.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;
    private final UserService  userService;
    @Autowired
    public WeatherController(WeatherService weatherService, UserService userService) {
        this.weatherService = weatherService;
        this.userService = userService;
    }
    @GetMapping("/forecast/{userId}")
    public List<Forecast> getForecastForUser(@PathVariable Long userId){
        return weatherService.getForecastForUser(userId);
    }
    @GetMapping("/historical/{cityId}")
    public List<Forecast> getHistoricalDataForCity(@PathVariable Long cityId) {
        return weatherService.getHistoricalDataForCity(cityId);
    }
    @PostMapping("/{userId}/main-city")
    public ResponseEntity<?> setMainCity(@PathVariable Long userId, @RequestBody City city) {
        userService.setMainCity(userId, city.getId());
        return ResponseEntity.ok("Main city set successfully");
    }
    @GetMapping("/{userId}/main-city")
    public ResponseEntity<?> getMainCityWithForecast(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        City mainCity = user.getMainCity();
        if (mainCity == null) {
            return ResponseEntity.notFound().build();
        }
        Forecast latestForecast = weatherService.getLatestForecastForCity(mainCity);
        Map<String, Object> response = new HashMap<>();
        response.put("city", mainCity);
        response.put("forecast", latestForecast);
        return ResponseEntity.ok(response);
    }

}
