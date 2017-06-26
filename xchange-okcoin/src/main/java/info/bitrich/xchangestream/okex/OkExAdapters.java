package info.bitrich.xchangestream.okex;

import info.bitrich.xchangestream.okex.dto.BalanceEx;
import info.bitrich.xchangestream.okex.dto.OkExTradeResult;
import info.bitrich.xchangestream.okex.dto.OkExUserInfoResult;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
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

/**
 * Created by Sergey Shurmin on 6/20/17.
 */
public class OkExAdapters {

    public static AccountInfo adaptUserInfo(OkExUserInfoResult okExUserInfoResult, String raw) {
        final OkExUserInfoResult.BalanceInfo btcInfo = okExUserInfoResult.getBtcInfo();

        final BigDecimal equity = btcInfo.getAccountRights();
        final BigDecimal margin = btcInfo.getKeepDeposit();
        final BigDecimal upl = btcInfo.getProfitUnreal();
        final BigDecimal wallet = equity.subtract(upl);
        final BigDecimal available = equity.subtract(margin);

        final BalanceEx balance = new BalanceEx(Currency.BTC,
                wallet,
                available,
                margin);
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
        final BigDecimal tradableAmount = data[1].setScale(0, BigDecimal.ROUND_HALF_UP);
        final ContractLimitOrder contractLimitOrder = new ContractLimitOrder(type, tradableAmount, currencyPair, id, timestamp, data[0]);
        contractLimitOrder.setAmountInBaseCurrency(data[2]);
        return contractLimitOrder;
    }


    public static LimitOrder adaptTradeResult(OkExTradeResult okExTradeResult) {
        final Order.OrderType orderType = OkExAdapters.adaptOrderType(okExTradeResult.getType());
        final CurrencyPair currencyPair = CurrencyPair.BTC_USD; //TODO remove hardcode somehow
        final String orderId = String.valueOf(okExTradeResult.getOrderId());
        final Order.OrderStatus orderStatus = OkCoinAdapters.adaptOrderStatus(okExTradeResult.getStatus());
        return new ContractLimitOrder(orderType, okExTradeResult.getAmount(), currencyPair,
                orderId, okExTradeResult.getCreateDate(),
                okExTradeResult.getPrice(),
                okExTradeResult.getPriceAvg(),
                okExTradeResult.getDealAmount(),
                orderStatus);
    }

    private static Order.OrderType adaptOrderType(Integer type) {
        if (type.equals(1)) {
            //long - buy - bid
            return Order.OrderType.BID;
        } else if (type.equals(2)) {
            //short - sell - ask
            return Order.OrderType.ASK;
        } else if (type.equals(3)) {
            //short - sell - ask
            return Order.OrderType.EXIT_BID;
        } else if (type.equals(4)) {
            //short - sell - ask
            return Order.OrderType.EXIT_ASK;
        }
        return null;
    }

}
