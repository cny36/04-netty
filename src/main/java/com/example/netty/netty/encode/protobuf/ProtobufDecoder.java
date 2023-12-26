package com.example.netty.netty.encode.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ProtobufDecoder extends ByteToMessageDecoder {

    private Class aClass;

    public ProtobufDecoder(Class target){
        this.aClass = target;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        Object deserializer = ProtobufUtils.deserializer(bytes, aClass);
        out.add(deserializer);
    }
}
