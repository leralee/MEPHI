package com.example.orderservice;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

import java.io.IOException;

/**
 * @author valeriali
 * @project Microservices
 */
public class OrderServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = NettyServerBuilder.forPort(5002)
                .addService(new OrderServiceImpl())
                .build()
                .start();

        System.out.println("OrderService gRPC Server started on port 5002");

        server.awaitTermination();
    }
}
