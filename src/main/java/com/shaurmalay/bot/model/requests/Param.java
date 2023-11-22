package com.shaurmalay.bot.model.requests;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Param {
    @JsonProperty("Route")
    private String route;
    @JsonProperty("Source")
    private String source;
}
