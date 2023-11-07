package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.exception.DiaryException;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;
import zerobase.weather.openapi.WeatherScrapper;

import java.time.LocalDate;
import java.util.List;

import static zerobase.weather.exception.ErrorCode.DIARY_NOT_FOUND;
import static zerobase.weather.exception.ErrorCode.INVALID_DATE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {
    private final WeatherScrapper weatherScrapper;
    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {
        diaryRepository.save(Diary.of(getDateWeather(date), text));
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> findDateWeathers =
                dateWeatherRepository.findAllByDate(date);

        if (findDateWeathers.isEmpty()) { // 현재 날씨 가져오기
            return DateWeather.of(weatherScrapper.getWeather());
        }

        return findDateWeathers.get(0);
    }

    public List<DiaryDto> getDiaries(LocalDate date) {
        validateDate(date);
        return diaryRepository.findAllByDate(date).stream()
                .map(DiaryDto::fromEntity)
                .toList();
    }

    public List<DiaryDto> getDiariesBetween(LocalDate startDate,
                                            LocalDate endDate) {
        validateDate(startDate);
        validateDate(endDate);
        return diaryRepository.findAllByDateBetween(startDate, endDate)
                .stream()
                .map(DiaryDto::fromEntity)
                .toList();
    }

    private static void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.ofYearDay(3050, 1)) ||
                date.isBefore(LocalDate.ofYearDay(1990, 1))) {
            throw new DiaryException(INVALID_DATE);
        }
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
