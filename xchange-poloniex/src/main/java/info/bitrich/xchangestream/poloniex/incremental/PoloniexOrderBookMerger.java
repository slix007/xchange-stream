package info.bitrich.xchangestream.poloniex.incremental;

import info.bitrich.xchangestream.poloniex.PoloniexStreamingMarketDataService;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Merge incremental updates of orderBook for Poloniex.
 * Created by Sergey Shurmin on 4/13/17.
 */
public class PoloniexOrderBookMerger {

    private static final Logger LOG = LoggerFactory.getLogger(PoloniexOrderBookMerger.class);

    public static OrderBook merge(OrderBook orderBook, PoloniexWebSocketDepth depthUpdate) {
        //TODO optimize it. Don't create a new orderBook, just replase data in the current?
        OrderBook newOrderBook = null;
        final PoloniexWebSocketDepth.DataObj data = depthUpdate.getData();
        final String depthUpdateType = depthUpdate.getType();
        switch (depthUpdateType) {
            case "orderBookRemove":
                if (data.getType().equals("bid")) {
                    final List<LimitOrder> newBids = orderBook.getBids().stream()
                            .filter(limitOrder ->
                                    limitOrder.getLimitPrice().compareTo(data.getRate()) != 0)
                            .collect(Collectors.toList());
                    if (newBids.size() == orderBook.getBids().size()) {
                        LOG.warn("Can't find an order to remove {},{},{}",
                                data.getType(), data.getRate(), data.getAdditionalProperties());
                    } else {
                        LOG.debug("Removed {},{}", data.getType(), data.getRate());
                    }
                    newOrderBook = new OrderBook(new Date(), orderBook.getAsks(), newBids);
                } else if (data.getType().equals("ask")) {
                    final List<LimitOrder> newAsks = orderBook.getAsks().stream()
                            .filter(limitOrder ->
                                    limitOrder.getLimitPrice().compareTo(data.getRate()) != 0)
                            .collect(Collectors.toList());
                    if (newAsks.size() == orderBook.getBids().size()) {
                        LOG.warn("Can't find an order to remove {},{},{}",
                                data.getType(), data.getRate(), data.getAdditionalProperties());
                    } else {
                        LOG.debug("Removed {},{}", data.getType(), data.getRate());
                    }
                    newOrderBook = new OrderBook(new Date(), newAsks, orderBook.getBids());
                }
                break;
            case "orderBookModify":
                if (data.getType().equals("bid")) {
                    final CurrencyPair currencyPair = orderBook.getBids().get(0).getCurrencyPair();
                    final List<LimitOrder> newBids = orderBook.getBids().stream()
                            .filter(limitOrder ->
                                    limitOrder.getLimitPrice().compareTo(data.getRate()) != 0)
                            .collect(Collectors.toList());
                    if (newBids.size() == orderBook.getBids().size()) {
                        LOG.debug("Added {},{}", data.getType(), data.getRate());
                    } else {
                        LOG.debug("Modified {},{}", data.getType(), data.getRate());
                    }
                    newBids.add(new LimitOrder(Order.OrderType.BID,
                            data.getAmount(),
                            currencyPair,
                            "changed " + data.getAmount(),
                            new Date(),
                            data.getRate()
                    ));
                    newOrderBook = new OrderBook(new Date(), orderBook.getAsks(), newBids);
                } else if (data.getType().equals("ask")) {
                    final CurrencyPair currencyPair = orderBook.getAsks().get(0).getCurrencyPair();
                    final List<LimitOrder> newAsks = orderBook.getAsks().stream()
                            .filter(limitOrder ->
                                    limitOrder.getLimitPrice().compareTo(data.getRate()) != 0)
                            .collect(Collectors.toList());
                    if (newAsks.size() == orderBook.getBids().size()) {
//                        LOG.debug("Can't find an order to remove {},{}", data.getType(), data.getRate());
                        // Can't find. Assume it's new
                    }
                    newAsks.add(new LimitOrder(Order.OrderType.ASK,
                            data.getAmount(),
                            currencyPair,
                            "changed " + data.getAmount(),
                            new Date(),
                            data.getRate()
                    ));

                    newOrderBook = new OrderBook(new Date(), newAsks, orderBook.getBids());
                }


                break;
            case "newTrade":
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unknown type " + depthUpdateType);
        }

        return newOrderBook;
    }
}
