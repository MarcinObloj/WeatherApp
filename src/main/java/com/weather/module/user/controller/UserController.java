package com.weather.module.user.controller;

import com.weather.database.mysql.entity.City;
import com.weather.database.mysql.entity.User;
import com.weather.module.city.service.CityService;
import com.weather.module.user.service.UserService;
import com.weather.shared.util.ApiConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstant.USER_API_BASE)
public class UserController {

    private final UserService userService;
    private final CityService cityService;

    @PostMapping(ApiConstant.REGISTER)
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        return userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
    }

    @PostMapping(ApiConstant.LOGIN)
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User loginRequest) {
        return userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @GetMapping("/{userId}/cities")
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @PostMapping(ApiConstant.ADD_CITY_TO_OBSERVED)
    public User addCityToObserved(@PathVariable Long userId, @PathVariable Long cityId) {
        City city = cityService.getCityById(cityId);
        return userService.addCityToObserved(userId, city);
    }

    @GetMapping("/{userId}"+ApiConstant.OBSERVED_CITIES)
    public List<City> getObservedCities(@PathVariable Long userId) {
        return userService.getObservedCities(userId);
    }
}