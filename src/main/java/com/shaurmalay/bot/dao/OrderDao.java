package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
public interface OrderDao extends CrudRepository<Order, Long> {
    Optional<List<Order>> findAllByCustomer_ChatId(Long id);
}
