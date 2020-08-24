package info.bitrich.xchangestream.okexv3.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class OkCoinDepth {

    private final BigDecimal[][] asks;
    private final BigDecimal[][] bids;
    private final String instrumentId;
    private final Date timestamp;
    private final Date receiveTimestamp;

    public OkCoinDepth(
            @JsonProperty("asks") final BigDecimal[][] asks,
            @JsonProperty("bids") final BigDecimal[][] bids,
            @JsonProperty("instrument_id") final String instrumentId,
            @JsonProperty(required = false, value = "timestamp") Date timestamp) {

        this.asks = asks;
        this.bids = bids;
        this.instrumentId = instrumentId;
        this.timestamp = timestamp;
        this.receiveTimestamp = new Date();
    }

    public BigDecimal[][] getAsks() {
        return asks;
    }

    public BigDecimal[][] getBids() {
        return bids;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Date getReceiveTimestamp() {
        return receiveTimestamp;
    }

    @Override
    public String toString() {
        return "OkCoinDepth{" +
                "asks=" + Arrays.toString(asks) +
                ", bids=" + Arrays.toString(bids) +
                ", instrumentId='" + instrumentId + '\'' +
                ", timestamp=" + timestamp +
                ", receiveTimestamp=" + receiveTimestamp +
                '}';
    }
}
