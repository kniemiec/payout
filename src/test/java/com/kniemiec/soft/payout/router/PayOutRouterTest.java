package com.kniemiec.soft.payout.router;

import com.kniemiec.soft.payout.PayOutConfiguration;
import com.kniemiec.soft.payout.errorhandler.GlobalErrorHandler;
import com.kniemiec.soft.payout.handler.TopUpHandler;
import com.kniemiec.soft.payout.model.Money;
import com.kniemiec.soft.payout.model.Status;
import com.kniemiec.soft.payout.model.TopUpData;
import com.kniemiec.soft.payout.model.TopUpStatusData;
import com.kniemiec.soft.payout.repository.TopUpRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringBootTest
@WebFluxTest
@ContextConfiguration(classes = { PayoutRouter.class, TopUpHandler.class, GlobalErrorHandler.class, PayOutConfiguration.class})
@AutoConfigureWebTestClient
public class PayOutRouterTest {


    @MockBean
    TopUpRepository topUpRepository;

    @Autowired
    Validator validator;

    @Autowired
    WebTestClient webTestClient;

    static String TOP_UP_URL = "/v1/topup";

    static String STATUS_URL = "/v1/topup/";


    @Test
    void topUp(){
        // given
        var id = UUID.randomUUID();
        var topUpDataToSave = new TopUpData(id, "sender1", "recipient1", new Money("PLN", BigDecimal.valueOf(10)));
        var topUpStatusDataToReceive = new TopUpStatusData(id.toString(),
                topUpDataToSave.getSenderId(),
                topUpDataToSave.getRecipientId(),
                topUpDataToSave.getMoney(),
                Status.COMPLETED);
        when(topUpRepository.save(isA(TopUpStatusData.class))).thenReturn(Mono.just(topUpStatusDataToReceive));

        // when
        webTestClient.post()
                .uri(TOP_UP_URL)
                .bodyValue(topUpDataToSave)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(TopUpStatusData.class)
                .consumeWith( topUpDataResponse -> {
                    var savedTopUpData = topUpDataResponse.getResponseBody();
                    assertNotNull(savedTopUpData);
                    assertEquals(savedTopUpData.getId(), topUpStatusDataToReceive.getId());
                });

        verify(topUpRepository).save(isA(TopUpStatusData.class));
    }


    @Test
    void topUpWhenInvalidData(){
        // given
        var id = UUID.randomUUID();
        var topUpDataToSave = new TopUpData(id, "", "", new Money("PLN", BigDecimal.valueOf(10)));

        // when
        webTestClient.post()
                .uri(TOP_UP_URL)
                .bodyValue(topUpDataToSave)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("topupData.recipient can not be empty,topupData.senderId can not be empty");

    }


    @Test
    void status(){
        // given
        var id = UUID.randomUUID();
        var topUpStatusData = new TopUpStatusData(id.toString(), "sender1", "recipient1", new Money("PLN", BigDecimal.valueOf(10)), Status.CREATED);
        when(topUpRepository.findById(topUpStatusData.getId())).thenReturn(Mono.just(topUpStatusData));

        // when
        webTestClient
                .get()
                .uri(STATUS_URL+topUpStatusData.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TopUpStatusData.class)
                .consumeWith( topUpDataResponse -> {
                    var savedTopUpData = topUpDataResponse.getResponseBody();
                    assertNotNull(savedTopUpData);
                    assertEquals(savedTopUpData.getId(), topUpStatusData.getId());
                });

        verify(topUpRepository).findById(topUpStatusData.getId());
    }

    @Test
    void statusWhenNotFound(){
        // given
        var invalidId = UUID.randomUUID();
        when(topUpRepository.findById(invalidId.toString())).thenReturn(Mono.empty());

        // when
        webTestClient
                .get()
                .uri(STATUS_URL+invalidId)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .equals("TopUp Operation not found: "+invalidId);

        verify(topUpRepository).findById(invalidId.toString());
    }

}
