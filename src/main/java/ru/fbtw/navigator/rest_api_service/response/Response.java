package ru.fbtw.navigator.rest_api_service.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.fbtw.navigator.rest_api_service.response.BaseResponse;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response implements BaseResponse {
    int status;
    String message;

    public Response(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
