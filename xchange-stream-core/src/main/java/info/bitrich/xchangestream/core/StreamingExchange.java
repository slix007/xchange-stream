package info.bitrich.xchangestream.core;

import io.reactivex.Completable;
import org.knowm.xchange.Exchange;

public interface StreamingExchange extends Exchange {
    /**
     * Connects to the WebSocket API of the exchange.
     *
     * @return {@link Completable} that completes upon successful connection.
     */
    Completable connect();

    /**
     * Register event onDisconnect. It is helpful for re-connections.
     * @return {@link Completable} that completes upon disconnect event.
     */
    Completable onDisconnect();

    /**
     * Disconnect from the WebSocket API.
     *
     * @return {@link Completable} that completes upon successful disconnect.
     */
    Completable disconnect();

    /**
     * Returns service that can be used to access market data.
     */
    StreamingMarketDataService getStreamingMarketDataService();


    /**
     * Returns service that can be used to access account data.
     */
    StreamingAccountService getStreamingAccountService();
}
