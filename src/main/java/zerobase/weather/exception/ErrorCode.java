package zerobase.weather.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST("잘못된 요청입니다."),
    INVALID_DATE_FORMAT("잘못된 날짜 형식입니다."),
    DIARY_NOT_FOUND("해당 다이어리가 없습니다."),
    INVALID_DATE("너무 과거 혹은 미래의 날짜입니다."),
    INTERNAL_SERVER_ERROR("예상치 못한 에러가 발생했습니다.");

    private final String description;
}
