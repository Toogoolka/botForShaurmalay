package com.shaurmalay.bot.services;


import com.shaurmalay.bot.dao.OrderDao;
import com.shaurmalay.bot.dao.UserDao;
import com.shaurmalay.bot.model.CallbackForMsg;
import com.shaurmalay.bot.model.Order;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Vladislav Tugulev
 * @Date 27.10.2023
 */
@Service
public class Handlers {
    private UserDao userDao;
    private OrderDao orderDao;

    @Autowired
    public Handlers(UserDao userDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public SendMessage handleCallback(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getMessage().getChat().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.enableHtml(true);
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (CallbackForMsg.MY_ORDERS.name().equals(data)) {
                List<Order> orderList = orderDao.findAllByCustomer_ChatId(userId).orElse(new ArrayList<>());
                StringBuilder sb = new StringBuilder();
                String history = orderList.stream().map(order -> {
                    sb.setLength(0);
                    return sb.append(":round_pushpin: "+ order.getOrderedAt()
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US)) +
                            " " + order.getAddres() + " - " +
                        order.getOrderSum() + "₽;\n");
                }).collect(Collectors.joining());
                sendMessage.setText(EmojiParser.parseToUnicode(":open_book: <b>История ваших заказов:</b>\n" + history));
            } else if(CallbackForMsg.CREATE_ORDER.name().equals(data)) {


            }
        }
        return sendMessage;
    }
}
