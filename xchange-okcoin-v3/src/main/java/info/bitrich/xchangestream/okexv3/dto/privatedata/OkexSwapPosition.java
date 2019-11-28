package info.bitrich.xchangestream.okexv3.dto.privatedata;

import java.math.BigDecimal;
import java.time.Instant;

public class OkexSwapPosition {

    private BigDecimal avail_position;
    private BigDecimal avg_cost;
    private String instrument_id; // "ETH-USD-SWAP"
    private BigDecimal last;
    private BigDecimal leverage;
    private BigDecimal liquidation_price;
    private BigDecimal maint_margin_ratio;
    private BigDecimal margin;
    private BigDecimal position;
    private BigDecimal realized_pnl;
    private BigDecimal settled_pnl;
    private BigDecimal settlement_price;
    private String side; //long, short
    private Instant timestamp;
    private BigDecimal unrealized_pnl;

    public OkexSwapPosition() {
    }

    public OkexSwapPosition(BigDecimal avail_position, BigDecimal avg_cost, String instrument_id, BigDecimal last, BigDecimal leverage,
                            BigDecimal liquidation_price, BigDecimal maint_margin_ratio, BigDecimal margin, BigDecimal position, BigDecimal realized_pnl,
                            BigDecimal settled_pnl, BigDecimal settlement_price, String side, Instant timestamp, BigDecimal unrealized_pnl) {
        this.avail_position = avail_position;
        this.avg_cost = avg_cost;
        this.instrument_id = instrument_id;
        this.last = last;
        this.leverage = leverage;
        this.liquidation_price = liquidation_price;
        this.maint_margin_ratio = maint_margin_ratio;
        this.margin = margin;
        this.position = position;
        this.realized_pnl = realized_pnl;
        this.settled_pnl = settled_pnl;
        this.settlement_price = settlement_price;
        this.side = side;
        this.timestamp = timestamp;
        this.unrealized_pnl = unrealized_pnl;
    }

    public BigDecimal getAvail_position() {
        return avail_position;
    }

    public void setAvail_position(BigDecimal avail_position) {
        this.avail_position = avail_position;
    }

    public BigDecimal getAvg_cost() {
        return avg_cost;
    }

    public void setAvg_cost(BigDecimal avg_cost) {
        this.avg_cost = avg_cost;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public BigDecimal getLeverage() {
        return leverage;
    }

    public void setLeverage(BigDecimal leverage) {
        this.leverage = leverage;
    }

    public BigDecimal getLiquidation_price() {
        return liquidation_price;
    }

    public void setLiquidation_price(BigDecimal liquidation_price) {
        this.liquidation_price = liquidation_price;
    }

    public BigDecimal getMaint_margin_ratio() {
        return maint_margin_ratio;
    }

    public void setMaint_margin_ratio(BigDecimal maint_margin_ratio) {
        this.maint_margin_ratio = maint_margin_ratio;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getPosition() {
        return position;
    }

    public void setPosition(BigDecimal position) {
        this.position = position;
    }

    public BigDecimal getRealized_pnl() {
        return realized_pnl;
    }

    public void setRealized_pnl(BigDecimal realized_pnl) {
        this.realized_pnl = realized_pnl;
    }

    public BigDecimal getSettled_pnl() {
        return settled_pnl;
    }

    public void setSettled_pnl(BigDecimal settled_pnl) {
        this.settled_pnl = settled_pnl;
    }

    public BigDecimal getSettlement_price() {
        return settlement_price;
    }

    public void setSettlement_price(BigDecimal settlement_price) {
        this.settlement_price = settlement_price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getUnrealized_pnl() {
        return unrealized_pnl;
    }

    public void setUnrealized_pnl(BigDecimal unrealized_pnl) {
        this.unrealized_pnl = unrealized_pnl;
    }

    public static OkexSwapPosition empty() {
        final OkexSwapPosition p = new OkexSwapPosition();
        p.avail_position = BigDecimal.ZERO;
        p.avg_cost = BigDecimal.ZERO;
        p.instrument_id = "";
        p.last = BigDecimal.ZERO;
        p.leverage = BigDecimal.ZERO;
        p.liquidation_price = BigDecimal.ZERO;
        p.maint_margin_ratio = BigDecimal.ZERO;
        p.margin = BigDecimal.ZERO;
        p.position = BigDecimal.ZERO;
        p.realized_pnl = BigDecimal.ZERO;
        p.settled_pnl = BigDecimal.ZERO;
        p.settlement_price = BigDecimal.ZERO;
        p.side = "";
        p.timestamp = null;
        p.unrealized_pnl = BigDecimal.ZERO;
        return p;
    }

    @Override
    public String toString() {
        return "OkexSwapPosition{" +
                "avail_position=" + avail_position +
                ", avg_cost=" + avg_cost +
                ", instrument_id='" + instrument_id + '\'' +
                ", last=" + last +
                ", leverage=" + leverage +
                ", liquidation_price=" + liquidation_price +
                ", maint_margin_ratio=" + maint_margin_ratio +
                ", margin=" + margin +
                ", position=" + position +
                ", realized_pnl=" + realized_pnl +
                ", settled_pnl=" + settled_pnl +
                ", settlement_price=" + settlement_price +
                ", side='" + side + '\'' +
                ", timestamp=" + timestamp +
                ", unrealized_pnl=" + unrealized_pnl +
                '}';
    }
}
