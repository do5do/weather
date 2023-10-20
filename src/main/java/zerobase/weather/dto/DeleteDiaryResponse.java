package zerobase.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDiaryResponse {
    private Long id;

    public static DeleteDiaryResponse from(DiaryDto diaryDto) {
        return new DeleteDiaryResponse(diaryDto.getId());
    }
}
