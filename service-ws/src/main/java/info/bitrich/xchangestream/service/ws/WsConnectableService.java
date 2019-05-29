package info.bitrich.xchangestream.service.ws;

import info.bitrich.xchangestream.service.ConnectableService;
import info.bitrich.xchangestream.service.netty.RetryWithDelay;
import info.bitrich.xchangestream.service.ws.pipeline.SocketChannelChannelInitializer;
import info.bitrich.xchangestream.service.ws.statistic.PingStatEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.ClientHandshakeStateEvent;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("Duplicates")
public abstract class WsConnectableService extends ConnectableService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_RETRY_DURATION = Duration.ofSeconds(15);

    private final int maxFramePayloadLength;
    private final URI uri;
    private boolean connectedSuccessfully = false;
    private Channel webSocketChannel;
    private Duration retryDuration;
    private Duration connectionTimeout;
    private CompletableEmitter handshakeEmitter;
    private volatile NioEventLoopGroup eventLoopGroup;

    private final List<ObservableEmitter<Throwable>> reconnFailEmitters = new LinkedList<>();
    private final List<ObservableEmitter<Object>> connectionSuccessEmitters = new LinkedList<>();
    private final List<ObservableEmitter<PingStatEvent>> pingStatEmitters = new LinkedList<>();

    private final WsConnectionSpec wsConnectionSpec = new WsConnectionSpec();

    public WsConnectableService(String apiUrl) {
        this(apiUrl, 65536);
    }

    public WsConnectableService(String apiUrl, int maxFramePayloadLength) {
        this(apiUrl, maxFramePayloadLength, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_RETRY_DURATION);
    }

    public WsConnectableService(String apiUrl, int maxFramePayloadLength, Duration connectionTimeout, Duration retryDuration) {
        try {
            this.maxFramePayloadLength = maxFramePayloadLength;
            wsConnectionSpec.setMaxFramePayloadLength(maxFramePayloadLength);

            this.retryDuration = retryDuration;
            this.connectionTimeout = connectionTimeout;
            this.uri = new URI(apiUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Error parsing URI " + apiUrl, e);
        }
    }

    @Override
    protected final Completable openConnection() {
        return Completable.create(completable -> {
            try {

                log.info("Connecting to {}", uri.toString());
                String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();

                String host = uri.getHost();
                if (host == null) {
                    throw new IllegalArgumentException("Host cannot be null.");
                }

                final int port = getPort(scheme);

                if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                    throw new IllegalArgumentException("Only WS(S) is supported.");
                }

                final SslContext sslCtx = getSslContext(scheme);

                final WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                        uri, WebSocketVersion.V13, null, true, getCustomHeaders(), maxFramePayloadLength);

                Bootstrap b = new Bootstrap();
                eventLoopGroup = new NioEventLoopGroup(2);

                final SocketChannelChannelInitializer channelInitializer = new SocketChannelChannelInitializer(
                        sslCtx, host, port, wsConnectionSpec,
                        handshaker,
                        getWebSocketClientExtensionHandler(),
                        createProtocolHandler());
                b.group(eventLoopGroup)
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, java.lang.Math.toIntExact(connectionTimeout.toMillis()))
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .channel(NioSocketChannel.class)
                        .handler(channelInitializer);

                // step 1: connect
                final ChannelFuture connectFuture = b.connect(host, port);
                connectFuture.addListener((ChannelFuture future) -> {
                    webSocketChannel = future.channel();
                    if (future.isSuccess()) {
                        // step 2: handshake
                        Completable.create(e -> this.handshakeEmitter = e)
                                .doOnComplete(completable::onComplete)
                                .doOnError(throwable -> handleError(completable, throwable))
                                .subscribe();
                    } else {
                        handleError(completable, future.cause());
                    }
                });

            } catch (Exception throwable) {
                handleError(completable, throwable);
            }
        }).doOnError(t -> {
            if (t instanceof WebSocketHandshakeException) {
                log.warn("Problem with connection: {} - {}", t.getClass(), t.getMessage());
            } else {
                log.warn("Problem with connection", t);
            }
            reconnFailEmitters.forEach(emitter -> emitter.onNext(t));
        }).retryWhen(
                new RetryWithDelay(retryDuration.toMillis())
        ).doOnComplete(() -> {
            connectedSuccessfully = true;
            afterConnect();
            connectionSuccessEmitters.forEach(emitter -> emitter.onNext(new Object()));
        });
    }

    protected WebSocketClientExtensionHandler getWebSocketClientExtensionHandler() {
        return null;
    }

    private SimpleChannelInboundHandler<String> createProtocolHandler() {
        return new SimpleChannelInboundHandler<String>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                getWsMessageHandler().onMessage(msg);
            }

            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                if (!(evt instanceof PingStatEvent)) {
                    log.info("evt=" + evt);
                }
                if (handshakeEmitter != null && evt == ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
                    handshakeEmitter.onComplete();
                }
                if (evt instanceof PingStatEvent) {
                    final PingStatEvent pingStatEvent = (PingStatEvent) evt;
                    pingStatEmitters.forEach(emitter -> emitter.onNext(pingStatEvent));
                }
                super.userEventTriggered(ctx, evt);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                if (handshakeEmitter != null && !handshakeEmitter.isDisposed()) {
                    handshakeEmitter.onError(cause);
                }
                getWsMessageHandler().onError(cause);
                super.exceptionCaught(ctx, cause);
            }
        };
    }

    private SslContext getSslContext(String scheme) throws SSLException {
        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
            if (wsConnectionSpec.isAcceptAllCertificates()) {
                sslContextBuilder = sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            }
            sslCtx = sslContextBuilder.build();
        } else {
            sslCtx = null;
        }
        return sslCtx;
    }

    private int getPort(String scheme) {
        final int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }
        return port;
    }

    protected abstract WsMessageHandler getWsMessageHandler();

    protected void handleError(CompletableEmitter completable, Throwable t) {
        if (webSocketChannel.isOpen() || webSocketChannel.isActive()) {
            webSocketChannel.disconnect().addListener(f ->
                    shutdownEventLoopGroup(completable, t));
        } else if (completable != null && !completable.isDisposed()) {
            shutdownEventLoopGroup(completable, t);
        }
    }

    private void shutdownEventLoopGroup(CompletableEmitter completable, Throwable t) {
        // shutdown sockets after disconnect for avoiding sockets leak
        eventLoopGroup.shutdownGracefully(2, 30, TimeUnit.SECONDS)
                .addListener(future -> {
                    if (completable != null && !completable.isDisposed()) {
                        if (t != null) {
                            completable.onError(t);
                        } else {
                            completable.onComplete();
                        }
                    }
                });
    }

    protected DefaultHttpHeaders getCustomHeaders() {
        return new DefaultHttpHeaders();
    }


    public Completable disconnect() {
        connectedSuccessfully = false;
        return Completable.create(completable -> {
            if (isSocketOpen()) {
                CloseWebSocketFrame closeFrame = new CloseWebSocketFrame();
                webSocketChannel.writeAndFlush(closeFrame)
                        .addListener(future -> shutdownEventLoopGroup(completable, null));
            } else {
                log.warn("Disconnect called but already disconnected");
                completable.onComplete();
            }
        });
    }

    protected List<ChannelInboundHandlerAdapter> getBeforeWebSocketClientHandlers() {
        return new ArrayList<>();
    }

    protected abstract void afterConnect();

    public Completable onDisconnect() {
        return Completable.create(completableEmitter -> {
            webSocketChannel.closeFuture().addListener(future -> {
                log.warn("WebSocket close event");
                completableEmitter.onComplete();
            });
        }).onErrorComplete();
    }

    public void sendMessage(String message) {
        log.debug("Sending message: {}", message);

        if (isSocketClosed()) {
            log.warn("WebSocket is not open! Call connect first.");
            return;
        }

        if (!webSocketChannel.isWritable()) {
            log.warn("Cannot send data to WebSocket as it is not writable.");
            return;
        }

        if (message != null) {
            WebSocketFrame frame = new TextWebSocketFrame(message);
            webSocketChannel.writeAndFlush(frame);
        }
    }

    public Observable<Throwable> subscribeReconnectFailure() {
        return Observable.create(reconnFailEmitters::add);
    }

    public Observable<Object> subscribeConnectionSuccess() {
        return Observable.create(connectionSuccessEmitters::add);
    }

    public Observable<PingStatEvent> subscribePingStats() {
        return Observable.create(pingStatEmitters::add);
    }

    public boolean isSocketClosed() {
        return webSocketChannel == null || !webSocketChannel.isOpen();
    }

    public boolean isSocketOpen() {
        return webSocketChannel != null && webSocketChannel.isOpen();
    }

    public boolean isConnectedSuccessfully() {
        return connectedSuccessfully;
    }

    public WsConnectionSpec getWsConnectionSpec() {
        return wsConnectionSpec;
    }
}
