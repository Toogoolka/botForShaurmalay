package com.shaurmalay.bot.services;

import com.shaurmalay.bot.config.BotConfig;
import com.shaurmalay.bot.dao.BuffDao;
import com.shaurmalay.bot.dao.GoodDao;
import com.shaurmalay.bot.dao.impl.DelPriceDao;
import com.shaurmalay.bot.dao.impl.GoodDaoImpl;
import com.shaurmalay.bot.exceptions.UserNotFoundException;
import com.shaurmalay.bot.model.Buff;
import com.shaurmalay.bot.model.DelPrice;
import com.shaurmalay.bot.model.Good;
import com.shaurmalay.bot.model.callbacks.*;
import com.shaurmalay.bot.services.markups_and_buttons.Markups;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
    private DelPriceDao delPriceDao;

    @Autowired
    public TelegramBot(BotConfig botConfig, UserService userService, GoodDaoImpl goodDaoImpl, BuffDao buffDao,
                       Handlers handlers, GoodDao goodDao, DelPriceDao delPriceDao) {
        this.botConfig = botConfig;
        this.userService = userService;
        this.goodDaoImpl = goodDaoImpl;
        this.buffDao = buffDao;
        this.handlers = handlers;
        this.goodDao = goodDao;
        this.delPriceDao = delPriceDao;
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
        List<DelPrice> prices = (List<DelPrice>) delPriceDao.findAll();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<Buff> buffList = (List<Buff>) buffDao.findAll();
        EditMessageText editMessageText = new EditMessageText();
        DeleteMessage deleteMessage = new DeleteMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);


        final String DEFAULT_ANSWER = ":thinking: Простите, я вас не понял..";
        final String DEFAULT_CALLBACK_ANSWER = ":zzz: Простите, эта кнопка пока на ремонте";

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (!userService.checkUserInDatabase(update.getMessage().getChatId())) {
                userService.registerUser(update.getMessage());
            }
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            deleteMessage.setMessageId(update.getMessage().getMessageId());
            deleteMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            if (messageText.trim().toLowerCase().startsWith("/adres")) {
                executeMessage(handlers.inputPhoneNumber(sendMessage, update));
                return;
            } else if (messageText.trim().toLowerCase().startsWith("/tel")) {
                executeMessage(handlers.sendToUserChat(sendMessage, update));
                executeMessage(handlers.sendOfferToDelChat(sendMessage, update, botConfig.getDelChatId()));
                return;
            }

            switch (messageText) {
                case "/start":
                    userService.registerUser(update.getMessage());
                    executeDeleteMessage(deleteMessage);
                    executeMessage(CommandsBot.startCommandReceived(chatId, update.getMessage().getChat().getFirstName()));
                    break;
                case "/unsubscribe":
                    userService.deleteUser(chatId);
                    sendMessage(chatId, "Вы были удалены из базы данных.");
                    log.info("User: " + update.getMessage().getChat().getFirstName() +
                            " deleted from database");
                    break;
                default: {
                    executeDeleteMessage(deleteMessage);
                    sendMessage(chatId, DEFAULT_ANSWER);
                    log.info("User: " + update.getMessage().getChat().getFirstName() +
                            " tried to action unknown command. COMMAND: \"" + update.getMessage().getText() + "\"");
                }
            }
        } else if (update.hasCallbackQuery()) {
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            editMessageText.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
            editMessageText.enableHtml(true);
            String data = update.getCallbackQuery().getData();
            if (!userService.checkUserInDatabase(update.getCallbackQuery().getMessage().getChatId())) {
                userService.registerUser(update.getCallbackQuery().getMessage());
            }
            if (data.equals(CallbackForMsg.SHAURMAS.name())) {
                executeEditMessage(handlers.shaurmaMenuCallbackHandler(update, goodList));
                return;
            } else if (data.equals(CallbackForMsg.DRINKS.name())) {
                executeEditMessage(handlers.drinksMenuCallbackHandler(update));
                return;
            } else if (data.equals(CallbackForMsg.MAIN_PAGE.name())) {
                executeEditMessage(handlers.mainPageCallbackHandler(update));
                return;
            } else if (data.equals(CallbackForMsg.CREATE_ORDER.name())) {
                executeEditMessage(handlers.createOrderCallbackHandler(editMessageText, update));
                return;
            } else if (CommandsBot.checkCallback(data, CallbackForShaurma.class) &&
                    !data.equals(CallbackForShaurma.EXECUTIONER.name())) {
                executeEditMessage(handlers.changeBuffCallbackHandler(editMessageText, buffList, update));
                return;
            } else if (data.equals(CallbackForShaurma.EXECUTIONER.name())) {
                executeEditMessage(handlers.addGoodHandler(editMessageText, goodList, update));
                return;
            } else if (data.equals(CallbackForMsg.CART.name())) {
                executeEditMessage(handlers.cartHandler(editMessageText, update));
                return;
            } else if (CommandsBot.checkCallback(data, CallbackForDrinks.class)) {
                executeEditMessage(handlers.addDrinkCallbackHandler(editMessageText, update));
                return;
            } else if (data.equals(CallbackForMsg.ADD_TO_CART.name())) {
                executeEditMessage(handlers.addShaurmaCallbackHandler(editMessageText, update));
                return;
            } else if (CommandsBot.checkCallback(data, CallbackForBuff.class)) {
                executeEditMessage(handlers.addBuffHandler(editMessageText, update, buffList));
                return;
            } else if (data.equals(CallbackForMsg.STARTERS.name())) {
                executeEditMessage(handlers.startersMenuCallbackHandler(update, goodDao.getAllByType_Id(3l).get()));
                return;
            } else if (data.equals(CallbackForMsg.MY_ORDERS.name())) {
                executeEditMessage(handlers.historyCallbackHandler(editMessageText, update));
                return;
            } else if (data.equals(CallbackForMsg.DELETE_ALL_FROM_CART.name())) {
                executeEditMessage(handlers.deleteAllFromCartHandler(editMessageText, update));
                return;
            } else if (data.equals(CallbackForMsg.DELETE_FROM_CART.name())) {
                executeEditMessage(handlers.deleteFromCartHandler(editMessageText, update));
                return;
            } else if (data.contains("DEL_GOOD_")) {
                executeEditMessage(handlers.actionDeleteFromCartHandler(editMessageText, update));
                return;
            } else if (CommandsBot.checkCallback(data, CallbackForStarter.class)) {
                executeEditMessage(handlers.addStarterHandler(editMessageText, update,
                        goodDao.getAllByType_Id(3l).get()));
                return;
            } else if (data.equals(CallbackForMsg.DELETE_BUFFS.name())) {
                executeEditMessage(handlers.deleteBuffs(editMessageText, update));
                return;
            } else if (data.equals(CallbackForMsg.DELETE_LAST_BUFF.name())) {
                executeEditMessage(handlers.deleteLastBuff(editMessageText, update, buffList));
                return;
            } else if (data.equals(CallbackForMsg.ORDERING.name())) {

                try {
                    executeEditMessage(handlers.inputAddress(editMessageText, update));
                } catch (UserNotFoundException e) {
                    sendMessage(update.getCallbackQuery().getMessage().getChatId(),
                            ":man_technologist: Упс..Что-то сломалось, но мы уже чиним");
                    log.error("ERROR occurred: User with chatId: " + update.getCallbackQuery().getMessage().getChatId() +
                            " not found in Database");
                }
                return;
            } else if (data.equals("YES")) {
                executeEditMessage(handlers.yesHandl(update, editMessageText, prices));
                return;
            } else if (data.equals("NO")) {
                executeEditMessage(handlers.canselByDeliveryMan(editMessageText, update));
                return;
            } else if (CommandsBot.checkCallback(data, CallbackForDelPrice.class)) {
                executeMessage(handlers.confirmationByUserHandler(sendMessage, update));
                executeEditMessage(handlers.awaitMoneyMsqToDeliveryHandler(editMessageText, update));
                return;
            } else if (data.equals("OTKAZ")) {
                executeEditMessage(handlers.otkazHandler(editMessageText, update));
                executeMessage(handlers.sendCancelOrderToDelivery(sendMessage, update, botConfig.getDelChatId()));
                return;
            } else if (data.equals(CallbackForMsg.BACK_TO_OFFER.name())) {
                executeEditMessage(handlers.backToOfferInDelChat(editMessageText, update, botConfig.getDelChatId()));
                return;
            } else if (CommandsBot.checkCallback(data, CallbackForReason.class)) {
                executeEditMessage(handlers.orderCanceledToDelChat(editMessageText, update));
                executeMessage(handlers.orderCanceledToUserChat(sendMessage, update));
                return;
            } else if (data.startsWith("MY_ADDRESS_")) {
                executeMessage(handlers.inputPhoneNumber(sendMessage, update));
                return;
            } else if (data.startsWith("MY_PHONE_")) {
                executeMessage(handlers.sendToUserChat(sendMessage, update));
                executeMessage(handlers.sendOfferToDelChat(sendMessage, update, botConfig.getDelChatId()));
                return;
            }
            editMessageText.setText(EmojiParser.parseToUnicode(DEFAULT_CALLBACK_ANSWER));
            rowsInLine.add(Markups.getBackPageLine(CallbackForMsg.MAIN_PAGE, "Назад"));
            keyboardMarkup.setKeyboard(rowsInLine);
            editMessageText.setReplyMarkup(keyboardMarkup);
            executeEditMessage(editMessageText);
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(EmojiParser.parseToUnicode(textToSend));
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

    private void executeDeleteMessage(DeleteMessage message) {
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
