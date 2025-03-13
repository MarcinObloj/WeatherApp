package com.weather.module.weather.controller;

import com.weather.database.mysql.entity.City;
import com.weather.database.mysql.entity.Forecast;
import com.weather.module.weather.service.WeatherService;
import com.weather.module.user.service.UserService;
import com.weather.shared.util.ApiConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstant.WEATHER_API_BASE)
public class WeatherController {

    private final WeatherService weatherService;
    private final UserService userService;



    @GetMapping(ApiConstant.FORECAST+"/{userId}")
    public List<Forecast> getForecastForUser(@PathVariable Long userId) {
        return weatherService.getForecastForUser(userId);
    }

    @GetMapping(ApiConstant.HISTORICAL+"/{cityId}")
    public List<Forecast> getHistoricalDataForCity(@PathVariable Long cityId) {
        return weatherService.getHistoricalDataForCity(cityId);
    }

    @PostMapping("/{userId}"+ApiConstant.MAIN_CITY)
    public ResponseEntity<?> setMainCity(@PathVariable Long userId, @RequestBody City city) {
        userService.setMainCity(userId, city.getId());
        return ResponseEntity.ok("Main city set successfully");
    }

    @GetMapping("/{userId}"+ApiConstant.MAIN_CITY)
    public ResponseEntity<?> getMainCityWithForecast(@PathVariable Long userId) {
        return weatherService.getMainCityWithForecast(userId);
    }
}