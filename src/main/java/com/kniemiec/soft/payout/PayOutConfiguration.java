package com.kniemiec.soft.payout;

import com.kniemiec.soft.payout.model.TopUpStatusData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class PayOutConfiguration {

    @Bean
    public Sinks.Many<TopUpStatusData> getTopUpDataSink(){
        reactor.core.publisher.Sinks.Many<TopUpStatusData> topUpDataSink = Sinks.many().replay().all();
        return topUpDataSink;
    }

}
