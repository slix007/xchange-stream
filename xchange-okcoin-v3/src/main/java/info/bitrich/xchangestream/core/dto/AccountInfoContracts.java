package info.bitrich.xchangestream.core.dto;

import java.math.BigDecimal;

/**
 * Created by Sergey Shurmin on 7/1/17.
 */
public class AccountInfoContracts {

    private BigDecimal wallet;
    private BigDecimal available;
    private BigDecimal eMark;
    private BigDecimal eLast;
    private BigDecimal eBest;
    private BigDecimal eAvg;
    private BigDecimal margin;
    private BigDecimal upl;
    private BigDecimal rpl;
    private BigDecimal riskRate;

    public AccountInfoContracts() {
    }

    public AccountInfoContracts(BigDecimal wallet, BigDecimal available,
            BigDecimal eMark, BigDecimal eLast, BigDecimal eBest, BigDecimal eAvg,
            BigDecimal margin, BigDecimal upl) {
        this.wallet = wallet;
        this.available = available;
        this.eMark = eMark;
        this.eLast = eLast;
        this.eBest = eBest;
        this.eAvg = eAvg;
        this.margin = margin;
        this.upl = upl;
    }

    public AccountInfoContracts(BigDecimal wallet, BigDecimal available,
            BigDecimal eMark, BigDecimal eLast, BigDecimal eBest, BigDecimal eAvg,
            BigDecimal margin, BigDecimal upl, BigDecimal rpl, BigDecimal riskRate) {
        this.wallet = wallet;
        this.available = available;
        this.eMark = eMark;
        this.eLast = eLast;
        this.eBest = eBest;
        this.eAvg = eAvg;
        this.margin = margin;
        this.upl = upl;
        this.rpl = rpl;
        this.riskRate = riskRate;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public BigDecimal geteMark() {
        return eMark;
    }

    public BigDecimal geteLast() {
        return eLast;
    }

    public BigDecimal geteBest() {
        return eBest;
    }

    public BigDecimal geteAvg() {
        return eAvg;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public BigDecimal getUpl() {
        return upl;
    }

    public BigDecimal getRpl() {
        return rpl;
    }

    public BigDecimal getRiskRate() {
        return riskRate;
    }

    @Override
    public String toString() {
        return "AccountInfoContracts{" +
                "wallet=" + wallet +
                ", available=" + available +
                ", eMark=" + eMark +
                ", eLast=" + eLast +
                ", eBest=" + eBest +
                ", eAvg=" + eAvg +
                ", margin=" + margin +
                ", upl=" + upl +
                ", rpl=" + rpl +
                ", riskRate=" + riskRate +
                '}';
    }
}
