package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import info.bitrich.xchangestream.okcoin.OkCoinAuthSigner;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;
import info.bitrich.xchangestream.okex.dto.OkExUserInfoResult;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.exceptions.ExchangeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey Shurmin on 6/18/17.
 */
public class OkExStreamingAccountInfoService {

    private final OkCoinStreamingService service;
    private final Exchange exchange;

    OkExStreamingAccountInfoService(OkCoinStreamingService service, Exchange exchange) {
        this.service = service;
        this.exchange = exchange;
    }

    public AccountInfo getAccountInfo() {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        final String sign = signer.digestParams(nameValueMap);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());


        // The same info for all subscriptions:
        // {"event":"login","parameters":{"api_key":"xxx","sign":"xxx"}}
        // or "channel": "ok_sub_spotusd_trades"
        // or "channel": "ok_sub_spotusd_userinfo",
        // Successful response for all: [{"data":{"result":"true"},"channel":"login"}]
        List<AccountInfo> accountInfo = new ArrayList<>();

        service.subscribeChannel("ok_futureusd_userinfo", apiKey, sign)
                .take(1)
                .timeout(2000, TimeUnit.MILLISECONDS)
                .blockingSubscribe(jsonNode -> {
                            final JsonNode dataNode = jsonNode.get("data");
                            if (!dataNode.get("result").asBoolean()) {
                                throw new ExchangeException(jsonNode.get("error_code").asText());
                            }
                            final JsonNode infoNode = dataNode.get("info");
                            final OkExUserInfoResult infoResult = mapper.treeToValue(infoNode, OkExUserInfoResult.class);

                            accountInfo.add(OkExAdapters.adaptUserInfo(infoResult, infoNode.toString()));
                        },
                        throwable -> {
                            throw new ExchangeException(throwable.getMessage());
                        });
        return accountInfo.size() > 0 ? accountInfo.get(0) : new AccountInfo();
    }
}
