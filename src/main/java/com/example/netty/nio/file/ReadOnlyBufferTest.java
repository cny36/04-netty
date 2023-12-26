package com.example.netty.nio.file;

import java.nio.ByteBuffer;

public class ReadOnlyBufferTest {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(6);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        System.out.println("原buffer：" + buffer);

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println("只读buffer：" + readOnlyBuffer);

//        byte b = readOnlyBuffer.get(1);
//        b *= 100;
//        readOnlyBuffer.put(b);

        byte b = buffer.get(1);
        b *= 100;
        buffer.put(1,b);
        readOnlyBuffer.flip();
        System.out.println("修改原buffer后只读buffer内容：");
        while (readOnlyBuffer.remaining() > 0) {
            System.out.println(readOnlyBuffer.get());
        }


    }
}
