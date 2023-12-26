package com.example.netty.netty.encode.java;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当有客户端连接上服务端，就会触发这个方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新客户端连接上。。。");
    }

    /**
     * 当有数据可读的时候，会触发这个方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Messager messager = (Messager) msg;
        log.info("接受到客户端消息：{}", messager);
    }

    /**
     * 当数据读取完毕之后，会触发该方法的执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //1.构建一个ByteBuf，作为传递的基本单位
        ByteBuf buf = Unpooled.copiedBuffer("hello! this is netty server".getBytes(CharsetUtil.UTF_8));
        //2.发送数据给到客户端
        ctx.writeAndFlush(buf);
    }
}
