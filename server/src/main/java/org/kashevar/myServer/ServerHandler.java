package org.kashevar.myServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.kashevar.myNetwork.Consumers.MyBiConsumer;
import org.kashevar.myNetwork.HelperClasses.GenerateList;
import org.kashevar.myNetwork.Request.*;
import org.kashevar.myNetwork.Response.GetFileListResponse;
import org.kashevar.myNetwork.Response.GetFileResponse;
import org.kashevar.myNetwork.Response.SendToFileResponse;
import org.kashevar.myNetwork.Response.StartServerResponse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Map<Class<? extends BasicRequest>, MyBiConsumer<ChannelHandlerContext, BasicRequest>> REQUEST_HANDLERS = new HashMap<>();

    static {
        REQUEST_HANDLERS.put(StartClientRequest.class, (channelHandlerContext, request) -> {
            StartClientRequest startClientRequest = (StartClientRequest) request;
            String name = startClientRequest.getNameUser();
            Path path = Storage.createUserRepository(name);
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
        REQUEST_HANDLERS.put(SendToFileRequest.class, (channelHandlerContext, request) -> {
            FileOutputStream fileOutputStream;
            SendToFileRequest sendToFileRequest = (SendToFileRequest) request;
            Path path = Path.of(sendToFileRequest.getPath());
            try {
                fileOutputStream = new FileOutputStream(path.toFile());
                fileOutputStream.write(sendToFileRequest.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<String> newList = GenerateList.generate(path.getParent());
            channelHandlerContext.writeAndFlush(new GetFileListResponse(newList));
            channelHandlerContext.writeAndFlush(new SendToFileResponse());
        });
        REQUEST_HANDLERS.put(GetFileRequest.class, ((channelHandlerContext, request) -> {
            FileInputStream fileInputStream;
            GetFileRequest getFileRequest = (GetFileRequest) request;
            Path path = Path.of(getFileRequest.getPath());
            try {
                long size = Files.size(path);
                byte[] file = new byte[(int)size];
                fileInputStream = new FileInputStream(path.toString());
                fileInputStream.read(file);
                channelHandlerContext.writeAndFlush(new GetFileResponse(file,path.getFileName().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
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
