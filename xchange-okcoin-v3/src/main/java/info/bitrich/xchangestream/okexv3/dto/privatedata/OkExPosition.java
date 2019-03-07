package info.bitrich.xchangestream.okexv3.dto.privatedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class OkExPosition {

    private BigDecimal longQty;
    private BigDecimal longAvailQty;
    private BigDecimal longMargin;
    private BigDecimal liquidationPrice; // actual
    private BigDecimal longLiquiPrice; // from doc
    private BigDecimal longPnlRatio;
    private BigDecimal longAvgCost;
    private BigDecimal longSettlementPrice;
    private BigDecimal realisedPnl;
    private BigDecimal shortQty;
    private BigDecimal shortAvailQty;
    private BigDecimal shortMargin;
    private BigDecimal shortLiquiPrice; // from doc
    private BigDecimal shortPnlRatio;
    private BigDecimal shortAvgCost;
    private BigDecimal shortSettlementPrice;
    private String instrumentId;
    private BigDecimal leverage; // actual
    private BigDecimal longLeverage;  // from documentation (not real)
    private BigDecimal shortLeverage; // from documentation (not real)
    private Date createdAt;
    private Date updatedAt;
    private String marginMode;

    public OkExPosition(
            @JsonProperty("long_qty") final BigDecimal longQty,
            @JsonProperty("long_avail_qty") final BigDecimal longAvailQty,
            @JsonProperty("long_margin") final BigDecimal longMargin,
            @JsonProperty("liquidation_price") final BigDecimal liquidationPrice,
            @JsonProperty("long_liqui_price") final BigDecimal longLiquiPrice,
            @JsonProperty("long_pnl_ratio") final BigDecimal longPnlRatio,
            @JsonProperty("long_avg_cost") final BigDecimal longAvgCost,
            @JsonProperty("long_settlement_price") final BigDecimal longSettlementPrice,
            @JsonProperty("realised_pnl") final BigDecimal realisedPnl,
            @JsonProperty("short_qty") final BigDecimal shortQty,
            @JsonProperty("short_avail_qty") final BigDecimal shortAvailQty,
            @JsonProperty("short_margin") final BigDecimal shortMargin,
            @JsonProperty("short_liqui_price") final BigDecimal shortLiquiPrice,
            @JsonProperty("short_pnl_ratio") final BigDecimal shortPnlRatio,
            @JsonProperty("short_avg_cost") final BigDecimal shortAvgCost,
            @JsonProperty("short_settlement_price") final BigDecimal shortSettlementPrice,
            @JsonProperty("instrument_id") final String instrumentId,
            @JsonProperty("leverage") final BigDecimal leverage,
            @JsonProperty("long_leverage") final BigDecimal longLeverage,
            @JsonProperty("short_leverage") final BigDecimal shortLeverage,
            @JsonProperty("created_at") final Date createdAt,
            @JsonProperty("updated_at") final Date updatedAt,
            @JsonProperty("margin_mode") final String marginMode
    ) {
        this.longQty = longQty;
        this.longAvailQty = longAvailQty;
        this.longMargin = longMargin;
        this.liquidationPrice = liquidationPrice;
        this.longLiquiPrice = longLiquiPrice;
        this.longPnlRatio = longPnlRatio;
        this.longAvgCost = longAvgCost;
        this.longSettlementPrice = longSettlementPrice;
        this.realisedPnl = realisedPnl;
        this.shortQty = shortQty;
        this.shortAvailQty = shortAvailQty;
        this.shortMargin = shortMargin;
        this.shortLiquiPrice = shortLiquiPrice;
        this.shortPnlRatio = shortPnlRatio;
        this.shortAvgCost = shortAvgCost;
        this.shortSettlementPrice = shortSettlementPrice;
        this.instrumentId = instrumentId;
        this.leverage = leverage;
        this.longLeverage = longLeverage;
        this.shortLeverage = shortLeverage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.marginMode = marginMode;
    }

    public BigDecimal getLongQty() {
        return longQty;
    }

    public BigDecimal getLongAvailQty() {
        return longAvailQty;
    }

    public BigDecimal getLongMargin() {
        return longMargin;
    }

    public BigDecimal getLiquidationPrice() {
        return liquidationPrice;
    }

    public BigDecimal getLongLiquiPrice() {
        return longLiquiPrice;
    }

    public BigDecimal getLongPnlRatio() {
        return longPnlRatio;
    }

    public BigDecimal getLongAvgCost() {
        return longAvgCost;
    }

    public BigDecimal getLongSettlementPrice() {
        return longSettlementPrice;
    }

    public BigDecimal getRealisedPnl() {
        return realisedPnl;
    }

    public BigDecimal getShortQty() {
        return shortQty;
    }

    public BigDecimal getShortAvailQty() {
        return shortAvailQty;
    }

    public BigDecimal getShortMargin() {
        return shortMargin;
    }

    public BigDecimal getShortLiquiPrice() {
        return shortLiquiPrice;
    }

    public BigDecimal getShortPnlRatio() {
        return shortPnlRatio;
    }

    public BigDecimal getShortAvgCost() {
        return shortAvgCost;
    }

    public BigDecimal getShortSettlementPrice() {
        return shortSettlementPrice;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public BigDecimal getLeverage() {
        return leverage;
    }

    public BigDecimal getLongLeverage() {
        return longLeverage;
    }

    public BigDecimal getShortLeverage() {
        return shortLeverage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getMarginMode() {
        return marginMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OkExPosition that = (OkExPosition) o;
        return Objects.equals(longQty, that.longQty) &&
                Objects.equals(longAvailQty, that.longAvailQty) &&
                Objects.equals(longMargin, that.longMargin) &&
                Objects.equals(liquidationPrice, that.liquidationPrice) &&
                Objects.equals(longLiquiPrice, that.longLiquiPrice) &&
                Objects.equals(longPnlRatio, that.longPnlRatio) &&
                Objects.equals(longAvgCost, that.longAvgCost) &&
                Objects.equals(longSettlementPrice, that.longSettlementPrice) &&
                Objects.equals(realisedPnl, that.realisedPnl) &&
                Objects.equals(shortQty, that.shortQty) &&
                Objects.equals(shortAvailQty, that.shortAvailQty) &&
                Objects.equals(shortMargin, that.shortMargin) &&
                Objects.equals(shortLiquiPrice, that.shortLiquiPrice) &&
                Objects.equals(shortPnlRatio, that.shortPnlRatio) &&
                Objects.equals(shortAvgCost, that.shortAvgCost) &&
                Objects.equals(shortSettlementPrice, that.shortSettlementPrice) &&
                Objects.equals(instrumentId, that.instrumentId) &&
                Objects.equals(leverage, that.leverage) &&
                Objects.equals(longLeverage, that.longLeverage) &&
                Objects.equals(shortLeverage, that.shortLeverage) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(marginMode, that.marginMode);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(longQty, longAvailQty, longMargin, liquidationPrice, longLiquiPrice, longPnlRatio, longAvgCost, longSettlementPrice, realisedPnl,
                        shortQty,
                        shortAvailQty, shortMargin, shortLiquiPrice, shortPnlRatio, shortAvgCost, shortSettlementPrice, instrumentId, leverage, longLeverage,
                        shortLeverage, createdAt, updatedAt, marginMode);
    }

    @Override
    public String toString() {
        return "OkExPosition{" +
                "longQty=" + longQty +
                ", longAvailQty=" + longAvailQty +
                ", longMargin=" + longMargin +
                ", liquidationPrice=" + liquidationPrice +
                ", longLiquiPrice=" + longLiquiPrice +
                ", longPnlRatio=" + longPnlRatio +
                ", longAvgCost=" + longAvgCost +
                ", longSettlementPrice=" + longSettlementPrice +
                ", realisedPnl=" + realisedPnl +
                ", shortQty=" + shortQty +
                ", shortAvailQty=" + shortAvailQty +
                ", shortMargin=" + shortMargin +
                ", shortLiquiPrice=" + shortLiquiPrice +
                ", shortPnlRatio=" + shortPnlRatio +
                ", shortAvgCost=" + shortAvgCost +
                ", shortSettlementPrice=" + shortSettlementPrice +
                ", instrumentId='" + instrumentId + '\'' +
                ", leverage=" + leverage +
                ", longLeverage=" + longLeverage +
                ", shortLeverage=" + shortLeverage +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", marginMode='" + marginMode + '\'' +
                '}';
    }
}
