package com.shaurmalay.bot.services;

import com.shaurmalay.bot.dao.GoodDao;
import com.shaurmalay.bot.dao.impl.GoodDaoImpl;
import com.shaurmalay.bot.model.Buff;
import com.shaurmalay.bot.model.Good;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Slf4j
@Service
public class CommandsBot {

    private GoodDaoImpl goodDaoImpl;
    private GoodDao goodDao;
    @Autowired
    public CommandsBot(GoodDaoImpl goodDaoImpl, GoodDao goodDao) {
        this.goodDaoImpl = goodDaoImpl;
        this.goodDao = goodDao;
    }


    public static SendMessage startCommandReceived(long chatId, String firstName) {
        String answer = EmojiParser.parseToUnicode("Приветствую, " + firstName + "!" + " :blush:" + " /menu - для заказа!");
        log.info("Replied to user: " + firstName + ".");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(answer);
        return sendMessage;
    }

    public static SendMessage sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        return sendMessage;
    }

    public static SendMessage menuCommandRecieved(long chatId, List<Good> goodList) {
        String answer = EmojiParser.parseToUnicode(":book:" + " Ознакомьтесь с нашим меню: ");
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(answer);
        message.setReplyMarkup(getShaurmaMarkup(goodList, 3));
        return message;
    }

    public static InlineKeyboardButton createButton(String textOnButton, String callbackData) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(EmojiParser.parseToUnicode(textOnButton));
        btn.setCallbackData(callbackData);
        return btn;
    }

    public static InlineKeyboardMarkup getShaurmaMarkup(List<Good> items, int countElementsOnRows) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            var btn = createButton(items.get(i).getEmoji() + " " + items.get(i).getName(),
                    items.get(i).getCallbackData());
            buttonsOnTheRow.add(btn);

            if (buttonsOnTheRow.size() == countElementsOnRows || i == items.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
    public static InlineKeyboardMarkup getBuffMarkup(List<Buff> items, int countElementsOnRows) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            var btn = createButton(items.get(i).getName(),
                    items.get(i).getCallbackData());
            buttonsOnTheRow.add(btn);

            if (buttonsOnTheRow.size() == countElementsOnRows || i == items.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
    public static Good callBackExec(String callbackData, Long chatId,List<Good> goods, List<Buff> buffs) {
        SendMessage message = new SendMessage();
        Good good = null;
        for (int i = 0; i < goods.size(); i++) {
            if (callbackData.equals(goods.get(i).getCallbackData())) {
                good = goods.get(i);
                message.setText(goods.get(i).getName() + " будет с..");
                message.setChatId(String.valueOf(chatId));
                message.setReplyMarkup(getBuffMarkup(buffs, 3));
            }
        }
        return good;
    }

    public static List<List<InlineKeyboardButton>> getShaurmaRowsMarkup(List<Good> items, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            var btn = createButton(items.get(i).getEmoji() + " " + items.get(i).getName(),
                    items.get(i).getCallbackData());
            buttonsOnTheRow.add(btn);

            if (buttonsOnTheRow.size() == countElementsOnRows || i == items.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        return rows;
    }
    public static List<List<InlineKeyboardButton>> getBuffsRowsMarkup(List<Buff> items, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            var btn = createButton(items.get(i).getName(),
                    items.get(i).getCallbackData());
            buttonsOnTheRow.add(btn);

            if (buttonsOnTheRow.size() == countElementsOnRows || i == items.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        return rows;
    }
}
