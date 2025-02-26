package com.weather.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_city_id")
    private City mainCity;
    @ManyToMany
    @JoinTable(
            name = "user_cities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private List<City> favoriteCities;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    public void addCityToFavorites(City city) {
        if (this.favoriteCities == null) {
            this.favoriteCities = new ArrayList<>();
        }
        if (!this.favoriteCities.contains(city)) {
            this.favoriteCities.add(city);
        }else{
            System.out.println("City is already in the favorites");
        }
    }

    public void removeCityFromFavorites(City city) {
        if (this.favoriteCities != null) {
            this.favoriteCities.remove(city);
        }
    }
}