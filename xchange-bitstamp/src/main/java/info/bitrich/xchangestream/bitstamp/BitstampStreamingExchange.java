package info.bitrich.xchangestream.bitstamp;

import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.pusher.PusherStreamingService;

import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;

import io.reactivex.Completable;

public class BitstampStreamingExchange extends BitstampExchange implements StreamingExchange {
    private static final String API_KEY = "de504dc5763aeef9ff52";
    private final PusherStreamingService streamingService;

    private BitstampStreamingMarketDataService streamingMarketDataService;

    public BitstampStreamingExchange() {
        streamingService = new PusherStreamingService(API_KEY);
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingMarketDataService = new BitstampStreamingMarketDataService(streamingService);
    }

    @Override
    public Completable connect() {
        return streamingService.connect();
    }

    @Override
    public Completable onDisconnect() {
        throw new NotAvailableFromExchangeException();
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
        throw new NotAvailableFromExchangeException();
    }
}
