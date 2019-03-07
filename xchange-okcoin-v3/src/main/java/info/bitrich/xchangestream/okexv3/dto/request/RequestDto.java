package info.bitrich.xchangestream.okexv3.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RequestDto {

    private final List<String> args;
    private final OP op;

    public RequestDto(@JsonProperty("args") OP op, @JsonProperty("args") List<String> args) {
        this.op = op;
        this.args = args;
    }

    public List<String> getArgs() {
        return args;
    }

    public OP getOp() {
        return op;
    }

    public enum OP {
        subscribe, unsubscribe, login
    }
}
