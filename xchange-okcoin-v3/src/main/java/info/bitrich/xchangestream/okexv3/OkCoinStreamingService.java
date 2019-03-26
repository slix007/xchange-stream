package info.bitrich.xchangestream.okexv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import info.bitrich.xchangestream.core.helper.WsObjectMapperHelper;
import info.bitrich.xchangestream.okexv3.dto.OkCoinAuthSigner;
import info.bitrich.xchangestream.okexv3.dto.request.LoginDto;
import info.bitrich.xchangestream.okexv3.dto.request.RequestDto;
import info.bitrich.xchangestream.okexv3.dto.request.RequestDto.OP;
import info.bitrich.xchangestream.service.ws.AuthSigner;
import info.bitrich.xchangestream.service.ws.WsMessageHandler;
import info.bitrich.xchangestream.service.ws.WsToRxStreamingService;
import io.reactivex.Completable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.okcoin.OkCoinException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sergei Shurmin on 02.03.19.
 */
public class OkCoinStreamingService extends WsToRxStreamingService<JsonNode> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ObjectMapper objectMapper = WsObjectMapperHelper.getObjectMapper();

    private final WsMessageHandler wsMessageHandler = new WsMessageHandler() {
        @Override
        public void onMessage(String message) {
            rawMessageHandler(message);
        }

        @Override
        public void onError(Throwable error) {
            log.info("Error: ", error);
            //TODO send to channels
            // or error_channel
        }
    };

    @Override
    protected WsMessageHandler getWsMessageHandler() {
        return wsMessageHandler;
    }

    public OkCoinStreamingService(String apiUrl) {
        super(apiUrl);
    }

    private void rawMessageHandler(String message) {
        log.debug("Received message: {}", message);

        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(message);
        } catch (IOException e) {
            log.error("Error parsing incoming message to JSON: {}", message);
            return;
        }

        // In case of array - handle every message separately.
        if (jsonNode.getNodeType().equals(JsonNodeType.ARRAY)) {
            for (JsonNode node : jsonNode) {
                parseJson(node);
            }
        } else {
            parseJson(jsonNode);
        }
    }

    /**
     * Parse channelId and send json forward.
     */
    private void parseJson(JsonNode message) {
        // Successful format:
        //  {"event":"<value>","channel":"<value>"}
        //  {"table":"channel","data":"[{"<value1>","<value2>"}]"}
        //  {"table":"channel", "action":"<value>","data":"[{"<value1>","<value2>"}]"} // Only in the futures/depth channel
        // Failure format:
        // {"event":"error","message":"<error_message>","error_code":"<error_code>"}

        // Examples:
        // 1) channel contains "<channelname>:<filter>"
        // 2) table contains  "<channelname>"
        // "channel":"futures/ticker:BTC-USD-170310"
        // "table":"futures/ticker"
        // NOTE: <filter> the save as the "instrument_id" field

        if (message.get("event") != null) {
            // errors
            final String event = message.get("event").asText();
            if (event.equals("error")) {
                log.warn("error event: " + message);
                final int errorCode = Integer.parseInt(message.get("errorCode").asText());
                final String errorMessage = message.get("message").asText();
                final OkCoinException okCoinException = new OkCoinException(errorCode, "errorCode: " + errorCode + ", message: " + errorMessage);

                String channel = getChannel(message);

                handleChannelError(channel, okCoinException);
            } else { // event == subscribe,unsubscribe,login
                // do nothing. It is just confirmation
                log.info("Confirmed event: " + message);
                if (event.equals("login")) {
                    handleChannelMessage("login", message);
                }
            }

        } else if (message.get("table") != null) {
            try {
//                final RespTable respTable = objectMapper.treeToValue(message, RespTable.class);
                String channel = getChannel(message);
                handleChannelMessage(channel, message);

            } catch (Exception e) {
                // do nothing
                log.info("Error handle 'table' message: " + message);
            }
        } else {
            log.warn("Warning: unrecognised response: " + message);
        }

    }

    private String getChannel(JsonNode message) {
        return message.get("table") != null ? message.get("table").asText() : UNKNOWN_CHANNEL_ID;
    }

    @Override
    protected Completable doLogin(AuthSigner authSigner) {
        this.authSigner = authSigner;
        return subscribeChannel("login", authSigner)
                .firstOrError()
                .map(json -> json.get("success") != null
                        && json.get("event") != null
                        && json.get("event").asText().equals("login")
                        && json.get("success").asBoolean())
                .toCompletable()
                .doOnError(e -> log.error("loginError", e))
                .andThen(super.doLogin(authSigner));
    }

    // Requests:

    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws Exception {
        if (args.length == 0) {
            RequestDto requestDto = new RequestDto(OP.subscribe, Collections.singletonList(channelName));
            return objectMapper.writeValueAsString(requestDto);
        }

        if (channelName.equals("batch")) {
            //noinspection unchecked
            final List<String> channelNames = (List<String>) args[0];
            RequestDto requestDto = new RequestDto(OP.subscribe, channelNames);
            return objectMapper.writeValueAsString(requestDto);
        }

        if (channelName.equals("login")) {
            final OkCoinAuthSigner s = (OkCoinAuthSigner) args[0];
            s.sign();
            final LoginDto loginDto = LoginDto.create(s.getApikey(), s.getPassphrase(),
                    s.getTimestamp(), s.getSign());
            return objectMapper.writeValueAsString(loginDto);
        }

        throw new IllegalArgumentException("can not create subscribe message for params:"
                + "channelName:" + channelName + ", args: " + Arrays.toString(args));
    }

    @Override
    public String getUnsubscribeMessage(List<String> channelNames) throws Exception {
        RequestDto requestDto = new RequestDto(OP.unsubscribe, channelNames);
        return objectMapper.writeValueAsString(requestDto);
    }

    @Override
    public String getSubscriptionUniqueId(String channelName, Object... args) {
        if (channelName.equals("login")) {
            return "login";
        }
        final String channelId = channelName.split(":")[0];
        log.debug("channelId=" + channelId);
        return channelId;
    }

}
