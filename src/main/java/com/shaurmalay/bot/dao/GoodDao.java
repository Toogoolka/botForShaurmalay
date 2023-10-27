package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Good;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
public interface GoodDao extends CrudRepository<Good, Long> {
    List<String> getAllByCallbackData(String callBackData);
}
