package info.bitrich.xchangestream.service.ws;

import io.netty.channel.ChannelHandler;
import io.netty.handler.logging.LogLevel;
import java.util.List;

public class WsConnectionSpec {

    private int maxFramePayloadLength = 65536;

    //debugging
    private boolean acceptAllCertificates = false;
    private boolean enableLoggingHandler = false;
    private LogLevel loggingHandlerLevel = LogLevel.DEBUG;
    private String socksProxyHost;
    private Integer socksProxyPort;

    // converters
    private List<ChannelHandler> customNettyHandlers = null;

    public int getMaxFramePayloadLength() {
        return maxFramePayloadLength;
    }

    public void setMaxFramePayloadLength(int maxFramePayloadLength) {
        this.maxFramePayloadLength = maxFramePayloadLength;
    }

    public boolean isAcceptAllCertificates() {
        return acceptAllCertificates;
    }

    public void setAcceptAllCertificates(boolean acceptAllCertificates) {
        this.acceptAllCertificates = acceptAllCertificates;
    }

    public boolean isEnableLoggingHandler() {
        return enableLoggingHandler;
    }

    public void setEnableLoggingHandler(boolean enableLoggingHandler) {
        this.enableLoggingHandler = enableLoggingHandler;
    }

    public LogLevel getLoggingHandlerLevel() {
        return loggingHandlerLevel;
    }

    public void setLoggingHandlerLevel(LogLevel loggingHandlerLevel) {
        this.loggingHandlerLevel = loggingHandlerLevel;
    }

    public String getSocksProxyHost() {
        return socksProxyHost;
    }

    public void setSocksProxyHost(String socksProxyHost) {
        this.socksProxyHost = socksProxyHost;
    }

    public Integer getSocksProxyPort() {
        return socksProxyPort;
    }

    public void setSocksProxyPort(Integer socksProxyPort) {
        this.socksProxyPort = socksProxyPort;
    }

    public List<ChannelHandler> getCustomNettyHandlers() {
        return customNettyHandlers;
    }

    public void setCustomNettyHandlers(List<ChannelHandler> customNettyHandlers) {
        this.customNettyHandlers = customNettyHandlers;
    }
}
