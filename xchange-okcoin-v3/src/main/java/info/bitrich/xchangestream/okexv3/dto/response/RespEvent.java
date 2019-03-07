package info.bitrich.xchangestream.okexv3.dto.response;

/**
 * Created by Sergei Shurmin on 02.03.19.
 */
public class RespEvent {

    private String event;
    private String channel;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "RespEvent{" +
                "event='" + event + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
