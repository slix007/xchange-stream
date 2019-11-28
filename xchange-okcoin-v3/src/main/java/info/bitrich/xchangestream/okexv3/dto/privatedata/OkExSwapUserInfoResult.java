package info.bitrich.xchangestream.okexv3.dto.privatedata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkExSwapUserInfoResult {

    private BigDecimal equity;
    private BigDecimal fixed_balance;
    private String instrument_id;
    private BigDecimal maint_margin_ratio;
    private BigDecimal margin;
    private BigDecimal margin_frozen;
    private String margin_mode;
    private BigDecimal margin_ratio;
    private BigDecimal realized_pnl;
    private LocalDateTime timestamp;
    private BigDecimal total_avail_balance;
    private BigDecimal unrealized_pnl;

    public OkExSwapUserInfoResult() {
    }

    public OkExSwapUserInfoResult(BigDecimal equity, BigDecimal fixed_balance, String instrument_id, BigDecimal maint_margin_ratio, BigDecimal margin,
                                  BigDecimal margin_frozen, String margin_mode, BigDecimal margin_ratio, BigDecimal realized_pnl, LocalDateTime timestamp,
                                  BigDecimal total_avail_balance, BigDecimal unrealized_pnl) {
        this.equity = equity;
        this.fixed_balance = fixed_balance;
        this.instrument_id = instrument_id;
        this.maint_margin_ratio = maint_margin_ratio;
        this.margin = margin;
        this.margin_frozen = margin_frozen;
        this.margin_mode = margin_mode;
        this.margin_ratio = margin_ratio;
        this.realized_pnl = realized_pnl;
        this.timestamp = timestamp;
        this.total_avail_balance = total_avail_balance;
        this.unrealized_pnl = unrealized_pnl;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public BigDecimal getFixed_balance() {
        return fixed_balance;
    }

    public void setFixed_balance(BigDecimal fixed_balance) {
        this.fixed_balance = fixed_balance;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
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

    public BigDecimal getMargin_frozen() {
        return margin_frozen;
    }

    public void setMargin_frozen(BigDecimal margin_frozen) {
        this.margin_frozen = margin_frozen;
    }

    public String getMargin_mode() {
        return margin_mode;
    }

    public void setMargin_mode(String margin_mode) {
        this.margin_mode = margin_mode;
    }

    public BigDecimal getMargin_ratio() {
        return margin_ratio;
    }

    public void setMargin_ratio(BigDecimal margin_ratio) {
        this.margin_ratio = margin_ratio;
    }

    public BigDecimal getRealized_pnl() {
        return realized_pnl;
    }

    public void setRealized_pnl(BigDecimal realized_pnl) {
        this.realized_pnl = realized_pnl;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getTotal_avail_balance() {
        return total_avail_balance;
    }

    public void setTotal_avail_balance(BigDecimal total_avail_balance) {
        this.total_avail_balance = total_avail_balance;
    }

    public BigDecimal getUnrealized_pnl() {
        return unrealized_pnl;
    }

    public void setUnrealized_pnl(BigDecimal unrealized_pnl) {
        this.unrealized_pnl = unrealized_pnl;
    }
}
