package info.bitrich.xchangestream.okexv3;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingPrivateDataService;
import info.bitrich.xchangestream.core.dto.PositionStream;
import info.bitrich.xchangestream.core.helper.WsObjectMapperHelper;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import info.bitrich.xchangestream.okexv3.dto.OkCoinAuthSigner;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExPosition;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExSwapUserInfoResult;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserInfoResult;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkExUserOrder;
import info.bitrich.xchangestream.okexv3.dto.privatedata.OkexSwapPosition;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfoContracts;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.okcoin.FuturesContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        final InstrumentDto instrumentDto = args.length == 0 ? null : (InstrumentDto) args[0];
        final boolean isSwap = instrumentDto != null && instrumentDto.getFuturesContract() == FuturesContract.Swap;
        final String channelName = isSwap
                ? "swap/account:" + instrumentDto.getInstrumentId()
                : "futures/account:" + curr;

        final Observable<AccountInfoContracts> observable;
        if (isSwap) {
            observable = service.subscribeChannel(channelName)
                    .observeOn(Schedulers.computation())
                    .map(s -> s.get("data"))
                    .flatMap(Observable::fromIterable)
                    .map(s -> objectMapper.treeToValue(s, OkExSwapUserInfoResult.class))
                    .map(result -> OkExAdapters.adaptSwapUserInfo(currencyPair.base, result));
        } else {
            observable = service.subscribeChannel(channelName)
                    .observeOn(Schedulers.computation())
                    .map(s -> s.get("data"))
                    .flatMap(Observable::fromIterable)
                    .map(s -> objectMapper.treeToValue(s, OkExUserInfoResult.class))
                    .map(result -> OkExAdapters.adaptUserInfo(currencyPair.base, result));
        }

        return service.isLoggedInSuccessfully()
                ? observable
                : login().andThen(observable);
    }

    @Override
    public Observable<PositionStream> getPositionObservable(InstrumentDto instrumentDto, Object... args) {
        // {"op": "subscribe", "args": ["futures/position:BTC-USD-170317"]}
        final String instrumentId = instrumentDto.getInstrumentId();
        final Observable<PositionStream> observable;
        if (instrumentDto.getFuturesContract() == FuturesContract.Swap) {
            final String channelName = "swap/position:" + instrumentId;
            observable = service.subscribeChannel(channelName)
                    .observeOn(Schedulers.computation())
                    .map(s -> s.get("data"))
                    .flatMap(Observable::fromIterable)
                    .map(s -> s.get("holding"))
                    .flatMap(Observable::fromIterable)
//                    .doOnNext(System.out::println)
                    .map(s -> objectMapper.treeToValue(s, OkexSwapPosition.class))
//                    .doOnNext(System.out::println)
                    .map(OkExAdapters::adaptSwapPosition);
        } else {
            final String channelName = "futures/position:" + instrumentId;
            observable = service.subscribeChannel(channelName)
                    .observeOn(Schedulers.computation())
                    .map(s -> s.get("data"))
                    .flatMap(Observable::fromIterable)
                    .map(s -> objectMapper.treeToValue(s, OkExPosition.class))
                    .map(OkExAdapters::adaptPosition);
        }

        return service.isLoggedInSuccessfully()
                ? observable
                : login().andThen(observable);
    }

    @Override
    public Observable<List<LimitOrder>> getTradesObservable(InstrumentDto instrumentDto) {
        // {"op": "subscribe", "args": ["futures/order:BTC-USD-170317"]}
        final String instrumentId = instrumentDto.getInstrumentId();
        final String channelName = instrumentDto.getFuturesContract() == FuturesContract.Swap
                ? "swap/order:" + instrumentId
                : "futures/order:" + instrumentId;
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

    // try workaround java.lang.NoSuchMethodError because of different XChange lib versions
    public Observable<OkExUserOrder[]> getTradesObservableRaw(InstrumentDto instrumentDto) {
        // {"op": "subscribe", "args": ["futures/order:BTC-USD-170317"]}
        final String instrumentId = instrumentDto.getInstrumentId();
        final String channelName = instrumentDto.getFuturesContract() == FuturesContract.Swap
                ? "swap/order:" + instrumentId
                : "futures/order:" + instrumentId;
        final Observable<OkExUserOrder[]> observable = service.subscribeChannel(channelName)
                .observeOn(Schedulers.computation())
                .map(s -> s.get("data"))
                .map(s -> objectMapper.treeToValue(s, OkExUserOrder[].class));

        return service.isLoggedInSuccessfully()
                ? observable
                : login().andThen(observable);
    }
}
