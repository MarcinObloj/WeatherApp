package com.weather.shared.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiConstant {
    public static final String USER_API_BASE = "/api/users";
    public static final String WEATHER_API_BASE = "/api/weather";


    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String OBSERVED_CITIES = "/observed-cities";
    public static final String ADD_CITY_TO_OBSERVED = "/{userId}/cities/{cityId}/observe";


    public static final String FORECAST = "/forecast";
    public static final String HISTORICAL = "/historical";
    public static final String MAIN_CITY = "/main-city";

    public static String WEATHER_API_URL ="https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s";
}
