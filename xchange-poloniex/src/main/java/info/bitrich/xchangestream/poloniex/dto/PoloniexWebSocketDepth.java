package info.bitrich.xchangestream.poloniex.dto;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
//@JsonPropertyOrder({"type", "data"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoloniexWebSocketDepth {

    // [{data: {rate: '0.00300888', type: 'bid', amount: '3.32349029'},type: 'orderBookModify'}]
    // [{data: {rate: '0.00311164', type: 'ask' },type: 'orderBookRemove'}]

    // [{"type":"orderBookRemove","data":{"type":"bid","rate":"1209.91547960"}},
    // {"type":"newTrade",
    //      "data":{"amount":"0.00082647","date":"2017-04-11 05:10:02","rate":"1209.91547960","total":"0.99995884","tradeID":"2490976","type":"sell"}
    // }]


    @JsonProperty("type")
    private String type;

    @JsonProperty("data")
    private DataObj dataObj;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataObj getData() {
        return dataObj;
    }

    public void setData(DataObj data) {
        this.dataObj = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataObj {
        @JsonProperty("rate")
        private BigDecimal rate;
        @JsonProperty("amount")
        private BigDecimal amount;
        @JsonProperty("type")
        private String type;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public DataObj() {
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {

            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {

            this.additionalProperties.put(name, value);
        }
    }


}
