package com.example.netty.nio.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

@Slf4j
public class NioClient {

    public static void main(String[] args) throws IOException {
        //1.创建SocketChannel对象
        SocketChannel socketChannel = SocketChannel.open();
        //2.设置通道为非阻塞模式
        socketChannel.configureBlocking(false);
        //3.设置要访问远程地址
        //由于通道是非阻塞的模式，所以此处connect也是非阻塞的
        if(!socketChannel.connect(new InetSocketAddress(8888))){
            while (!socketChannel.finishConnect()){
                log.info("链接服务器中。。。");
            }
        }
        log.info("链接服务器中成功");

        //实现给服务端发送消息
        Scanner scanner = new Scanner(System.in);
        while (true){
            socketChannel.write(ByteBuffer.wrap(scanner.nextLine().getBytes()));
        }
    }
}
