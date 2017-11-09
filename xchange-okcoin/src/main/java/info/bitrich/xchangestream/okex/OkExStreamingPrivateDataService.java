package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import info.bitrich.xchangestream.core.StreamingPrivateDataService;
import info.bitrich.xchangestream.core.dto.PrivateData;
import info.bitrich.xchangestream.okcoin.OkCoinAuthSigner;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;
import info.bitrich.xchangestream.okex.dto.BalanceEx;
import info.bitrich.xchangestream.okex.dto.OkExTradeResult;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfoContracts;
import org.knowm.xchange.dto.account.Position;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sergey Shurmin on 6/18/17.
 */
public class OkExStreamingPrivateDataService implements StreamingPrivateDataService {
    private static final Logger logger = LoggerFactory.getLogger(OkExStreamingPrivateDataService.class);

    private final OkCoinStreamingService service;
    private final Exchange exchange;

    OkExStreamingPrivateDataService(OkCoinStreamingService service, Exchange exchange) {
        this.service = service;
        this.exchange = exchange;
    }

    @Override
    public Observable<PrivateData> getAllPrivateDataObservable() {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        final String sign = signer.digestParams(nameValueMap);

        // The same info for all subscriptions:
        // Successful response for all: [{"data":{"result":"true"},"channel":"login"}]
        return service.subscribeBatchChannels("ok_sub_futureusd_userinfo",
                Arrays.asList("ok_sub_futureusd_userinfo",
                        "ok_sub_futureusd_positions",
                        "ok_sub_futureusd_trades"),
                apiKey,
                sign)
                .observeOn(Schedulers.computation())
                .map(this::parseResult);
    }

    public Observable<PrivateData> getUserInfoObservable() {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        final String sign = signer.digestParams(nameValueMap);

        return service.subscribeChannel("ok_sub_futureusd_userinfo",
                apiKey,
                sign)
                .observeOn(Schedulers.computation())
                .map(this::parseResult);
    }

    public Observable<PrivateData> getTradesObservable() {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        final String sign = signer.digestParams(nameValueMap);

        return service.subscribeChannel("ok_sub_futureusd_trades",
                apiKey,
                sign)
                .observeOn(Schedulers.computation())
                .map(this::parseResult);
    }

    public Observable<PrivateData> getPositionObservable() {
        final String apiKey = exchange.getExchangeSpecification().getApiKey();
        final String secretKey = exchange.getExchangeSpecification().getSecretKey();
        final OkCoinAuthSigner signer = new OkCoinAuthSigner(apiKey, secretKey);
        final Map<String, String> nameValueMap = new HashMap<>();
        final String sign = signer.digestParams(nameValueMap);

        return service.subscribeChannel("ok_sub_futureusd_positions",
                apiKey,
                sign)
                .observeOn(Schedulers.computation())
                .map(this::parseResult);
    }

    PrivateData parseResult(JsonNode jsonNode) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        List<LimitOrder> trades = new ArrayList<>();
        AccountInfoContracts accountInfo = null;
        Position positionInfo = null;

        final JsonNode channel = jsonNode.get("channel");
        if (channel != null) {
            final JsonNode dataNode = jsonNode.get("data");

            logger.debug("PrivateData:" + channel.asText() + ":" + dataNode.toString());

            switch (channel.asText()) {
                case "ok_sub_futureusd_trades":
                    // TODO parse future trades
                    final OkExTradeResult okExTradeResult = mapper.treeToValue(dataNode, OkExTradeResult.class);
                    final LimitOrder limitOrder = OkExAdapters.adaptTradeResult(okExTradeResult);
                    trades.add(limitOrder);
                    break;
                case "ok_sub_futureusd_userinfo":
                    // TODO parse user info
                    final BigDecimal wallet = new BigDecimal(dataNode.get("balance").asText());
//                    final BigDecimal amountStr = new BigDecimal(dataNode.get("unit_amount").asText());
                    final BigDecimal profitReal = new BigDecimal(dataNode.get("profit_real").asText());
                    final BigDecimal margin = new BigDecimal(dataNode.get("keep_deposit").asText());
//                    final BigDecimal available = wallet.subtract(equity);
                    accountInfo = new AccountInfoContracts(wallet, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                            margin, BigDecimal.ZERO, profitReal, BigDecimal.ZERO);
                    break;
                case "ok_sub_futureusd_positions":
                    final JsonNode positionsNode = dataNode.get("positions");
                    positionInfo = adaptPosition(positionsNode);
                    break;
                default:
                    System.out.println("Warning unknown response channel");
            }
        }

        return new PrivateData(trades, accountInfo, positionInfo);
    }

    private Position adaptPosition(JsonNode positionsNode) {
        BigDecimal positionLong = BigDecimal.ZERO;
        BigDecimal positionShort = BigDecimal.ZERO;
        for (JsonNode node : positionsNode) {
            final String holdAmountInContracts = node.get("hold_amount").asText();
//            final String available = node.get("eveningup").asText();
//            final String bondfreez = node.get("bondfreez").asText();
            final String position = node.get("position").asText();
            if (position.equals("1")) { // long - buy - bid
                positionLong = new BigDecimal(holdAmountInContracts);
            } else if (position.equals("2")) { // short - sell - ask
                positionShort = new BigDecimal(holdAmountInContracts);
            }
        }

        return new Position(positionLong, positionShort, BigDecimal.ZERO, BigDecimal.ZERO, positionsNode.toString());
    }

}
