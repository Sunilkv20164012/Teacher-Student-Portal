package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5700)){
            System.out.println("Server running");
            while(true){
                new Echoer(serverSocket.accept()).start();
            }
        }catch (IOException e){
            System.out.println("Server exception " + e.getMessage());
        }
    }
}