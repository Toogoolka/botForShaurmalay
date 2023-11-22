package com.shaurmalay.bot.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentInitObj {
    @JsonProperty("TerminalKey")
    private String TerminalKey;
    @JsonProperty("Amount")
    private Integer Amount;
    @JsonProperty("OrderId")
    private String OrderId;
    @JsonProperty("CustomerKey")
    private Long CustomerKey;
    @JsonProperty("PayType")
    private String PayType;
    @JsonProperty("Receipt")
    private Receipt Receipt;
    @JsonProperty("Token")
    private String Token;
}
