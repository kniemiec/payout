package com.kniemiec.soft.payout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopUpData {

    UUID transferId;

    @NotBlank(message = "topupData.senderId can not be empty")
    String senderId;

    @NotBlank(message = "topupData.recipient can not be empty")
    String recipientId;

    Money money;

    public static TopUpStatusData toTopUpStatusData(TopUpData topUpData){
        return new TopUpStatusData(
                topUpData.getTransferId().toString(),
                topUpData.getSenderId(),
                topUpData.getRecipientId(),
                topUpData.getMoney(),
                Status.CREATED
        );
    }
}
