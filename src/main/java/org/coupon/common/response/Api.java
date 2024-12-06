package org.coupon.common.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Api<T> {
    private final int resultCode;
    private final T data;

    private Api(int resultCode, T data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public static <T> Api<T> success(T data) {
        return new Api<>(
                HttpStatus.OK.value(),
                data
        );
    }
}
