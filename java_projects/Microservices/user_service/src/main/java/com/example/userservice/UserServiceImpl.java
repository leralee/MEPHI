package com.example.userservice;

import com.example.userservice.proto.CreateUserRequest;
import com.example.userservice.proto.GetUserRequest;
import com.example.userservice.proto.UserResponse;
import com.example.userservice.proto.UserServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

/**
 * @author valeriali
 * @project Microservices
 */
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        // Имитация сохранения пользователя в БД. Генерируем user_id
        String userId = UUID.randomUUID().toString();

        UserResponse response = UserResponse.newBuilder()
                .setUserId(userId)
                .setName(request.getName())
                .setEmail(request.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUser (GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        // Имитация поиска пользователя в БД по user_id
        // Для примера всегда возвращаем одни и те же данные
        UserResponse response = UserResponse.newBuilder()
                .setUserId("123")
                .setName("Test User")
                .setEmail("test@example.com")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}
