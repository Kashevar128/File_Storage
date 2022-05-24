package org.kashevar.myServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.kashevar.myNetwork.Consumers.MyBiConsumer;
import org.kashevar.myNetwork.HelperClasses.FileHelper;
import org.kashevar.myNetwork.HelperClasses.FileInfo;
import org.kashevar.myNetwork.HelperClasses.PathHelper;
import org.kashevar.myNetwork.Request.*;
import org.kashevar.myNetwork.Response.GetFileListResponse;
import org.kashevar.myNetwork.Response.StartServerResponse;

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
            List<String> startList = PathHelper.generateStringList(path);
            channelHandlerContext.writeAndFlush(new StartServerResponse(startList));
        });

        REQUEST_HANDLERS.put(GetFileListRequest.class, (channelHandlerContext, request) -> {
            GetFileListRequest getFileListRequest = (GetFileListRequest) request;
            Path path = Paths.get(getFileListRequest.getPath());
            List<String> newList = PathHelper.generateStringList(path);
            channelHandlerContext.writeAndFlush(new GetFileListResponse(newList));
        });

        REQUEST_HANDLERS.put(SendFileRequest.class, (channelHandlerContext, request) -> {
            SendFileRequest sendFileRequest = (SendFileRequest) request;
            FileInfo fileInfo = sendFileRequest.getFileInfo();
            byte[] file = sendFileRequest.getFile();
            Path dstPath = Path.of(sendFileRequest.getDstPath());

            switch (fileInfo.getType()) {
                case FILE:
                    FileHelper.writeBytesToFile(dstPath, file);

                case DIRECTORY:
                    if(!Files.exists(dstPath)) {
                        try {
                            Files.createDirectory(dstPath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            }

            List<String> newList = PathHelper.generateStringList(dstPath.getParent());
            channelHandlerContext.writeAndFlush(new GetFileListResponse(newList));
        });
//        REQUEST_HANDLERS.put(GetFileRequest.class, ((channelHandlerContext, request) -> {
//
//        }));
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
