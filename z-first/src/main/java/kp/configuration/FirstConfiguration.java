package kp.configuration;

import kp.proto.NumbersChatServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class FirstConfiguration {
    @Bean
    NumbersChatServiceGrpc.NumbersChatServiceStub createNumbersChatServiceStub(GrpcChannelFactory channels) {
        return NumbersChatServiceGrpc.newStub(channels.createChannel("grpcServerOnSecond"));
    }

    // FIXME ##################################################################################################
    @Bean
    NumbersChatServiceGrpc.NumbersChatServiceBlockingStub createNumbersChatServiceBlockingStub(GrpcChannelFactory channels) {
        return NumbersChatServiceGrpc.newBlockingStub(channels.createChannel("grpcServerOnSecond"));
    }
}