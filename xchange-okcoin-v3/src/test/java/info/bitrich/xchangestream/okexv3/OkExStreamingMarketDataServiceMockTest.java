package info.bitrich.xchangestream.okexv3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.FuturesContract;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OkExStreamingMarketDataServiceMockTest {

    @Mock
    private OkCoinStreamingService okCoinStreamingService;
    private OkExStreamingMarketDataService marketDataService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        marketDataService = new OkExStreamingMarketDataService(okCoinStreamingService);
//        System.setProperty("log4j2.configurationFile", "./log4j2.xml");

//        final Logger logger = LogManager.getLogger(OkExStreamingMarketDataServiceMockTest.class.getName());
    }


    @Test
    public void getOrderBook() throws IOException {
        // Given order book in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("order-book.json"));

        when(okCoinStreamingService.subscribeChannel(any())).thenReturn(Observable.just(jsonNode));

        Date timestamp = Date.from(Instant.parse("2018-12-17T09:48:09.978Z"));

        List<LimitOrder> bids = new ArrayList<>();
        bids.add(new LimitOrder(Order.OrderType.BID, new BigDecimal("12"), CurrencyPair.BTC_USD, null, timestamp, new BigDecimal("3921.608")));

        List<LimitOrder> asks = new ArrayList<>();
        asks.add(new LimitOrder(Order.OrderType.ASK, new BigDecimal("1"), CurrencyPair.BTC_USD, null, timestamp, new BigDecimal("3921.8772")));

        OrderBook expected = new OrderBook(timestamp, asks, bids);

        // Call get order book observable
        final InstrumentDto instrumentDto = new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek);
        TestObserver<OrderBook> test = marketDataService.getOrderBook(CurrencyPair.BTC_USD, instrumentDto, true).test();

        // Get order book object in correct order
        test.assertResult(expected);
    }

    @Test
    public void getTicker() throws IOException {
        // Given order book in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("ticker-from2019-04-26.json"));

        when(okCoinStreamingService.subscribeChannel(any())).thenReturn(Observable.just(jsonNode));

        // Call get order book observable
        final InstrumentDto instrumentDto = new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek);
        TestObserver<Ticker> test = marketDataService.getTicker(CurrencyPair.BTC_USD, instrumentDto).test();

        test.awaitTerminalEvent();

        System.out.println(test.values());
        test.assertValueCount(1);
        test.assertNoErrors();

        final Ticker actual = test.values().get(0);

        Assert.assertEquals(BigDecimal.valueOf(3702.2), actual.getLast());
        Assert.assertEquals(BigDecimal.valueOf(3702.24), actual.getAsk());
        Assert.assertEquals(BigDecimal.valueOf(3702.2), actual.getBid());
        Assert.assertEquals(new Date(1551679265315L), actual.getTimestamp());
        Assert.assertEquals(CurrencyPair.BTC_USD, actual.getCurrencyPair());
        Assert.assertEquals(BigDecimal.valueOf(2904871), actual.getVolume());

//        final Ticker ticker = new Builder()
//                .last(BigDecimal.valueOf(3702.2))
//                .ask(BigDecimal.valueOf(3702.24))
//                .bid(BigDecimal.valueOf(3702.2))
//                .timestamp(new Date(1551679265315L))
//                .currencyPair(CurrencyPair.BTC_USD)
//                .volume(BigDecimal.valueOf(2904871))
//                .build();
//
//        test.assertValue(ticker);
    }
}