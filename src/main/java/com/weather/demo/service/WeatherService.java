package com.weather.demo.service;

import com.weather.demo.entity.City;
import com.weather.demo.entity.Forecast;
import com.weather.demo.entity.User;
import com.weather.demo.repository.CityRepository;
import com.weather.demo.repository.ForecastRepository;
import com.weather.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class WeatherService {

    private final UserRepository userRepository;
    private final ForecastRepository forecastRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CityRepository cityRepository;
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    public WeatherService(UserRepository userRepository, ForecastRepository forecastRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
    }

    public List<Forecast> getForecastForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<City> cities = user.getFavoriteCities();
        return forecastRepository.findByCityIn(cities);
    }

    public List<Forecast> getHistoricalDataForCity(Long cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(() -> new IllegalArgumentException("City not found"));
        return forecastRepository.findByCity(city);
    }

    public Forecast getLatestForecastForCity(City city) {
        return forecastRepository.findTopByCityOrderByTimestampDesc(city);
    }
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void fetchWeatherData() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            for (City city : user.getFavoriteCities()) {
                String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s",
                        city.getLatitude(), city.getLongitude(), apiKey);
                WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
                System.out.println(response);
                if (response != null) {
                    System.out.println("Weather Response: " + response);
                    System.out.println("Rain Data: " + response.getRain());
                    Forecast forecast = new Forecast();
                    forecast.setCity(city);
                    forecast.setTemperature(response.getMain().getTemp() - 273.15);
                    forecast.setHumidity(response.getMain().getHumidity());
                    forecast.setFeelsLikeTemperature(response.getMain().getFeelsLike() - 273.15);
                    forecast.setMainWeather(response.getWeather().get(0).getMain());

                    if (response.getRain() != null && response.getRain().get1h() != null) {
                        forecast.setRainChance(response.getRain().get1h().toString());
                    } else {
                        forecast.setRainChance("0");
                    }
                    forecast.setSunset(LocalDateTime.ofEpochSecond(response.getSys().getSunset(), 0, ZoneOffset.UTC));
                    forecast.setTimestamp(LocalDateTime.now());
                    forecastRepository.save(forecast);
                }
            }
        }
    }
}