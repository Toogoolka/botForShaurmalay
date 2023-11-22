package com.shaurmalay.bot.services.markups_and_buttons;

import com.shaurmalay.bot.model.*;
import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;

import java.util.List;



/*
Здесь создаются клавитуры для отправки в сообщения.
Также можно получить не всю клавиатуру, но просто конкретную строку кнопок
*/


public class Markups {
    public static List<List<InlineKeyboardButton>> getShaurmaMarkup(List<Good> items, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            var btn = createButton(items.get(i).getEmoji() + " " + items.get(i).getName(),
                    items.get(i).getCallBack().toUpperCase());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == items.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getCartLine());
        rows.add(getBackPageLine(CallbackForMsg.CREATE_ORDER, "Назад"));
        return rows;
    }

    public static List<List<InlineKeyboardButton>> getBuffMarkup(List<Buff> items, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();


        for (int i = 0; i < items.size(); i++) {
            var btn = createButton(items.get(i).getName(),
                    items.get(i).getCallback().toUpperCase());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == items.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getConfirmLine());
//        rows.add(getBackPageLine(CallbackForMsg.DELETE_BUFFS, "Ничего не нужно"));
        return rows;
    }


    public static List<List<InlineKeyboardButton>> getDrinksMurkup(List<Good> drinks, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        for (int i = 0; i < drinks.size(); i++) {
            var btn = createButton(drinks.get(i).getEmoji() + " " + drinks.get(i).getName(),
                    drinks.get(i).getCallBack().toUpperCase());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == drinks.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getCartLine());
        rows.add(getBackPageLine(CallbackForMsg.CREATE_ORDER, "Назад"));
        return rows;
    }

    public static List<List<InlineKeyboardButton>> getStartersMurkup(List<Good> starters, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        for (int i = 0; i < starters.size(); i++) {
            if (starters.get(i).getCallBack().contains("S_ASHPOCHMAKI")) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
                rows.add(getAnyLine(starters.get(i).getEmoji() + " " + starters.get(i).getName(),
                        starters.get(i).getCallBack().toUpperCase()));
                continue;
            }
            var btn = createButton(starters.get(i).getEmoji() + " " + starters.get(i).getName(),
                    starters.get(i).getCallBack().toUpperCase());
            buttonsOnTheRow.add(btn);

            if (buttonsOnTheRow.size() == countElementsOnRows || i == starters.size() - 1 &&
                    starters.get(i).getCallBack().contains("S_ASHPOCHMAKI")) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getCartLine());
        rows.add(getBackPageLine(CallbackForMsg.CREATE_ORDER, "Назад"));
        return rows;
    }

    public static List<List<InlineKeyboardButton>> getDelPriceMurkup(List<DelPrice> prices, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            var btn = createButton(prices.get(i).getLocation(),
                    prices.get(i).getCallBack().toUpperCase());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == prices.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getAnyLine(":back: Назад к заявке", CallbackForMsg.BACK_TO_OFFER.name()));
        return rows;
    }
    public static List<List<InlineKeyboardButton>> getDelReasonMarkup(List<Reason> reasons, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        for (int i = 0; i < reasons.size(); i++) {
            var btn = createButton(reasons.get(i).getName(),
                    reasons.get(i).getCallback().toUpperCase());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == reasons.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getAnyLine(":back: Назад к заявке", CallbackForMsg.BACK_TO_OFFER.name()));
        return rows;
    }
    public static List<List<InlineKeyboardButton>> getMyAddressMarkup(List<Order> orders, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();

        for (int i = 0; i < orders.size(); i++) {
            if (i < orders.size() - 1 && orders.get(i).getAddres().equals(orders.get(i + 1).getAddres())) {
                continue;
            }
            var btn = createButton(orders.get(i).getAddres(),
                    "MY_ADDRESS_" + orders.get(i).getId());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == orders.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getAnyLine(":back: Вернуться к корзине", CallbackForMsg.CART.name()));
        return rows;
    }
    public static List<List<InlineKeyboardButton>> getMyPhonesMarkup(List<Order> orders, int countElementsOnRows) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            if (i < orders.size() - 1 && orders.get(i).getPhone().equals(orders.get(i + 1).getPhone())) {
                continue;
            }
            var btn = createButton(orders.get(i).getPhone(),
                    "MY_PHONE_" + orders.get(i).getId());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == orders.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getAnyLine(":back: Вернуться к корзине", CallbackForMsg.CART.name()));
        return rows;
    }
    public static InlineKeyboardButton createButton(String textOnButton, String callbackData) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(EmojiParser.parseToUnicode(textOnButton));
        btn.setCallbackData(callbackData);
        return btn;
    }

    public static List<InlineKeyboardButton> getCartLine() {
        List<InlineKeyboardButton> cartLine = new ArrayList<>();
        cartLine.add(Buttons.getCartBtn());
        return cartLine;
    }

    public static List<InlineKeyboardButton> getConfirmLine() {
        List<InlineKeyboardButton> line = new ArrayList<>();
        line.add(Buttons.getConfirmBuffBtn());
        line.add(Buttons.createBtn(":o:", "Отменить добавку", CallbackForMsg.DELETE_LAST_BUFF));
        return line;
    }

    public static List<InlineKeyboardButton> getMainPageLine() {
        List<InlineKeyboardButton> mainLine = new ArrayList<>();
        mainLine.add(Buttons.getMainPageBtn());
        return mainLine;
    }

    public static List<InlineKeyboardButton> getAnyLine(String textOnBtn, String callback) {
        List<InlineKeyboardButton> cartLine = new ArrayList<>();
        cartLine.add(createButton(textOnBtn, callback));
        return cartLine;
    }
    public static List<InlineKeyboardButton> getPaymentLine(String textOnBtn, String url) {
        List<InlineKeyboardButton> cartLine = new ArrayList<>();
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(textOnBtn);
        btn.setUrl(url);
        cartLine.add(btn);
        return cartLine;
    }

    public static List<InlineKeyboardButton> getBackPageLine(CallbackForMsg callback, String text) {
        List<InlineKeyboardButton> mainLine = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":back: " + text));
        button.setCallbackData(callback.name());
        mainLine.add(button);
        return mainLine;
    }

    public static List<List<InlineKeyboardButton>> getCartMarkup() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        buttonsOnTheRow.add(createButton(":pencil: Убрать товар из корзины",
                CallbackForMsg.DELETE_FROM_CART.name()));
        buttonsOnTheRow.add(createButton(":wastebasket: Очистить корзину",
                CallbackForMsg.DELETE_ALL_FROM_CART.name()));
        rows.add(buttonsOnTheRow);
        rows.add(getAnyLine(":rocket: Оформить доставку", CallbackForMsg.ORDERING.name()));
        rows.add(getBackPageLine(CallbackForMsg.CREATE_ORDER, "Назад"));
        return rows;
    }

    public static List<List<InlineKeyboardButton>> getGoodsInCartMarkup(String goods, int countElementsOnRows, List<GoodInCart> goodInCarts) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        String[] arrayGoods = goods.split("\n");
        for (int i = 0; i < goodInCarts.size(); i++) {
            var btn = createButton(arrayGoods[i], "DEL_GOOD_" + goodInCarts.get(i).getId());
            buttonsOnTheRow.add(btn);
            if (buttonsOnTheRow.size() == countElementsOnRows || i == goodInCarts.size() - 1) {
                rows.add(buttonsOnTheRow);
                buttonsOnTheRow = new ArrayList<>();
            }
        }
        rows.add(getAnyLine(":wastebasket: Очистить корзину",
                CallbackForMsg.DELETE_ALL_FROM_CART.name()));
        rows.add(getBackPageLine(CallbackForMsg.CART, "Назад к корзине"));
        return rows;
    }

    public static List<List<InlineKeyboardButton>> getOfferMarkup() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonsOnTheRow = new ArrayList<>();
        buttonsOnTheRow.add(createButton(":moneybag: Рассчитать сумму доставки",
                "YES"));
        buttonsOnTheRow.add(createButton(":o: Отменить заказ",
                "NO"));
        rows.add(buttonsOnTheRow);
        return rows;
    }
}
