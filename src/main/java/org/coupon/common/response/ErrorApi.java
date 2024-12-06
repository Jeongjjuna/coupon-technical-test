package org.coupon.common.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorApi {
    private final int resultCode;
    private final String timestamp;
    private final List<String> errorMessages;

    private ErrorApi(int resultCode, String timestamp, List<String> errorMessages) {
        this.resultCode = resultCode;
        this.timestamp = timestamp;
        this.errorMessages = errorMessages;
    }

    public static ErrorApi of(int resultCode, LocalDateTime localDateTime, List<String> errorMessages) {
        return new ErrorApi(resultCode, localDateTime.toString(), errorMessages);
    }
}
