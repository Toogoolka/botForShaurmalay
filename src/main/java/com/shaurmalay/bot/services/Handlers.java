package com.shaurmalay.bot.services;


import com.shaurmalay.bot.dao.*;
import com.shaurmalay.bot.dao.impl.CartDaoImpl;
import com.shaurmalay.bot.model.*;
import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.shaurmalay.bot.services.markups_and_buttons.Buttons;
import com.shaurmalay.bot.services.markups_and_buttons.Markups;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
    private GoodInCartDao goodInCartDao;
    private GoodInCartService goodInCartService;

    @Autowired
    public Handlers(UserDao userDao, OrderDao orderDao, GoodDao goodDao, BuffDao buffDao, CartDao cartDao, CartDaoImpl cartDaoImpl, GoodInCartDao goodInCartDao, GoodInCartService goodInCartService) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.goodDao = goodDao;
        this.buffDao = buffDao;
        this.cartDao = cartDao;
        this.cartDaoImpl = cartDaoImpl;
        this.goodInCartDao = goodInCartDao;
        this.goodInCartService = goodInCartService;
    }

    public EditMessageText historyCallbackHandler(EditMessageText editMessageText, Update update) {
        String data = update.getCallbackQuery().getData();
        ;
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Long userId = update.getCallbackQuery().getMessage().getChat().getId();
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
            editMessageText.setText(EmojiParser.parseToUnicode(":open_book: <b>История ваших заказов:</b>\n" + history));
            rowsInLine.add(Markups.getBackPageLine(CallbackForMsg.MAIN_PAGE, "Назад"));
            keyboardMarkup.setKeyboard(rowsInLine);
            editMessageText.setReplyMarkup(keyboardMarkup);
        }
        return editMessageText;
    }

    public EditMessageText shaurmaMenuCallbackHandler(Update update, List<Good> goods) {
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

    public EditMessageText startersMenuCallbackHandler(Update update, List<Good> starters) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        EditMessageText editMessageText = new EditMessageText();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        String data = update.getCallbackQuery().getData();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setMessageId(messageId);
        editMessageText.enableHtml(true);
        editMessageText.setChatId(String.valueOf(chatId));
        keyboardMarkup.setKeyboard(Markups.getStartersMurkup(starters, 2));
        editMessageText.setText(EmojiParser.parseToUnicode(":fire: <b>Да-да, брат - это то, что нужно:</b>"));
        editMessageText.setReplyMarkup(keyboardMarkup);
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


        //TODO Для чего это???
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
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(Buttons.getCreateOrderBtn());
        buttons.add(Buttons.getMyOrdersBtn());
        rowsInLine.add(buttons);
        rowsInLine.add(Markups.getCartLine());
        keyboardMarkup.setKeyboard(rowsInLine);
        editMessageText.setText(EmojiParser.parseToUnicode(":gem: <b>Главное меню:</b>"));
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }

    public EditMessageText createOrderCallbackHandler(EditMessageText editMessageText, Update update) {
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
    public EditMessageText changeBuffCallbackHandler(EditMessageText editMessageText, List<Buff> buffs, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        String good = update.getCallbackQuery().getData();
        GoodInCart goodInCart = new GoodInCart();
        goodInCart.setGoodCallbacck(good);
        goodInCart.setCart(cart);
        cart.addGoodInCart(goodInCart);
        goodInCartDao.save(goodInCart);
        cartDao.save(cart);
        keyboardMarkup.setKeyboard(Markups.getBuffMarkup(buffs, 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":fire: <b>Добавить в шаурму?</b>\n" +
                "<i>Можно закинуть <b><u>до 3х</u></b> ингридиентов</i>"));
        return editMessageText;
    }

    @Transactional
    public EditMessageText cartHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        keyboardMarkup.setKeyboard(Markups.getCartMarkup());

        editMessageText.setReplyMarkup(keyboardMarkup);
        String goods = goodInCartService.getGoodsWithBuffsToString(cart);
        String temp = "";
        if (goodInCartService.calculateSumForGoodsByCart(cart) == 0) {
            temp = "В корзине ничего пока нет";
        } else {
            temp = ":shopping_cart: <b> В корзине: </b>\n\n" + goods +
                    "\n:moneybag: <b>Товаров на сумму: </b>" + goodInCartService.calculateSumForGoodsByCart(cart) + "₽";
        }
        editMessageText.setText(EmojiParser.parseToUnicode(temp));
        return editMessageText;
    }

    @Transactional
    public EditMessageText addDrinkCallbackHandler(EditMessageText editMessageText, Update update) {
        List<Good> drinks = goodDao.getAllByType_Id(2L).orElse(new ArrayList<>());
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        String good = update.getCallbackQuery().getData();
        GoodInCart goodInCart = new GoodInCart();
        goodInCart.setGoodCallbacck(good);
        goodInCart.setCart(cart);
        cart.addGoodInCart(goodInCart);
        goodInCartDao.save(goodInCart);
        cartDao.save(cart);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>Отлично! </b> Добавил <b>" +
                goodDao.getGoodByCallBack(good).getName()) + "</b>");
        keyboardMarkup.setKeyboard(Markups.getDrinksMurkup(drinks, 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }

    @Transactional
    public EditMessageText addShaurmaCallbackHandler(EditMessageText editMessageText, Update update) {
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        List<GoodInCart> goodInCartList = cart.getGoodIncarts();
        GoodInCart lastRow = goodInCartList.get(goodInCartList.size() - 1);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<Good> shaurmas = goodDao.getAllByType_Id(1L).orElse(new ArrayList<>());
        keyboardMarkup.setKeyboard(Markups.getShaurmaMarkup(shaurmas, 3));
        Good lastGood = goodDao.getGoodByCallBack(lastRow.getGoodCallbacck());
        List<Buff> buffs = lastRow.getBuffs();
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(getAnswerStringForApprove(lastGood, buffs));
        return editMessageText;
    }

    @Transactional
    public EditMessageText addBuffHandler(EditMessageText editMessageText, Update update, List<Buff> buffs) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String buff = update.getCallbackQuery().getData();
        String answer = "";
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        List<GoodInCart> goodIncarts = cart.getGoodIncarts();
        GoodInCart lastRow = goodIncarts.get(goodIncarts.size() - 1);

        answer = lastRow.addBuff(buffDao.getBuffByCallback(buff))
                ? EmojiParser.parseToUnicode(":zap: <b>Шаурма прокачена!</b> Добавил <b>" +
                buffDao.getBuffByCallback(buff) + "</b>")
                : EmojiParser.parseToUnicode(":no_entry: <b>Нельзя добавить больше 3х ингридиентов</b>" +
                "\n\n:bulb:<i><u>Подсказка:</u> Вы можете отменить последнюю добавку и заменить на нужную</i>" +
                "\n\n\n <b>Сейчас добавлено: </b>" + lastRow.getBuffs().toString()
                .replace("[", "").replace("]", ""));
        goodInCartDao.save(lastRow);
        keyboardMarkup.setKeyboard(Markups.getBuffMarkup(buffs, 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(answer);
        return editMessageText;
    }

    @Transactional
    public EditMessageText addStarterHandler(EditMessageText editMessageText, Update update, List<Good> starters) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        String good = update.getCallbackQuery().getData();
        GoodInCart goodInCart = new GoodInCart();
        goodInCart.setGoodCallbacck(good);
        goodInCart.setCart(cart);
        cart.addGoodInCart(goodInCart);
        goodInCartDao.save(goodInCart);
        cartDao.save(cart);
        keyboardMarkup.setKeyboard(Markups.getStartersMurkup(starters, 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>Добро! Я тебя услышал</b>\n\nДобавил <b>"
                + goodDao.getGoodByCallBack(good)) + "</b>");
        return editMessageText;
    }

    public String getAnswerStringForApprove(Good good, List<Buff> buffs) {
        String string = EmojiParser.parseToUnicode(":zap: Закинул в корзину <b>" + good + "</b>");
        if (!buffs.isEmpty() && buffs != null) {
            string = EmojiParser.parseToUnicode(":zap: Закинул в корзину\n<b>" + good + ": </b>" +
                    buffs.toString()
                            .replace("[", "")
                            .replace("]", ""));
        }
        return string;
    }

    @Transactional
    public EditMessageText deleteBuffs(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        List<GoodInCart> goodIncarts = cart.getGoodIncarts();
        GoodInCart lastRow = goodIncarts.get(goodIncarts.size() - 1);
        lastRow.deleteAllBuffs();
        goodInCartDao.save(lastRow);
        keyboardMarkup.setKeyboard(Markups.getShaurmaMarkup(goodDao.getAllByType_Id(1l).get(), 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>Добро!</b> Добавил только <b>"
                + goodDao.getGoodByCallBack(lastRow.getGoodCallbacck()) + "</b>"));
        return editMessageText;
    }

    @Transactional
    public EditMessageText deleteLastBuff(EditMessageText editMessageText, Update update, List<Buff> buffs) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        List<GoodInCart> goodIncarts = cart.getGoodIncarts();
        GoodInCart lastRow = goodIncarts.get(goodIncarts.size() - 1);
        String lastBuff = lastRow.getBuffs().isEmpty()
                ? "А, погоди-ка... Добавок еще нет в заказе"
                : "Убираю: " + lastRow.getBuffs()
                .get(lastRow.getBuffs()
                        .size() - 1).toString();
        lastRow.deleteLastBuff();
        goodInCartDao.save(lastRow);
        keyboardMarkup.setKeyboard(Markups.getBuffMarkup(buffs, 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>ГАЛЯ! У нас отмена!</b> " + lastBuff));
        return editMessageText;
    }

    @Transactional
    public EditMessageText deleteAllFromCartHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        goodInCartService.deleteAllFromCart(cart);
        rowsInLine.add(Markups.getBackPageLine(CallbackForMsg.CREATE_ORDER, "Назад"));
        keyboardMarkup.setKeyboard(rowsInLine);
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>Корзина очищена!</b>"));
        return editMessageText;
    }

    @Transactional
    public EditMessageText deleteFromCartHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        List<GoodInCart> goodsInCarts = goodInCartDao.findAllByCart(cart).get();
        keyboardMarkup.setKeyboard(Markups.getGoodsInCartMarkup(goodInCartService.getGoodsWithBuffsToString(cart),
                2, goodsInCarts));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>Выберите товары, которые хотите удалить</b>"));
        return editMessageText;
    }

    @Transactional
    public EditMessageText actionDeleteFromCartHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String data = update.getCallbackQuery().getData();
        Long goodInCartId = Long.parseLong(data.substring(data.lastIndexOf("_") + 1));
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        String name = goodInCartService.getGoodInCartTostring(cart, goodInCartId);
        goodInCartService.deleteGoodFromCartById(goodInCartId);
        List<GoodInCart> goodsInCarts = goodInCartDao.findAllByCart(cart).get();
        keyboardMarkup.setKeyboard(Markups.getGoodsInCartMarkup(goodInCartService.getGoodsWithBuffsToString(cart),
                2, goodsInCarts));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: Удалил <b>" + name + "</b>\n Что-то ещё?"));
        return editMessageText;
    }

}
