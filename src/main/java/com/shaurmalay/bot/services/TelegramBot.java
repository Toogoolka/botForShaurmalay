package com.shaurmalay.bot.services;

import com.shaurmalay.bot.config.BotConfig;
import com.shaurmalay.bot.dao.BuffDao;
import com.shaurmalay.bot.dao.GoodDao;
import com.shaurmalay.bot.dao.impl.GoodDaoImpl;
import com.shaurmalay.bot.model.Buff;
import com.shaurmalay.bot.model.Good;
import com.shaurmalay.bot.model.callbacks.CallbackForBuff;
import com.shaurmalay.bot.model.callbacks.CallbackForDrinks;
import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.shaurmalay.bot.model.callbacks.CallbackForShaurma;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

/**
 * @author Vladislav Tugulev
 * @Date 23.10.2023
 */
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private BotConfig botConfig;
    private UserService userService;
    private GoodDaoImpl goodDaoImpl;
    private BuffDao buffDao;
    private Handlers handlers;
    private GoodDao goodDao;

    @Autowired
    public TelegramBot(BotConfig botConfig, UserService userService, GoodDaoImpl goodDaoImpl, BuffDao buffDao, Handlers handlers, GoodDao goodDao) {
        this.botConfig = botConfig;
        this.userService = userService;
        this.goodDaoImpl = goodDaoImpl;
        this.buffDao = buffDao;
        this.handlers = handlers;
        this.goodDao = goodDao;
    }

    public TelegramBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<Good> goodList = goodDaoImpl.getAllShaurmas();
        Good temp = new Good();
        List<Buff> buffList =(List<Buff>) buffDao.findAll();
        EditMessageText editMessageText = new EditMessageText();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            final String DEFAULT_ANSWER = "Простите, я вас не понял..";

            switch (messageText) {
                case "/start":
                    userService.registerUser(update.getMessage());
                    executeMessage(CommandsBot.startCommandReceived(chatId, update.getMessage().getChat().getFirstName()));
                    break;
                case "/unsubscribe":
                    userService.deleteUser(chatId);
                    sendMessage(chatId, "Вы были удалены из базы данных.");
                    log.info("User: " + update.getMessage().getChat().getFirstName() +
                            " deleted from database");
                    break;
                default: {
                    sendMessage(chatId, DEFAULT_ANSWER);
                    log.info("User: " + update.getMessage().getChat().getFirstName() +
                            " tried to action unknown command. COMMAND: \"" + update.getMessage().getText() + "\"");
                }
            }
        }  else if (update.hasCallbackQuery()) {
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            editMessageText.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
            editMessageText.enableHtml(true);
            if (update.getCallbackQuery().getData().equals(CallbackForMsg.SHAURMAS.name())) {
                executeEditMessage(handlers.shaurmaMenuCallbackHandler(update, goodList));
                return;
            } else if (update.getCallbackQuery().getData().equals(CallbackForMsg.DRINKS.name())) {
                executeEditMessage(handlers.shaurmaMenuCallbackHandler(update));
                return;
            } else if (update.getCallbackQuery().getData().equals(CallbackForMsg.MAIN_PAGE.name())) {
                executeEditMessage(handlers.mainPageCallbackHandler(update));
                return;
            } else if (update.getCallbackQuery().getData().equals(CallbackForMsg.CREATE_ORDER.name())) {
                executeEditMessage(handlers.createOrderCallbackHandler(editMessageText,update));
                return;
            } else if (CommandsBot.checkCallback(update.getCallbackQuery().getData(), CallbackForShaurma.class)){
                executeEditMessage(handlers.changeBuffCallbackHandler(editMessageText, buffList,update));
                return;
            } else if (update.getCallbackQuery().getData().equals(CallbackForMsg.CART.name())) {
                executeEditMessage(handlers.cartHandler(editMessageText,update));
                return;
            } else if (CommandsBot.checkCallback(update.getCallbackQuery().getData(), CallbackForDrinks.class)) {
                executeEditMessage(handlers.addDrinkCallbackHandler(editMessageText,update));
                return;
            } else if (update.getCallbackQuery().getData().equals(CallbackForMsg.ADD_TO_CART.name())) {
                executeEditMessage(handlers.addShaurmaCallbackHandler(editMessageText,update));
                return;
            } else if (CommandsBot.checkCallback(update.getCallbackQuery().getData(), CallbackForBuff.class)) {
                handlers.addBuffHandler(update);
                return;
            }
            executeMessage(handlers.historyCallbackHandler(update));
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
    private void executeEditMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
    private void sendOrderToChat(List<Good> order) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(botConfig.getDelChatId()));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
