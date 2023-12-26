package com.example.netty.netty.heartbeat;

import com.example.netty.netty.basic.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClient {

    private static Bootstrap bootstrap;

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new HeartBeatClientHandler());
                    }
                });

        try {
//            ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
//            log.info("连接服务端成功");
//            future.channel().closeFuture().sync();
            reconnect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        finally {
//            group.shutdownGracefully();
//        }

    }

    public static void reconnect() throws InterruptedException{
        ChannelFuture future = bootstrap.connect("localhost", 8888);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    log.info("连接服务端成功");
                } else {
                    log.info("连接服务端失败");
                    future.channel().eventLoop().schedule(()->{
                        log.info("1秒后重新连接服务端");
                        try {
                            reconnect();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        });
        future.channel().closeFuture().sync();

    }
}
