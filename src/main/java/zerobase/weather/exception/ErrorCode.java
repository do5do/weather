package zerobase.weather.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST("잘못된 요청입니다."),
    DIARY_NOT_FOUND("해당 다이어리가 없습니다.");

    private final String description;
}
