package info.bitrich.xchangestream.okexv3.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

public class OkcoinPriceRange {

    private BigDecimal highest;
    private BigDecimal lowest;
    private String instrumentId;
    private Instant timestamp;

    public OkcoinPriceRange(
            @JsonProperty("highest") BigDecimal highest,
            @JsonProperty("lowest") BigDecimal lowest,
            @JsonProperty("instrument_id") String instrumentId,
            @JsonProperty("timestamp") Instant timestamp) {
        this.highest = highest;
        this.lowest = lowest;
        this.instrumentId = instrumentId;
        this.timestamp = timestamp;
    }

    public BigDecimal getHighest() {
        return highest;
    }

    public BigDecimal getLowest() {
        return lowest;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "OkcoinPriceRange{" +
                "highest=" + highest +
                ", lowest=" + lowest +
                ", instrumentId='" + instrumentId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
