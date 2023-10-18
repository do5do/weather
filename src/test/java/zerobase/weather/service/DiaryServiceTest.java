package zerobase.weather.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.stylesheets.LinkStyle;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.exception.ApiBadRequestException;
import zerobase.weather.exception.ErrorCode;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {
    @Mock
    DiaryRepository diaryRepository;

    @InjectMocks
    DiaryService diaryService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(diaryService, "key", "dd894f938bc590a12be5ca2f0fced472");
    }

    @Test
    @DisplayName("일기 생성 성공")
    void createDiary() {
        // given
        given(diaryRepository.save(any()))
                .willReturn(diary());

        // when
        DiaryDto diary = diaryService.createDiary(LocalDate.now(), "dd", "busan");

        // then
        assertEquals("Clear", diary.getWeather());
        assertEquals(293.14, diary.getTemperature());
        assertEquals("오늘 날씨 일기", diary.getText());
        assertEquals(LocalDate.now(), diary.getDate());
    }

    @Test
    @DisplayName("api 잘못된 요청 - 일기 생성 실패")
    void createDiary_invalidRequest() {
        // given
        // when
        ApiBadRequestException exception = assertThrows(ApiBadRequestException.class,
                () -> diaryService.createDiary(LocalDate.now(), "dd", ""));

        // then
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    @DisplayName("날짜로 다이어리 조회")
    void getDiariesByDate() {
        // given
        List<Diary> diaries = List.of(diary(), diary());

        given(diaryRepository.findAllByDate(any()))
                .willReturn(diaries);

        // when
        List<DiaryDto> diariesByDate = diaryService.getDiariesByDate(LocalDate.now());

        // then
        assertEquals(2, diariesByDate.size());
    }

    private static Diary diary() {
        return Diary.builder()
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.now())
                .build();
    }
}