package hotel.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class CustomException extends RuntimeException{
    private static final long serialVersionUID = -3163757886763387095L;
    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private String[] params;

    public CustomException(String message, String code, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CustomException(String message, String code, HttpStatus httpStatus, List<String> params) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = code;
        if (!CollectionUtils.isEmpty(params)) {
            this.params = new String[params.size()];
            params.toArray(this.params);
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
