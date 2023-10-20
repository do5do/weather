package zerobase.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.dto.CreateDiary;
import zerobase.weather.dto.DeleteDiaryResponse;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.dto.UpdateDiary;
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
    public ResponseEntity<List<DiaryDto>> getDiaries(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(diaryService.getDiariesByDate(date));
    }

    @PatchMapping("/diaries")
    public ResponseEntity<UpdateDiary.Response> updateDiary(
            @RequestBody UpdateDiary.Request request) {
        return ResponseEntity.ok(UpdateDiary.Response.from(
                diaryService.updateDiary(request.getId(), request.getText())
        ));
    }

    @DeleteMapping("/diaries/{id}")
    public ResponseEntity<DeleteDiaryResponse> deleteDiary(@PathVariable Long id) {
        return ResponseEntity.ok(DeleteDiaryResponse.from(
                diaryService.deleteDiary(id)));
    }
}
