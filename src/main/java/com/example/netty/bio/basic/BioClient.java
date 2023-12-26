package com.example.netty.bio.basic;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class BioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        try {
            socket = new Socket("127.0.0.1", 8888);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            printWriter.println("hello server");

            log.info("服务端回复消息：{}",bufferedReader.readLine());

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
        }
    }
}
