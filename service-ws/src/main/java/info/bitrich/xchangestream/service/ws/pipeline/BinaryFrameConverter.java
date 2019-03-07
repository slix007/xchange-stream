package info.bitrich.xchangestream.service.ws.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.nio.charset.StandardCharsets;
import java.util.zip.Inflater;

@SuppressWarnings("Duplicates")
class BinaryFrameConverter extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
        // change message
        final String decompressed = decompress(msg.content());
        final TextWebSocketFrame textFrame = new TextWebSocketFrame(decompressed);
        ctx.fireChannelRead(textFrame); // send textFrame forward in pipeline
    }

    /**
     * Okex specific decompressor.
     */
    private String decompress(ByteBuf byteBuf) {
        byte[] temp = new byte[byteBuf.readableBytes()];
        ByteBufInputStream bis = new ByteBufInputStream(byteBuf);
        StringBuilder appender = new StringBuilder();
        try {
            //noinspection ResultOfMethodCallIgnored
            bis.read(temp);
            bis.close();
            Inflater infl = new Inflater(true);
            infl.setInput(temp, 0, temp.length);
            byte[] result = new byte[1024];
            while (!infl.finished()) {
                int length = infl.inflate(result);
                appender.append(new String(result, 0, length, StandardCharsets.UTF_8));
            }
            infl.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appender.toString();
    }
//
//    private ByteBuf decompressToByteBuf(ByteBuf byteBuf) {
//        byte[] temp = new byte[byteBuf.readableBytes()];
//        ByteBufInputStream bis = new ByteBufInputStream(byteBuf);
//        ByteBuf appender = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
//        try {
//            //noinspection ResultOfMethodCallIgnored
//            bis.read(temp);
//            bis.close();
//            Inflater infl = new Inflater(true);
//            infl.setInput(temp, 0, temp.length);
//            byte[] result = new byte[1024];
//            int ind = 0;
//            while (!infl.finished()) {
//                int length = infl.inflate(result);
//                appender.setBytes(ind, result, 0, length);
////                appender.append(new String(result, 0, length, StandardCharsets.UTF_8));
//            }
//            infl.end();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return appender;
//    }
}