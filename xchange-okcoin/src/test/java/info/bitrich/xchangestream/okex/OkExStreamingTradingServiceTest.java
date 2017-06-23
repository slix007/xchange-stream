package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;
import info.bitrich.xchangestream.okex.dto.ContractOrderType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.okcoin.FuturesContract;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Sergey Shurmin on 6/21/17.
 */
public class OkExStreamingTradingServiceTest {

    @Mock
    private OkCoinStreamingService okCoinStreamingService;
    @Mock
    private Exchange exchange;

    private OkExStreamingTradingService tradingService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tradingService = new OkExStreamingTradingService(okCoinStreamingService, exchange);
    }

    @Test
    public void placeContractOrder() throws Exception {
        // Given order book in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("place-order-response.json"));
        when(okCoinStreamingService.sendAndSubscribe(any(), any())).thenReturn(Observable.just(jsonNode));

        final ExchangeSpecification exchangeSpecification = new ExchangeSpecification(OkExStreamingExchange.class);
        when(exchange.getExchangeSpecification()).thenReturn(exchangeSpecification);

        final String orderId = tradingService.placeContractOrder("btc_usd", FuturesContract.ThisWeek,
                new BigDecimal("2991.53"), new BigDecimal("0.01"),
                ContractOrderType.OPEN_LONG_POSITION_BUY);

        Assert.assertEquals("5017287829", orderId);
    }

}