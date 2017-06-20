package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;

import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Sergey Shurmin on 6/20/17.
 */
public class OkExStreamingMarketDataServiceTest {
    @Mock
    private OkCoinStreamingService okCoinStreamingService;
    private OkExStreamingMarketDataService marketDataService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        marketDataService = new OkExStreamingMarketDataService(okCoinStreamingService);
    }

    @Test
    public void getOrderBook() throws Exception {
        // Given order book in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("order-book-features.json"));

        when(okCoinStreamingService.subscribeChannel(any())).thenReturn(Observable.just(jsonNode));

        List<LimitOrder> bids = new ArrayList<>();
        final Date date = new Date(1497987207553L);
        bids.add(new LimitOrder(Order.OrderType.BID, new BigDecimal("0.6591"), CurrencyPair.BTC_USD, null, date, new BigDecimal("2882.49")));
        bids.add(new LimitOrder(Order.OrderType.BID, new BigDecimal("1.7346"), CurrencyPair.BTC_USD, null, date, new BigDecimal("2882.48")));
        bids.add(new LimitOrder(Order.OrderType.BID, new BigDecimal("0.451"), CurrencyPair.BTC_USD, null, date, new BigDecimal("2882.46")));

        List<LimitOrder> asks = new ArrayList<>();
        asks.add(new LimitOrder(Order.OrderType.ASK, new BigDecimal("1.3762"), CurrencyPair.BTC_USD, null, date, new BigDecimal("2906.44")));
        asks.add(new LimitOrder(Order.OrderType.ASK, new BigDecimal("4.9157"), CurrencyPair.BTC_USD, null, date, new BigDecimal("2909.0")));
        asks.add(new LimitOrder(Order.OrderType.ASK, new BigDecimal("6.0123"), CurrencyPair.BTC_USD, null, date, new BigDecimal("2910.68")));

        OrderBook expected = new OrderBook(date, asks, bids);

        // Call get order book observable
        TestObserver<OrderBook> test = marketDataService.getOrderBook(CurrencyPair.BTC_USD).test();

        // Get order book object in correct order
        test.assertResult(expected);
    }

}