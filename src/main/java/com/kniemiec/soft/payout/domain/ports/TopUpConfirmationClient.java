package com.kniemiec.soft.payout.domain.ports;

import com.kniemiec.soft.transferorchestrator.topup.TopUpResponse;
import reactor.core.publisher.Mono;

public interface TopUpConfirmationClient {
    Mono<TopUpResponse> confirmTopUp(String transferId);
}
