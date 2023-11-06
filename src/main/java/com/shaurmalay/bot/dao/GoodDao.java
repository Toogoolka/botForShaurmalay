package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Good;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
public interface GoodDao extends CrudRepository<Good, Long> {
    Optional<List<Good>> getAllByType_Id(Long id);
    Good getGoodByCallBack(String callback);
}
