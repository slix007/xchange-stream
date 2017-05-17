package info.bitrich.xchange.coinmate;

import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.core.StreamingTradingService;
import info.bitrich.xchangestream.service.pusher.PusherStreamingService;

import org.knowm.xchange.coinmate.CoinmateExchange;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;

import io.reactivex.Completable;

public class CoinmateStreamingExchange extends CoinmateExchange implements StreamingExchange {
    private static final String API_KEY = "af76597b6b928970fbb0";
    private final PusherStreamingService streamingService;

    private CoinmateStreamingMarketDataService streamingMarketDataService;

    public CoinmateStreamingExchange() {
        streamingService = new PusherStreamingService(API_KEY);
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingMarketDataService = new CoinmateStreamingMarketDataService(streamingService);
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

    @Override
    public StreamingTradingService getStreamingTradingService() {
        throw new NotAvailableFromExchangeException();
    }
}
