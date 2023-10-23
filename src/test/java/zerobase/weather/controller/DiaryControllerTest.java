package zerobase.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @MockBean
    DiaryService diaryService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("일기 생성")
    void createDiary() throws Exception {
        // given
        doNothing().when(diaryService).createDiary(any(), anyString());
        String text = "today diary";

        // when
        // then
        mockMvc.perform(post("/create/diary?date=2023-10-23")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(text)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("해당 날짜의 일기 조회")
    void getDiaries() throws Exception {
        // given
        DiaryDto diaryDto = DiaryDto.builder()
                .id(1L)
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.parse("2023-10-18"))
                .build();

        List<DiaryDto> diaryDtos = List.of(diaryDto, diaryDto);

        given(diaryService.getDiaries(any()))
                .willReturn(diaryDtos);

        // when
        // then
        mockMvc.perform(get("/read/diary?date=2023-10-18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date")
                        .value("2023-10-18"))
                .andExpect(jsonPath("$[1].date")
                        .value("2023-10-18"))
                .andDo(print());
    }

    @Test
    @DisplayName("시작 날짜와 끝 날짜의 일기 조회")
    void getDiariesBetween() throws Exception {
        // given
        DiaryDto diaryDto = DiaryDto.builder()
                .id(1L)
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.parse("2023-10-18"))
                .build();

        DiaryDto diaryDto2 = DiaryDto.builder()
                .id(1L)
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.parse("2023-10-23"))
                .build();

        List<DiaryDto> diaryDtos = List.of(diaryDto, diaryDto2);

        given(diaryService.getDiariesBetween(any(), any()))
                .willReturn(diaryDtos);

        // when
        // then
        mockMvc.perform(get("/read/diaries?startDate=2023-10-18" +
                        "&endDate=2023-10-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date")
                        .value("2023-10-18"))
                .andExpect(jsonPath("$[1].date")
                        .value("2023-10-23"))
                .andDo(print());
    }

    @Test
    @DisplayName("일기 수정")
    void updateDiary() throws Exception {
        // given
        doNothing().when(diaryService).updateDiary(any(), anyString());
        String text = "today diary";

        // when
        // then
        mockMvc.perform(put("/update/diary?date=2023-10-18")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(text)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("일기 삭제")
    void deleteDiary() throws Exception {
        // given
        doNothing().when(diaryService).deleteDiary(any());

        // when
        // then
        mockMvc.perform(delete("/delete/diary?date=2023-10-18"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}