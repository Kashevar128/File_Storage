package org.kashevar.myServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.kashevar.myNetwork.Consumers.MyBiConsumer;
import org.kashevar.myNetwork.HelperClasses.GenerateList;
import org.kashevar.myNetwork.Request.BasicRequest;
import org.kashevar.myNetwork.Request.GetFileListRequest;
import org.kashevar.myNetwork.Request.StartClientRequest;
import org.kashevar.myNetwork.Response.GetFileListResponse;
import org.kashevar.myNetwork.Response.StartServerResponse;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Map<Class<? extends BasicRequest>, MyBiConsumer<ChannelHandlerContext, BasicRequest>> REQUEST_HANDLERS = new HashMap<>();

    static {
        REQUEST_HANDLERS.put(StartClientRequest.class, (channelHandlerContext, request) -> {
            StartClientRequest startClientRequest = (StartClientRequest) request;
            String name = startClientRequest.getNameUser();
            Path path = Storage.getStorage().createUserRepository(name);
            List<String> startList = GenerateList.generate(path);
            channelHandlerContext.writeAndFlush(new StartServerResponse(startList));

        });
        REQUEST_HANDLERS.put(GetFileListRequest.class, (channelHandlerContext, request) -> {
            GetFileListRequest getFileListRequest = (GetFileListRequest) request;
            String name = getFileListRequest.getName();
            Path path = Paths.get(getFileListRequest.getPath());
            List<String> newList = GenerateList.generate(path);
            channelHandlerContext.writeAndFlush(new GetFileListResponse(newList));
        });
//        REQUEST_HANDLERS.put(GetFileListRequest.class, (channelHandlerContext, request) -> {
//
//        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicRequest request = (BasicRequest) msg;
        System.out.println(request.getType());
        MyBiConsumer<ChannelHandlerContext, BasicRequest> channelServerHandlerContextConsumer = REQUEST_HANDLERS.get(request.getClass());
        channelServerHandlerContextConsumer.accept(ctx, request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }


}
