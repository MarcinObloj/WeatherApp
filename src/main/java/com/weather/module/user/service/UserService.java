package com.weather.module.user.service;

import com.weather.database.mysql.entity.City;
import com.weather.database.mysql.entity.Role;
import com.weather.database.mysql.entity.User;
import com.weather.database.mysql.repository.CityRepository;
import com.weather.database.mysql.repository.RoleRepository;
import com.weather.database.mysql.repository.UserRepository;
import com.weather.application.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final JwtUtil jwtUtil;



    public ResponseEntity<Map<String, String>> registerUser(String username, String password, String email) {
        Map<String, String> response = new HashMap<>();
        try {
            List<User> existingUsers = userRepository.findByUsername(username);
            if (!existingUsers.isEmpty()) {
                response.put("error", "User already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);

            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_USER");
                        return roleRepository.save(newRole);
                    });

            user.setRole(role);
            userRepository.save(user);

            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> loginUser(String username, String password) {
        Map<String, String> response = new HashMap<>();
        try {
            User user = findUserByUsername(username);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.put("error", "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = jwtUtil.generateToken(user.getUsername());
            response.put("token", token);
            response.put("userId", user.getId().toString());
            response.put("message", "Login successful! Welcome, " + user.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public User findUserByUsername(String username) {
        List<User> users = userRepository.findByUsername(username);
        if (users.size() == 1) {
            return users.get(0);
        }
        throw new IllegalArgumentException("User not found or not unique");
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User addCityToObserved(Long userId, City city) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getFavoriteCities().contains(city)) {
            user.getFavoriteCities().remove(city);
        } else {
            user.getFavoriteCities().add(city);
        }
        return userRepository.save(user);
    }

    @Transactional
    public void setMainCity(Long userId, Long cityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        City city = cityRepository.findById(cityId).orElseThrow(() -> new IllegalArgumentException("City not found"));
        user.setMainCity(city);
        userRepository.save(user);
    }

    public List<City> getObservedCities(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getFavoriteCities();
    }
}