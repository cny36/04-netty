package com.example.netty.netty.websocket.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        log.info("{} 说 {}", ctx.channel().remoteAddress(), text);
        channels.writeAndFlush(new TextWebSocketFrame(ctx.channel() + "说：" + text));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        log.info("{}上线了", ctx.channel());
        log.info("当前在线人数为：{}", channels.size());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}下线了", ctx.channel());
    }
}
