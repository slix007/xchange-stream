package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingService;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.okcoin.FuturesContract;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinDepth;

import io.reactivex.Observable;

public class OkExStreamingMarketDataService implements StreamingMarketDataService {
    private final OkCoinStreamingService service;

    OkExStreamingMarketDataService(OkCoinStreamingService service) {
        this.service = service;
    }

    public enum Tool {
        BTC("btc"), LTC("ltc");
        String name;
        Tool(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public enum Depth {
        DEPTH_20("20"), DEPTH_60("60");
        String name;
        Depth(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        // ok_sub_future_%s_depth_%s_%s - Market Depth(Full)
        // 1 value of X is: btc, ltc
        // 2 value of Y is: this_week, next_week, quarter
        // 3 value of Z is: 20, 60
        if (args.length != 3) {
            throw new IllegalArgumentException("Missing required params:\n" +
                    "1 value of X is: btc, ltc\n" +
                    "2 value of Y is: this_week, next_week, quarter\n" +
                    "3 value of Z is: 20, 60");
        }
        final Tool tool = Tool.valueOf(String.valueOf(args[0]));
        final FuturesContract futuresContract = FuturesContract.valueOf(String.valueOf(args[1]));
        final Depth depth = Depth.valueOf(String.valueOf(args[1]));

        return getOrderBook(currencyPair, tool, futuresContract, depth);
    }

    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Tool tool, FuturesContract futuresContract, Depth depth) {
        String channel = String.format("ok_sub_future_%s_depth_%s_%s", tool, futuresContract, depth);
        return service.subscribeChannel(channel)
                .map(s -> {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    OkCoinDepth okCoinDepth = mapper.treeToValue(s.get("data"), OkCoinDepth.class);
                    return OkExAdapters.adaptOrderBook(okCoinDepth, currencyPair);
                });
    }

    @Override
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        throw new NotYetImplementedForExchangeException("Use StreamingPrivateMarketDataService instead");
    }
}
