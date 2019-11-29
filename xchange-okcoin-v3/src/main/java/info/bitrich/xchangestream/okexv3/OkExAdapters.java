package info.bitrich.xchangestream.okexv3;

import info.bitrich.xchangestream.core.dto.PositionStream;
import info.bitrich.xchangestream.okexv3.dto.marketdata.OkCoinDepth;
import info.bitrich.xchangestream.okexv3.dto.marketdata.OkcoinTicker;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExPosition;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExSwapUserInfoResult;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserInfoResult;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserInfoResult.BalanceInfo;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserOrder;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkexSwapPosition;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderStatus;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.AccountInfoContracts;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.OkCoinAdapters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OkExAdapters {

    public OkExAdapters() {
    }


    public static AccountInfoContracts adaptSwapUserInfo(Currency baseTool, OkExSwapUserInfoResult acc) {
        final String inst = acc.getInstrument_id();
        if (inst != null && inst.length() > 2 && inst.substring(0, 3).equals(baseTool.getCurrencyCode())) { // BTC, ETH
            BigDecimal equity = acc.getEquity().setScale(8, 4);
            BigDecimal margin = acc.getMargin().setScale(8, 4);
            BigDecimal upl = acc.getUnrealized_pnl().setScale(8, 4);
            BigDecimal wallet = equity.subtract(upl).setScale(8, 4);
            BigDecimal available = acc.getTotal_avail_balance().setScale(8, 4);
//        BigDecimal available = equity.subtract(margin).setScale(8, 4);
            BigDecimal rpl = acc.getRealized_pnl().setScale(8, 4);
            BigDecimal riskRate = acc.getMargin_ratio() == null ? BigDecimal.ZERO
                    : acc.getMargin_ratio().setScale(8, 4);
            return new AccountInfoContracts(wallet, available, (BigDecimal) null, equity, (BigDecimal) null, (BigDecimal) null, margin, upl, rpl, riskRate);

        }
        return null;
    }

    public static AccountInfoContracts adaptUserInfo(Currency baseTool, OkExUserInfoResult okExUserInfoResult) {
        BalanceInfo btcInfo;
        switch (baseTool.getCurrencyCode()) {
            case "BTC":
                btcInfo = okExUserInfoResult.getBtcInfo();
                break;
            case "ETH":
                btcInfo = okExUserInfoResult.getEthInfo();
                break;
            default:
                throw new IllegalArgumentException("Unsuported baseTool " + baseTool);
        }

        BigDecimal equity = btcInfo.getEquity().setScale(8, 4);
        BigDecimal margin = btcInfo.getMargin().setScale(8, 4);
        BigDecimal upl = btcInfo.getUnrealizedPnl().setScale(8, 4);
        BigDecimal wallet = equity.subtract(upl).setScale(8, 4);
        BigDecimal available = btcInfo.getTotalAvailBalance().setScale(8, 4);
//        BigDecimal available = equity.subtract(margin).setScale(8, 4);
        BigDecimal rpl = btcInfo.getRealizedPnl().setScale(8, 4);
        BigDecimal riskRate = btcInfo.getMarginRatio().setScale(8, 4);
        return new AccountInfoContracts(wallet, available, (BigDecimal) null, equity, (BigDecimal) null, (BigDecimal) null, margin, upl, rpl, riskRate);
    }

    public static OrderBook adaptOrderBook(OkCoinDepth depth, CurrencyPair currencyPair) {
        List<LimitOrder> asks = adaptLimitOrders(OrderType.ASK, depth.getAsks(), currencyPair, depth.getTimestamp());
//        Collections.reverse(asks);
        List<LimitOrder> bids = adaptLimitOrders(OrderType.BID, depth.getBids(), currencyPair, depth.getTimestamp());
        return new OrderBook(depth.getTimestamp(), asks, bids);
    }

    private static List<LimitOrder> adaptLimitOrders(OrderType type, BigDecimal[][] list, CurrencyPair currencyPair, Date timestamp) {
        List<LimitOrder> limitOrders = new ArrayList<>(list.length);
        for (int i = 0; i < list.length; ++i) {
            BigDecimal[] data = list[i];
            limitOrders.add(adaptLimitOrder(type, data, currencyPair, (String) null, timestamp));
        }

        return limitOrders;
    }

    private static LimitOrder adaptLimitOrder(OrderType type, BigDecimal[] data, CurrencyPair currencyPair, String id, Date timestamp) {
        //[411.8,6,8,4][double ,int ,int ,int]
        // 411.8 is the price,
        // 6 is the size of the price,
        // 8 is the number of force-liquidated orders,
        // 4 is the number of orders of the price，
        // timestamp is the timestamp of the orderbook.
        BigDecimal tradableAmount = data[1].setScale(0, 4);
        LimitOrder contractLimitOrder = new LimitOrder(type, tradableAmount, currencyPair, id, timestamp, data[0]);
//        contractLimitOrder.setAmountInBaseCurrency(data[2]);
        return contractLimitOrder;
    }

    public static Ticker adaptTicker(OkcoinTicker okCoinTicker, CurrencyPair currencyPair) {
        return (new Ticker.Builder()).currencyPair(currencyPair)
                .high(null)
                .low(null)
                .bid(okCoinTicker.getBestBid())
                .ask(okCoinTicker.getBestAsk())
                .last(okCoinTicker.getLast())
                .volume(okCoinTicker.getVolume24h())
                .timestamp(Date.from(okCoinTicker.getTimestamp())).build();

    }
//
//    private static LimitOrder adaptLimitOrder(OrderType type, BigDecimal[] data, CurrencyPair currencyPair, String id, Date timestamp) {
//        BigDecimal tradableAmount = data[1].setScale(0, 4);
//        ContractLimitOrder contractLimitOrder = new ContractLimitOrder(type, tradableAmount, currencyPair, id, timestamp, data[0]);
//        contractLimitOrder.setAmountInBaseCurrency(data[2]);
//        return contractLimitOrder;
//    }

    public static List<LimitOrder> adaptTradeResult(OkExUserOrder[] okExUserOrders) {
        List<LimitOrder> res = new ArrayList<>();
        for (OkExUserOrder okExUserOrder : okExUserOrders) {

            OrderType orderType = adaptOrderType(okExUserOrder.getType());
            CurrencyPair currencyPair = parseCurrencyPair(okExUserOrder.getInstrumentId());
//            String orderId = String.valueOf(okExUserOrder.getOrderId());
            OrderStatus orderStatus = OkCoinAdapters.adaptOrderStatus(okExUserOrder.getStatus());
//        return new ContractLimitOrder(orderType, okExUserOrder.getAmount(), currencyPair, orderId, okExUserOrder.getCreateDate(), okExUserOrder.getPrice(), okExUserOrder.getPriceAvg(), okExUserOrder.getDealAmount(), orderStatus);

//            final Date timestamp = Date.from(okExUserOrder.getTimestamp());
            final LimitOrder limitOrder = new LimitOrder(orderType,
                    okExUserOrder.getSize(),
                    currencyPair,
                    okExUserOrder.getOrderId(),
                    okExUserOrder.getTimestamp(),
                    okExUserOrder.getPrice(),
                    okExUserOrder.getPriceAvg(),
                    okExUserOrder.getFilledQty(),
//                    okExUserOrder.getFee(),
                    orderStatus);
            res.add(limitOrder);
        }
        return res;
    }

    private static CurrencyPair parseCurrencyPair(String instrumentId) { // instrumentId BTC-USD-170317
        final String[] split = instrumentId.split("-");
        final String base = split[0];
        final String counter = split[1];
        return new CurrencyPair(Currency.getInstance(base), Currency.getInstance(counter));
    }

    private static OrderType adaptOrderType(String type) {
        if (type.equals("1")) {
            return OrderType.BID;
        } else if (type.equals("2")) {
            return OrderType.ASK;
        } else if (type.equals("3")) {
            return OrderType.EXIT_BID;
        } else {
            return type.equals("4") ? OrderType.EXIT_ASK : null;
        }
    }

    public static PositionStream adaptPosition(OkExPosition p) {
        return new PositionStream(
                p.getLongQty(),
                p.getShortQty(),
                p.getLongAvailQty(),
                p.getShortAvailQty(),
                p.getLeverage(),
                p.getLiquidationPrice(),
                p.getLongAvgCost(),
                p.getShortAvgCost(),
                BigDecimal.ZERO, //mark value
                p.getInstrumentId(),
                p.getUpdatedAt().toInstant(),
                p.toString(),
                p.getLongPnl().add(p.getShortPnl())
        );

    }

    public static PositionStream adaptSwapPosition(OkexSwapPosition p) {
        final boolean aLong = p.getSide().equals("long");
        if (aLong) {
            return new PositionStream(
                    p.getPosition(),
                    null,
                    p.getAvail_position(),
                    null,
                    p.getLeverage(),
                    p.getLiquidation_price(),
                    p.getAvg_cost(),
                    null,
                    BigDecimal.ZERO, //mark value
                    p.getInstrument_id(),
                    p.getTimestamp(),
                    p.toString(),
                    p.getUnrealized_pnl());
        }
        // else short
        return new PositionStream(
                null,
                p.getPosition(),
                null,
                p.getAvail_position(),
                p.getLeverage(),
                p.getLiquidation_price(),
                null,
                p.getAvg_cost(),
                BigDecimal.ZERO, //mark value
                p.getInstrument_id(),
                p.getTimestamp(),
                p.toString(),
                p.getUnrealized_pnl());
    }

}
