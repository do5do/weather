package zerobase.weather.exception;

import lombok.Getter;

@Getter
public class DiaryException extends RuntimeException {
    private final ErrorCode errorCode;

    public DiaryException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
