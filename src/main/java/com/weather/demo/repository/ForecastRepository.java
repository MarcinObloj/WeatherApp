package com.weather.demo.repository;

import com.weather.demo.entity.City;
import com.weather.demo.entity.Forecast;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

List<Forecast> findByCityIn(List<City> cities);
List<Forecast> findByCity(City city);
@Modifying
@Transactional
@Query("DELETE FROM Forecast")
void deleteAllForecast();
    Forecast findTopByCityOrderByTimestampDesc(City city);
}
