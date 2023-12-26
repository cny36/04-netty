package com.example.netty.nio.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NioServer {
    public static void main(String[] args) throws IOException {
        //1.创建ServerSocketChannel对象，并设置监听端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //2.设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //3.创建一个Selector对象,linux版本的JDK会选择epoll的实现
        Selector selector = Selector.open();
        //4.将ServerSocketChannle注册到Selector上，声明关注客户端的连接事件
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("服务端在8888端口开始监听");

        while (true){
            //5.阻塞等待需要处理就绪事件
            //判断当前跟Selector绑定的Channle是否有就绪的事件
            selector.select();

            //6.获取到Selector中的就绪列表
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                //根据不同的事件做处理
                if(key.isAcceptable()){
                    log.info("处理accept事件");
                    //1.处理连接事件
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    //通过服务端的channel创建对应的客户端channel
                    SocketChannel socketChannel = server.accept();
                    //注册读事件，用于读取客户端发送过来的数据
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    log.info("{}客户端连接成功",socketChannel);
                } else if(key.isReadable()){
                    log.info("处理read事件");
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //创建ByteBuffer来读取通道传递过来的数据
                    int length = socketChannel.read(buffer);
                    if(length > 0){
                        log.info("收到客户端消息：{}", new String(buffer.array()));
                    }
                }

                //删除本次处理的selectKey，避免下次select重复处理
                iterator.remove();

            }

        }



    }
}
