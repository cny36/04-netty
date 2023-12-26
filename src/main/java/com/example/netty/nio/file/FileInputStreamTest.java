package com.example.netty.nio.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileInputStreamTest {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\陈能缘\\Desktop\\file.txt");

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(12);

        System.out.println("初始化buffer: " + byteBuffer);

        channel.read(byteBuffer);

        byteBuffer.flip();

        System.out.println("读取后buffer: " + byteBuffer);

        while (byteBuffer.remaining() > 0) {
            byte b = byteBuffer.get();
            System.out.println(java.lang.String.valueOf(b));
        }

        channel.close();
    }
}
