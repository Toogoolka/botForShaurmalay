package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.StatusUser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Vladislav Tugulev
 * @Date 25.10.2023
 */
public interface StatusUserDao extends CrudRepository<StatusUser, Long> {
    StatusUser findByStatus(String nameStatus);

}
