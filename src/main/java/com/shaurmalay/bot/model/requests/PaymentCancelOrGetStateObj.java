package com.shaurmalay.bot.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentCancelOrGetStateObj {
    @JsonProperty("TerminalKey")
    private String terminalKey;
    @JsonProperty("PaymentId")
    private Long paymentId;
    @JsonProperty("Token")
    private String token;
}
