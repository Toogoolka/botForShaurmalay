package com.shaurmalay.bot.services;

import com.shaurmalay.bot.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Vladislav Tugulev
 * @Date 25.10.2023
 */
@Service
@Slf4j
public class AdminCommands {
    private UserDao userDao;

    @Autowired
    public AdminCommands(UserDao userDao) {
        this.userDao = userDao;
    }
}
