package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import info.bitrich.xchangestream.core.StreamingTradingService;
import info.bitrich.xchangestream.okcoin.OkCoinAuthSigner;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.okcoin.OkCoinAdapters;
import org.knowm.xchange.okcoin.dto.trade.OkCoinOrderResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class OkExStreamingTradingService implements StreamingTradingService {
    private final OkCoinStreamingService service;
    private final Exchange exchange;

    OkExStreamingTradingService(OkCoinStreamingService service, Exchange exchange) {
        this.service = service;
        this.exchange = exchange;
    }

    @Override
    public Observable<OpenOrders> getOpenOrdersObservable() {
        throw new NotAvailableFromExchangeException();
    }

    @Override
    public Observable<OpenOrders> getOpenOrderObservable(Object... args) {
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
                    final OkCoinOrderResult okCoinOrderResult = parseResult(jsonNode);
                    final OpenOrders openOrders = OkCoinAdapters.adaptOpenOrders(Collections.singletonList(okCoinOrderResult));
                    return openOrders;
                });
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
