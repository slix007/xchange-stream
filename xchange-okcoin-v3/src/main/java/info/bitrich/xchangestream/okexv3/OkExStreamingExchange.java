package info.bitrich.xchangestream.okexv3;

import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.core.StreamingPrivateDataService;
import info.bitrich.xchangestream.core.StreamingTradingService;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;

/**
 * Created by Sergei Shurmin on 02.03.19.
 */
public class OkExStreamingExchange extends OkCoinStreamingExchange {
    protected static final String API_URI = "wss://real.okex.com:10442/ws/v3";

    private OkExStreamingPrivateDataService streamingPrivateDataService;
//    private OkExStreamingAcc streamingPrivateDataService;
    private OkExStreamingMarketDataService streamingMarketDataService;

    public OkExStreamingExchange() {
        super(API_URI);
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingPrivateDataService = new OkExStreamingPrivateDataService(streamingService, this);
        streamingMarketDataService = new OkExStreamingMarketDataService(streamingService);
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        return streamingMarketDataService;
    }

    @Override
    public StreamingPrivateDataService getStreamingPrivateDataService() {
        return streamingPrivateDataService;
    }

    @Override
    public StreamingAccountService getStreamingAccountService() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public StreamingTradingService getStreamingTradingService() {
        throw new NotYetImplementedForExchangeException();
    }

}
