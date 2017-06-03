package info.bitrich.xchangestream.okcoin;

import info.bitrich.xchangestream.core.StreamingTradingService;

import org.knowm.xchange.dto.trade.OpenOrders;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import io.reactivex.Observable;

public class OkCoinStreamingTradingService implements StreamingTradingService {
    private final OkCoinStreamingService service;

    OkCoinStreamingTradingService(OkCoinStreamingService service) {
        this.service = service;
    }

    @Override
    public Observable<OpenOrders> getOpenOrdersObservable() {
        throw new NotImplementedException();
    }

}
