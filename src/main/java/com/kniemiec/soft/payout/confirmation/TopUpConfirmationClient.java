package com.kniemiec.soft.payout.confirmation;

import com.kniemiec.soft.payout.model.Status;
import com.kniemiec.soft.transferorchestrator.topup.TopUpConfirmationServiceGrpc;
import com.kniemiec.soft.transferorchestrator.topup.TopUpRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TopUpConfirmationClient {

    private final TopUpConfirmationServiceGrpc.TopUpConfirmationServiceBlockingStub blockingStub;

    String host = "transfer-orchestrator";

    int port = 9090;

    ManagedChannel channel;

    public TopUpConfirmationClient(){
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        blockingStub = TopUpConfirmationServiceGrpc.newBlockingStub(channel);

    }

    public void confirmTopUp(String transferId){
        TopUpRequest topUpRequest = TopUpRequest
                .newBuilder()
                .setTransferId(transferId)
                .setStatus(Status.COMPLETED.name())
                .build();
        blockingStub.topUpCompleted(topUpRequest);
        log.info("TopUp Confirmation send for {}",transferId);

    }
}
