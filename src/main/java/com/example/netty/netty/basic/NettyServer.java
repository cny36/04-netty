package com.example.netty.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        // 1.创建2个线程组，读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //2.创建一个服务端启动对象
        ServerBootstrap bootstrap = new ServerBootstrap();

        //3.为启动对象设置相关参数
        //设置线程池,采用主从线程的Reactor模式
        bootstrap.group(bossGroup, workGroup)
                //设置通道的类型为NIO类型
                .channel(NioServerSocketChannel.class)
                //设置从线程的处理逻辑
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //此处可以添加自定义的处理逻辑
                        ch.pipeline().addLast(new NettyServerHandler());
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
