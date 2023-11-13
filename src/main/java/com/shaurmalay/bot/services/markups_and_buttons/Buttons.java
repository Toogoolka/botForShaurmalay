package com.shaurmalay.bot.services.markups_and_buttons;


import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


@Slf4j
public class Buttons {

    public static InlineKeyboardButton createBtn(String emoji, String textOnBtn, CallbackForMsg callbackForMsg) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(emoji + " " + textOnBtn));
        button.setCallbackData(callbackForMsg.name());
        return button;
    }

    public static InlineKeyboardButton getCartBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":shopping_cart: Корзина"));
        button.setCallbackData(CallbackForMsg.CART.name());
        return button;
    }
    public static InlineKeyboardButton getMainPageBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":rocket: Главное меню"));
        button.setCallbackData(CallbackForMsg.MAIN_PAGE.name());
        return button;
    }
    public static InlineKeyboardButton getMyOrdersBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":bookmark_tabs: История заказов"));
        button.setCallbackData(CallbackForMsg.MY_ORDERS.name());
        return button;
    }
    public static InlineKeyboardButton getCreateOrderBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":rocket: Сделать заказ"));
        button.setCallbackData(CallbackForMsg.CREATE_ORDER.name());
        return button;
    }

    public static InlineKeyboardButton getShaurmaBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":burrito: Шаурма"));
        button.setCallbackData(CallbackForMsg.SHAURMAS.name());
        return button;
    }

    public static InlineKeyboardButton getDrinksBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":cup_with_straw: Напитки"));
        button.setCallbackData(CallbackForMsg.DRINKS.name());
        return button;
    }
    public static InlineKeyboardButton getStartersBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":fries: В прикуску"));
        button.setCallbackData(CallbackForMsg.STARTERS.name());
        return button;
    }
    public static InlineKeyboardButton getConfirmBuffBtn() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(EmojiParser.parseToUnicode(":white_check_mark: Шаурма готова"));
        button.setCallbackData(CallbackForMsg.ADD_TO_CART.name());
        return button;
    }
}
