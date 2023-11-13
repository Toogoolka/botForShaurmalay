package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Buff;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
public interface BuffDao extends CrudRepository<Buff, Long> {
    Buff getBuffByCallback(String callback);
}
