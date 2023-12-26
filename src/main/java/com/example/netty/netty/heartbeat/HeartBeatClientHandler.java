package com.example.netty.netty.heartbeat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 连接成功后给服务端发送心跳包
        while (channel.isActive()) {
            Thread.sleep(1000*new Random().nextInt());
            channel.writeAndFlush("heartbeat");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接");
        NettyClient.reconnect();
    }
}
