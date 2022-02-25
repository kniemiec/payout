package com.kniemiec.soft.payout.router;

import com.kniemiec.soft.payout.handler.TopUpHandler;
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
                .POST("/v1/topup", request -> topUpHandler.topUp(request))
                .GET("/v1/topup/{id}", request -> topUpHandler.status(request))
                .GET("/v1/topups/stream", request -> topUpHandler.topUpStreams(request))
                .build();

    }
}
