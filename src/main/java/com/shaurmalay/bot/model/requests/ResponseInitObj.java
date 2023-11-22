package com.shaurmalay.bot.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseInitObj {
    @JsonProperty("Success")
    private boolean Success;
    @JsonProperty("ErrorCode")
    private int ErrorCode;
    @JsonProperty("TerminalKey")
    private String TerminalKey;
    @JsonProperty("Status")
    private String Status;
    @JsonProperty("PaymentId")
    private Long PaymentId;
    @JsonProperty("OrderId")
    private Long OrderId;
    @JsonProperty("Amount")
    private int Amount;
    @JsonProperty("PaymentURL")
    private String PaymentURL;
}
