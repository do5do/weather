package zerobase.weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class CreateDiary {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotEmpty
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate date;
        private String text;

        @NotBlank
        private String city;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;

        public static Response from(DiaryDto diaryDto) {
            return new Response(diaryDto.getId());
        }
    }
}
