package com.example.orderservice;

import com.example.orderservice.proto.CreateOrderRequest;
import com.example.orderservice.proto.GetOrderRequest;
import com.example.orderservice.proto.OrderResponse;
import com.example.orderservice.proto.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

/**
 * @author valeriali
 * @project Microservices
 */
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    @Override
    public void createOrder(CreateOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        String orderId = UUID.randomUUID().toString();
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setUserId(request.getUserId())
                .setAmount(request.getAmount())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrder(GetOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        // Имитация получения заказа из БД
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(request.getOrderId())
                .setUserId("dummy_user_id")
                .setAmount(99.99)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
