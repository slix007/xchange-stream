package info.bitrich.xchangestream.core;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.marketdata.OrderBook;

import io.reactivex.Observable;

/**
 * Created by Sergey Shurmin on 5/8/17.
 */
public interface StreamingAccountService {

    /**
     * Get an order book representing the current offered exchange rates (market depth). Emits {@link
     * info.bitrich.xchangestream.service.exception.NotConnectedException} When not connected to the WebSocket API.
     * Emits {@link info.bitrich.xchangestream.service.exception.NotAuthorizedException} When not authorized.
     *
     * @param currencyPair Currency pair of the order book
     * @return {@link Observable} that emits {@link org.knowm.xchange.dto.account.AccountInfo} when exchange sends the
     * update.
     */
    Observable<AccountInfo> getAccountInfoObservable(CurrencyPair currencyPair, Object... args);
}
