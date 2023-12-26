package com.example.netty.netty.encode.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    public static void main(String[] args) {
        //1.创建一个线程池，用于读写交互
        EventLoopGroup group = new NioEventLoopGroup();
        //2.创建一个客户端启动对象
        Bootstrap bootstrap = new Bootstrap();

        //3.设置相关参数
        //设置线程池
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 对象编码器
                                //.addLast(new ObjectEncoder())
                                // 自定义的Protobuf编码器
                                .addLast(new ProtobufEncoder(ProtobufMessager.class))
                                .addLast(new NettyClientHandler());
                    }
                });

        //4.连接服务端
        try {
            ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
            log.info("连接服务端成功");
            //对通道关闭进行监听
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
