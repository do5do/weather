package zerobase.weather.exception;

import lombok.Getter;

@Getter
public class ApiBadRequestException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApiBadRequestException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
