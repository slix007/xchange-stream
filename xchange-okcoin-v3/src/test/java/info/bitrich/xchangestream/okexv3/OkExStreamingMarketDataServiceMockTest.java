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
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
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
}