package com.example.netty.netty.heartbeat;

import com.example.netty.netty.basic.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                // 检测心跳超时 3s未接收到客户端心跳包是就超时
                                .addLast(new IdleStateHandler(3,0,0, TimeUnit.SECONDS))
                                .addLast(new HeartBeatServerHandler());
                    }
                });

        //4.绑定监听端口
        try {
            ChannelFuture future = bootstrap.bind(8888).sync();
            log.info("服务器已经启动，在8888端口进行监听...");
            //
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
