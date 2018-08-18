package info.bitrich.xchangestream.okex;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;
import info.bitrich.xchangestream.okex.dto.Tool;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.ContractLimitOrder;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.FuturesContract;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        final ContractLimitOrder b1 = new ContractLimitOrder(Order.OrderType.BID, BigDecimal.valueOf(19), CurrencyPair.BTC_USD, null, date, new BigDecimal("2882.49"));
        b1.setAmountInBaseCurrency(new BigDecimal("0.6591"));
        final ContractLimitOrder b2 = new ContractLimitOrder(Order.OrderType.BID, BigDecimal.valueOf(50), CurrencyPair.BTC_USD, null, date, new BigDecimal("2882.48"));
        b2.setAmountInBaseCurrency(new BigDecimal("1.7346"));
        final ContractLimitOrder b3 = new ContractLimitOrder(Order.OrderType.BID, BigDecimal.valueOf(13), CurrencyPair.BTC_USD, null, date, new BigDecimal("2882.46"));
        b3.setAmountInBaseCurrency(new BigDecimal("0.451"));
        bids.add(b1);
        bids.add(b2);
        bids.add(b3);

        List<LimitOrder> asks = new ArrayList<>();
        final ContractLimitOrder a1 = new ContractLimitOrder(Order.OrderType.ASK, BigDecimal.valueOf(40), CurrencyPair.BTC_USD, null, date, new BigDecimal("2906.44"));
        a1.setAmountInBaseCurrency(new BigDecimal("1.3762"));
        final ContractLimitOrder a2 = new ContractLimitOrder(Order.OrderType.ASK, BigDecimal.valueOf(143), CurrencyPair.BTC_USD, null, date, new BigDecimal("2909.0"));
        a2.setAmountInBaseCurrency(new BigDecimal("4.9157"));
        final ContractLimitOrder a3 = new ContractLimitOrder(Order.OrderType.ASK, BigDecimal.valueOf(175), CurrencyPair.BTC_USD, null, date, new BigDecimal("2910.68"));
        a3.setAmountInBaseCurrency(new BigDecimal("6.0123"));
        asks.add(a1);
        asks.add(a2);
        asks.add(a3);

        OrderBook expected = new OrderBook(date, asks, bids);

        // Call get order book observable
        TestObserver<OrderBook> test = marketDataService.getOrderBook(CurrencyPair.BTC_USD, Tool.BTC,
                FuturesContract.ThisWeek, OkExStreamingMarketDataService.Depth.DEPTH_20).test();

        // Get order book object in correct order
        test.assertResult(expected);
    }

}