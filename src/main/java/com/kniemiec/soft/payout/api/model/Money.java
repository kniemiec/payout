package com.kniemiec.soft.payout.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Money {

    String currency;

    @Min(value = 0, message = "money amount can not be below 0")
    BigDecimal value;
}
