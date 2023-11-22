package com.shaurmalay.bot.services;

import com.shaurmalay.bot.config.BotConfig;
import com.shaurmalay.bot.config.Sha256EncoderConfig;
import com.shaurmalay.bot.model.Order;
import com.shaurmalay.bot.model.requests.Item;
import com.shaurmalay.bot.model.requests.PaymentCancelOrGetStateObj;
import com.shaurmalay.bot.model.requests.PaymentInitObj;
import com.shaurmalay.bot.model.requests.Receipt;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RequestObjBuildService {
    private BotConfig botConfig;
    private Sha256EncoderConfig encoderConfig;

    public RequestObjBuildService(BotConfig botConfig, Sha256EncoderConfig encoderConfig) {
        this.botConfig = botConfig;
        this.encoderConfig = encoderConfig;
    }


    public PaymentInitObj initObjBuilder(String userChatId, int total, Order order) {
        PaymentInitObj paymentInitObj = new PaymentInitObj();
        Receipt receipt = new Receipt();
        Item item = new Item();
        String hashStringToEncoding = total + userChatId + order.getId() + botConfig.getTerminalPass() + "O" + botConfig.getTerminalId();
        String token = encoderConfig.sha256Encoder().encode(hashStringToEncoding);

        paymentInitObj.setTerminalKey(botConfig.getTerminalId().toString());
        paymentInitObj.setAmount(total);
        paymentInitObj.setOrderId(String.valueOf(order.getId()));
        paymentInitObj.setCustomerKey(Long.parseLong(userChatId));
        paymentInitObj.setPayType("O");

        item.setName("Шаурмалай.Доставка");
        item.setPrice(total);
        item.setQuantity(1);
        item.setAmount(total);
        item.setTax("none");

        receipt.setItems(Collections.singletonList(item));
        receipt.setPhone("+79063324310");
        receipt.setTaxation("patent");
        paymentInitObj.setReceipt(receipt);
        paymentInitObj.setToken(token);
        return paymentInitObj;
    }

    public PaymentCancelOrGetStateObj cancelOrGetStateObjBuilder(Order order) {
        PaymentCancelOrGetStateObj paymentCancelOrGetStateObj = new PaymentCancelOrGetStateObj();
        String hashStringToEncoding = botConfig.getTerminalPass() + order.getPaymentId() + botConfig.getTerminalId();
        paymentCancelOrGetStateObj.setTerminalKey(String.valueOf(botConfig.getTerminalId()));
        paymentCancelOrGetStateObj.setPaymentId(order.getPaymentId());
        String token = encoderConfig.sha256Encoder().encode(hashStringToEncoding);
        paymentCancelOrGetStateObj.setToken(token);
        return paymentCancelOrGetStateObj;
    }
}
