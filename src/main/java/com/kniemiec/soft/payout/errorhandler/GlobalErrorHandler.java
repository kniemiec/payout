package com.kniemiec.soft.payout.errorhandler;

import com.kniemiec.soft.payout.error.ReviewDataException;
import com.kniemiec.soft.payout.error.TopUpNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Exception {}", ex.getMessage(),ex);
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        var errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        if(ex instanceof ReviewDataException){
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().writeWith(Mono.just(errorMessage));
        }
        if(ex instanceof TopUpNotFoundException){
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().writeWith(Mono.just(errorMessage));
        }
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return  exchange.getResponse().writeWith(Mono.just(errorMessage));
    }
}
