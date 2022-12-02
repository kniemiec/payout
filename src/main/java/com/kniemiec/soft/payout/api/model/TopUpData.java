package com.kniemiec.soft.payout.api.model;

import com.kniemiec.soft.payout.persistence.model.Status;
import com.kniemiec.soft.payout.persistence.model.TopUpStatusData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
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
