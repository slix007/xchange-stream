package info.bitrich.xchangestream.service.ws.pipeline;

import info.bitrich.xchangestream.service.ws.statistic.PingStatEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleHandler extends SimpleChannelInboundHandler<PongWebSocketFrame> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    private int testCnt = 0;

    private long pingTime = 0;
    private boolean pongWaiting = false;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PongWebSocketFrame msg) throws Exception {
        gotPong(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                log.warn("Warning: IdleState.READER_IDLE. Closing the connection due to inactive.");
                ctx.fireUserEventTriggered(evt); // send forward
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                log.debug("IdleState.WRITER_IDLE. Sending 'ping'.");
                sendPing(ctx);
//
//                if (testCnt++ > 2) {
//                    testCnt = 0;
//                    log.warn("TESTING: CLOSE THE Connection");
//                    ctx.close();
//                }
//                ctx.fireUserEventTriggered(evt); // send forward
            }
        }
    }

    private void sendPing(ChannelHandlerContext ctx) {
        WebSocketFrame frame = new PingWebSocketFrame();
        ctx.channel().writeAndFlush(frame);
        pingTime = Instant.now().toEpochMilli();
        pongWaiting = true;
    }

    private void gotPong(ChannelHandlerContext ctx) {
        if (pongWaiting) {
            final long ms = Instant.now().toEpochMilli() - pingTime;
            log.debug("ping-pong delay is " + ms + " ms");
            ctx.fireUserEventTriggered(new PingStatEvent(ms)); // send forward
            pongWaiting = false;
        }
    }


}
