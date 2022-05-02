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
import org.kashevar.myNetwork.requests.Message;
import org.kashevar.myNetwork.requests.StartRequest;

public class NettyClient {

    public static final int MB_100 = 100 * 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.remoteAddress("localhost", 45001);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(
                        new ObjectDecoder(MB_100, ClassResolvers.cacheDisabled(null)),
                        new ObjectEncoder(),
                        new ClientHandler()
                );
            }
        });
        ChannelFuture channelFuture = bootstrap.connect().sync();
        Channel channel = channelFuture.channel();

        Message startRequest = new StartRequest("Привет сервак!");
        channel.writeAndFlush(startRequest);

        channelFuture.channel().closeFuture().sync();
    }
}
