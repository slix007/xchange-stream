package info.bitrich.xchangestream.okcoin;

import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;

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
public class OkCoinStreamingExchange extends OkCoinExchange implements StreamingExchange {
    private static final String API_URI = "wss://real.okcoin.com:10440/websocket/okcoinapi";

    private final OkCoinStreamingService streamingService;
    private OkCoinStreamingMarketDataService streamingMarketDataService;

    public OkCoinStreamingExchange() {
        streamingService = new OkCoinStreamingService(API_URI);
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingMarketDataService = new OkCoinStreamingMarketDataService(streamingService);
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
}
