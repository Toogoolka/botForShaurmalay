package com.shaurmalay.bot.services;

import com.shaurmalay.bot.dao.GoodDao;
import com.shaurmalay.bot.dao.impl.GoodDaoImpl;
import com.shaurmalay.bot.services.markups_and_buttons.Buttons;
import com.shaurmalay.bot.services.markups_and_buttons.Markups;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
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
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();;
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        String answer = EmojiParser.parseToUnicode("Сәлам, <b>" + firstName + "!</b>" + " :blush:");
        buttons.add(Buttons.getCreateOrderBtn());
        buttons.add(Buttons.getMyOrdersBtn());
        rowsInLine.add(buttons);
        rowsInLine.add(Markups.getCartLine());
        keyboardMarkup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(keyboardMarkup);
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
    public static <T extends Enum<T>> boolean checkCallback(String str, Class<T> enumClass) {
        try {
            T enumValue = Enum.valueOf(enumClass, str);
            return enumValue != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
