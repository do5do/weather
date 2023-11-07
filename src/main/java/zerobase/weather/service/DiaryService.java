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
    private static final LocalDate MAX_DATE = LocalDate.ofYearDay(3050, 1);
    private static final LocalDate MIN_DATE = LocalDate.ofYearDay(1990, 1);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {
        diaryRepository.save(Diary.of(getDateWeather(date), text));
    }

    private DateWeather getDateWeather(LocalDate date) {
        return dateWeatherRepository.findByDate(date)
                .orElse(DateWeather.of(weatherScrapper.getWeather()));
    }

    public List<DiaryDto> getDiaries(LocalDate date) {
        validateDate(date);
        return diaryRepository.findAllByDate(date).stream()
                .map(DiaryDto::fromEntity)
                .toList();
    }

    public List<DiaryDto> getDiariesBetween(LocalDate startDate,
                                            LocalDate endDate) {
        validateDate(startDate, endDate);
        return diaryRepository.findAllByDateBetween(startDate, endDate)
                .stream()
                .map(DiaryDto::fromEntity)
                .toList();
    }

    private static void validateDate(LocalDate... dates) {
        for (LocalDate date : dates) {
            if (date.isAfter(MAX_DATE) || date.isBefore(MIN_DATE)) {
                throw new DiaryException(INVALID_DATE);
            }
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
