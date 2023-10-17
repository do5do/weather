package zerobase.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.dto.WeatherApiResponse;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Value("${key.open-weather-map}")
    private String key;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public DiaryDto createDiary(LocalDate date, String text, String city) {
        WeatherApiResponse weatherApiResponse = getWeather(city);
        return DiaryDto.fromEntity(diaryRepository.save(Diary.of(
                weatherApiResponse, text, date)));
    }

    private WeatherApiResponse getWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("q", city)
                .queryParam("appid", key)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(uri.toUri(), HttpMethod.GET,
                            new HttpEntity<>(headers), String.class);

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(response.getBody(), WeatherApiResponse.class);
        } catch (JsonProcessingException | RestClientException e) {
            log.error("error occurred ", e);
            throw new RuntimeException("예외처리 필요 (올바르지 않은 요청)");
        }
    }

    public List<DiaryDto> getDiaryByDate(LocalDate date) {
        return diaryRepository.findAllByDate(date).stream()
                .map(DiaryDto::fromEntity)
                .toList();
    }
}
