package info.bitrich.xchangestream.service.ws;

import io.reactivex.ObservableEmitter;

class Subscription<T> {

    final ObservableEmitter<T> emitter;
    final String channelName;
    final Object[] args;

    Subscription(ObservableEmitter<T> emitter, String channelName, Object[] args) {
        this.emitter = emitter;
        this.channelName = channelName;
        this.args = args;
    }

}
