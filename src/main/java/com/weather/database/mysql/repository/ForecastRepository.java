package com.weather.database.mysql.repository;

import com.weather.database.mysql.entity.City;
import com.weather.database.mysql.entity.Forecast;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT f FROM Forecast f WHERE f.city = :city ORDER BY f.timestamp DESC")
    List<Forecast> findByCityOrderByTimestampDesc(City city, Pageable pageable);
}
