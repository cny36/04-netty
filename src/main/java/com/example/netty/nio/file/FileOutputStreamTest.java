package com.example.netty.nio.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileOutputStreamTest {
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\陈能缘\\Desktop\\file.txt");

        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(3);
        System.out.println("初始化buffer: " + buffer);

        buffer.put((byte) 22);
        buffer.put((byte) 24);
        buffer.put((byte) 26);
        System.out.println("写入buffer后buffer: " + buffer);

        buffer.flip();
        System.out.println("flip后buffer: " + buffer);

        channel.write(buffer);


        channel.close();


    }
}
