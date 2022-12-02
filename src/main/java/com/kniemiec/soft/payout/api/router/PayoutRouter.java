package com.kniemiec.soft.payout.api.router;

import com.kniemiec.soft.payout.domain.TopUpHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PayoutRouter {

    @Bean
    public RouterFunction<ServerResponse> payOutRouter(TopUpHandler topUpHandler){
        return route()
                .POST("/v1/topup", topUpHandler::topUp)
                .GET("/v1/topup/{id}", topUpHandler::status)
                .GET("/v1/topups/stream", topUpHandler::topUpStreams)
                .build();

    }
}
