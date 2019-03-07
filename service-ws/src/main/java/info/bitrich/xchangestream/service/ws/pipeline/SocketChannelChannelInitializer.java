package info.bitrich.xchangestream.service.ws.pipeline;

import info.bitrich.xchangestream.service.ws.WsConnectionSpec;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.SocketUtils;

public class SocketChannelChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private static final int MAX_WEBSOCKET_HANDSHAKE_SIZE = 8192;

    private final SslContext sslCtx;
    private final String host;
    private final int port;
    private final WsConnectionSpec wsConnectionSpec;
    private final WebSocketClientHandshaker handshaker;
    private final WebSocketClientExtensionHandler clientExtensionHandler;
    private final SimpleChannelInboundHandler<String> protocolHandler;

    public SocketChannelChannelInitializer(SslContext sslCtx, String host, int port,
            WsConnectionSpec wsConnectionSpec,
            WebSocketClientHandshaker handshaker,
            WebSocketClientExtensionHandler clientExtensionHandler,
            SimpleChannelInboundHandler<String> protocolHandler) {
        this.sslCtx = sslCtx;
        this.host = host;
        this.port = port;
        this.wsConnectionSpec = wsConnectionSpec;
        this.handshaker = handshaker;
        this.clientExtensionHandler = clientExtensionHandler;
        this.protocolHandler = protocolHandler;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        if (wsConnectionSpec.getSocksProxyHost() != null) {
            p.addLast(new Socks5ProxyHandler(SocketUtils.socketAddress(wsConnectionSpec.getSocksProxyHost(), wsConnectionSpec.getSocksProxyPort())));
        }
        if (sslCtx != null) {
            p.addLast("ssl-handler", sslCtx.newHandler(ch.alloc(), host, port));
        }

        p.addLast("http-codec", new HttpClientCodec());
        p.addLast("idleStateHandler", new IdleStateHandler(60, 15, 0));
        p.addLast("idleHandler", new IdleHandler());

//        p.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        if (wsConnectionSpec.isEnableLoggingHandler()) {
            p.addLast("log-handler", new LoggingHandler(wsConnectionSpec.getLoggingHandlerLevel()));
        }
        if (clientExtensionHandler != null) {
            p.addLast("client-ex", clientExtensionHandler);
        }
        //TODO remove HTTP handlers from the pipeline after handshake
        p.addLast(new HttpObjectAggregator(MAX_WEBSOCKET_HANDSHAKE_SIZE));

        p.addLast("handshaker", new WebSocketClientProtocolHandler(handshaker));
        p.addLast("ws-frame-agg", new WebSocketFrameAggregator(wsConnectionSpec.getMaxFramePayloadLength()));

        // pipeline of converters
        if (wsConnectionSpec.getCustomNettyHandlers() == null) {
            p.addLast("binary-frame", new BinaryFrameConverter());
            p.addLast("text-frame", new TextFrameConverter());
//            p.addLast("json", new JsonObjectDecoder());
        } else {
            p.addLast(wsConnectionSpec.getCustomNettyHandlers().toArray(new ChannelHandler[0]));
        }

        p.addLast(protocolHandler);
    }


}