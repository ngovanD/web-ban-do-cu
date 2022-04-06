package haui.cntt.myproject.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StandardResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public static StandardResponse error(String message) {
        return StandardResponse.builder()
                .success(false)
                .message(message).build();
    }

    public static StandardResponse error(String message, Integer errorCode) {
        return StandardResponse.builder()
                .success(false)
                .errorCode(errorCode)
                .message(message).build();
    }

    public static StandardResponse error(String message, Integer errorCode, Object data) {
        return StandardResponse.builder()
                .success(false)
                .errorCode(errorCode)
                .data(data)
                .message(message).build();
    }

    public static StandardResponse success(String message) {
        return StandardResponse.builder()
                .success(true)
                .message(message)
                .build();
    }

    public static StandardResponse success(Object data) {
        return StandardResponse.builder()
                .success(true)
                .data(data)
                .build();
    }


    public static StandardResponse success(Object data, String message) {
        return StandardResponse.builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }
}
