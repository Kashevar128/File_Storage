package org.kashevar.myServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.kashevar.myNetwork.requests.StartResponse;
import org.kashevar.myNetwork.requests.Message;
import org.kashevar.myNetwork.requests.StartRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BasicHandler extends ChannelInboundHandlerAdapter {

    private static final Map<Class<? extends Message>, Consumer<ChannelHandlerContext>> REQUEST_HANDLERS = new HashMap<>();

    static {
        REQUEST_HANDLERS.put(StartRequest.class, channelHandlerContext -> {
            channelHandlerContext.writeAndFlush(new StartResponse("Привет пользователь!"));
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message request = (Message) msg;
        System.out.println(request.getMsg());
        Consumer<ChannelHandlerContext> channelHandlerContextConsumer = REQUEST_HANDLERS.get(request.getClass());
        channelHandlerContextConsumer.accept(ctx);
    }
}
