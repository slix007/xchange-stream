package info.bitrich.xchangestream.service.ws.statistic;

public class PingStatEvent {

    private final long pingPongMs;

    public PingStatEvent(long pingPongMs) {
        this.pingPongMs = pingPongMs;
    }

    public long getPingPongMs() {
        return pingPongMs;
    }
}
