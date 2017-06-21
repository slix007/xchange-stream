package info.bitrich.xchangestream.okex;

import info.bitrich.xchangestream.okex.dto.OkExUserInfoResult;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinDepth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Sergey Shurmin on 6/20/17.
 */
public class OkExAdapters {

    /**
     * getTotal == Wallet Balance
     * getAvailable == Available Balance
     */
    public final static Currency WALLET_CURRENCY = new Currency("WALLET");
    /**
     * getTotal == Margin Balance
     * getAvailable == Margin Balance
     */
    public final static Currency MARGIN_CURRENCY = new Currency("MARGIN");
    /**
     * getTotal == Size(contract)
     * getAvailable == Value(XBT)
     */
    public final static Currency POSITION_CURRENCY = new Currency("POSITION");

    public static AccountInfo adaptUserInfo(OkExUserInfoResult okExUserInfoResult) {
        final OkExUserInfoResult.BalanceInfo btcInfo = okExUserInfoResult.getBtcInfo();
        Map<String, Balance.Builder> builders = new TreeMap<>();

        builders.put("btc", new Balance.Builder().currency(WALLET_CURRENCY)
                .total(btcInfo.getAccountRights())
                .available(btcInfo.getAccountRights())
                .depositing(btcInfo.getKeepDeposit())
                .loaned(btcInfo.getProfitReal())
                .borrowed(btcInfo.getProfitUnreal())
                .frozen(btcInfo.getRiskRate())
        );

        List<Balance> wallet = new ArrayList<>(builders.size());

        for (Balance.Builder builder : builders.values()) {
            wallet.add(builder.build());
        }

        return new AccountInfo(new Wallet(wallet));
    }

    public static OrderBook adaptOrderBook(OkCoinDepth depth, CurrencyPair currencyPair) {

        List<LimitOrder> asks = adaptLimitOrders(Order.OrderType.ASK, depth.getAsks(), currencyPair, depth.getTimestamp());
        Collections.reverse(asks);

        List<LimitOrder> bids = adaptLimitOrders(Order.OrderType.BID, depth.getBids(), currencyPair, depth.getTimestamp());
        return new OrderBook(depth.getTimestamp(), asks, bids);
    }

    private static List<LimitOrder> adaptLimitOrders(Order.OrderType type, BigDecimal[][] list, CurrencyPair currencyPair, Date timestamp) {

        List<LimitOrder> limitOrders = new ArrayList<LimitOrder>(list.length);
        for (int i = 0; i < list.length; i++) {
            BigDecimal[] data = list[i];
            limitOrders.add(adaptLimitOrder(type, data, currencyPair, null, timestamp));
        }
        return limitOrders;
    }

    private static LimitOrder adaptLimitOrder(Order.OrderType type, BigDecimal[] data, CurrencyPair currencyPair, String id, Date timestamp) {
        // [Price, Amount(Contract), Amount(Coin),Cumulant(Coin),Cumulant(Contract)]
        return new LimitOrder(type, data[2], currencyPair, id, timestamp, data[0]);
    }

}
