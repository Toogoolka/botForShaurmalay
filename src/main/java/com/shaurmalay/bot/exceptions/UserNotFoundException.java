package com.shaurmalay.bot.exceptions;

/**
 * @author Vladislav Tugulev
 * @Date 14.11.2023
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Пользователь не найден");
    }

}
