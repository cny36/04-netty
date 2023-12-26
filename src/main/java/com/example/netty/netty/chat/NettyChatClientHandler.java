package com.example.netty.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyChatClientHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 当channel有可读的数据时，则触发该函数的执行
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info(msg);
    }
}
