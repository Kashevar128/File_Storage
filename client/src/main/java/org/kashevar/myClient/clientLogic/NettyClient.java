package org.kashevar.myClient.clientLogic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.kashevar.myClient.GUI.ClientController;
import org.kashevar.myNetwork.HelperClasses.Constants;
import org.kashevar.myNetwork.Request.StartClientRequest;
import org.kashevar.myNetwork.Request.BasicRequest;

public class NettyClient {


    private ClientController clientController;

    private Channel channel = null;

    private String nameUser = "Bro";

    public String getNameUser() {
        return nameUser;
    }

    private CurrentListServer currentListServer;

    public CurrentListServer getCurrentListServer() {
        return currentListServer;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public NettyClient(ClientController clientController) throws InterruptedException {
        this.clientController = clientController;
        new Thread(()-> {
            currentListServer = new CurrentListServer();
            EventLoopGroup eventLoopGroup = null;
            try {
                eventLoopGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventLoopGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.remoteAddress("localhost", 45001);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(
                                new ObjectDecoder(Constants.MB_20.getValue(), ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder(),
                                new ClientHandler(NettyClient.this)
                        );
                    }
                });
                ChannelFuture channelFuture = bootstrap.connect().sync();
                channel = channelFuture.channel();
                StartClientRequest startRequest = new StartClientRequest(nameUser);
                sendMessage(startRequest);
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                eventLoopGroup.shutdownGracefully();
            }
        }).start();
    }
    public void sendMessage(Object msg) {
        BasicRequest request = (BasicRequest) msg;
        channel.writeAndFlush(request);
    }
}
