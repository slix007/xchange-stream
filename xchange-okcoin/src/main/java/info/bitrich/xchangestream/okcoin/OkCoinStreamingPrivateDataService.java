package info.bitrich.xchangestream.okcoin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import info.bitrich.xchangestream.core.StreamingPrivateDataService;
import info.bitrich.xchangestream.core.dto.PrivateData;
import info.bitrich.xchangestream.okcoin.dto.OkCoinTradeResult;
import info.bitrich.xchangestream.okcoin.dto.OkCoinUserInfoResult;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.OkCoinAdapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;

public class OkCoinStreamingPrivateDataService implements StreamingPrivateDataService {
    private final OkCoinStreamingService service;
    private final Exchange exchange;

    OkCoinStreamingPrivateDataService(OkCoinStreamingService service, Exchange exchange) {
        this.service = service;
        this.exchange = exchange;
    }

    @Override
    public Observable<PrivateData> getAllPrivateDataObservable(String mainToolName) {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        final String sign = signer.digestParams(nameValueMap);

        // The same info for all subscriptions:
        // {"event":"login","parameters":{"api_key":"xxx","sign":"xxx"}}
        // or "channel": "ok_sub_spotusd_trades"
        // or "channel": "ok_sub_spotusd_userinfo",
        // Successful response for all: [{"data":{"result":"true"},"channel":"login"}]
        return service.subscribeBatchChannels("ok_sub_spotusd_trades",
                Arrays.asList("ok_sub_spotusd_trades", "ok_sub_spotusd_userinfo"),
                apiKey,
                sign)
                .map(this::parseResult);
    }

    PrivateData parseResult(JsonNode jsonNode) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        List<LimitOrder> trades = new ArrayList<>();
        AccountInfo accountInfo = null;

        final JsonNode channel = jsonNode.get("channel");
        if (channel != null) {
            final JsonNode dataNode = jsonNode.get("data");
            switch (channel.asText()) {
                case "ok_sub_spotusd_trades":
                    final OkCoinTradeResult okCoinTradeResult = mapper.treeToValue(dataNode, OkCoinTradeResult.class);
                    final LimitOrder limitOrder = adaptTradeResult(okCoinTradeResult);
                    trades.add(limitOrder);
                    break;
                case "ok_sub_spotusd_userinfo":
                    final OkCoinUserInfoResult okCoinUserInfoResult = mapper.treeToValue(dataNode, OkCoinUserInfoResult.class);
                    accountInfo = adaptUserInfo(okCoinUserInfoResult);
                    break;
                default:
                    System.out.println("Warning unknown response channel");
            }
        }

        return new PrivateData(trades, accountInfo);
    }

    private AccountInfo adaptUserInfo(OkCoinUserInfoResult okCoinUserInfoResult) {
        final OkCoinUserInfoResult.Free free = okCoinUserInfoResult.getInfo().getFree();
        Map<String, Balance.Builder> builders = new TreeMap<>();

        builders.put("btc", new Balance.Builder().currency(Currency.getInstance("btc")).available(free.getBtc()));
        builders.put("usd", new Balance.Builder().currency(Currency.getInstance("usd")).available(free.getUsd()));

        List<Balance> wallet = new ArrayList<>(builders.size());

        for (Balance.Builder builder : builders.values()) {
            wallet.add(builder.build());
        }

        return new AccountInfo(new Wallet(wallet));
    }

    private LimitOrder adaptTradeResult(OkCoinTradeResult okCoinTradeResult) {
        final Order.OrderType orderType = OkCoinAdapters.adaptOrderType(okCoinTradeResult.getTradeType());
        final CurrencyPair currencyPair = OkCoinAdapters.adaptSymbol(okCoinTradeResult.getSymbol());
        final String orderId = String.valueOf(okCoinTradeResult.getOrderId());
        final Order.OrderStatus orderStatus = OkCoinAdapters.adaptOrderStatus(okCoinTradeResult.getStatus());
        return new LimitOrder(orderType, okCoinTradeResult.getTradeAmount(), currencyPair,
                orderId, okCoinTradeResult.getCreatedDate(),
                okCoinTradeResult.getTradeUnitPrice(),
                okCoinTradeResult.getAveragePrice(),
                okCoinTradeResult.getCompletedTradeAmount(),
                orderStatus);
    }

}
