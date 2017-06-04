package info.bitrich.xchangestream.okcoin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import info.bitrich.xchangestream.core.StreamingTradingService;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.okcoin.OkCoinAdapters;
import org.knowm.xchange.okcoin.dto.trade.OkCoinOrder;
import org.knowm.xchange.okcoin.dto.trade.OkCoinOrderResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class OkCoinStreamingTradingService implements StreamingTradingService {
    private final OkCoinStreamingService service;
    private final Exchange exchange;

    OkCoinStreamingTradingService(OkCoinStreamingService service, Exchange exchange) {
        this.service = service;
        this.exchange = exchange;
    }

    @Override
    public Observable<OpenOrders> getOpenOrdersObservable(Object... args) {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final String sign;
        final String symbol = args[0].toString();
        final String orderId = args[1].toString();

        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put("symbol", symbol);
        nameValueMap.put("order_id", orderId);
        sign = signer.digestParams(nameValueMap);

        return service.subscribeChannel("ok_spotusd_orderinfo", apiKey, sign, symbol, orderId)
                .map(jsonNode -> {
                    System.out.println("ORDER: "
                            + "id=" + jsonNode.get("data").get("orders").get(0).get("order_id")
                            + "status=" + jsonNode.get("data").get("orders").get(0).get("status")
                    );
                    final OkCoinOrderResult okCoinOrderResult = parseResult(jsonNode);
                    final OpenOrders openOrders = OkCoinAdapters.adaptOpenOrders(Collections.singletonList(okCoinOrderResult));
                    return openOrders;
                });
    }

    List<OkCoinOrder> parseOrderList(JsonNode jsonNode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        final JsonNode dataNode = jsonNode.get("data").get("orders");
        final String jsonAsString = dataNode.toString();
        return Arrays.asList(mapper.readValue(jsonAsString, OkCoinOrder[].class));
    }

    OkCoinOrderResult parseResult(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        final JsonNode dataNode = jsonNode.get("data");
        final OkCoinOrderResult okCoinOrderResult = mapper.treeToValue(dataNode, OkCoinOrderResult.class);
        return okCoinOrderResult;
    }


}
