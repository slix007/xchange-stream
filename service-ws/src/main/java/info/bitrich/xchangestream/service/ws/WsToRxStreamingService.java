package info.bitrich.xchangestream.service.ws;

import info.bitrich.xchangestream.service.exception.NotConnectedException;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WsToRxStreamingService<T> extends WsConnectableService implements ChannelSubscriber<T> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final String UNKNOWN_CHANNEL_ID = "unknown_channel";

    protected AuthSigner authSigner;
    private boolean loggedInSuccessfully = false;
    private final Map<String, Subscription<T>> channels = new ConcurrentHashMap<>();

    public WsToRxStreamingService(String apiUrl) {
        super(apiUrl);
    }

    public WsToRxStreamingService(String apiUrl, int maxFramePayloadLength) {
        super(apiUrl, maxFramePayloadLength);
    }

    public WsToRxStreamingService(String apiUrl, int maxFramePayloadLength, Duration connectionTimeout, Duration retryDuration) {
        super(apiUrl, maxFramePayloadLength, connectionTimeout, retryDuration);
    }

    @Override
    protected void afterConnect() {
        // resubscribe to channels
        log.warn("afterConnect: Resubscribing to the channels " + channels.keySet());
        if (channels.containsKey("login") && this.authSigner != null) {
            final Disposable disposable = doLogin(this.authSigner)
                    .subscribe(this::resubscribeToChannels,
                            throwable -> log.error("login failed", throwable));
        } else {
            loggedInSuccessfully = false;
            resubscribeToChannels();
        }
    }

    @Override
    public Completable onDisconnect() {
        return super.onDisconnect()
                .doOnEvent((throwable) -> {
                    loggedInSuccessfully = false;
                    channels.clear();
                });
    }

    private void resubscribeToChannels() {
        for (String channelId : channels.keySet()) {
            if (channelId.equals("login")) {
                continue;
            }
            try {
                Subscription subscription = channels.get(channelId);
                sendMessage(getSubscribeMessage(subscription.channelName, subscription.args));
            } catch (Exception e) {
                log.error("Failed to reconnect channel: {}", channelId);
            }
        }
    }

    protected Completable doLogin(AuthSigner authSigner) {
        loggedInSuccessfully = true;
        return Completable.complete();
    }


    public Observable<T> subscribeBatchChannels(
            List<String> channelNames,
            Object... args) {

        // Example:
        // channelName:
        // futures/ticker:BTC-USD-170310
        // futures/ticker:ETH-USD-170310
        // channelId:
        // futures/ticker
        // futures/ticker

        final String channelId = getSubscriptionUniqueId(channelNames.get(0), args);

        final String allChannelNames = Arrays.toString(new List[]{channelNames});
        log.info("Subscribing to channels {}", allChannelNames);

        return Observable.<T>create(e -> {
            if (isSocketClosed()) {
                e.onError(new NotConnectedException());
                return;
            }
            if (channels.containsKey(channelId)) {
                e.onError(new IllegalStateException("Channel already registered " + channelId));
                return;
            }

            Subscription<T> newSubscription = new Subscription<>(e, channelId, args);
            channels.put(channelId, newSubscription);
            try {
                sendMessage(getSubscribeMessage("batch", channelNames, args));
            } catch (IOException throwable) {
                e.onError(throwable);
            }

        }).doOnDispose(() -> {
            log.warn("Unsubscribing from channels {}", allChannelNames);
            if (channels.containsKey(channelId)) {
                channels.remove(channelId);
                sendMessage(getUnsubscribeMessage(channelNames));
            }
        }).share();
    }

    public Observable<T> subscribeChannel(String channelName, Object... args) {
        final String channelId = getSubscriptionUniqueId(channelName, args);
        log.info("Subscribing to channel {}", channelId);

        return Observable.<T>create(e -> {
            if (isSocketClosed()) {
                e.onError(new NotConnectedException());
            }
            if (channels.containsKey(channelId)) {
                e.onError(new IllegalStateException("Channel already registered " + channelId));
                return;
            }

            Subscription<T> newSubscription = new Subscription<>(e, channelName, args);
            channels.put(channelId, newSubscription);
            try {
                sendMessage(getSubscribeMessage(channelName, args));
            } catch (IOException throwable) {
                e.onError(throwable);
            }

        }).doOnDispose(() -> {
            if (!channelId.equals("login")) {
                log.info("Unsubscribing from channel {}", channelName);
                if (channels.containsKey(channelId)) {
                    channels.remove(channelId);
                    sendMessage(getUnsubscribeMessage(channelName));
                }
            }
        }).share();
    }

    private String getUnsubscribeMessage(String channelName) throws Exception {
        return getUnsubscribeMessage(Collections.singletonList(channelName));
    }


    public Observable<T> subscirbeUnknownChannel() {
        return Observable.create(e -> {
            if (!channels.containsKey(UNKNOWN_CHANNEL_ID)) {
                Subscription<T> newSubscription = new Subscription<>(e, UNKNOWN_CHANNEL_ID, new Object[0]);
                channels.put(UNKNOWN_CHANNEL_ID, newSubscription);
            }
        });
    }


    protected void handleChannelError(String channel, Throwable t) {
        ObservableEmitter<T> emitter = getEmitterForChannel(channel);
        if (emitter != null) {
            emitter.onError(t);
        }
    }

    protected void handleChannelMessage(String channel, T message) {
        ObservableEmitter<T> emitter = getEmitterForChannel(channel);
        if (emitter != null) {
            emitter.onNext(message);
        }
    }

    private ObservableEmitter<T> getEmitterForChannel(String channel) {
        Subscription<T> subscription = channels.get(channel);
        if (subscription == null) {
            log.debug("Channel has been closed {}.", channel);
            return null;
        }
        ObservableEmitter<T> emitter = subscription.emitter;
        if (emitter == null) {
            log.debug("No subscriber for channel {}.", channel);
            return null;
        }
        return emitter;
    }

    public boolean isLoggedInSuccessfully() {
        return loggedInSuccessfully;
    }
}
