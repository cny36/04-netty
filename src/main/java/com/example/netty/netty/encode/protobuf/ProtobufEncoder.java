package com.example.netty.netty.encode.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtobufEncoder extends MessageToByteEncoder<Object> {

    private Class aClass;

    public ProtobufEncoder(Class target){
        this.aClass = target;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(msg != null && msg.getClass().equals(aClass)){
            out.writeBytes(ProtobufUtils.serializer(msg));
        }
    }

}
