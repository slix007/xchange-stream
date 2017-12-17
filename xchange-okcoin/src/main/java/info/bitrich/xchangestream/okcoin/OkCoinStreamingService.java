package info.bitrich.xchangestream.okcoin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.bitrich.xchangestream.okcoin.dto.RequestMessage;
import info.bitrich.xchangestream.okcoin.dto.RequestOrderInfoParameters;
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
        return message.get("channel") != null ? message.get("channel").asText() : "";
    }

    @Override
    public String getSubscribeMessage(String channelName, String... parameters) throws JsonProcessingException {
        RequestOrderInfoParameters requestOrderInfoParameters = null;
        if (parameters.length == 2) {
            try {
                final String apiKey = parameters[0];
                final String sign = parameters[1];
                requestOrderInfoParameters = new RequestOrderInfoParameters(apiKey, sign, null, null);
            } catch (Exception e) {
                throw new IllegalArgumentException("Incorrect parameters " + Arrays.toString(parameters));
            }
        }
        if (parameters.length == 4) {
            try {
                final String apiKey = parameters[0];
                final String sign = parameters[1];
                final String symbol = parameters[2];
                final String orderId = parameters[3];
                requestOrderInfoParameters = new RequestOrderInfoParameters(apiKey, sign, symbol, orderId);
            } catch (Exception e) {
                throw new IllegalArgumentException("Incorrect parameters " + Arrays.toString(parameters));
            }
        }

        RequestMessage requestMessage;
        if (channelName.equals("login")) {
            requestMessage = new RequestMessage("login", null, requestOrderInfoParameters);
        } else {
            requestMessage = new RequestMessage("addChannel", channelName, requestOrderInfoParameters);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(RequestOrderInfoParameters.class);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper.writeValueAsString(requestMessage);
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        RequestMessage requestMessage = new RequestMessage("removeChannel", channelName, null);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestMessage);
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
