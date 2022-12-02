package com.kniemiec.soft.payout.config;

import com.kniemiec.soft.payout.persistence.model.TopUpStatusData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class PayOutConfiguration {

    @Bean
    public Sinks.Many<TopUpStatusData> getTopUpDataSink(){
        return Sinks.many().replay().all();
    }

}
