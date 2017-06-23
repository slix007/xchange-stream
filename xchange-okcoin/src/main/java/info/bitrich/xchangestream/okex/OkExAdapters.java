package info.bitrich.xchangestream.okex;

import info.bitrich.xchangestream.okcoin.dto.OkCoinTradeResult;
import info.bitrich.xchangestream.okex.dto.BalanceEx;
import info.bitrich.xchangestream.okex.dto.OkExTradeResult;
import info.bitrich.xchangestream.okex.dto.OkExUserInfoResult;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.ContractLimitOrder;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.OkCoinAdapters;
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
     * getTotal == Wallet Balance<br>
     * getAvailable == Available Balance
     */
    public final static Currency WALLET_CURRENCY = new Currency("WALLET");
    /**
     * getTotal == Margin Balance<br>
     * getAvailable == Margin Balance
     */
    public final static Currency MARGIN_CURRENCY = new Currency("MARGIN");
    /**
     * getFrozen == bondfreez
     */
    public final static Currency POSITION_LONG_CURRENCY = new Currency("POSITION_LONG");

    /**
     * getFrozen == bondfreez
     */
    public final static Currency POSITION_SHORT_CURRENCY = new Currency("POSITION_SHORT");

    public static AccountInfo adaptUserInfo(OkExUserInfoResult okExUserInfoResult, String raw) {
        final OkExUserInfoResult.BalanceInfo btcInfo = okExUserInfoResult.getBtcInfo();
        Map<String, Balance.Builder> builders = new TreeMap<>();

        final BigDecimal total = btcInfo.getAccountRights();
        final BigDecimal equity = btcInfo.getKeepDeposit();
        final BigDecimal available = total.subtract(equity);

        final BalanceEx balance = new BalanceEx(OkExAdapters.WALLET_CURRENCY,
                total,
                available,
                equity);
        balance.setRaw(raw);

        return new AccountInfo(new Wallet(balance));
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
        final ContractLimitOrder contractLimitOrder = new ContractLimitOrder(type, data[2], currencyPair, id, timestamp, data[0]);
        contractLimitOrder.setContractPrice(data[1]);
        return contractLimitOrder;
    }


    public static LimitOrder adaptTradeResult(OkExTradeResult okExTradeResult) {
        final Order.OrderType orderType = OkExAdapters.adaptOrderType(okExTradeResult.getType());
        final CurrencyPair currencyPair = new CurrencyPair(new Currency("BTC"), new Currency("MRG"));
        final String orderId = String.valueOf(okExTradeResult.getOrderId());
        final Order.OrderStatus orderStatus = OkCoinAdapters.adaptOrderStatus(okExTradeResult.getStatus());
        final ContractLimitOrder contractLimitOrder = new ContractLimitOrder(orderType, okExTradeResult.getAmount(), currencyPair,
                orderId, okExTradeResult.getCreateDate(),
                okExTradeResult.getPrice(),
                okExTradeResult.getPriceAvg(),
                okExTradeResult.getDealAmount(),
                orderStatus);
        contractLimitOrder.setContractPrice(new BigDecimal(okExTradeResult.getUnitAmount()));
        return contractLimitOrder;
    }

    private static Order.OrderType adaptOrderType(Integer type) {
        if (type.equals(1)) {
            //long - buy - bid
            return Order.OrderType.BID;
        } else if (type.equals(2)) {
            //short - sell - ask
            return Order.OrderType.ASK;
        }
        return null;
    }

}
