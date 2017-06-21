package info.bitrich.xchangestream.okex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkExUserInfoResult {

    private BalanceInfo btcInfo;
    private BalanceInfo ltcInfo;

    public OkExUserInfoResult(@JsonProperty("btc") BalanceInfo btcInfo,
                              @JsonProperty("ltc") BalanceInfo ltcInfo) {
        this.btcInfo = btcInfo;
        this.ltcInfo = ltcInfo;
    }

    public static class BalanceInfo {
        private BigDecimal riskRate;
        private BigDecimal accountRights;
        private BigDecimal profitUnreal;
        private BigDecimal profitReal;
        private BigDecimal keepDeposit;

        public BalanceInfo(@JsonProperty("risk_rate") BigDecimal riskRate,
                           @JsonProperty("account_rights") BigDecimal accountRights,
                           @JsonProperty("profit_unreal") BigDecimal profitUnreal,
                           @JsonProperty("profit_real") BigDecimal profitReal,
                           @JsonProperty("keep_deposit") BigDecimal keepDeposit) {
            this.riskRate = riskRate;
            this.accountRights = accountRights;
            this.profitUnreal = profitUnreal;
            this.profitReal = profitReal;
            this.keepDeposit = keepDeposit;
        }

        public BigDecimal getRiskRate() {
            return riskRate;
        }

        public BigDecimal getAccountRights() {
            return accountRights;
        }

        public BigDecimal getProfitUnreal() {
            return profitUnreal;
        }

        public BigDecimal getProfitReal() {
            return profitReal;
        }

        public BigDecimal getKeepDeposit() {
            return keepDeposit;
        }
    }

    public BalanceInfo getBtcInfo() {
        return btcInfo;
    }

    public BalanceInfo getLtcInfo() {
        return ltcInfo;
    }
}
