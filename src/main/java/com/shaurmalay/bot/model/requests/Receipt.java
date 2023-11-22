package com.shaurmalay.bot.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
@Data
public class Receipt {
    @JsonProperty("Items")
    private List<Item> Items;
    @JsonProperty("Phone")
    private String Phone;
    @JsonProperty("Taxation")
    private String Taxation;
}
