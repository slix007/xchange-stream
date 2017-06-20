package info.bitrich.xchangestream.okex;

import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.core.StreamingPrivateDataService;
import info.bitrich.xchangestream.core.StreamingTradingService;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;

import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.okcoin.OkCoinExchange;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

/**
 * OkCoin keeps the channel open about 5 min.
 * After this time you'll receive {@link #onDisconnect()} event.
 *
 * To avoid disconnection we have to send keepalive heartbeats(ping).
 */
public class OkExStreamingExchange extends OkCoinExchange implements StreamingExchange {
    private static final String API_URI_FEATURES = "wss://real.okex.com:10440/websocket/okcoinapi";

    private final OkCoinStreamingService streamingService;
    private OkExStreamingMarketDataService streamingMarketDataService;
    private OkExStreamingTradingService streamingTradingService;
    private OkExStreamingPrivateDataService streamingPrivateDataService;

    public OkExStreamingExchange() {
        streamingService = new OkCoinStreamingService(API_URI_FEATURES);
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingMarketDataService = new OkExStreamingMarketDataService(streamingService);
        streamingTradingService = new OkExStreamingTradingService(streamingService, this);

        streamingPrivateDataService = new OkExStreamingPrivateDataService(streamingService, this);

    }

    @Override
    public Completable connect() {
        return streamingService.connect(1L, TimeUnit.MINUTES);
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
    public StreamingMarketDataService getStreamingMarketDataService() {
        return streamingMarketDataService;
    }

    @Override
    public StreamingAccountService getStreamingAccountService() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public StreamingTradingService getStreamingTradingService() {
        return streamingTradingService;
    }

    public StreamingPrivateDataService getStreamingPrivateDataService() {
        return streamingPrivateDataService;
    }
}
