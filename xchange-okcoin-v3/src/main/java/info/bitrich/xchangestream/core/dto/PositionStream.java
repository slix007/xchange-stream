package info.bitrich.xchangestream.core.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Created by Sergey Shurmin on 6/28/17.
 */
public class PositionStream {

    private BigDecimal positionLong;
    private BigDecimal positionShort;
    private BigDecimal longAvailToClose;
    private BigDecimal shortAvailToClose;
    private BigDecimal leverage;
    private BigDecimal liquidationPrice;
    private BigDecimal priceAvgLong;
    private BigDecimal priceAvgShort;
    private BigDecimal markValue;
    private String instrumentId;
    private Instant timestamp;
    private String raw;
    private BigDecimal plPos;

    public PositionStream(BigDecimal positionLong, BigDecimal positionShort, BigDecimal longAvailToClose, BigDecimal shortAvailToClose,
                          BigDecimal leverage, BigDecimal liquidationPrice, BigDecimal priceAvgLong, BigDecimal priceAvgShort, BigDecimal markValue,
                          String instrumentId, Instant timestamp, String raw, BigDecimal plPos) {
        this.positionLong = positionLong;
        this.positionShort = positionShort;
        this.longAvailToClose = longAvailToClose;
        this.shortAvailToClose = shortAvailToClose;
        this.leverage = leverage;
        this.liquidationPrice = liquidationPrice;
        this.priceAvgLong = priceAvgLong;
        this.priceAvgShort = priceAvgShort;
        this.markValue = markValue;
        this.instrumentId = instrumentId;
        this.timestamp = timestamp;
        this.raw = raw;
        this.plPos = plPos;
    }

    public BigDecimal getPositionLong() {
        return positionLong;
    }

    public BigDecimal getPositionShort() {
        return positionShort;
    }

    public BigDecimal getLongAvailToClose() {
        return longAvailToClose;
    }

    public BigDecimal getShortAvailToClose() {
        return shortAvailToClose;
    }

    public BigDecimal getLeverage() {
        return leverage;
    }

    public BigDecimal getLiquidationPrice() {
        return liquidationPrice;
    }

    public BigDecimal getPriceAvgLong() {
        return priceAvgLong;
    }

    public BigDecimal getPriceAvgShort() {
        return priceAvgShort;
    }

    public BigDecimal getMarkValue() {
        return markValue;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getRaw() {
        return raw;
    }

    public BigDecimal getPlPos() {
        return plPos;
    }
}
