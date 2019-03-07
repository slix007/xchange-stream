package info.bitrich.xchangestream.okexv3;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import io.reactivex.disposables.Disposable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.okcoin.FuturesContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sergei Shurmin on 02.03.19.
 */
public class OkExManualExample {

    private static final Logger LOG = LoggerFactory.getLogger(OkExManualExample.class);

    public static void main(String[] args) {
        final InstrumentDto instrumentDto = new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek);

        StreamingExchange exchange = StreamingExchangeFactory.INSTANCE.createExchange(OkExStreamingExchange.class.getName());
        final ExchangeSpecification spec = exchange.getExchangeSpecification();
        spec.setExchangeSpecificParametersItem(StreamingExchange.ENABLE_LOGGING_HANDLER, Boolean.TRUE);
        exchange.applySpecification(spec);
        exchange.connect().blockingAwait();

//        CurrencyPair btcUsd = new CurrencyPair(new Currency("BTC"), new Currency("USD"));
        final Disposable obSub = exchange.getStreamingMarketDataService()
                .getOrderBook(CurrencyPair.BTC_USD, instrumentDto, true)
                .subscribe(orderBook -> {
                    LOG.info("First ask: {}", orderBook.getAsks().get(0));
                    LOG.info("First bid: {}", orderBook.getBids().get(0));
                }, throwable -> LOG.error("ERROR in getting order book: ", throwable));

        final Disposable tickerSub = exchange.getStreamingMarketDataService()
                .getTicker(CurrencyPair.BTC_USD, instrumentDto)
                .subscribe(ticker -> {
                    LOG.info("TICKER: {}", ticker);
                }, throwable -> LOG.error("ERROR in getting ticker: ", throwable));

//        exchange.getStreamingMarketDataService().getTrades(btcUsdt).subscribe(trade -> {
//            LOG.info("TRADE: {}", trade);
//        }, throwable -> LOG.error("ERROR in getting trades: ", throwable));

        try {
            System.out.println("SLEEP");
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("DONE");
            obSub.dispose();
            tickerSub.dispose();
            exchange.disconnect()
                    .subscribe();
        }
    }
}
