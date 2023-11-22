package com.shaurmalay.bot.model.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseGetStateObj {
    @JsonProperty("TerminalKey")
    private String TerminalKey;
    @JsonProperty("Amount")
    private int Amount;
    @JsonProperty("OrderId")
    private Long OrderId;
    @JsonProperty("Success")
    private boolean Success;
    @JsonProperty("Status")
    private String Status;
    @JsonProperty("PaymentId")
    private Long PaymentId;
    @JsonProperty("ErrorCode")
    private int ErrorCode;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Params")
    private List<Map<String,String>> params;

}
