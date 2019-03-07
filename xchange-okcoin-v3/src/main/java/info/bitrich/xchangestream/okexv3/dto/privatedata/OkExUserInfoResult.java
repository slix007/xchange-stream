package info.bitrich.xchangestream.okexv3.dto.privatedata;

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
    private BalanceInfo ethInfo;

    public OkExUserInfoResult(@JsonProperty("BTC") BalanceInfo btcInfo,
            @JsonProperty("LTC") BalanceInfo ltcInfo,
            @JsonProperty("ETH") BalanceInfo ethInfo) {
        this.btcInfo = btcInfo;
        this.ltcInfo = ltcInfo;
        this.ethInfo = ethInfo;
    }

    //{
    //    "table": "futures/account",
    //    "data": [{
    //        "BTC": {
    //            "equity": "102.38162222",
    //            "margin": "3.773884998",
    //            "margin_mode": "crossed",
    //            "margin_ratio": "27.129",
    //            "realized_pnl": "-34.53829072",
    //            "total_avail_balance": "135.54",
    //            "unrealized_pnl": "1.37991294"
    //        }
    //    }]
    //}
    public static class BalanceInfo {

        private BigDecimal equity;
        private BigDecimal margin;
        private String marginMode;
        private BigDecimal marginRatio;
        private BigDecimal realizedPnl;
        private BigDecimal totalAvailBalance;
        private BigDecimal unrealizedPnl;

        public BalanceInfo(@JsonProperty("equity") BigDecimal equity,
                @JsonProperty("margin") BigDecimal margin,
                @JsonProperty("margin_mode") String marginMode,
                @JsonProperty("margin_ratio") BigDecimal marginRatio,
                @JsonProperty("realized_pnl") BigDecimal realizedPnl,
                @JsonProperty("total_avail_balance") BigDecimal totalAvailBalance,
                @JsonProperty("unrealized_pnl") BigDecimal unrealizedPnl
        ) {
            this.equity = equity;
            this.margin = margin;
            this.marginMode = marginMode;
            this.marginRatio = marginRatio;
            this.realizedPnl = realizedPnl;
            this.totalAvailBalance = totalAvailBalance;
            this.unrealizedPnl = unrealizedPnl;
        }

        public BigDecimal getEquity() {
            return equity;
        }

        public BigDecimal getMargin() {
            return margin;
        }

        public String getMarginMode() {
            return marginMode;
        }

        public BigDecimal getMarginRatio() {
            return marginRatio;
        }

        public BigDecimal getRealizedPnl() {
            return realizedPnl;
        }

        public BigDecimal getTotalAvailBalance() {
            return totalAvailBalance;
        }

        public BigDecimal getUnrealizedPnl() {
            return unrealizedPnl;
        }
    }

    public BalanceInfo getBtcInfo() {
        return btcInfo;
    }

    public BalanceInfo getLtcInfo() {
        return ltcInfo;
    }

    public BalanceInfo getEthInfo() {
        return ethInfo;
    }
}
