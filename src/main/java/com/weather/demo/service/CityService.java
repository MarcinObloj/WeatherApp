package com.weather.demo.service;

import com.weather.demo.entity.City;
import com.weather.demo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {


    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> new IllegalArgumentException("City not found"));
    }
}