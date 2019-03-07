package info.bitrich.xchangestream.okexv3.dto.response;

public class RespError {

    private final String event;
    private final String message;
    private final String errorCode;

    public RespError(String event, String message, String errorCode) {
        this.event = event;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
