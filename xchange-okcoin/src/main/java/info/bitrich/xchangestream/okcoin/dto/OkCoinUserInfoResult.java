package info.bitrich.xchangestream.okcoin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkCoinUserInfoResult {

    private BigDecimal borrowFreeze;
    private Info info;

    public OkCoinUserInfoResult(@JsonProperty("borrowFreeze") BigDecimal borrowFreeze,
                                @JsonProperty("info") Info info) {
        this.borrowFreeze = borrowFreeze;
        this.info = info;
    }

    public BigDecimal getBorrowFreeze() {
        return borrowFreeze;
    }

    public Info getInfo() {
        return info;
    }

    public static class Info {
        private Free free;

        public Info(@JsonProperty("free") Free free) {
            this.free = free;
        }

        public Free getFree() {
            return free;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "free=" + free +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Free {
        private BigDecimal btc;
        private BigDecimal usd;

        public Free(@JsonProperty("btc") BigDecimal btc,
                    @JsonProperty("usd") BigDecimal usd) {
            this.btc = btc;
            this.usd = usd;
        }

        public BigDecimal getBtc() {
            return btc;
        }

        public BigDecimal getUsd() {
            return usd;
        }

        @Override
        public String toString() {
            return "Free{" +
                    "btc=" + btc +
                    ", usd=" + usd +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OkCoinUserInfoResult{" +
                "borrowFreeze=" + borrowFreeze +
                ", info=" + info +
                '}';
    }
}
