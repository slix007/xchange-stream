package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import info.bitrich.xchangestream.core.StreamingTradingService;
import info.bitrich.xchangestream.okcoin.OkCoinAuthSigner;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;
import info.bitrich.xchangestream.okcoin.dto.RequestMessage;
import info.bitrich.xchangestream.okcoin.dto.RequestOrderInfoParameters;
import info.bitrich.xchangestream.okex.dto.ContractOrderType;
import info.bitrich.xchangestream.okex.dto.RequestPlaceOrderParameters;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.okcoin.FuturesContract;
import org.knowm.xchange.okcoin.OkCoinAdapters;
import org.knowm.xchange.okcoin.dto.trade.OkCoinOrderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class OkExStreamingTradingService implements StreamingTradingService {
    private static final Logger logger = LoggerFactory.getLogger(OkExStreamingTradingService.class);

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
        throw new NotYetImplementedForExchangeException("Use getOrderInfo");
    }

    public String placeContractOrder(String symbol, FuturesContract contractType,
                                     BigDecimal price,
                                     BigDecimal amount,
                                     ContractOrderType type) {

        final String channelName = "ok_futureusd_trade";
        String subscribeMessage;

        // 1. compose the message
        try {
            subscribeMessage = composePlaceOrderRequest(symbol, contractType, price, amount, type, channelName);
        } catch (JsonProcessingException e) {
            throw new ExchangeException(e.getMessage());
        }

        logger.info(subscribeMessage);
        // send the message and wait for an answer
        List<String> orderList = new ArrayList<>();
        StringBuilder errorSb = new StringBuilder();

        service.sendAndSubscribe(subscribeMessage, channelName)
                .map(jsonNode -> {
                    final JsonNode dataNode = jsonNode.get("data");
                    final boolean result = dataNode.get("result").asBoolean();
                    if (!result) {
                        errorSb.append(jsonNode.get("error_code"));
                        throw new ExchangeException(errorSb.toString());
                    }
                    final String orderIdString = dataNode.get("order_id").asText();
                    return orderIdString;
                })
                .timeout(2000, TimeUnit.MILLISECONDS)
                .take(1)
                .blockingSubscribe(orderList::add,
                        throwable -> errorSb.append(throwable.toString())
                );
        if (orderList.size() == 0) {
            throw new ExchangeException(errorSb.toString());
        }

        return orderList.get(0);
    }

    private String composePlaceOrderRequest(String symbol, FuturesContract contractType, BigDecimal price, BigDecimal amount, ContractOrderType type, String channelName) throws JsonProcessingException {
        String subscribeMessage;
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final String sign;
        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put("symbol", symbol);
        nameValueMap.put("contract_type", contractType.getName());
        nameValueMap.put("price", price.toPlainString());
        nameValueMap.put("amount", amount.toPlainString());
        nameValueMap.put("type", type.getValue());
        nameValueMap.put("match_price", "0"); // 0 - maker, 1 - taker
        nameValueMap.put("lever_rate", "10"); // 10/20 (10 by default)
        sign = signer.digestParams(nameValueMap);

        final RequestPlaceOrderParameters requestPlaceOrderParameters = new RequestPlaceOrderParameters(apiKey,
                sign,
                symbol,
                contractType.getName(),
                price.toPlainString(),
                amount.toPlainString(),
                type.getValue(),
                "0",
                "10"
        );
        RequestMessage webSocketMessage = new RequestMessage("addChannel", channelName, requestPlaceOrderParameters);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(RequestOrderInfoParameters.class);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        subscribeMessage = objectMapper.writeValueAsString(webSocketMessage);
        return subscribeMessage;
    }

    // TODO check it
    public LimitOrder getOrderInfo(String symbol, String orderId) {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final String sign;

        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put("symbol", symbol);
        nameValueMap.put("order_id", orderId);
        sign = signer.digestParams(nameValueMap);

        final OpenOrders openOrders = service.subscribeChannel("ok_spotusd_orderinfo", apiKey, sign, symbol, orderId)
                .map(jsonNode -> {
                    final OkCoinOrderResult okCoinOrderResult = parseResult(jsonNode);
                    return OkCoinAdapters.adaptOpenOrders(Collections.singletonList(okCoinOrderResult));
                }).blockingSingle();
        return openOrders.getOpenOrders().get(0);
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
