package com.kniemiec.soft.payout.confirmation;

import com.kniemiec.soft.payout.model.Status;
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
public class TopUpConfirmationClient {

    private final TopUpConfirmationServiceGrpc.TopUpConfirmationServiceBlockingStub blockingStub;

    private final String host;

    private final int port;

    ManagedChannel channel;

    public TopUpConfirmationClient(@Value("${orchestrator.grpc.host}") String host,
                                   @Value("${orchestrator.grpc.port}") int port){
        this.host = host;
        this.port = port;
        channel = ManagedChannelBuilder.forAddress(this.host, this.port)
                .usePlaintext()
                .build();

        blockingStub = TopUpConfirmationServiceGrpc.newBlockingStub(channel);
    }

    public Mono<TopUpResponse> confirmTopUp(String transferId){
        TopUpRequest topUpRequest = TopUpRequest
                .newBuilder()
                .setTransferId(transferId)
                .setStatus(Status.COMPLETED.name())
                .build();
        return Mono.just(blockingStub.topUpCompleted(topUpRequest));

    }
}
