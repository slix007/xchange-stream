package info.bitrich.xchangestream.service.ws.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public final class TextFrameConverter extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // Handle text frame
//        System.out.println("WebSocket Client received message: " + msg.text());
        ctx.fireChannelRead(msg.text()); // send 'String' forward in pipeline
    }
}