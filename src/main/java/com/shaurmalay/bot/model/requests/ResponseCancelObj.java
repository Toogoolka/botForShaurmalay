package com.shaurmalay.bot.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseCancelObj {
    @JsonProperty("TerminalKey")
    private String terminalKey;
    @JsonProperty("OrderId")
    private Long orderId;
    @JsonProperty("Success")
    private boolean success;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("OriginalAmount")
    private int originalAmount;
    @JsonProperty("NewAmount")
    private  int newAmount;
    @JsonProperty("PaymentId")
    private Long paymentId;
    @JsonProperty("ErrorCode")
    private String errorCode;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Details")
    private String details;
}
