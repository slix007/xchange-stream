package info.bitrich.xchangestream.okcoin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.okcoin.dto.trade.OkCoinOrder;
import org.knowm.xchange.okcoin.dto.trade.OkCoinOrderResult;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sergey Shurmin on 6/3/17.
 */
public class OkCoinStreamingTradingServiceTest {

    @Mock
    private OkCoinStreamingService okCoinStreamingService;
    @Mock
    private Exchange exchange;
    private OkCoinStreamingTradingService tradingService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tradingService = new OkCoinStreamingTradingService(okCoinStreamingService, exchange);
    }

    @Test
    public void parseOrderList() throws Exception {
        String inputData = "{\"data\":{\"result\":true,\"orders\":[{\"symbol\":\"btc_usd\",\"amount\":0.01,\"orders_id\":558911891,\"price\":2584.49,\"avg_price\":0,\"create_date\":1496507716000,\"type\":\"buy\",\"deal_amount\":0,\"order_id\":558911891,\"status\":0}]},\"channel\":\"ok_spotusd_orderinfo\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(inputData);

        final List<OkCoinOrder> okCoinOrders = tradingService.parseOrderList(jsonNode);

        assertEquals(558911891, okCoinOrders.get(0).getOrderId());
        assertEquals("btc_usd", okCoinOrders.get(0).getSymbol());
    }

    @Test
    public void parseResult() throws Exception {
        String inputData = "{\"data\":{\"result\":true,\"orders\":[{\"symbol\":\"btc_usd\",\"amount\":0.01,\"orders_id\":558911891,\"price\":2584.49,\"avg_price\":0,\"create_date\":1496507716000,\"type\":\"buy\",\"deal_amount\":0,\"order_id\":558911891,\"status\":0}]},\"channel\":\"ok_spotusd_orderinfo\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(inputData);

        final OkCoinOrderResult okCoinOrderResult = tradingService.parseResult(jsonNode);

        assertEquals(558911891, okCoinOrderResult.getOrders()[0].getOrderId());
        assertEquals("btc_usd", okCoinOrderResult.getOrders()[0].getSymbol());
    }

    @Test
    public void filledResult() throws Exception {
        String inputData = "{\"data\":{\"result\":true,\"orders\":[{\"symbol\":\"btc_usd\",\"amount\":0.02,\"orders_id\":559436585,\"price\":2581,\"avg_price\":2580.81,\"create_date\":1496522652000,\"type\":\"buy\",\"deal_amount\":0.02,\"order_id\":559436585,\"status\":2}]},\"channel\":\"ok_spotusd_orderinfo\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(inputData);

        final OkCoinOrderResult okCoinOrderResult = tradingService.parseResult(jsonNode);

        assertEquals(559436585, okCoinOrderResult.getOrders()[0].getOrderId());
        assertEquals("btc_usd", okCoinOrderResult.getOrders()[0].getSymbol());
    }

}