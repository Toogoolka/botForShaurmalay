package com.shaurmalay.bot.services;

import com.shaurmalay.bot.dao.OrderDao;
import com.shaurmalay.bot.dao.UserDao;
import com.shaurmalay.bot.dao.WorkStatusBotDao;
import com.shaurmalay.bot.model.WorkStatusBot;
import com.shaurmalay.bot.model.callbacks.CallbackForAdminPanel;
import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.shaurmalay.bot.services.markups_and_buttons.Markups;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 25.10.2023
 */
@Service
@Slf4j
public class AdminCommands {
    private UserDao userDao;
    private UserService userService;
    private WorkStatusBotDao workStatusBotDao;
    private OrderDao orderDao;
    @Autowired
    public AdminCommands(UserDao userDao, UserService userService, WorkStatusBotDao workStatusBotDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.userService = userService;
        this.workStatusBotDao = workStatusBotDao;
        this.orderDao = orderDao;
    }


    public SendMessage adminPanelHandler(Update update, SendMessage sendMessage) {
        Long chatId = update.getMessage().getChatId();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        WorkStatusBot status = workStatusBotDao.findById(1l).get();
        String statusString = status.isWorking() ? "\uD83D\uDFE2 Включен" : ":red_circle: Отключен";
        sendMessage.setChatId(String.valueOf(chatId));
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (userService.isAdmin(chatId)) {
            sendMessage.setText(EmojiParser.parseToUnicode(":hammer_and_wrench: Добро пожаловать в админ панель\n" +
                    "\nСтатус бота: <b>" + statusString + "</b>"));
            rows.add(Markups.getAnyLine("Выключатель бота", CallbackForAdminPanel.TOGGLE_SWITCH_BOT.name()));
            rows.add(Markups.getBackPageLine(CallbackForMsg.MAIN_PAGE, ":runner: Выйти из админ-панели"));
        } else {
            sendMessage.setText(EmojiParser.parseToUnicode(":x: У вас нет доступа к функциям администратора"));
            rows.add(Markups.getMainPageLine());
        }
        keyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public EditMessageText switchToggleBotHandler(EditMessageText editMessageText) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        WorkStatusBot status = workStatusBotDao.findById(1l).get();
        String statusString = status.isWorking() ? "\uD83D\uDFE2 Включен" : ":red_circle: Отключен";
        editMessageText.setText(EmojiParser.parseToUnicode("Статус: <b>" + statusString + "</b>"));

        rows.add(Markups.getAnyLine("Переключить", CallbackForAdminPanel.SWITCH_ACTION.name()));
        rows.add(Markups.getBackPageLine(CallbackForMsg.ADMIN_PANEL, "Назад"));
        keyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }
    public EditMessageText switchActionHandler(EditMessageText editMessageText) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        WorkStatusBot status = workStatusBotDao.findById(1l).get();
        status.setWorking(status.isWorking() ? false : true);
        workStatusBotDao.save(status);
        String statusString = status.isWorking() ? "\uD83D\uDFE2 Включен" : ":red_circle: Отключен";
        editMessageText.setText(EmojiParser.parseToUnicode("Статус: <b>" + statusString + "</b>"));

        rows.add(Markups.getAnyLine("Переключить", CallbackForAdminPanel.SWITCH_ACTION.name()));
        rows.add(Markups.getBackPageLine(CallbackForMsg.ADMIN_PANEL, "Назад"));
        keyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }
    public EditMessageText backToAdminPanelHandler(Update update, EditMessageText editMessageText) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        WorkStatusBot status = workStatusBotDao.findById(1l).get();
        String statusString = status.isWorking() ? "\uD83D\uDFE2 Включен" : ":red_circle: Отключен";
        editMessageText.setChatId(String.valueOf(chatId));
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (userService.isAdmin(chatId)) {
            editMessageText.setText(EmojiParser.parseToUnicode(":hammer_and_wrench: Добро пожаловать в админ панель\n" +
                    "\nСтатус бота: <b>" + statusString + "</b>"));
            rows.add(Markups.getAnyLine("Выключатель бота", CallbackForAdminPanel.TOGGLE_SWITCH_BOT.name()));
            rows.add(Markups.getBackPageLine(CallbackForMsg.MAIN_PAGE, ":runner: Выйти из админ-панели"));
        } else {
            editMessageText.setText(EmojiParser.parseToUnicode(":x: У вас нет доступа к функциям администратора"));
            rows.add(Markups.getMainPageLine());
        }
        keyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }
    @Transactional
    public boolean checkStatusBot() {
        WorkStatusBot workStatus = workStatusBotDao.findById(1l).get();
        return workStatus.isWorking();
    }
}
