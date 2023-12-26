package com.example.netty.bio.basic;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class BioServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        try {
            serverSocket = new ServerSocket(8888);
            log.error("服务器开启端口 8888 监听");

            while (true){
                socket = serverSocket.accept();
                log.error("接收到客户端请求：{}", socket);

                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);

                log.info("消息：{}", bufferedReader.readLine());
                printWriter.println("服务端已经收到消息");
            }
        }catch (Exception e){

        }finally {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(printWriter != null){
                printWriter.close();
            }
            if(socket != null){
                socket.close();
            }
            if(serverSocket != null){
                serverSocket.close();
            }
        }

    }
}
