package info.bitrich.xchangestream.service.ws.pipeline;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleHandler extends ChannelDuplexHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    private int testCnt = 0;

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
                ctx.writeAndFlush(new PingWebSocketFrame());
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

}
