package zerobase.weather.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.exception.ApiBadRequestException;
import zerobase.weather.exception.DiaryException;
import zerobase.weather.exception.ErrorCode;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static zerobase.weather.exception.ErrorCode.DIARY_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {
    @Mock
    DiaryRepository diaryRepository;

    @InjectMocks
    DiaryService diaryService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(diaryService, "key",
                "dd894f938bc590a12be5ca2f0fced472");
    }

    @Test
    @DisplayName("일기 생성 성공")
    void createDiary() {
        // given
        given(diaryRepository.save(any()))
                .willReturn(diary());

        // when
        DiaryDto diary = diaryService.createDiary(LocalDate.now(),
                "dd", "busan");

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
        ApiBadRequestException exception =
                assertThrows(ApiBadRequestException.class,
                        () -> diaryService.createDiary(
                                LocalDate.now(), "dd", ""));

        // then
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    @DisplayName("해당 날짜의 일기 조회")
    void getDiariesByDate() {
        // given
        List<Diary> diaries = List.of(diary(), diary());

        given(diaryRepository.findAllByDate(any()))
                .willReturn(diaries);

        // when
        List<DiaryDto> diariesByDate =
                diaryService.getDiariesByDate(LocalDate.now());

        // then
        assertEquals(2, diariesByDate.size());
    }

    @Test
    @DisplayName("일기 수정 성공")
    void updateDiary() {
        // given
        given(diaryRepository.findById(any()))
                .willReturn(Optional.of(diary()));

        // when
        DiaryDto diaryDto = diaryService.updateDiary(1L, "일기 수정");

        // then
        assertEquals("일기 수정", diaryDto.getText());
    }

    @Test
    @DisplayName("해당 일기 없음 - 일기 수정 실패")
    void updateDiary_diaryNotFound() {
        // given
        given(diaryRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        DiaryException exception = assertThrows(DiaryException.class,
                () -> diaryService.updateDiary(1L, "일기 수정"));

        // then
        assertEquals(DIARY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("일기 삭제 성공")
    void deleteDiary() {
        // given
        given(diaryRepository.findById(any()))
                .willReturn(Optional.of(diary()));

        doNothing().when(diaryRepository).deleteById(anyLong());

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        // when
        DiaryDto diaryDto = diaryService.deleteDiary(2L);

        // then
        verify(diaryRepository, times(1))
                .deleteById(captor.capture());
        assertEquals(1L, captor.getValue());
        assertEquals("Clear", diaryDto.getWeather());
    }

    @Test
    @DisplayName("해당 일기 없음 - 일기 삭제 실패")
    void deleteDiary_diaryNotFound() {
        // given
        given(diaryRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        DiaryException exception = assertThrows(DiaryException.class,
                () -> diaryService.deleteDiary(1L));

        // then
        assertEquals(DIARY_NOT_FOUND, exception.getErrorCode());
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
}