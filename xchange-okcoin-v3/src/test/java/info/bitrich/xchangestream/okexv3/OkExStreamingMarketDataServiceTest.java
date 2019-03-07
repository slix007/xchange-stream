package info.bitrich.xchangestream.okexv3;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.okcoin.FuturesContract;

public class OkExStreamingMarketDataServiceTest {

    private OkExStreamingExchange exchange;

    @Before
    public void setUp() {
        exchange = (OkExStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(OkExStreamingExchange.class.getName());
        exchange.connect().blockingAwait();
    }

    @Test
    public void getOrderBook() {
        final InstrumentDto instrumentDto = new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek);

        final OrderBook orderBook = exchange.getStreamingMarketDataService()
                .getOrderBook(CurrencyPair.BTC_USD, instrumentDto, true)
                .blockingFirst();

        Assert.assertNotNull(orderBook);
        Assert.assertTrue(orderBook.getAsks().size() > 0);
        Assert.assertTrue(orderBook.getAsks().get(0).getOriginalAmount().signum() > 0);
    }

    @Test
    public void getTicker() {
        final InstrumentDto instrumentDto = new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek);

        final Ticker ticker = exchange.getStreamingMarketDataService()
                .getTicker(CurrencyPair.BTC_USD, instrumentDto)
                .blockingFirst();

        Assert.assertNotNull(ticker);
        Assert.assertNotNull(ticker.getAsk());
        Assert.assertNotNull(ticker.getLast());
    }

    @Test
    public void getTickerSpot() {
        final Ticker ticker = exchange.getStreamingMarketDataService()
                .getTicker(CurrencyPair.BTC_USD, null, "ETH-BTC")
                .blockingFirst();

        Assert.assertNotNull(ticker);
        Assert.assertNotNull(ticker.getAsk());
        Assert.assertNotNull(ticker.getLast());
    }


}