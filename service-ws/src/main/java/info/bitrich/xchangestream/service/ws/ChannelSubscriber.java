package info.bitrich.xchangestream.service.ws;

import io.reactivex.Observable;
import java.util.List;

public interface ChannelSubscriber<T> {

    String getSubscriptionUniqueId(String channelName, Object... args);

    String getSubscribeMessage(String channelName, Object... args) throws Exception;

    String getUnsubscribeMessage(List<String> channelNames) throws Exception;

    Observable<T> subscribeBatchChannels(List<String> channelNames, Object... args);

    Observable<T> subscribeChannel(String channelName, Object... args);

}
