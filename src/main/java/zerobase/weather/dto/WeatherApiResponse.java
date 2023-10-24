package zerobase.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherApiResponse(List<Weather> weather, Main main) {
    public Weather getWeather() {
        return weather.get(0);
    }

    public record Weather(@JsonProperty("main") String weather,
                          String icon) {}

    public record Main(@JsonProperty("temp") Double temperature) {
    }
}
