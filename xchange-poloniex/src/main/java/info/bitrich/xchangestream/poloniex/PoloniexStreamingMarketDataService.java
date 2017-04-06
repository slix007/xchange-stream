package info.bitrich.xchangestream.poloniex;

import com.fasterxml.jackson.databind.JsonNode;

import info.bitrich.xchangestream.core.StreamingMarketDataServiceExtended;
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
import java.util.Date;

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
//        Incremental data return (Return full data for the first query)
//        Delete (BTC/LTC amount is 0)
//        Edit (Same price(rate), different amount)
//        Add (Price(rate) inexist)
        // Poloniex uses USDT_BTC (instead of BTC_USD)
        String orderBookChannel = String.format("%s_%s", currencyPair.counter.getSymbol(), currencyPair.base.getSymbol());
        // Looks like Poloniex sends only updates to orders, but not all of them
        return streamingService.subscribeChannel(orderBookChannel)
                .map(pubSubData -> {
                    // documentation:
                    // [{data: {rate: '0.00300888', type: 'bid', amount: '3.32349029'},type: 'orderBookModify'}]
                    // [{data: {rate: '0.00311164', type: 'ask' },type: 'orderBookRemove'}]

                    // debugging pubSubData:
                    // details: {}
                    // arguments: [{"type":"orderBookModify","data":{"type":"bid","rate":"1134.00000001","amount":"1.41239744"}}]
                    // keywordArguments: {"seq":87216685}

                    // TODO use object mapper
//                    ObjectMapper mapper = new ObjectMapper();
//                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                    mapper.treeToValue(pubSubData.arguments())
//                    List<PoloniexWebSocketDepth> depth = mapper.readValue(pubSubData.arguments().asText(),
//                            mapper.getTypeFactory().constructCollectionType(List.class, PoloniexWebSocketDepth.class));
//                    PoloniexWebSocketDepth poloniexDepth = mapper.treeToValue(pu.get("data"), PoloniexWebSocketDepth.class);

//                    List<PoloniexWebSocketDepth> myObjects = mapper.readValue(pubSubData.arguments().asText(),
//                            new TypeReference<List<PoloniexWebSocketDepth>>(){});

                    StringBuilder inputUpdateType = new StringBuilder();
                    final StringBuilder inputRate = new StringBuilder();
                    final StringBuilder inputOrderType = new StringBuilder();
                    final StringBuilder inputAmount = new StringBuilder();

                    final JsonNode jsonNode = pubSubData.arguments().get(0);
                    jsonNode.fields().forEachRemaining(stringJsonNodeEntry -> {
                        switch (stringJsonNodeEntry.getKey()) {
                            case "type":
                                inputUpdateType.append(stringJsonNodeEntry.getValue().asText());
                                break;
                            case "data":
                                final JsonNode dataNode = stringJsonNodeEntry.getValue();
                                dataNode.fields().forEachRemaining(dataField -> {
                                    switch (dataField.getKey()) {
                                        case "type":
                                            inputOrderType.append(dataField.getValue().asText());
                                            break;
                                        case "rate":
                                            inputRate.append(dataField.getValue().asText());
                                            break;
                                        case "amount":
                                            inputAmount.append(dataField.getValue().asText());
                                            break;
                                    }
                                });
                                break;
                        }
                    });

                    // "bid" or "ask"
                    Order.OrderType orderType = inputOrderType.equals("bid") ? Order.OrderType.BID : Order.OrderType.ASK;

                    final OrderBookUpdate orderBookUpdate = new OrderBookUpdate(
                            orderType,
                            new BigDecimal(inputAmount.toString().length() > 0 ? inputAmount.toString() : "0"),
                            currencyPair,
                            new BigDecimal(inputRate.toString()),
                            new Date(),
                            new BigDecimal(inputAmount.toString().length() > 0 ? inputAmount.toString() : "0")
                    );
                    return orderBookUpdate;
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
