package info.bitrich.xchangestream.core.dto;

import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.util.List;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
public class PrivateData {

    private List<LimitOrder> trades;
    private AccountInfo accountInfo;

    public PrivateData(List<LimitOrder> trades, AccountInfo accountInfo) {
        this.trades = trades;
        this.accountInfo = accountInfo;
    }

    public void setTrades(List<LimitOrder> trades) {
        this.trades = trades;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public List<LimitOrder> getTrades() {
        return trades;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (trades.size() < 1) {
            sb.append("[]");
        } else {
            for (LimitOrder  trade: trades) {
                sb.append("[trade=");
                sb.append(trade.toString());
                sb.append("]");
            }
        }
        final String tradesString = sb.toString();

        return "PrivateData{" +
                "trades=" + tradesString +
                ", accountInfo=" + accountInfo +
                '}';
    }
}
