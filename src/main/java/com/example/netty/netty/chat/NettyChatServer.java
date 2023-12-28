package com.example.netty.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty聊天室 服务端
 */
@Slf4j
public class NettyChatServer {

    public static void main(String[] args) {
        // 1.创建2个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        // 2.创建服务端启动器对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 3.设置主从线程组
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // TODO 处理我们自己的逻辑
                        ch.pipeline()
                                // ByteBuf -> String
                                .addLast(new StringDecoder())
                                // Netty提供的编码器 String -> ByteBuf
                                .addLast(new StringEncoder())
                                // 聊天handler
                                .addLast(new NettyChatServerHandler())
                                //添加一个固定长度的解码器，解决粘包拆包的问题
                                //客户端传递的字符的长度最大上限是18
                                //.addLast(new FixedLengthFrameDecoder(19))
                                //添加一个特殊分隔符解码器
                                //.addLast(new DelimiterBasedFrameDecoder(18, true, true, Unpooled.copiedBuffer("$".getBytes())))
                        ;
                    }
                });

        // 4.绑定监听端口
        try {
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            log.info("服务端开启8888端口监听");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
