package info.bitrich.xchangestream.core.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Sergey Shurmin on 6/28/17.
 */
public class Position {

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
    private String raw;

    public Position(BigDecimal positionLong, BigDecimal positionShort, BigDecimal leverage, BigDecimal liquidationPrice, String raw) {
        this.positionLong = positionLong;
        this.positionShort = positionShort;
        this.leverage = leverage;
        this.liquidationPrice = liquidationPrice;
        this.raw = raw;
    }

    public Position(BigDecimal positionLong, BigDecimal positionShort, BigDecimal leverage, BigDecimal liquidationPrice, BigDecimal markValue, String raw) {
        this.positionLong = positionLong;
        this.positionShort = positionShort;
        this.leverage = leverage;
        this.liquidationPrice = liquidationPrice;
        this.markValue = markValue;
        this.raw = raw;
    }

    public Position(BigDecimal positionLong, BigDecimal positionShort,
            BigDecimal longAvailToClose, BigDecimal shortAvailToClose,
            BigDecimal leverage, BigDecimal liquidationPrice, BigDecimal markValue,
            BigDecimal priceAvgLong, BigDecimal priceAvgShort, String instrumentId, String raw) {
        this.positionLong = positionLong;
        this.positionShort = positionShort;
        this.longAvailToClose = longAvailToClose;
        this.shortAvailToClose = shortAvailToClose;
        this.leverage = leverage;
        this.liquidationPrice = liquidationPrice;
        this.markValue = markValue;
        this.priceAvgLong = priceAvgLong;
        this.priceAvgShort = priceAvgShort;
        this.instrumentId = instrumentId;
        this.raw = raw;
    }

    public BigDecimal getPositionLong() {
        return positionLong;
    }

    public void setPositionLong(BigDecimal positionLong) {
        this.positionLong = positionLong;
    }

    public BigDecimal getPositionShort() {
        return positionShort;
    }

    public void setPositionShort(BigDecimal positionShort) {
        this.positionShort = positionShort;
    }

    public BigDecimal getLongAvailToClose() {
        return longAvailToClose;
    }

    public void setLongAvailToClose(BigDecimal longAvailToClose) {
        this.longAvailToClose = longAvailToClose;
    }

    public BigDecimal getShortAvailToClose() {
        return shortAvailToClose;
    }

    public void setShortAvailToClose(BigDecimal shortAvailToClose) {
        this.shortAvailToClose = shortAvailToClose;
    }

    public BigDecimal getLeverage() {
        return leverage;
    }

    public void setLeverage(BigDecimal leverage) {
        this.leverage = leverage;
    }

    public BigDecimal getLiquidationPrice() {
        return liquidationPrice;
    }

    public void setLiquidationPrice(BigDecimal liquidationPrice) {
        this.liquidationPrice = liquidationPrice;
    }

    public BigDecimal getPriceAvgLong() {
        return priceAvgLong;
    }

    public void setPriceAvgLong(BigDecimal priceAvgLong) {
        this.priceAvgLong = priceAvgLong;
    }

    public BigDecimal getPriceAvgShort() {
        return priceAvgShort;
    }

    public void setPriceAvgShort(BigDecimal priceAvgShort) {
        this.priceAvgShort = priceAvgShort;
    }

    public BigDecimal getMarkValue() {
        return markValue;
    }

    public void setMarkValue(BigDecimal markValue) {
        this.markValue = markValue;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionLong=" + positionLong +
                ", positionShort=" + positionShort +
                ", longAvailToClose=" + longAvailToClose +
                ", shortAvailToClose=" + shortAvailToClose +
                ", leverage=" + leverage +
                ", liquidationPrice=" + liquidationPrice +
                ", priceAvgLong=" + priceAvgLong +
                ", priceAvgShort=" + priceAvgShort +
                ", markValue=" + markValue +
                '}';
    }

    public String toFullString() {
        return "Position{" +
                "positionLong=" + positionLong +
                ", positionShort=" + positionShort +
                ", longAvailToClose=" + longAvailToClose +
                ", shortAvailToClose=" + shortAvailToClose +
                ", leverage=" + leverage +
                ", liquidationPrice=" + liquidationPrice +
                ", priceAvgLong=" + priceAvgLong +
                ", priceAvgShort=" + priceAvgShort +
                ", markValue=" + markValue +
                ", raw='" + raw + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return Objects.equals(positionLong, position.positionLong) &&
                Objects.equals(positionShort, position.positionShort) &&
                Objects.equals(longAvailToClose, position.longAvailToClose) &&
                Objects.equals(shortAvailToClose, position.shortAvailToClose) &&
                Objects.equals(leverage, position.leverage) &&
                Objects.equals(liquidationPrice, position.liquidationPrice) &&
                Objects.equals(priceAvgLong, position.priceAvgLong) &&
                Objects.equals(priceAvgShort, position.priceAvgShort) &&
                Objects.equals(markValue, position.markValue) &&
                Objects.equals(instrumentId, position.instrumentId) &&
                Objects.equals(raw, position.raw);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(positionLong, positionShort, longAvailToClose, shortAvailToClose, leverage, liquidationPrice, priceAvgLong, priceAvgShort, markValue,
                        instrumentId, raw);
    }
}
