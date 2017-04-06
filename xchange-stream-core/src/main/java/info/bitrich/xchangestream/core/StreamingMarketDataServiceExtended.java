package info.bitrich.xchangestream.core;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.OrderBookUpdate;

import io.reactivex.Observable;

/**
 * Created by Sergey Shurmin on 4/6/17.
 */
public interface StreamingMarketDataServiceExtended extends StreamingMarketDataService {

    /**
     * Get only updates for order book (market depth).
     * Emits {@link info.bitrich.xchangestream.service.exception.NotConnectedException} When not connected to the WebSocket API.
     *
     * @param currencyPair Currency pair of the order book
     * @return {@link Observable} that emits {@link OrderBook} when exchange sends the update.
     */
    Observable<OrderBookUpdate> getOrderBookUpdate(CurrencyPair currencyPair, Object... args);

}
