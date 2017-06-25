package info.bitrich.xchangestream.core.dto;

import java.math.BigDecimal;

/**
 * Created by Sergey Shurmin on 6/24/17.
 */
public class PositionInfo {
    private BigDecimal positionLong;
    private BigDecimal positionShort;
    private String raw;

    public PositionInfo(BigDecimal positionLong, BigDecimal positionShort, String raw) {
        this.positionLong = positionLong;
        this.positionShort = positionShort;
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

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return "PositionInfo{" +
                "positionLong=" + positionLong +
                ", positionShort=" + positionShort +
                ", raw='" + raw + '\'' +
                '}';
    }
}
