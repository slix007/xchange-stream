package info.bitrich.xchangestream.poloniex;

import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.wamp.WampStreamingService;

import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.poloniex.PoloniexExchange;

import io.reactivex.Completable;

public class PoloniexStreamingExchange extends PoloniexExchange implements StreamingExchange {
    private static final String API_URI = "wss://api.poloniex.com";
    private static final String API_REALM = "realm1";

    private final WampStreamingService streamingService;
    private PoloniexStreamingMarketDataService streamingMarketDataService;

    public PoloniexStreamingExchange() {
        streamingService = new WampStreamingService(API_URI, API_REALM);
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingMarketDataService = new PoloniexStreamingMarketDataService(streamingService);
    }

    @Override
    public Completable connect() {
        return streamingService.connect();
    }

    @Override
    public Completable onDisconnect() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public Completable disconnect() {
        return null;
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
