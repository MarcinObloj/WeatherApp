package com.weather.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "forecast")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "humidity", nullable = false)
    private Double humidity;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "feels_like_temperature", nullable = false)
    private double feelsLikeTemperature;

    @Column(name = "main_weather", nullable = false)
    private String mainWeather;

    @Column(name = "rain_chance", nullable = false)
    private String rainChance;

    @Column(name = "sunset", nullable = false)
    private LocalDateTime sunset;
}