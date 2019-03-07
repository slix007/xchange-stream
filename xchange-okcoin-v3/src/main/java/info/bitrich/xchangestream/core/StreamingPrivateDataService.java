package info.bitrich.xchangestream.core;

import info.bitrich.xchangestream.core.dto.AccountInfoContracts;
import info.bitrich.xchangestream.core.dto.Position;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import io.reactivex.Completable;
import io.reactivex.Observable;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;

/**
 * Channels that require login.
 * <br>
 * Created by Sergey Shurmin on 6/10/17.
 */
public interface StreamingPrivateDataService {

    Completable login();

    Observable<AccountInfoContracts> getAccountInfoObservable(CurrencyPair currencyPair, Object... args);

    Observable<Position> getPositionObservable(InstrumentDto instrumentDto, Object... args);

    Observable<List<LimitOrder>> getTradesObservable(InstrumentDto instrumentDto);


}