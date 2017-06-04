package info.bitrich.xchangestream.okcoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketMessage {
    private final String event;
    private final String channel;
    private final WebSocketMessageParameters parameters;

    public WebSocketMessage(@JsonProperty("event") String event,
                            @JsonProperty("channel") String channel,
                            @JsonProperty("parameters") WebSocketMessageParameters parameters) {
        this.event = event;
        this.channel = channel;
        this.parameters = parameters;
    }

    public String getEvent() {
        return event;
    }

    public String getChannel() {
        return channel;
    }

    @JsonProperty("parameters")
    public WebSocketMessageParameters getParameters() {
        return parameters;
    }
}
