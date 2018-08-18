package info.bitrich.xchangestream.okex;

import info.bitrich.xchangestream.okex.dto.OkExTradeResult;
import info.bitrich.xchangestream.okex.dto.OkExUserInfoResult;
import info.bitrich.xchangestream.okex.dto.Tool;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfoContracts;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.ContractLimitOrder;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.OkCoinAdapters;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinDepth;

/**
 * Created by Sergey Shurmin on 6/20/17.
 */
public class OkExAdapters {

    public static AccountInfoContracts adaptUserInfo(Tool baseTool, OkExUserInfoResult okExUserInfoResult, String raw) {
        final OkExUserInfoResult.BalanceInfo btcInfo;
        switch (baseTool) {
            case BTC:
                btcInfo = okExUserInfoResult.getBtcInfo();
                break;
            case ETH:
                btcInfo = okExUserInfoResult.getEthInfo();
                break;
            default:
                throw new IllegalArgumentException("Unsuported baseTool " + baseTool);
        }

        final BigDecimal equity = btcInfo.getAccountRights().setScale(8, BigDecimal.ROUND_HALF_UP);
        final BigDecimal margin = btcInfo.getKeepDeposit().setScale(8, BigDecimal.ROUND_HALF_UP);
        final BigDecimal upl = btcInfo.getProfitUnreal().setScale(8, BigDecimal.ROUND_HALF_UP);
        final BigDecimal wallet = equity.subtract(upl).setScale(8, BigDecimal.ROUND_HALF_UP);
        final BigDecimal available = equity.subtract(margin).setScale(8, BigDecimal.ROUND_HALF_UP);
        final BigDecimal rpl = btcInfo.getProfitReal().setScale(8, BigDecimal.ROUND_HALF_UP);
        final BigDecimal riskRate = btcInfo.getRiskRate().setScale(8, BigDecimal.ROUND_HALF_UP);

        return new AccountInfoContracts(wallet, available, null, equity, null, null, margin, upl, rpl, riskRate);
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
