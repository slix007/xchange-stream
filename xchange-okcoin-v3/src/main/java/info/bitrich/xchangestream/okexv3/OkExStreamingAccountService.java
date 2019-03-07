package info.bitrich.xchangestream.okexv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.dto.AccountInfoContracts;
import info.bitrich.xchangestream.okexv3.dto.OkCoinAuthSigner;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserInfoResult;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.exceptions.ExchangeException;

/**
 * Created by Sergey Shurmin on 6/18/17.
 */
public class OkExStreamingAccountService implements StreamingAccountService {

    private final OkCoinStreamingService service;
    private final Exchange exchange;

    OkExStreamingAccountService(OkCoinStreamingService service, Exchange exchange) {
        this.service = service;
        this.exchange = exchange;
    }

    @Override
    public Observable<AccountInfo> getAccountInfoObservable(CurrencyPair currencyPair, Object... args) {

        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final OkCoinAuthSigner signer = OkCoinAuthSigner.fromExchange(exchange);
//        final Map<String, String> nameValueMap = new HashMap<>();
//        final String sign = signer.sign();
        return null;

//        return service.subscribeChannel("ok_futureusd_userinfo", apiKey, sign)
//                .map(jsonNode -> {
//                    final JsonNode dataNode = jsonNode.get("data");
//                    if (!dataNode.get("result").asBoolean()) {
//                        throw new ExchangeException(String.format("Translated error: %s(%s). Full response: %s",
//                                jsonNode.get("error_code") != null ? jsonNode.get("error_code").asText() : "",
//                                jsonNode.get("errorcode") != null ? jsonNode.get("errorcode").asText() : "",
//                                jsonNode));
//                    }
//                    final JsonNode infoNode = dataNode.get("info");
//                    final OkExUserInfoResult infoResult = mapper.treeToValue(infoNode, OkExUserInfoResult.class);
//
//                    return OkExAdapters.adaptUserInfo(baseTool, infoResult, infoNode.toString());
//                });
    }

    public AccountInfoContracts getAccountInfo() {
        return null;
//        final String apiKey = exchange.getExchangeSpecification().getApiKey();
//        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
//        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
//        final Map<String, String> nameValueMap = new HashMap<>();
//        final String sign = signer.digestParams(nameValueMap);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.registerModule(new JavaTimeModule());
//
//        List<AccountInfoContracts> accountInfo = new ArrayList<>();
//
//        service.subscribeChannel("ok_futureusd_userinfo", apiKey, sign)
//                .take(1)
//                .timeout(5000, TimeUnit.MILLISECONDS)
//                .blockingSubscribe(jsonNode -> {
//                            final JsonNode dataNode = jsonNode.get("data");
//                            if (!dataNode.get("result").asBoolean()) {
//                                throw new ExchangeException(jsonNode.get("error_code").asText());
//                            }
//                            final JsonNode infoNode = dataNode.get("info");
//                            final OkExUserInfoResult infoResult = mapper.treeToValue(infoNode, OkExUserInfoResult.class);
//
//                            accountInfo.add(OkExAdapters.adaptUserInfo(baseTool, infoResult, infoNode.toString()));
//                        },
//                        throwable -> {
//                            throw new ExchangeException(throwable.getMessage());
//                        });
//        return accountInfo.size() > 0 ? accountInfo.get(0) : new AccountInfoContracts();
    }

//    public Observable<AccountInfoContracts> accountInfoObservable(Tool baseTool) {
//        final String apiKey = exchange.getExchangeSpecification().getApiKey();
//        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
//        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
//        final Map<String, String> nameValueMap = new HashMap<>();
//        final String sign = signer.digestParams(nameValueMap);
//
//
//        return service.subscribeChannel("ok_futureusd_userinfo", apiKey, sign)
//                .map(jsonNode -> {
//                    final JsonNode dataNode = jsonNode.get("data");
//                    if (!dataNode.get("result").asBoolean()) {
//                        throw new ExchangeException(String.format("Translated error: %s(%s). Full response: %s",
//                                jsonNode.get("error_code") != null ? jsonNode.get("error_code").asText() : "",
//                                jsonNode.get("errorcode") != null ? jsonNode.get("errorcode").asText() : "",
//                                jsonNode));
//                    }
//                    final JsonNode infoNode = dataNode.get("info");
//                    final OkExUserInfoResult infoResult = mapper.treeToValue(infoNode, OkExUserInfoResult.class);
//
//                    return OkExAdapters.adaptUserInfo(baseTool, infoResult, infoNode.toString());
//                });
//    }

}

