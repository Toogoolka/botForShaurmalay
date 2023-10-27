package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Buff;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
public interface BuffDao extends CrudRepository<Buff, Long> {
}
