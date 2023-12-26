package com.example.netty.netty.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketServer {

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
                        ch.pipeline()
                             //1,添加Netty提供的处理Http请求的相关Handler
                            //将请求和应答消息做编解码
                            //HttpServerCodec extends CombinedChannelDuplexHandler<HttpRequestDecoder,HttpResponseEncoder>
                                .addLast(new HttpServerCodec())
                        //将Http消息的多个部分组成一个完整的HTTP消息
                        //1024表示聚合支持的最大长度
                        .addLast(new HttpObjectAggregator(1024))
                        //支持传输大文件，在此案例是非必须的
                        .addLast(new ChunkedWriteHandler())
                        //2,添加Netty提供的处理websocket协议的相关Handler
                        .addLast(new WebSocketServerProtocolHandler("/ws"))
                        .addLast(new WebSocketServerHandler())
                        ;
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
