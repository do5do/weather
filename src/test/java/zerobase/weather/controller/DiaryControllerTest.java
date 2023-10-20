package zerobase.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zerobase.weather.dto.CreateDiary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.dto.UpdateDiary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
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
        given(diaryService.createDiary(any(), anyString(), anyString()))
                .willReturn(diaryDto());

        // when
        // then
        mockMvc.perform(post("/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateDiary.Request(
                                        LocalDate.now(), "today's diary",
                                        "busan")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("해당 날짜의 일기 조회")
    void getDiaries() throws Exception {
        // given
        List<DiaryDto> diaryDtos = List.of(diaryDto(), diaryDto());

        given(diaryService.getDiariesByDate(any()))
                .willReturn(diaryDtos);

        // when
        // then
        mockMvc.perform(get("/diaries?date=2023-10-18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date")
                        .value("2023-10-18"))
                .andExpect(jsonPath("$[1].date")
                        .value("2023-10-18"))
                .andDo(print());
    }

    @Test
    @DisplayName("일기 수정")
    void updateDiary() throws Exception {
        // given
        given(diaryService.updateDiary(anyLong(), anyString()))
                .willReturn(diaryDto());

        // when
        // then
        mockMvc.perform(patch("/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateDiary.Request(1L, "일기 수정")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("일기 삭제")
    void deleteDiary() throws Exception {
        // given
        given(diaryService.deleteDiary(anyLong()))
                .willReturn(diaryDto());

        // when
        // then
        mockMvc.perform(delete("/diaries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    private static DiaryDto diaryDto() {
        return DiaryDto.builder()
                .id(1L)
                .weather("Clear")
                .temperature(293.14)
                .text("오늘 날씨 일기")
                .date(LocalDate.now())
                .build();
    }
}