package org.kashevar.myClient.clientLogic;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import org.kashevar.myNetwork.Consumers.MyTripleConsumer;
import org.kashevar.myNetwork.Response.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    NettyClient nettyClient;

    private static final Map<Class<? extends BasicResponse>, MyTripleConsumer<ChannelHandlerContext, BasicResponse, NettyClient>> RESPONSE_HANDLERS = new HashMap<>();

    static {
        RESPONSE_HANDLERS.put(StartServerResponse.class, (channelHandlerContext, response, nettyClient) -> {
            System.out.println("Привет пользователь!");
            StartServerResponse startServerResponse = (StartServerResponse) response;
            List<String> startList = startServerResponse.getListStart();
            nettyClient.getClientController().serverPC.updateList(startList);
        });
        RESPONSE_HANDLERS.put(GetFileListResponse.class, (channelHandlerContext, response, nettyClient) -> {
            GetFileListResponse getFileListResponse = (GetFileListResponse) response;
            List<String> currentList = getFileListResponse.getList();
            nettyClient.getClientController().serverPC.updateList(currentList);
        });
        RESPONSE_HANDLERS.put(SendToFileResponse.class, ((channelHandlerContext, response, nettyClient) -> {
            System.out.println("...");
        }));
        RESPONSE_HANDLERS.put(GetFileResponse.class, ((channelHandlerContext, response, nettyClient) -> {
            try {
                GetFileResponse getFileResponse = (GetFileResponse) response;
                Path path = nettyClient.getClientController().getDstPath();
                FileOutputStream fileOutputStream = new FileOutputStream(path.toString());
                fileOutputStream.write(getFileResponse.getFile());
                nettyClient.getClientController().clientPC.updateList(path.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    ClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicResponse response = (BasicResponse) msg;
        System.out.println(response.getType());
        MyTripleConsumer<ChannelHandlerContext, BasicResponse, NettyClient> channelClientHandlerContextConsumer = RESPONSE_HANDLERS.get(response.getClass());
        channelClientHandlerContextConsumer.accept(ctx, response, nettyClient);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
