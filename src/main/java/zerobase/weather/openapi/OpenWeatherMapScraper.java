package zerobase.weather.openapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import zerobase.weather.dto.WeatherApiResponse;
import zerobase.weather.exception.ApiBadRequestException;

import java.io.IOException;

import static zerobase.weather.exception.ErrorCode.INVALID_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Component
public class OpenWeatherMapScraper implements WeatherScrapper {
    @Value("${key.open-weather-map}")
    private String key;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    public WeatherApiResponse getWeather() {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("q", "seoul")
                .queryParam("appid", key)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(uri.toUri(), HttpMethod.GET,
                            new HttpEntity<>(headers), String.class);

            objectMapper.configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            return objectMapper.readValue(response.getBody(),
                    WeatherApiResponse.class);
        } catch (HttpClientErrorException | IOException e) {
            log.error("OpenWeatherMap API HttpClientErrorException is occurred. ", e);
            throw new ApiBadRequestException(INVALID_REQUEST);
        }
    }
}
