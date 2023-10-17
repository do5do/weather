package zerobase.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.dto.CreateDiary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ResponseEntity<CreateDiary.Response> createDiary(
            @RequestBody CreateDiary.Request request) {
        return ResponseEntity.ok(CreateDiary.Response.from(
                diaryService.createDiary(request.getDate(),
                        request.getText(), request.getCity())));
    }

    @GetMapping("/diaries")
    public ResponseEntity<List<DiaryDto>> getDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(diaryService.getDiaryByDate(date));
    }
}
