package com.shaurmalay.bot.services;


import com.shaurmalay.bot.controllers.TransactionController;
import com.shaurmalay.bot.dao.*;
import com.shaurmalay.bot.dao.impl.DelPriceDao;
import com.shaurmalay.bot.dao.impl.OrderDaoImpl;
import com.shaurmalay.bot.exceptions.UserNotFoundException;
import com.shaurmalay.bot.model.*;
import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.shaurmalay.bot.model.requests.ResponseCancelObj;
import com.shaurmalay.bot.model.requests.ResponseGetStateObj;
import com.shaurmalay.bot.model.requests.ResponseInitObj;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Handlers {
    private OrderDao orderDao;
    private OrderDaoImpl orderDaoimpl;
    private GoodDao goodDao;
    private BuffDao buffDao;
    private CartDao cartDao;
    private OrderService orderService;
    private GoodInCartDao goodInCartDao;
    private GoodInCartService goodInCartService;
    private DelPriceDao delPriceDao;
    private ReasonDao reasonDao;
    private TransactionController controller;
    private RequestObjBuildService requestObjBuildService;

    @Autowired
    public Handlers(OrderDao orderDao, OrderDaoImpl orderDaoimpl, GoodDao goodDao, BuffDao buffDao, CartDao cartDao, OrderService orderService,
                    GoodInCartDao goodInCartDao, GoodInCartService goodInCartService, DelPriceDao delPriceDao,
                    ReasonDao reasonDao, TransactionController controller, RequestObjBuildService requestObjBuildService) {
        this.orderDao = orderDao;
        this.orderDaoimpl = orderDaoimpl;
        this.goodDao = goodDao;
        this.buffDao = buffDao;
        this.cartDao = cartDao;
        this.orderService = orderService;
        this.goodInCartDao = goodInCartDao;
        this.goodInCartService = goodInCartService;
        this.delPriceDao = delPriceDao;
        this.reasonDao = reasonDao;
        this.controller = controller;
        this.requestObjBuildService = requestObjBuildService;
    }


    /*    Возвращает историю заказов для Юзера*/
    public EditMessageText historyCallbackHandler(EditMessageText editMessageText, Update update) {
        String data = update.getCallbackQuery().getData();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Long userId = update.getCallbackQuery().getMessage().getChat().getId();
        if (CallbackForMsg.MY_ORDERS.name().equals(data)) {
            List<Order> orderList = orderDao.
                    findAllByCustomer_ChatIdAndOrderStatus_Id(userId, 1l).get();
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

    //    Возвращает сообщение со всем меню шаурмы
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
        editMessageText.setText(EmojiParser.parseToUnicode(":fire: <b>Да-да - это то, что нужно:</b>"));
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }

    public EditMessageText drinksMenuCallbackHandler(Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);
        String data = update.getCallbackQuery().getData();
        List<Good> drinks = goodDao.getAllByType_Id(2L).orElse(new ArrayList<>());
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(String.valueOf(chatId));
        keyboardMarkup.setKeyboard(Markups.getDrinksMurkup(drinks, 3));
        editMessageText.setText(EmojiParser.parseToUnicode(":droplet: <b>Сушнячок:</b>"));
        editMessageText.setReplyMarkup(keyboardMarkup);

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
//        buttons.add(Buttons.getMyOrdersBtn());
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
    public EditMessageText addGoodHandler(EditMessageText editMessageText, List<Good> goods, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        String good = update.getCallbackQuery().getData();
        GoodInCart goodInCart = new GoodInCart();
        goodInCart.setGoodCallbacck(good);
        goodInCart.setCart(cart);
        cart.addGoodInCart(goodInCart);
        goodInCartDao.save(goodInCart);
        cartDao.save(cart);
        keyboardMarkup.setKeyboard(Markups.getShaurmaMarkup(goods, 3));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>Отлично! </b> Добавил <b>" +
                goodDao.getGoodByCallBack(good).getName()) + "</b>");
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
                    "\n:moneybag: <b>Товаров на сумму: </b>" + goodInCartService.calculateSumForGoodsByCart(cart) + "₽" +
                    "\n\n:bulb:<i>Сейчас вы видите сумму, без учёта стоимости доставки</i>";
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
        keyboardMarkup.setKeyboard(Markups.getStartersMurkup(starters, 2));
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
        String goodsToString = goodInCartService.getGoodsWithBuffsToString(cart);
        List<GoodInCart> goodsInCarts = goodInCartDao.findAllByCart(cart).get();
        keyboardMarkup.setKeyboard(Markups.getGoodsInCartMarkup(goodsToString,
                2, goodsInCarts));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>Выберите товары, которые хотите удалить</b>" +
                "\n\n:shopping_cart: <b>Сейчас в корзине: </b>\n\n" + goodsToString));
        return editMessageText;
    }

    @Transactional
    public EditMessageText actionDeleteFromCartHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
        String data = update.getCallbackQuery().getData();
        Long goodInCartId = Long.parseLong(data.substring(data.lastIndexOf("_") + 1));
        String name = goodInCartService.getGoodInCartTostring(cart, goodInCartId);
        goodInCartService.deleteGoodFromCartById(goodInCartId);
        String goods = goodInCartService.getGoodsWithBuffsToString(cart);
        List<GoodInCart> goodsInCarts = goodInCartDao.findAllByCart(cart).get();
        keyboardMarkup.setKeyboard(Markups.getGoodsInCartMarkup(goodInCartService.getGoodsWithBuffsToString(cart),
                2, goodsInCarts));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":zap: <b>ГАЛЯ! У нас отмена!</b>" + name +
                "</b>\n\n:shopping_cart: <b> Сейчас в корзине: </b>\n\n" + goods +
                "\n\nЧто-то ещё?"));
        return editMessageText;
    }

    @Transactional
    public SendMessage sendOfferToDelChat(SendMessage sendMessage, Update update, Long delChatId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String chatId = "";

        if (update.hasMessage() && !update.hasCallbackQuery()) {
            chatId = String.valueOf(update.getMessage().getChatId());
        } else {
            chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        }
        Cart cart = cartDao.findByUserId(Long.parseLong(chatId));
        String goods = goodInCartService.getGoodsWithBuffsToString(cart);
        Integer sum = goodInCartService.calculateSumForGoodsByCart(cart);
        List<Order> orders = orderDao.findAllByCustomer_ChatId(Long.parseLong(chatId)).get();
        Order last = orders.get(orders.size() - 1);
        keyboardMarkup.setKeyboard(Markups.getOfferMarkup());
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setChatId(String.valueOf(delChatId));
        sendMessage.setText(EmojiParser.parseToUnicode(":scroll: <b>Заказ#" + last.getId() + ":</b>" +
                "\n" + last.getPositions() +
                "\n:moneybag: Сумма заказа: <b>" + sum + "₽</b>" +
                "\n:round_pushpin:" + last.getAddres() +
                "\n:telephone_receiver:" + last.getPhone() + "\n\n\nChatId:_" + chatId + "\nOrderId:#" + last.getId()));
        return sendMessage;
    }

    @Transactional
    public EditMessageText backToOfferInDelChat(EditMessageText editMessageText, Update update, Long delChatId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String message = update.getCallbackQuery().getMessage().getText();
        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        Cart cart = cartDao.findByUserId(Long.parseLong(chatId));
        String goods = goodInCartService.getGoodsWithBuffsToString(cart);
        Integer sum = goodInCartService.calculateSumForGoodsByCart(cart);
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        keyboardMarkup.setKeyboard(Markups.getOfferMarkup());
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setChatId(String.valueOf(delChatId));
        editMessageText.setText(EmojiParser.parseToUnicode(":scroll: <b>Заказ#" + last.getId() + ":</b>\n" + goods +
                "\n:moneybag: Сумма заказа: <b>" + sum + "₽</b>" +
                "\n:round_pushpin:" + last.getAddres() +
                "\n:telephone_receiver:" + last.getPhone() + "\n\n\nChatId:_" + chatId + "\nOrderId:#" + orderId));
        return editMessageText;
    }

    @Transactional
    public SendMessage sendToUserChat(SendMessage sendMessage, Update update) {
        String message = "";
        String phone = "";
        Long chatId = 0L;
        if (update.hasMessage() && !update.hasCallbackQuery()) {
            message = update.getMessage().getText().trim();
            phone = message.substring(message.lastIndexOf("тел") + 4).trim();
            chatId = update.getMessage().getChatId();
        } else {
            message = update.getCallbackQuery().getData();
            String orderWithPhoneId = update.getCallbackQuery().getData()
                    .substring(message.lastIndexOf("_") + 1);
            phone = orderDao.findById(Long.parseLong(orderWithPhoneId)).get().getPhone();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        Cart cart = cartDao.findByUserId(chatId);
        var orders = orderDao.findAllByCustomer_ChatId(chatId).get();
        var last = orders.get(orders.size() - 1);
        orderService.changeStatusOrder(2l, last);
        last.setOrderSum(goodInCartService.calculateSumForGoodsByCart(cart));

        last.setPhone(phone);
        orderDao.save(last);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(EmojiParser.parseToUnicode(":hourglass_flowing_sand: Мы обрабатываем вашу заявку - " +
                "идёт расчёт стоимости доставки... <b>Дождитесь завершения обработки</b>"));
        return sendMessage;
    }

    @Transactional
    public EditMessageText calculateDelPriceHandler(Update update, EditMessageText editMessageText, List<DelPrice> prices) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        String message = update.getCallbackQuery().getMessage().getText();
        System.out.println(message);
        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        inlineKeyboardMarkup.setKeyboard(Markups.getDelPriceMurkup(prices, 2));
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        editMessageText.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessageText.setText(EmojiParser.parseToUnicode("<b>Заказ#" + last.getId() + ":</b>" +
                "\n:moneybag: Выберите стоимость доставки, согласно адресу предоставленного клиентом." +
                "\n<b>Адрес: </b>" + last.getAddres() +
                "\nChatId:_" + chatId + "\nOrderId:#" + last.getId()));
        return editMessageText;
    }

    @Transactional
    public EditMessageText otkazHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String message = update.getCallbackQuery().getMessage().getText();
        String orderId = message.substring(message.lastIndexOf("#") + 1);
        Cart cart = cartDao.findByUserId(chatId);
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        orderService.changeStatusOrder(7L, last);
        goodInCartService.deleteAllFromCart(cart);
        ResponseCancelObj responseCancelObj = null;

        try {
            responseCancelObj = controller.getCancelPayment(requestObjBuildService.cancelOrGetStateObjBuilder(last));
        } catch (Exception e) {
            log.error("REST ERROR: " + e.getMessage());
        }
        if (responseCancelObj.isSuccess() && responseCancelObj.getStatus().equals("CANCELED")) {
            orderDao.save(last);
            keyboardMarkup.setKeyboard(Collections.singletonList(Markups.getMainPageLine()));
            editMessageText.setReplyMarkup(keyboardMarkup);
            editMessageText.setText(EmojiParser.parseToUnicode(":white_check_mark:<b> Заказ#" + orderId + "</b> отменен"));
        }
        return editMessageText;
    }

    public SendMessage sendCancelOrderToDelivery(SendMessage sendMessage, Update update, Long delChatId) {
        String message = update.getCallbackQuery().getMessage().getText();
        String orderId = message.substring(message.lastIndexOf("#") + 1);
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        sendMessage.setChatId(String.valueOf(delChatId));
        sendMessage.setText(EmojiParser.parseToUnicode("<b>Заказ#" + last.getId() + "</b>" +
                "\n:x: Клиент отказался от заказа"));
        return sendMessage;
    }

    @Transactional
    public EditMessageText inputAddress(EditMessageText editMessageText, Update update) throws UserNotFoundException {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        List<Order> orders = orderDao.findDistinctByCustomer_ChatIdAndOrderStatus_IdAndAddresIsNotNull(chatId, 1L)
                .get();
        keyboardMarkup.setKeyboard(Markups.getMyAddressMarkup(orders, 2));
        orderService.registerOrder(chatId);
        editMessageText.setText(EmojiParser.parseToUnicode("Отправьте Ваш адрес мне в сообщение или выберите из существующих." +
                "\n\nЧтобы я понял, что Вы отправляете адрес, <b><u>ОБЯЗАТЕЛЬНО</u></b> начните ваше сообщение с <b>адрес</b>" +
                "\n:bulb: <i><u>Например:</u> <b>адрес</b> ул.Пушкина, 45-3</i>"));
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }

    @Transactional
    public SendMessage inputPhoneNumber(SendMessage sendMessage, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String message = "";
        String address = "";
        Long chatId = 0l;
        if (update.hasMessage() && !update.hasCallbackQuery()) {
            message = update.getMessage().getText().trim();
            address = message.substring(message.toLowerCase().lastIndexOf("адрес") + 6).trim();
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            String orderWithAddresId = data.substring(data.lastIndexOf("_") + 1);
            Order orderWithAddres = orderDao.findById(Long.parseLong(orderWithAddresId)).get();
            address = orderWithAddres.getAddres();
        }
        Cart cart = cartDao.findByUserId(chatId);

        List<Order> ordersWithPhone = orderDao.findDistinctByCustomer_ChatIdAndOrderStatus_IdAndPhoneIsNotNull(chatId, 1L)
                .get();
        var orders = orderDao.findAllByCustomer_ChatId(chatId).get();
        var last = orders.get(orders.size() - 1);
        last.setAddres(address);
        last.setPositions(goodInCartService.getGoodsWithBuffsToString(cart));
        keyboardMarkup.setKeyboard(Markups.getMyPhonesMarkup(ordersWithPhone, 2));
        orderService.changeStatusOrder(4l, last);
        orderDao.save(last);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(EmojiParser.parseToUnicode("<b>Отлично!</b> Оставьте номер телефона для связи, " +
                "или выберите из существующих" +
                "\n\nЧтобы я понял, что Вы отправляете телефон, <b><u>ОБЯЗАТЕЛЬНО</u></b> начните Ваше сообщение с <b>тел</b>\n\n" +
                ":bulb: <i><u>Например:</u> <b>тел</b> 89172223333</i>"));
        return sendMessage;
    }

    @Transactional
    public SendMessage confirmationByUserHandler(SendMessage sendMessage, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        ResponseInitObj responseInitObj = null;


        String message = update.getCallbackQuery().getMessage().getText();
        String data = update.getCallbackQuery().getData();
        String userChatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1);
        Cart cart = cartDao.findByUserId(Long.parseLong(userChatId));
        String goods = goodInCartService.getGoodsWithBuffsToString(cart);
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        int goodsSum = last.getOrderSum();
        int deliveryPrice = delPriceDao.findByCallBack(data).get().getPrice();
        int total = userChatId.equals(String.valueOf(537130248))
                ? deliveryPrice
                : goodsSum + deliveryPrice;
        try {
            responseInitObj = controller.getLinkFromBank(requestObjBuildService.initObjBuilder(userChatId,
                    total * 100, last));
            last.setPaymentId(responseInitObj.getPaymentId());
        } catch (Exception e) {
            log.error("REST ERROR: " + e.getMessage());
        }
        String paymentUrl = responseInitObj.getPaymentURL();
        last.setPaymentId(responseInitObj.getPaymentId());
        last.setLinkToPay(paymentUrl);
        last.setOrderSum(total);
        orderDao.save(last);

        rows.add(Markups.getPaymentLine("Оплатить заказ", paymentUrl));
//        rows.add(Markups.getAnyLine("Отказаться от заказа", "OTKAZ"));
        rows.add(Markups.getAnyLine(":white_check_mark: Оплатил заказ", "PAID"));
        keyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setChatId(userChatId);
        sendMessage.setText(EmojiParser.parseToUnicode(":receipt:<b>Заказ#" + last.getId() + ":</b>\n\n" + goods +
                "\n:shopping_cart: <b>Товаров на сумму: </b>" + goodsSum + "₽\n" +
                ":motor_scooter: <b>Стоимость доставки: </b>" + deliveryPrice + "₽" +
                "\n\n:moneybag: <b>Итоговая сумма</b>: " + total + "₽\n\n\nOrder#" + orderId));
        return sendMessage;
    }

    public EditMessageText awaitMoneyMsqToDeliveryHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        String message = update.getCallbackQuery().getMessage().getText();
        Long deliveryChatId = update.getCallbackQuery().getMessage().getChatId();
        String userChatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        System.out.println(userChatId + "\n" + orderId);

        rows.add(Markups.getAnyLine(":eyes: Проверить оплату", CallbackForMsg.CHECK_STATE_PAYMENT.name()));
        keyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setChatId(String.valueOf(deliveryChatId));
        editMessageText.setText(EmojiParser.parseToUnicode(":hourglass_flowing_sand: Клиенту отправлена конечная " +
                "стоимость, после оплаты заказа, вы сможете взять в доставку этот заказ\n\n\nChatId:_" + userChatId +
                "\nOrderId:#" + orderId));

        return editMessageText;
    }

    public EditMessageText canselByDeliveryMan(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<Reason> reasons = (List<Reason>) reasonDao.findAll();
        String message = update.getCallbackQuery().getMessage().getText();
        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        keyboardMarkup.setKeyboard(Markups.getDelReasonMarkup(reasons, 2));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":scroll: <b>Заказ#" + last.getId() + "</b>" +
                "\nУкажите причину отказа" +
                "\nChatId:_" + chatId + "\nOrderId:#" + orderId));
        return editMessageText;
    }

    public EditMessageText orderCanceledToDelChat(EditMessageText editMessageText, Update update) {
        String message = update.getCallbackQuery().getMessage().getText();
        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1);
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        editMessageText.setText(EmojiParser.parseToUnicode(":white_check_mark: <b>Заказ#" + last.getId() +
                "</b> был отменен курьером"));
        return editMessageText;
    }

    public SendMessage orderCanceledToUserChat(SendMessage sendMessage, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        keyboardMarkup.setKeyboard(Collections.singletonList(Markups
                .getBackPageLine(CallbackForMsg.CART, "Назад к оформлению")));
        String message = update.getCallbackQuery().getMessage().getText();
        String data = update.getCallbackQuery().getData();
        Reason reason = reasonDao.findByCallback(data).orElse(new Reason("неизвестно"));
        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        orderService.changeStatusOrder(7L, last);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText(EmojiParser.parseToUnicode(":o: Заказ#" + orderId + "был <b>отменен</b>." +
                "\nПричина: <b>" + reason.getName() + "</b>\n Вы можете уточнить по номеру"));
        return sendMessage;
    }

    @Transactional
    public EditMessageText checkPaidPaymentHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        ResponseGetStateObj getStateObj = null;
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        String message = update.getCallbackQuery().getMessage().getText();
        String orderId = message.substring(message.lastIndexOf("#") + 1);
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        try {
            getStateObj = controller.getStatePayment(requestObjBuildService.cancelOrGetStateObjBuilder(last));
        } catch (Exception e) {
            log.error("REST ERROR: " + e.getMessage());
        }

        if (getStateObj.getStatus().equals("CONFIRMED")) {
            Cart cart = cartDao.findByUserId(update.getCallbackQuery().getMessage().getChatId());
            goodInCartService.deleteAllFromCart(cart);
            orderService.changeStatusOrder(1L, last);
            editMessageText.setText(EmojiParser.parseToUnicode(":white_check_mark: Отлично! " +
                    "Мы уже начали выполнять Ваш заказ.\n\n<b>Приятного аппетита!</b>"));
            rows.add(Markups.getMainPageLine());
        } else {
            editMessageText.setText(EmojiParser.parseToUnicode(":x: Ваш платёж <b>не подтверждён</b>." +
                    "\nЕсли Вы ещё <b><u>не оплачивали</u></b> Ваш заказ, воспользуйтесь кнопкой оплатить заказ ниже." +
                    "\n\n<b>Заказ#" + last.getId() + ":</b> Итого к оплате: " + last.getOrderSum() +
                    "\nЕсли <b>заказ оплачен</b>, ожидайте подтверждения\n\n\nOrder#" + orderId));
            rows.add(Markups.getPaymentLine("Оплатить заказ", last.getLinkToPay()));
//            rows.add(Markups.getAnyLine("Отказаться от заказа", "OTKAZ"));
        }
        keyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }

    @Transactional
    public EditMessageText checkStatePaymentForDel(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        ResponseGetStateObj getStateObj = null;
        String message = update.getCallbackQuery().getMessage().getText();
        Long chatId = Long.parseLong(message.substring(message.lastIndexOf("_") + 1,
                message.lastIndexOf("\n")).trim());
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        Order last = orderDao.findById(Long.parseLong(orderId)).get();

        try {
            getStateObj = controller.getStatePayment(requestObjBuildService.cancelOrGetStateObjBuilder(last));
        } catch (Exception e) {
            log.error("REST ERROR: " + e.getMessage());
        }
        if (getStateObj.getStatus().equals("CONFIRMED")) {
            editMessageText.setText(EmojiParser.parseToUnicode("<b>Заказ#" + last.getId() + ":</b>" +
                    "\n\nСтатус: :white_check_mark: <b>Оплачен</b>" +
                    "\n\n\nChatId:_" + chatId + "\nOrderId:#" + last.getId()));
            rows.add(Markups.getAnyLine("Взять в работу", CallbackForMsg.GET_TO_DEL.name()));
        } else {
            editMessageText.setText(EmojiParser.parseToUnicode("<b>Заказ#" + last.getId() + ":</b>" +
                    "\n\nСтатус: :red_circle: <b>Не оплачен</b>" +
                    "\n\n\nChatId:_" + chatId + "\nOrderId:#" + last.getId()));
            rows.add(Markups.getAnyLine(":eyes: Проверить ещё раз", CallbackForMsg.CHECK_STATE_PAYMENT.name()));
        }
        rows.add(Markups.getAnyLine("Отменить заказ", CallbackForMsg.CANCEL_ORDER_AFTER_PAYMENT.name()));
        keyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return editMessageText;
    }

    @Transactional
    public EditMessageText getToDelOrderHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String message = update.getCallbackQuery().getMessage().getText();
        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        Cart cart = cartDao.findByUserId(Long.parseLong(chatId));
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        keyboardMarkup.setKeyboard(Collections.singletonList(Markups.getAnyLine("Отменить заказ",
                CallbackForMsg.CANCEL_ORDER_AFTER_PAYMENT.name())));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":scroll: Заказ#" + last.getId() + ":<b>\n" +
                last.getPositions() +
                "</b>\n:moneybag: Сумма заказа: <b>" + last.getOrderSum() + "₽</b>" +
                "\n:round_pushpin:" + last.getAddres() +
                "\n:telephone_receiver:" + last.getPhone() + "\n\n\nChatId:_" + chatId + "\nOrderId:#" + last.getId()));
        return editMessageText;
    }

    @Transactional
    public void startDeliveringSendToUser(SendMessage sendMessage, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        String message = update.getCallbackQuery().getMessage().getText();
        Long chatId = Long.parseLong(message.substring(message.lastIndexOf("_") + 1,
                message.lastIndexOf("\n")).trim());
        Long orderId = Long.parseLong(message.substring(message.lastIndexOf("#") + 1));
        Order order = orderDao.findById(orderId).get();
        orderService.changeStatusOrder(1L, order);
        Cart cart = cartDao.findByUserId(chatId);
        goodInCartService.deleteAllFromCart(cart);
//        rows.add(Markups.getMainPageLine());
//        keyboardMarkup.setKeyboard(rows);
//        sendMessage.setText(EmojiParser.parseToUnicode(":rocket: Курьер уже взял в работу Ваш Заказ#" + orderId +
//                "\nДоставим в течение часа"));
//        sendMessage.setReplyMarkup(keyboardMarkup);
//        sendMessage.setChatId(String.valueOf(chatId));
    }

    public EditMessageText confirmingCancelingHandler(EditMessageText editMessageText, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        String message = update.getCallbackQuery().getMessage().getText();
        String orderId = message.substring(message.lastIndexOf("#") + 1);
        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n"));

        rows.add(Markups.getAnyLine("Да, отменить оплаченый заказ",
                CallbackForMsg.ACTION_CANCELING_AFTER_PAYMENT.name()));
        rows.add(Markups.getAnyLine("Нет, я передумал",
                CallbackForMsg.ROLLBACK_CANCELING_AFTER_PAYMENT.name()));
        keyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText(EmojiParser.parseToUnicode(":bangbang: Вы действительно хотите отменить <b>Заказ#"
                + orderId +
                "?</b>" +
                "\n\n\nChatId:_" + chatId + "\nOrderId:#" + orderId));
        return editMessageText;
    }

    public SendMessage actionCancelingAfterPayment(SendMessage sendMessage, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        keyboardMarkup.setKeyboard(Collections.singletonList(Markups
                .getBackPageLine(CallbackForMsg.MAIN_PAGE, "На главную")));
        String message = update.getCallbackQuery().getMessage().getText();
        String data = update.getCallbackQuery().getData();

        String chatId = message.substring(message.lastIndexOf("_") + 1, message.lastIndexOf("\n")).trim();
        String orderId = message.substring(message.lastIndexOf("#") + 1).trim();
        Order last = orderDao.findById(Long.parseLong(orderId)).get();
        orderService.changeStatusOrder(7L, last);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText(EmojiParser.parseToUnicode(":o: Заказ#" + orderId + " был <b>отменен</b>." +
                "\n:pensive: Извините, нам пришлось отменить заказ по непредвиденным обстоятельствам.." +
                "\n\nДеньги будут <b>возвращены</b> Вам в течение 2 часов."));
        return sendMessage;


    }


}
