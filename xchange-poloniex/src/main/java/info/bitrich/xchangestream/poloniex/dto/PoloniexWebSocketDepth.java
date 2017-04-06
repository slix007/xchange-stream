package info.bitrich.xchangestream.poloniex.dto;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"type", "data"})
public class PoloniexWebSocketDepth {

    // [{data: {rate: '0.00300888', type: 'bid', amount: '3.32349029'},type: 'orderBookModify'}]
    // [{data: {rate: '0.00311164', type: 'ask' },type: 'orderBookRemove'}]
/*
    @JsonProperty("type")
    private String type;

    @JsonProperty("data")
    private DataObj dataObj;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("data")
    public DataObj getData() {
        return dataObj;
    }

    @JsonProperty("data")
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
*/

}
