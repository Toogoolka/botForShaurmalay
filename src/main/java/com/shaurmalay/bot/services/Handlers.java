package com.shaurmalay.bot.services;


import com.shaurmalay.bot.dao.*;
import com.shaurmalay.bot.dao.impl.CartDaoImpl;
import com.shaurmalay.bot.model.Buff;
import com.shaurmalay.bot.model.Good;
import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.shaurmalay.bot.model.Cart;
import com.shaurmalay.bot.model.Order;
import com.shaurmalay.bot.services.markups_and_buttons.Buttons;
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
@Slf4j
public class Handlers {
    private UserDao userDao;
    private OrderDao orderDao;
    private GoodDao goodDao;
    private BuffDao buffDao;
    private CartDao cartDao;
    private CartDaoImpl cartDaoImpl;

    @Autowired
    public Handlers(UserDao userDao, OrderDao orderDao, GoodDao goodDao, BuffDao buffDao, CartDao cartDao, CartDaoImpl cartDaoImpl) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.goodDao = goodDao;
        this.buffDao = buffDao;
        this.cartDao = cartDao;
        this.cartDaoImpl = cartDaoImpl;
    }

    public SendMessage historyCallbackHandler(Update update) {
        String data;
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        InlineKeyboardMarkup keyboardMarkup;
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getMessage().getChat().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.enableHtml(true);
        data = update.getCallbackQuery().getData();
        if (CallbackForMsg.MY_ORDERS.name().equals(data)) {
            List<Order> orderList = orderDao.findAllByCustomer_ChatId(userId).orElse(new ArrayList<>());
            StringBuilder sb = new StringBuilder();
            String history = orderList.stream().map(order -> {
                sb.setLength(0);
                return sb.append(":round_pushpin: " + order.getOrderedAt()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US)) +
                        " " + order.getAddres() + " - " +
                        order.getOrderSum() + "₽;\n");
            }).collect(Collectors.joining());
            sendMessage.setText(EmojiParser.parseToUnicode(":open_book: <b>История ваших заказов:</b>\n" + history));
        }
        return sendMessage;
    }

    public EditMessageText shaurmaMenuCallbackHandler(Update update,List<Good> goods) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        EditMessageText editMessageText = new EditMessageText();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        String data = update.getCallbackQuery().getData();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setMessageId(messageId);
        editMessageText.enableHtml(true);
        editMessageText.setChatId(String.valueOf(chatId));

        if (CallbackForMsg.SHAURMAS.name().equals(data)) {
            keyboardMarkup.setKeyboard(Markups.getShaurmaMarkup(goods, 3));
            editMessageText.setText(EmojiParser.parseToUnicode(":fire: Вся коллекция:"));
            editMessageText.setReplyMarkup(keyboardMarkup);
        }
        return editMessageText;
    }
    public EditMessageText shaurmaMenuCallbackHandler(Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);
        String data = update.getCallbackQuery().getData();
        List<Good> drinks = goodDao.getAllByType_Id(2L).orElse(new ArrayList<>());
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(String.valueOf(chatId));

        if (CallbackForMsg.DRINKS.name().equals(data)) {
            keyboardMarkup.setKeyboard(Markups.getDrinksMurkup(drinks, 3));
            editMessageText.setText(EmojiParser.parseToUnicode(":droplet: Сушнячок:"));
            editMessageText.setReplyMarkup(keyboardMarkup);
        }
        return editMessageText;
    }
    public EditMessageText mainPageCallbackHandler(Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        EditMessageText editMessageText = new EditMessageText();
        String data = update.getCallbackQuery().getData();
        List<Good> drinks = goodDao.getAllByType_Id(2L).orElse(new ArrayList<>());
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setMessageId(messageId);
        editMessageText.enableHtml(true);
        editMessageText.setChatId(String.valueOf(chatId));

        if (CallbackForMsg.MAIN_PAGE.name().equals(data)) {
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            buttons.add(Buttons.getCreateOrderBtn());
            buttons.add(Buttons.getMyOrdersBtn());
            rowsInLine.add(buttons);
            keyboardMarkup.setKeyboard(rowsInLine);
            editMessageText.setText(EmojiParser.parseToUnicode(":rocket: <b>ГЛАВНОЕ МЕНЮ:</b>"));
            editMessageText.setReplyMarkup(keyboardMarkup);
        }
        return editMessageText;
    }

    public EditMessageText createOrderCallbackHandler(EditMessageText editMessageText,Update update) {
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(Buttons.getShaurmaBtn());
        buttons.add(Buttons.getDrinksBtn());
        buttons.add(Buttons.getStartersBtn());
        rowsInLine.add(buttons);
        rowsInLine.add(Markups.getCartLine());
        rowsInLine.add(Markups.getMainPageLine());
        keyboardMarkup.setKeyboard(rowsInLine);
        editMessageText.setText(EmojiParser.parseToUnicode("<b>Чем сегодня себя побалуем?</b> :sunglasses:"));
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }

    @Transactional
    public EditMessageText changeBuffCallbackHandler(EditMessageText editMessageText,List<Buff> buffs,Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        Good good = goodDao.getGoodByCallBack(update.getCallbackQuery().getData());
        List<Good> goods = cart.getGoods();
        goods.add(good);
        cart.setGoods(goods);
        cartDao.save(cart);
        System.out.println(cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId()).getGoods());
        keyboardMarkup.setKeyboard(Markups.getBuffMarkup(buffs,3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText("Добавить в шаурму?");
        return editMessageText;
    }
    public EditMessageText cartHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        keyboardMarkup.setKeyboard(Markups.getCartMarkup());

        editMessageText.setReplyMarkup(keyboardMarkup);

        String temp = "";
        StringBuilder sb = new StringBuilder();
        for (Good g: cart.getGoods()) {
            sb.append(g.toString().replace("[", "").replace("]",""));
            sb.append("\n");
        }
        if (cart.getGoods().isEmpty()) {
            temp = "В корзине ничего пока нет";
        } else {
            temp = "<b> В корзине: </b>\n\n" + sb.toString() +
            "\n:moneybag: <b>Товаров на сумму: </b>" + cartDaoImpl.getSumInCart(cart.getId()) + "₽";
        }
        editMessageText.setText(EmojiParser.parseToUnicode(temp));
        return editMessageText;
    }
    public EditMessageText addDrinkCallbackHandler(EditMessageText editMessageText, Update update) {
        List<Good> drinks = goodDao.getAllByType_Id(2L).orElse(new ArrayList<>());
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String callbackData = update.getCallbackQuery().getData();
        Good good = goodDao.getGoodByCallBack(callbackData);
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        List<Good> goods = cartDaoImpl.getGoodsByCartId(cart.getId());
        cart.setGoods(goods);
        good.setCart(cart);
        cartDao.save(cart);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: Закинул в корзину: " + good.getName()));
        keyboardMarkup.setKeyboard(Markups.getDrinksMurkup(drinks, 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }
    public EditMessageText addShaurmaCallbackHandler(EditMessageText editMessageText,Update update) {
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<Good> shaurmas = goodDao.getAllByType_Id(1L).orElse(new ArrayList<>());
        keyboardMarkup.setKeyboard(Markups.getShaurmaMarkup(shaurmas, 3));
        var goodsInCart = cart.getGoods();
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: Закинул в корзину: " +
                goodsInCart.get(goodsInCart.size() - 1).toString()
                        .replace("[","")
                        .replace("]","")));
        return editMessageText;
    }
    @Transactional
    public void addBuffHandler(Update update) {
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        List<Good> goodsInCart = cart.getGoods();
        System.out.println(cart.getGoods());
        Good lastGood = goodsInCart.get(goodsInCart.size() -1);
        log.debug(goodsInCart.toString());
        lastGood.addToBuffList(buffDao.getBuffByCallback(update.getCallbackQuery().getData()));
    }

}
