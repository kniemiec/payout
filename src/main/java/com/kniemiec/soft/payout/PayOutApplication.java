package com.kniemiec.soft.payout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication
public class PayOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayOutApplication.class, args);
    }

}
