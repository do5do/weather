package zerobase.weather.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateDiary {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotEmpty
        private Long id;
        private String text;
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
