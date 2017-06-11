package info.bitrich.xchangestream.okcoin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.bitrich.xchangestream.core.dto.PrivateData;

import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Sergey Shurmin on 6/10/17.
 */
public class OkCoinStreamingPrivateDataServiceTest {

    @Mock
    private OkCoinStreamingService okCoinStreamingService;
    @Mock
    private Exchange exchange;
    private OkCoinStreamingPrivateDataService privateDataService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        privateDataService = new OkCoinStreamingPrivateDataService(okCoinStreamingService, exchange);
    }

    @Test
    public void parseResult() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("private-data.json"));

        final PrivateData privateData = privateDataService.parseResult(jsonNode.get(0));
        final PrivateData privateData1 = privateDataService.parseResult(jsonNode.get(1));

        assertEquals("577806943", privateData.getTrades().get(0).getId());
        assertEquals("0.02807171", privateData1.getAccountInfo().getWallet().getBalance(Currency.BTC).getAvailable().toPlainString());
        assertEquals("363.4578", privateData1.getAccountInfo().getWallet().getBalance(Currency.USD).getAvailable().toPlainString());
    }

    @Test
    public void parseTradeOnlyResult() throws Exception {
        // Given order book in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader()
                .getResourceAsStream("private-data-trade-only.json"));

        final PrivateData privateData = privateDataService.parseResult(jsonNode.get(0));

        final List<LimitOrder> trades = privateData.getTrades();
        assertNotNull(trades);
        assertEquals("577806943", trades.get(0).getId());
        assertEquals("2991.53", trades.get(0).getLimitPrice().toPlainString());
        assertEquals("0.01", trades.get(0).getTradableAmount().toPlainString());
        assertEquals("0.01", trades.get(0).getCumulativeAmount().toPlainString());
        assertNull(privateData.getAccountInfo());
    }

}