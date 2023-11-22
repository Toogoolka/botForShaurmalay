package com.shaurmalay.bot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaurmalay.bot.config.BotConfig;
import com.shaurmalay.bot.model.requests.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/shaurmalay/api")
@Slf4j
public class TransactionController {
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private BotConfig botConfig;

    public TransactionController(RestTemplate restTemplate, ObjectMapper objectMapper, BotConfig botConfig) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.botConfig = botConfig;
    }

//    @PostMapping()
    public ResponseInitObj getLinkFromBank(@RequestBody PaymentInitObj paymentInitObj) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(paymentInitObj);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        log.info("POST REQUEST FOR INIT PAY SESSION: " + body);
        String response = restTemplate.postForObject(botConfig.getUrlInit(), entity, String.class);
        log.info("RESPONSE FOR INIT: " + response);
        return objectMapper.readValue(response, ResponseInitObj.class);
    }


    public ResponseCancelObj getCancelPayment(@RequestBody PaymentCancelOrGetStateObj paymentCancelOrGetStateObj) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(paymentCancelOrGetStateObj);
        HttpEntity<String> entity = new HttpEntity<>(body,headers);
        log.info("POST REQUEST FOR CANCEL PAYMENT: " + body);
        String response = restTemplate.postForObject(botConfig.getUrlCancel(), entity, String.class);
        log.info("RESPONSE FOR CANCEL: " + response);
        return objectMapper.readValue(response, ResponseCancelObj.class);
    }
    public ResponseGetStateObj getStatePayment(@RequestBody PaymentCancelOrGetStateObj paymentCancelOrGetStateObj) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(paymentCancelOrGetStateObj);
        HttpEntity<String> entity = new HttpEntity<>(body,headers);
        log.info("POST REQUEST FOR GET STATE PAYMENT: " + body);
        String response = restTemplate.postForObject(botConfig.getUrlGetState(), entity, String.class);
        log.info("RESPONSE FOR GET STATE: " + response);

        return objectMapper.readValue(response, ResponseGetStateObj.class);
    }
}
