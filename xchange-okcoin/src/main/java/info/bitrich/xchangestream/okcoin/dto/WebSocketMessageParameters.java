package info.bitrich.xchangestream.okcoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Sergey Shurmin on 6/3/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"apiKey", "sign", "symbol", "orderId"})
public class WebSocketMessageParameters {

    @JsonProperty("api_key")
    private final String apiKey;
    @JsonProperty("sign")
    private final String sign;
    @JsonProperty("symbol")
    private final String symbol;
    @JsonProperty("order_id")
    private final String orderId;

    public WebSocketMessageParameters(@JsonProperty("api_key") String apiKey,
                                      @JsonProperty("sign") String sign,
                                      @JsonProperty("symbol") String symbol,
                                      @JsonProperty("order_id") String orderId) {
        this.apiKey = apiKey;
        this.sign = sign;
        this.symbol = symbol;
        this.orderId = orderId;
    }
}
