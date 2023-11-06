package com.shaurmalay.bot.services.markups_and_buttons;

import com.shaurmalay.bot.model.Buff;
import com.shaurmalay.bot.model.Good;
import com.shaurmalay.bot.model.callbacks.CallbackForMsg;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 29.10.2023
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
        rows.add(getBackPageLine(CallbackForMsg.ADD_TO_CART, "Ничего не нужно"));
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
        rows.add(getBackPageLine(CallbackForMsg.CREATE_ORDER,"Назад"));
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
        List<InlineKeyboardButton> cartLine = new ArrayList<>();
        cartLine.add(Buttons.getConfirmBuffBtn());
        return cartLine;
    }
    public static List<InlineKeyboardButton> getMainPageLine() {
        List<InlineKeyboardButton> mainLine = new ArrayList<>();
        mainLine.add(Buttons.getMainPageBtn());
        return mainLine;
    }
    public static List<InlineKeyboardButton> getBackPageLine(CallbackForMsg callback,String text) {
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
        rows.add(getBackPageLine(CallbackForMsg.CREATE_ORDER, "Назад"));
        return rows;
    }

}
