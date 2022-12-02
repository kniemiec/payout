package com.kniemiec.soft.payout.adapters;

import com.kniemiec.soft.payout.domain.ports.TopUpConfirmationClient;
import com.kniemiec.soft.payout.persistence.model.Status;
import com.kniemiec.soft.transferorchestrator.topup.TopUpConfirmationServiceGrpc;
import com.kniemiec.soft.transferorchestrator.topup.TopUpRequest;
import com.kniemiec.soft.transferorchestrator.topup.TopUpResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GrpcTopUpConfirmationClient implements TopUpConfirmationClient {

    private final TopUpConfirmationServiceGrpc.TopUpConfirmationServiceBlockingStub blockingStub;

    ManagedChannel channel;

    public GrpcTopUpConfirmationClient(@Value("${orchestrator.grpc.host}") String host,
                                       @Value("${orchestrator.grpc.port}") int port){
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        blockingStub = TopUpConfirmationServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Mono<TopUpResponse> confirmTopUp(String transferId){
        TopUpRequest topUpRequest = TopUpRequest
                .newBuilder()
                .setTransferId(transferId)
                .setStatus(Status.COMPLETED.name())
                .build();
        return Mono.just(blockingStub.topUpCompleted(topUpRequest));

    }
}
