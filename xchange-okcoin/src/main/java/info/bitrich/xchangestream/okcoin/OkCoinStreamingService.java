package info.bitrich.xchangestream.okcoin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.bitrich.xchangestream.okcoin.dto.WebSocketMessage;
import info.bitrich.xchangestream.okcoin.dto.WebSocketMessageParameters;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;

import org.knowm.xchange.exceptions.ExchangeException;

import java.io.IOException;
import java.util.Arrays;

public class OkCoinStreamingService extends JsonNettyStreamingService {

    public OkCoinStreamingService(String apiUrl) {
        super(apiUrl);
    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) throws IOException {
        return message.get("channel").asText();
    }

    @Override
    public String getSubscribeMessage(String channelName, String... parameters) throws IOException {
        WebSocketMessageParameters webSocketMessageParameters = null;
        if (parameters.length > 0) {
            try {
                final String apiKey = parameters[0];
                final String sign = parameters[1];
                final String symbol = parameters[2];
                final String orderId = parameters[3];
                webSocketMessageParameters = new WebSocketMessageParameters(apiKey, sign, symbol, orderId);
            } catch (Exception e) {
                throw new IllegalArgumentException("Incorrect parameters " + Arrays.toString(parameters));
            }
        }

        WebSocketMessage webSocketMessage = new WebSocketMessage("addChannel", channelName, webSocketMessageParameters);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(WebSocketMessageParameters.class);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper.writeValueAsString(webSocketMessage);
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        WebSocketMessage webSocketMessage = new WebSocketMessage("removeChannel", channelName, null);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(webSocketMessage);
    }

    @Override
    protected void handleMessage(JsonNode message) {
        if (message.get("success") != null) {
            boolean success = message.get("success").asBoolean();
            if (!success) {
                super.handleError(message, new ExchangeException("Error code: " + message.get("errorcode").asText()));
            }
            return;
        }
        super.handleMessage(message);
    }
}
