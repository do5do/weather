package zerobase.weather.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String description;
}
