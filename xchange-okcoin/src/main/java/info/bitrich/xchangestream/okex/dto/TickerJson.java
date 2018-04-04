package info.bitrich.xchangestream.okex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by Sergey Shurmin on 4/4/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerJson {
    private String limitHigh;
    private String limitLow;
    private BigDecimal last;
    private BigDecimal sell;
    private BigDecimal buy;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal vol;


    public String getLimitHigh() {
        return limitHigh;
    }

    public void setLimitHigh(String limitHigh) {
        this.limitHigh = limitHigh;
    }

    public String getLimitLow() {
        return limitLow;
    }

    public void setLimitLow(String limitLow) {
        this.limitLow = limitLow;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }
}
