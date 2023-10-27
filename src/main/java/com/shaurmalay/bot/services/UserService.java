package com.shaurmalay.bot.services;

import com.shaurmalay.bot.dao.StatusUserDao;
import com.shaurmalay.bot.dao.UserDao;
import com.shaurmalay.bot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Service
@Slf4j
public class UserService {
    private UserDao userDao;
    private StatusUserDao statusUserDao;

    @Autowired
    public UserService(UserDao userDao, StatusUserDao statusUserDao) {
        this.userDao = userDao;
        this.statusUserDao = statusUserDao;
    }
    @Transactional
    public void registerUser(Message message) {
        if (userDao.findByChatId(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();
            User user = new User();

            user.setChatId(chatId);
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            user.setStatuses(Collections.singletonList(statusUserDao.findByStatus("user")));
            userDao.save(user);
            log.info("SAVE TO DATABASE: " + user);
        }
    }

    @Transactional
    public void deleteUser(Long chatId) {
        userDao.deleteByChatId(chatId);
    }
}
