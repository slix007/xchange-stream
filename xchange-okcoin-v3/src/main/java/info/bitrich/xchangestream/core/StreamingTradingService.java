package info.bitrich.xchangestream.core;

import io.reactivex.Observable;
import org.knowm.xchange.dto.trade.OpenOrders;

public interface StreamingTradingService {

    /**
     * Get an order book representing the current offered exchange rates (market depth). Emits {@link info.bitrich.xchangestream.service.exception.NotConnectedException}
     * When not connected to the WebSocket API. Emits {@link info.bitrich.xchangestream.service.exception.NotAuthorizedException} When not authorized.
     *
     * @return {@link Observable} that emits {@link org.knowm.xchange.dto.trade.OpenOrders} when exchange sends the update.
     */
    Observable<OpenOrders> getOpenOrdersObservable();

    /**
     * Get one order information.
     *
     * @param args additional parameters like orderId
     * @return {@link Observable} that emits {@link org.knowm.xchange.dto.trade.OpenOrders} when exchange sends the update.
     */
    Observable<OpenOrders> getOpenOrderObservable(Object... args);
}
