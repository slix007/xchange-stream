package info.bitrich.xchangestream.okex.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import info.bitrich.xchangestream.okcoin.dto.RequestParameters;

/**
 * Created by Sergey Shurmin on 6/3/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"apiKey", "sign", "symbol", "contract_type", "price", "amount", "type", "match_price", "lever_rate"})
public class RequestPlaceOrderParameters implements RequestParameters {
    @JsonProperty("api_key")
    private final String apiKey;
    @JsonProperty("sign")
    private final String sign;
    @JsonProperty("symbol")
    private final String symbol;
    @JsonProperty("contract_type")
    private final String contractType;
    @JsonProperty("price")
    private final String price;
    @JsonProperty("amount")
    private final String amount;
    @JsonProperty("type")
    private final String type;
    @JsonProperty("match_price")
    private final String match_price;
    @JsonProperty("lever_rate")
    private final String lever_rate;

    public RequestPlaceOrderParameters(@JsonProperty("api_key") String apiKey,
                                       @JsonProperty("sign") String sign,
                                       @JsonProperty("symbol") String symbol,
                                       @JsonProperty("contract_type") String contractType,
                                       @JsonProperty("price") String price,
                                       @JsonProperty("amount") String amount,
                                       @JsonProperty("type") String type,
                                       @JsonProperty("match_price") String match_price,
                                       @JsonProperty("lever_rate") String lever_rate) {
        this.apiKey = apiKey;
        this.sign = sign;
        this.symbol = symbol;
        this.contractType = contractType;
        this.price = price;
        this.amount = amount;
        this.type = type;
        this.match_price = match_price;
        this.lever_rate = lever_rate;
    }
}
