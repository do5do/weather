package zerobase.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.dto.WeatherApiResponse;
import zerobase.weather.exception.ApiBadRequestException;
import zerobase.weather.exception.DiaryException;
import zerobase.weather.exception.ErrorCode;
import zerobase.weather.repository.DiaryRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static zerobase.weather.exception.ErrorCode.DIARY_NOT_FOUND;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Value("${key.open-weather-map}")
    private String key;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void createDiary(LocalDate date, String text) {
        diaryRepository.save(Diary.of(getWeather(), text, date));
    }

    private WeatherApiResponse getWeather() {
        String url = "https://api.openweathermap.org/data/2.5/weather";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("q", "seoul")
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
        } catch (HttpClientErrorException | IOException e) {
            log.error("API HttpClientErrorException is occurred. ", e);
            throw new ApiBadRequestException(ErrorCode.INVALID_REQUEST);
        }
    }

    public List<DiaryDto> getDiaries(LocalDate date) {
        return diaryRepository.findAllByDate(date).stream()
                .map(DiaryDto::fromEntity)
                .toList();
    }

    public List<DiaryDto> getDiariesBetween(LocalDate startDate,
                                            LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate)
                .stream()
                .map(DiaryDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary diary = diaryRepository.findFirstByDate(date).orElseThrow(
                () -> new DiaryException(DIARY_NOT_FOUND));
        diary.updateDiary(text);
    }

    @Transactional
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
