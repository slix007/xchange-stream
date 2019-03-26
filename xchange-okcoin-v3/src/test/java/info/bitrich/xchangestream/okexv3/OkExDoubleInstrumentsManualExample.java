package info.bitrich.xchangestream.okexv3;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import io.reactivex.disposables.Disposable;
import java.util.Arrays;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.okcoin.FuturesContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sergei Shurmin on 02.03.19.
 */
public class OkExDoubleInstrumentsManualExample {

    private static final Logger LOG = LoggerFactory.getLogger(OkExDoubleInstrumentsManualExample.class);

    public static void main(String[] args) {
        StreamingExchange exchange = StreamingExchangeFactory.INSTANCE.createExchange(OkExStreamingExchange.class.getName());
        exchange.connect().blockingAwait();

        final List<InstrumentDto> instrumentDtos = Arrays.asList(
                new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek),
//                new InstrumentDto(CurrencyPair.ETH_USD, FuturesContract.Quarter)
                new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.Quarter)
        );

        final Disposable subscribe = ((OkExStreamingMarketDataService) exchange.getStreamingMarketDataService())
                .getOrderBooks(instrumentDtos, true).subscribe(orderBook -> {
//                    LOG.info(orderBook.toString());
//                    LOG.info("First ask: {}", orderBook.getAsks().get(0));
//                    LOG.info("First bid: {}", orderBook.getBids().get(0));
                }, throwable -> LOG.error("ERROR in getting order book: ", throwable));

//        final Disposable subscribe = ((OkExStreamingMarketDataService) exchange.getStreamingMarketDataService())
//                .getTickers(currencies, expDate).subscribe(ticker -> {
//                    LOG.info("TICKER: {}", ticker);
//                }, throwable -> LOG.error("ERROR in getting ticker: ", throwable));

//        exchange.getStreamingMarketDataService().getTrades(btcUsdt).subscribe(trade -> {
//            LOG.info("TRADE: {}", trade);
//        }, throwable -> LOG.error("ERROR in getting trades: ", throwable));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            subscribe.dispose(); // sending 'unsubscribe' (while in progress leads to messages 'Channel has been closed...')
            exchange.disconnect().subscribe();
        }
    }
}
