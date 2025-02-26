package com.weather.demo.controller;

import com.weather.demo.entity.City;
import com.weather.demo.entity.User;
import com.weather.demo.service.CityService;
import com.weather.demo.service.UserService;
import com.weather.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private CityService cityService;
    private JwtUtil jwtUtil;
    @Autowired
    public UserController(UserService userService, CityService cityService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.cityService = cityService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/{userId}/cities")
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User loginRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println("Login request received for user: " + loginRequest.getUsername());
            User user = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(user.getUsername());
            System.out.println("Generated token: " + token);
            response.put("token", token);
            response.put("userId",user.getId().toString());
            response.put("message", "Login successful! Welcome, " + user.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{userId}/cities/{cityId}/observe")
    public User addCityToObserved(@PathVariable Long userId, @PathVariable Long cityId) {
        City city = cityService.getCityById(cityId);
        return userService.addCityToObserved(userId, city);
    }


    @GetMapping("/{userId}/observed-cities")
    public List<City> getObservedCities(@PathVariable Long userId) {
        return userService.getObservedCities(userId);
    }
}