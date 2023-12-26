package com.example.netty.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class NettyChatClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // Netty提供的编码器 String -> ByteBuf
                                .addLast(new StringEncoder())
                                // ByteBuf -> String
                                .addLast(new StringDecoder())
                                .addLast(new NettyChatClientHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
        log.info("{} 连接服务端成功", channelFuture.channel().localAddress());

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            channelFuture.channel().writeAndFlush(scanner.nextLine());
        }
        group.shutdownGracefully();

    }
}
