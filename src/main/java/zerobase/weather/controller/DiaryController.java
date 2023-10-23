package zerobase.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/create/diary")
    public void createDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text) {
        diaryService.createDiary(date, text);
    }

    @GetMapping("/read/diary")
    public List<DiaryDto> getDiaries(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.getDiaries(date);
    }

    @GetMapping("/read/diaries")
    public List<DiaryDto> getDiariesBetween(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return diaryService.getDiariesBetween(startDate, endDate);
    }

    @PutMapping("/update/diary")
    public void updateDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text) {
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    public void deleteDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }
}
