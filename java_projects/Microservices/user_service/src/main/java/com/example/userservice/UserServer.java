package com.example.userservice;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

import java.io.IOException;

/**
 * @author valeriali
 * @project Microservices
 */
public class UserServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = NettyServerBuilder.forPort(5001)
                .addService(new UserServiceImpl())
                .build()
                .start();

        System.out.println("Server started, listening on " + server.getPort());
        server.awaitTermination();
    }
}
