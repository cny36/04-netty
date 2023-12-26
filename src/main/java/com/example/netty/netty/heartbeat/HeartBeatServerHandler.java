package com.example.netty.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 记录超时次数
     */
    private int timeoutCount = 0;

    /**
     * 最近一次的超时时间
     */
    private long lastTimeoutTime = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if("heartbeat".equals(msg)){
            log.info("收到 {} 的心跳包", ctx.channel());
            if(System.currentTimeMillis() - lastTimeoutTime >= 3000){
                log.info("{} 已经有一段时间表现稳定，超时次数清零", ctx.channel());
                timeoutCount = 0;
            } else {
                log.info("非心跳信息 不处理。。。");
            }
        }
    }

    /**
     * 心跳超时触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
        switch (idleStateEvent.state()){
            case READER_IDLE:
                Date date = new Date();
                log.info("{} 出现了一次读超时 {}", ctx.channel(), date);
                timeoutCount++;
                lastTimeoutTime = date.getTime();
            default:
                break;
        }
        if(timeoutCount >= 3){
            log.info("{} 出现了三次超时了", ctx.channel());
            ctx.channel().close();
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}: 上线了", ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}: 下线了", ctx.channel());
    }
}
