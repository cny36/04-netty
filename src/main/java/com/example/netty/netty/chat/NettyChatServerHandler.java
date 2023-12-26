package com.example.netty.netty.chat;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 需要一个集合来管理所有的客户端
     */
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当Channle建立连接之后，会触发该方法的执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 获取到当前连接上了的channel并且保存 方便给其他客户端发消息
        channels.add(ctx.channel());
        log.info("客户端：{} 上线了", ctx.channel().remoteAddress());
        channels.writeAndFlush("用户：" + ctx.channel().remoteAddress() + "上线了！");
    }

    /**
     * 当Channle断开连接之后，会触发该方法的执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端断开连接后，需要删除客户端channel
        channels.remove(ctx.channel());
        log.info("客户端：{} 溜了", ctx.channel().remoteAddress());
        channels.writeAndFlush("用户：" + ctx.channel().remoteAddress() + "溜了！");
    }

    /**
     * 当Channle建立连接之后，会触发该方法的执行
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 循环发送消息
        Channel currentChannel = ctx.channel();
        log.info("收到 {} 的消息 {}", currentChannel.remoteAddress(), msg);
        channels.forEach(channel -> {
            if(channel == currentChannel){
                currentChannel.writeAndFlush("我说：" + msg);
            } else {
                channel.writeAndFlush("用户_" + currentChannel.remoteAddress() + "说：" + msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
