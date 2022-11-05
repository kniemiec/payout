package com.kniemiec.soft.payout.handler;

import com.kniemiec.soft.payout.confirmation.TopUpConfirmationClient;
import com.kniemiec.soft.payout.error.ReviewDataException;
import com.kniemiec.soft.payout.error.TopUpNotFoundException;
import com.kniemiec.soft.payout.model.TopUpData;
import com.kniemiec.soft.payout.model.TopUpStatusData;
import com.kniemiec.soft.payout.repository.TopUpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.Duration;
import java.util.stream.Collectors;


@Component
@Slf4j
public class TopUpHandler {

    TopUpRepository topUpRepository;

    Validator validator;

    Sinks.Many<TopUpStatusData> sink;

    TopUpConfirmationClient topUpConfirmationClient;

    @Autowired
    public TopUpHandler(Validator validator, Sinks.Many<TopUpStatusData> sink
    , TopUpConfirmationClient topUpConfirmationClient,
                        TopUpRepository topUpRepository){
        this.validator = validator;
        this.sink = sink;
        this.topUpConfirmationClient = topUpConfirmationClient;
        this.topUpRepository = topUpRepository;

        this.sink.asFlux()
                .log()
                .delayElements(Duration.ofMillis(5000))
                .subscribe( topUpStatusData -> topUpConfirmationClient.confirmTopUp(topUpStatusData.getId())
                        .flatMap( confirmation -> topUpRepository.save(TopUpStatusData.toCompleted(topUpStatusData)))
                        .log()
                );
    }

    public Mono<ServerResponse> topUp(ServerRequest request) {
        return request.bodyToMono(TopUpData.class)
                .doOnNext(this::validate)
                .map(topUpData -> TopUpData.toTopUpStatusData(topUpData))
                .flatMap(topUpRepository::save)
                .doOnNext( topUpStatusData -> sink.tryEmitNext(topUpStatusData))
                .log()
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    private void validate(TopUpData topUpData) {
        var violations = validator.validate(topUpData);
        if(violations.size() > 0) {
            log.warn("Data constraint violation {}",violations);
            var messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new ReviewDataException(messages);
        }


    }

    public Mono<ServerResponse> status(ServerRequest request) {
        var id = request.pathVariable("id");
        var topUpStatus = topUpRepository
            .findById(id).switchIfEmpty(Mono.error(new TopUpNotFoundException("TopUp Operation not found: "+id)));
        return topUpStatus.flatMap(ServerResponse.ok()::bodyValue).log();
    }

    public Mono<ServerResponse> topUpStreams(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_NDJSON)
                .body(this.sink.asFlux(), TopUpStatusData.class);
    }
}
