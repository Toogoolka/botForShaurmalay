package com.shaurmalay.bot.dao;


import com.shaurmalay.bot.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
public interface UserDao extends CrudRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);
    void deleteByChatId(Long chatId);
}
