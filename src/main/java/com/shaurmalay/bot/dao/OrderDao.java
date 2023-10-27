package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Order;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
public interface OrderDao extends CrudRepository<Order, Long> {
}
