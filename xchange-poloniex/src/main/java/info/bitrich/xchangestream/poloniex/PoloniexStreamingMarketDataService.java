package info.bitrich.xchangestream.poloniex;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import info.bitrich.xchangestream.core.StreamingMarketDataServiceExtended;
import info.bitrich.xchangestream.poloniex.dto.PoloniexWebSocketDepth;
import info.bitrich.xchangestream.service.wamp.WampStreamingService;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.OrderBookUpdate;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.poloniex.PoloniexAdapters;
import org.knowm.xchange.poloniex.PoloniexUtils;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexMarketData;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexTicker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

public class PoloniexStreamingMarketDataService implements StreamingMarketDataServiceExtended {
    private final WampStreamingService streamingService;

    public PoloniexStreamingMarketDataService(WampStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        throw new NotAvailableFromExchangeException();
    }

    @Override
    public Observable<OrderBookUpdate> getOrderBookUpdate(CurrencyPair currencyPair, Object... args) {
        // Poloniex uses USDT_BTC (instead of BTC_USD)
        String orderBookChannel = String.format("%s_%s", currencyPair.counter.getSymbol(), currencyPair.base.getSymbol());

        return streamingService.subscribeChannel(orderBookChannel)
                .flatMap(pubSubData -> {
                    // documentation:
                    // [{data: {rate: '0.00300888', type: 'bid', amount: '3.32349029'},type: 'orderBookModify'}]
                    // [{data: {rate: '0.00311164', type: 'ask' },type: 'orderBookRemove'}]
                    // [{"type":"orderBookRemove","data":{"type":"bid","rate":"1209.91547960"}},{"type":"newTrade","data":{"amount":"0.00082647","date":"2017-04-11 05:10:02","rate":"1209.91547960","total":"0.99995884","tradeID":"2490976","type":"sell"}}]

                    // debugging pubSubData:
                    // details: {}
                    // arguments: [{"type":"orderBookModify","data":{"type":"bid","rate":"1134.00000001","amount":"1.41239744"}}]
                    // keywordArguments: {"seq":87216685}

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    final ArrayNode arguments = pubSubData.arguments();
                    List<PoloniexWebSocketDepth> depthList = new ArrayList<>();
                    for (final JsonNode objNode : arguments) {
                        final PoloniexWebSocketDepth depth =
                                mapper.treeToValue(objNode, PoloniexWebSocketDepth.class);

                        if (depth.getType().equals("orderBookModify")
                                || depth.getType().equals("orderBookRemove")) {
                            System.out.println("Depth " + depth.getType()
                                    + ", " + depth.getData().getRate()
                                    + ", " + depth.getData().getAmount());
                            depthList.add(depth);
                        } else {
                            System.out.println("WARNING " + depth.toString());
                        }


                    }

                    // TODO optimize it using only rxJava
                    final List<OrderBookUpdate> updateList = depthList.stream()
                            .map(depth -> {

//                                System.out.println(String.format("%s:%s", depth.getType(), depth.getData().getRate()));

                                final BigDecimal amount = depth.getData().getAmount() != null
                                        ? depth.getData().getAmount()
                                        : new BigDecimal(0);
                                return new OrderBookUpdate(
                                        depth.getData().getType().equals("bid") ? Order.OrderType.BID : Order.OrderType.ASK,
                                        amount,
                                        currencyPair,
                                        new BigDecimal(depth.getData().getRate().toString()),
                                        new Date(),
                                        amount
                                );
                            }).collect(Collectors.toList());

                    return Observable.fromIterable(updateList);
                });
    }

    @Override
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        return streamingService.subscribeChannel("ticker")
                .map(pubSubData -> {
                    PoloniexMarketData marketData = new PoloniexMarketData();
                    marketData.setLast(new BigDecimal(pubSubData.arguments().get(1).asText()));
                    marketData.setLowestAsk(new BigDecimal(pubSubData.arguments().get(2).asText()));
                    marketData.setHighestBid(new BigDecimal(pubSubData.arguments().get(3).asText()));
                    marketData.setPercentChange(new BigDecimal(pubSubData.arguments().get(4).asText()));
                    marketData.setBaseVolume(new BigDecimal(pubSubData.arguments().get(5).asText()));
                    marketData.setQuoteVolume(new BigDecimal(pubSubData.arguments().get(6).asText()));
                    marketData.setHigh24hr(new BigDecimal(pubSubData.arguments().get(8).asText()));
                    marketData.setLow24hr(new BigDecimal(pubSubData.arguments().get(9).asText()));

                    PoloniexTicker ticker = new PoloniexTicker(marketData, PoloniexUtils.toCurrencyPair(pubSubData.arguments().get(0).asText()));
                    return PoloniexAdapters.adaptPoloniexTicker(ticker, ticker.getCurrencyPair());
                })
                .filter(ticker -> ticker.getCurrencyPair().equals(currencyPair));
    }

    @Override
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        throw new NotYetImplementedForExchangeException();
    }
}
