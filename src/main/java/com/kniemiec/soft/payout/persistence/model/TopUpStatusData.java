package com.kniemiec.soft.payout.persistence.model;

import com.kniemiec.soft.payout.api.model.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class TopUpStatusData {

    @Id
    String id;

    String senderId;
    String recipientId;

    Money money;

    Status status;

    public static TopUpStatusData toCompleted(TopUpStatusData topUpStatusData){
        return TopUpStatusData.builder()
                .id(topUpStatusData.id)
                .status(Status.COMPLETED)
                .money(topUpStatusData.money)
                .recipientId(topUpStatusData.recipientId)
                .senderId(topUpStatusData.getSenderId())
                .build();
    }
}
