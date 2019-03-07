package info.bitrich.xchangestream.okexv3.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

public class OkcoinTicker {

    private final BigDecimal last;
    private final BigDecimal bestBid;
    private final BigDecimal bestAsk;
    private final BigDecimal hight24h;
    private final BigDecimal low24h;
    private final BigDecimal volume24h;
    private final String instrumentId;
    private final Instant timestamp;

    public OkcoinTicker(
            @JsonProperty("last") BigDecimal last,
            @JsonProperty("best_bid") BigDecimal bestBid,
            @JsonProperty("best_ask") BigDecimal bestAsk,
            @JsonProperty("high_24h") BigDecimal high24h,
            @JsonProperty("low_24h") BigDecimal low24h,
            @JsonProperty("volume_24h") BigDecimal volume24h,
            @JsonProperty("instrument_id") String instrumentId,
            @JsonProperty("timestamp") Instant timestamp) {
        this.last = last;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.hight24h = high24h;
        this.low24h = low24h;
        this.volume24h = volume24h;
        this.instrumentId = instrumentId;
        this.timestamp = timestamp;
    }

    public BigDecimal getLast() {
        return last;
    }

    public BigDecimal getBestBid() {
        return bestBid;
    }

    public BigDecimal getBestAsk() {
        return bestAsk;
    }

    public BigDecimal getHight24h() {
        return hight24h;
    }

    public BigDecimal getLow24h() {
        return low24h;
    }

    public BigDecimal getVolume24h() {
        return volume24h;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "OkcoinTicker{" +
                "last=" + last +
                ", bestBid=" + bestBid +
                ", bestAsk=" + bestAsk +
                ", hight24h=" + hight24h +
                ", low24h=" + low24h +
                ", volume24h=" + volume24h +
                ", instrumentId='" + instrumentId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
