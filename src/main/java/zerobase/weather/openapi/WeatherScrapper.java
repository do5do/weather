package zerobase.weather.openapi;

import zerobase.weather.dto.WeatherApiResponse;

public interface WeatherScrapper {
    WeatherApiResponse getWeather();
}
