package zerobase.weather.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.exception.DiaryException;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static zerobase.weather.exception.ErrorCode.DIARY_NOT_FOUND;
import static zerobase.weather.exception.ErrorCode.INVALID_DATE;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {
    @Mock
    DiaryRepository diaryRepository;

    @Mock
    @SpyBean
    DateWeatherRepository dateWeatherRepository;

    @InjectMocks
    DiaryService diaryService;

//    @BeforeEach
//    void setUp() {
//        ReflectionTestUtils.setField(diaryService, "key",
//                "dd894f938bc590a12be5ca2f0fced472");
//    }

    @Test
    @DisplayName("일기 생성 성공 - dateWeather가 있는 경우")
    void createDiary() {
        // given
        given(dateWeatherRepository.findAllByDate(any()))
                .willReturn(List.of(dateWeather()));

        given(diaryRepository.save(any()))
                .willReturn(diary());

        // when
        // then
        assertDoesNotThrow(() ->
                diaryService.createDiary(LocalDate.now(), "dd"));
    }

    @Test
    @DisplayName("일기 생성 성공 - dateWeather가 없는 경우")
    void createDiary_noDateWeather() {
        // given
        given(dateWeatherRepository.findAllByDate(any()))
                .willReturn(List.of());

        given(diaryRepository.save(any()))
                .willReturn(diary());

        // when
        // then
        assertDoesNotThrow(() ->
                diaryService.createDiary(LocalDate.now(), "dd"));
    }

    @Test
    @DisplayName("해당 날짜의 일기 조회")
    void getDiaries() {
        // given
        List<Diary> diaries = List.of(diary(), diary());

        given(diaryRepository.findAllByDate(any()))
                .willReturn(diaries);

        // when
        List<DiaryDto> diariesByDate =
                diaryService.getDiaries(LocalDate.now());

        // then
        assertEquals(2, diariesByDate.size());
    }

    @Test
    @DisplayName("너무 먼 미래의 날짜 조회 - 일기 조회 실패")
    void getDiaries_invalidDate() {
        // given
        LocalDate date = LocalDate.ofYearDay(3050, 2);

        // when
        DiaryException exception = assertThrows(DiaryException.class,
                () -> diaryService.getDiaries(date));

        // then
        assertEquals(INVALID_DATE, exception.getErrorCode());
    }

    @Test
    @DisplayName("너무 과거의 날짜 조회 - 일기 조회 실패")
    void getDiaries_invalidDate_past() {
        // given
        LocalDate date = LocalDate.ofYearDay(1880, 1);

        // when
        DiaryException exception = assertThrows(DiaryException.class,
                () -> diaryService.getDiaries(date));

        // then
        assertEquals(INVALID_DATE, exception.getErrorCode());
    }

    @Test
    @DisplayName("시작 날짜와 끝 날짜의 일기 조회")
    void getDiariesBetween() {
        // given
        Diary diary = Diary.builder()
                .id(1L)
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.of(2023, Month.OCTOBER, 18))
                .build();

        Diary diary2 = Diary.builder()
                .id(1L)
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.of(2023, Month.OCTOBER, 23))
                .build();
        List<Diary> diaries = List.of(diary, diary2);

        given(diaryRepository.findAllByDateBetween(any(), any()))
                .willReturn(diaries);

        // when
        List<DiaryDto> diariesByDate =
                diaryService.getDiariesBetween(
                        LocalDate.of(2023, Month.OCTOBER, 18),
                        LocalDate.of(2023, Month.OCTOBER, 23)
                );

        // then
        assertEquals(2, diariesByDate.size());
    }

    @Test
    @DisplayName("너무 먼 미래 또는 과거의 날짜 조회 - 시작 날짜와 끝 날짜의 일기 조회 실패")
    void getDiariesBetween_invalidDate() {
        // given
        LocalDate startDate = LocalDate.ofYearDay(3080, 1);
        LocalDate endDate = LocalDate.ofYearDay(1880, 1);

        // when
        DiaryException exception = assertThrows(DiaryException.class,
                () -> diaryService.getDiariesBetween(startDate, endDate));

        // then
        assertEquals(INVALID_DATE, exception.getErrorCode());
    }

    @Test
    @DisplayName("일기 수정 성공")
    void updateDiary() {
        // given
        given(diaryRepository.findFirstByDate(any()))
                .willReturn(Optional.of(diary()));

        // when
        // then
        assertDoesNotThrow(() ->
                diaryService.updateDiary(LocalDate.now(), "일기 수정"));
    }

    @Test
    @DisplayName("해당 일기 없음 - 일기 수정 실패")
    void updateDiary_diaryNotFound() {
        // given
        given(diaryRepository.findFirstByDate(any()))
                .willReturn(Optional.empty());

        // when
        DiaryException exception = assertThrows(DiaryException.class,
                () -> diaryService.updateDiary(LocalDate.now(),
                        "일기 수정"));

        // then
        assertEquals(DIARY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("일기 삭제")
    void deleteDiary() {
        // given
        doNothing().when(diaryRepository).deleteAllByDate(any());

        // when
        // then
        assertDoesNotThrow(() ->
                diaryService.deleteDiary(LocalDate.now()));
    }

    private static Diary diary() {
        return Diary.builder()
                .id(1L)
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.now())
                .build();
    }

    private static DateWeather dateWeather() {
        return DateWeather.builder()
                .date(LocalDate.now())
                .weather("Clear")
                .temperature(293.14)
                .build();
    }
}