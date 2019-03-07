package info.bitrich.xchangestream.okexv3;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchangeEx;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.core.StreamingPrivateDataService;
import info.bitrich.xchangestream.core.StreamingTradingService;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.apache.commons.lang3.StringUtils;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.okcoin.OkCoinExchange;

/**
 * Created by Sergei Shurmin on 02.03.19.
 */
public class OkCoinStreamingExchange extends OkCoinExchange implements StreamingExchangeEx {

    private static final String API_URI = "wss://real.okcoin.com:10442/ws/v3";

    private final String apiUrl;

    protected OkCoinStreamingService streamingService;

    public OkCoinStreamingExchange() {
        this.apiUrl = API_URI;
    }

    protected OkCoinStreamingExchange(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingService = createStreamingService(apiUrl);
    }

    protected OkCoinStreamingService createStreamingService(String apiUrl) {
        OkCoinStreamingService streamingService = new OkCoinStreamingService(apiUrl);
        applyStreamingSpecification(getExchangeSpecification(), streamingService);
        if (StringUtils.isNotEmpty(exchangeSpecification.getApiKey())) {
//            streamingService.setApiKey(exchangeSpecification.getApiKey());
//            streamingService.setApiSecret(exchangeSpecification.getSecretKey());
        }
        return streamingService;
    }

    @Override
    public Completable connect(ProductSubscription... args) {
        return streamingService.connect();
    }

    @Override
    public Completable onDisconnect() {
        return streamingService.onDisconnect();
    }

    @Override
    public Completable disconnect() {
        return streamingService.disconnect();
    }

    @Override
    public boolean isAlive() {
        return streamingService.isSocketOpen();
    }

    @Override
    public Observable<Throwable> reconnectFailure() {
        return streamingService.subscribeReconnectFailure();
    }

    @Override
    public Observable<Object> connectionSuccess() {
        return streamingService.subscribeConnectionSuccess();
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public StreamingPrivateDataService getStreamingPrivateDataService() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public StreamingAccountService getStreamingAccountService() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public StreamingTradingService getStreamingTradingService() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {
        // always true
    }
}
