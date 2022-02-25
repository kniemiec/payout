package com.kniemiec.soft.payout;

import com.kniemiec.soft.payout.model.Money;
import com.kniemiec.soft.payout.model.Status;
import com.kniemiec.soft.payout.model.TopUpData;
import com.kniemiec.soft.payout.model.TopUpStatusData;
import com.kniemiec.soft.payout.repository.TopUpRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class TopUpIT {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    TopUpRepository topUpRepository;

    List<TopUpStatusData> topUpsList =  List.of(
            new TopUpStatusData(UUID.randomUUID().toString(), "sender1", "recipient1", new Money("PLN", BigDecimal.valueOf(100)), Status.COMPLETED),
            new TopUpStatusData(UUID.randomUUID().toString(), "sender2", "recipient2", new Money("USD", BigDecimal.valueOf(100)),Status.COMPLETED)
            );;

    static String TOPUP_URL = "/v1/topup";

    static String STATUS_URL = "/v1/topup/";

    @BeforeEach
    void setUp(){
        topUpRepository.saveAll(topUpsList).blockLast();
    }


    @AfterEach
    void tearDown(){
        topUpRepository.deleteAll().block();
    }


    @Test
    void testTopUp(){
        var newTopUp = new TopUpData(UUID.randomUUID(), "sender3", "recipient3", new Money("PLN", BigDecimal.valueOf(100)));
        var transferId = newTopUp.getTransferId();

        webTestClient.post()
                .uri(TOPUP_URL)
                .bodyValue(newTopUp)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(TopUpStatusData.class)
                .consumeWith( topUpResult -> {
                    var savedTopUp = topUpResult.getResponseBody();
                    assertNotNull(savedTopUp);
                    assertEquals(savedTopUp.getId(), transferId.toString());
                });

        assertEquals(3,topUpRepository.findAll().count().block());
    }

    @Test
    void testGetStatus(){
        var transferId = topUpsList.get(0).getId().toString();

        assertEquals(2,topUpRepository.findAll().count().block());

        webTestClient
                .get()
                .uri(STATUS_URL+transferId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TopUpStatusData.class)
                .consumeWith( topUpDataResponse -> {
                    var receivedTopUpData = topUpDataResponse.getResponseBody();
                    assertNotNull(receivedTopUpData);
                    assertEquals(receivedTopUpData.getId(), transferId);
                });

        assertEquals(2,topUpRepository.findAll().count().block());
    }
}
