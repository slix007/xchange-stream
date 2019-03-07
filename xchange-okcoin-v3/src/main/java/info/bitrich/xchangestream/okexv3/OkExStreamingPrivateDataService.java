package info.bitrich.xchangestream.okexv3;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingPrivateDataService;
import info.bitrich.xchangestream.core.dto.AccountInfoContracts;
import info.bitrich.xchangestream.core.dto.Position;
import info.bitrich.xchangestream.core.helper.WsObjectMapperHelper;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import info.bitrich.xchangestream.okexv3.dto.OkCoinAuthSigner;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExPosition;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserInfoResult;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserOrder;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sergey Shurmin on 6/18/17.
 */
public class OkExStreamingPrivateDataService implements StreamingPrivateDataService {

    private static final Logger logger = LoggerFactory.getLogger(OkExStreamingPrivateDataService.class);

    private final ObjectMapper objectMapper = WsObjectMapperHelper.getObjectMapper();

    private final OkCoinStreamingService service;
    private final Exchange exchange;

    OkExStreamingPrivateDataService(OkCoinStreamingService service, Exchange exchange) {
        this.service = service;
        this.exchange = exchange;
    }

    @Override
    public Completable login() {
        final OkCoinAuthSigner authSigner = OkCoinAuthSigner.fromExchange(exchange);
        return service.doLogin(authSigner);
    }

    @Override
    public Observable<AccountInfoContracts> getAccountInfoObservable(CurrencyPair currencyPair, Object... args) {
        // {"op": "subscribe", "args": ["futures/account:BTC"]}
        final String curr = currencyPair.base.toString().toUpperCase();
        final String channelName = "futures/account:" + curr;

        final Observable<AccountInfoContracts> observable = service.subscribeChannel(channelName)
                .observeOn(Schedulers.computation())
                .map(s -> s.get("data"))
                .flatMap(Observable::fromIterable)
                .map(s -> objectMapper.treeToValue(s, OkExUserInfoResult.class))
                .map(result -> OkExAdapters.adaptUserInfo(currencyPair.base, result));

        return service.isLoggedInSuccessfully()
                ? observable
                : login().andThen(observable);
    }

    @Override
    public Observable<Position> getPositionObservable(InstrumentDto instrumentDto, Object... args) {
        // {"op": "subscribe", "args": ["futures/position:BTC-USD-170317"]}
        final String instrumentId = instrumentDto.getInstrumentId();
        final String channelName = "futures/position:" + instrumentId;
        final Observable<Position> observable = service.subscribeChannel(channelName)
                .observeOn(Schedulers.computation())
//                .doOnNext(System.out::println)
                .map(s -> s.get("data"))
                .flatMap(Observable::fromIterable)
//                .doOnNext(System.out::println)
                .map(s -> objectMapper.treeToValue(s, OkExPosition.class))
                .map(OkExAdapters::adaptPosition);

        return service.isLoggedInSuccessfully()
                ? observable
                : login().andThen(observable);
    }

    @Override
    public Observable<List<LimitOrder>> getTradesObservable(InstrumentDto instrumentDto) {
        // {"op": "subscribe", "args": ["futures/order:BTC-USD-170317"]}
        final String instrumentId = instrumentDto.getInstrumentId();
        final String channelName = "futures/order:" + instrumentId;
        final Observable<List<LimitOrder>> observable = service.subscribeChannel(channelName)
                .observeOn(Schedulers.computation())
                .map(s -> s.get("data"))
                .map(s -> objectMapper.treeToValue(s, OkExUserOrder[].class))
//                .doOnNext(s -> System.out.println(Arrays.asList(s)))
                .map(OkExAdapters::adaptTradeResult);

        return service.isLoggedInSuccessfully()
                ? observable
                : login().andThen(observable);

    }
}
