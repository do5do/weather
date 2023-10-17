package zerobase.weather.dto;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class CreateDiary {

    @Getter
    public static class Request {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate date;
        private String text;
        private String city;
    }

    @Getter
    public static class Response {
        private Long id;

        public Response(Long id) {
            this.id = id;
        }

        public static Response from(DiaryDto diaryDto) {
            return new Response(diaryDto.getId());
        }
    }
}
