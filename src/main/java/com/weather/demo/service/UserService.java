package com.weather.demo.service;

import com.weather.demo.entity.City;
import com.weather.demo.entity.Role;
import com.weather.demo.entity.User;
import com.weather.demo.repository.CityRepository;
import com.weather.demo.repository.RoleRepository;
import com.weather.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
    }

    public User registerUser(String username, String password, String email) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        user.setEmail(email);

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        user.setRole(role);
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User loginUser(String username, String rawPassword) {
        User user = findUserByUsername(username);
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
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
