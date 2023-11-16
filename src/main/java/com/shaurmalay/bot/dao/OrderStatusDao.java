package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusDao extends CrudRepository<OrderStatus,Long> {
    Optional<OrderStatus> findById(Long id);
}
