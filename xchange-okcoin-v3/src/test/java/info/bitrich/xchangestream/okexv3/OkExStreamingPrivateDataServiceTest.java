package info.bitrich.xchangestream.okexv3;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import info.bitrich.xchangestream.service.ws.statistic.PingStatEvent;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.okcoin.FuturesContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkExStreamingPrivateDataServiceTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String apiKey = "68d6396d-7f11-40a1-8564-12fb6da81134";
    private static final String displayName = "localtest";
    private static final String secretKey = "92CEFAB9CC9F9F1E771C7B26904546FE";
    private static final String passphrase = "LocalTestKey";

    private OkExStreamingExchange exchange;

    @Before
    public void setUp() {
        exchange = (OkExStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(OkExStreamingExchange.class.getName());
        final ExchangeSpecification spec = exchange.getExchangeSpecification();
        spec.setApiKey(apiKey);
        spec.setPassword(passphrase);
        spec.setSecretKey(secretKey);
        exchange.applySpecification(spec);
        exchange.connect().blockingAwait();
    }

//    @Test
    public void getAllPrivateDataObservable() throws Exception {

        exchange.streamingService.subscirbeUnknownChannel()
//                .doOnEach(e -> log.info("error_channel: " + e.toString(), e))
//                .doOnError(e -> log.info("error_channel: " + e))
                .subscribe(jsonNode -> log.info("unknown_channel: " + jsonNode.toString()),
                        e -> log.info("unknown_channel exception: " + e.toString()));

        exchange.reconnectFailure()
                .doOnNext(System.out::println)
                .subscribe();
        exchange.connectionSuccess()
                .doOnNext(System.out::println)
                .subscribe();
        exchange.onDisconnect()
                .doOnEvent(System.out::println)
                .subscribe();
        exchange.subscribePingStats()
                .map(PingStatEvent::getPingPongMs)
                .doOnNext(System.out::println)
                .subscribe();

        final boolean loginSuccess = exchange.getStreamingPrivateDataService()
                .login()
                .blockingAwait(5, TimeUnit.SECONDS);
        System.out.println("Login success=" + loginSuccess);

        final Disposable subscribe = exchange.getStreamingPrivateDataService()
                .getAccountInfoObservable(CurrencyPair.BTC_USD)
                .doOnNext(System.out::println)
                .take(5)
                .subscribe();

        final Disposable subscribe1 = exchange.getStreamingPrivateDataService()
                .getPositionObservable(new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek))
                .doOnNext(System.out::println)
                .take(5)
                .subscribe();

        final Disposable subscribe3 = exchange.getStreamingPrivateDataService()
                .getTradesObservable(new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek))
                .doOnNext(System.out::println)
                .take(5)
                .subscribe();

        Thread.sleep(10000 * 6 * 30);

        exchange.disconnect().subscribe();
    }
}