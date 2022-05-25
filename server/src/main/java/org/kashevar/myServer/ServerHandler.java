package org.kashevar.myServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.kashevar.myNetwork.Consumers.MyBiConsumer;
import org.kashevar.myNetwork.HelperClasses.FileHelper;
import org.kashevar.myNetwork.HelperClasses.FileInfo;
import org.kashevar.myNetwork.HelperClasses.PathHelper;
import org.kashevar.myNetwork.Request.*;
import org.kashevar.myNetwork.Response.GetFileListResponse;
import org.kashevar.myNetwork.Response.GetFileResponse;
import org.kashevar.myNetwork.Response.StartServerResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

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
            List<String> newList = null;

            switch (fileInfo.getType()) {
                case FILE:
                    FileHelper.writeBytesToFile(dstPath, file);
                    newList = PathHelper.generateStringList(dstPath.getParent());
                    break;
                case DIRECTORY:
                    FileInfo fileChildrenInfo = sendFileRequest.getFileChildrenInfo();
                    Path directoryPath = dstPath.resolve(fileInfo.getFilename());
                    if (!Files.exists(directoryPath)) {
                        try {
                            Files.createDirectory(directoryPath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Path filePath = directoryPath.resolve(fileChildrenInfo.getFilename());
                    System.out.println(filePath);
                    FileHelper.writeBytesToFile(filePath, file);
                    newList = PathHelper.generateStringList(dstPath);
            }
            channelHandlerContext.writeAndFlush(new GetFileListResponse(newList));
        });

        REQUEST_HANDLERS.put(DeleteFileRequest.class, (channelHandlerContext, request) -> {
            DeleteFileRequest deleteFileRequest = (DeleteFileRequest) request;
            FileInfo fileInfo = deleteFileRequest.getFileInfo();
            Path path = Path.of(deleteFileRequest.getPath());

            switch (fileInfo.getType()) {
                case FILE:
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case DIRECTORY:
                    try (Stream<Path> walk = Files.walk(path)) {
                        walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
            List<String> newList = PathHelper.generateStringList(path.getParent());
            channelHandlerContext.writeAndFlush(new GetFileListResponse(newList));
        });

        REQUEST_HANDLERS.put(GetFileRequest.class, ((channelHandlerContext, request) -> {
            GetFileRequest getFileRequest = (GetFileRequest) request;
            Path path = Path.of(getFileRequest.getSrcPath());
            FileInfo fileInfo = new FileInfo(path);
            switch (fileInfo.getType()) {
                case FILE:
                    byte[] file = FileHelper.readToByteFile(path);
                    channelHandlerContext.writeAndFlush(new GetFileResponse(file, fileInfo));
                case DIRECTORY:
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
