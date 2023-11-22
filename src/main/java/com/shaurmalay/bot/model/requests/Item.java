package com.shaurmalay.bot.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Item {
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Price")
    private int Price;
    @JsonProperty("Quantity")
    private int Quantity;
    @JsonProperty("Amount")
    private int Amount;
    @JsonProperty("Tax")
    private String Tax;
}
