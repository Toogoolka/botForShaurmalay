package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends CrudRepository<Order, Long> {
    Optional<List<Order>> findAllByCustomer_ChatId(Long id);
    Optional<List<Order>> findAllByCustomer_ChatIdAndOrderStatus_Id(Long customerId, Long statusId);
    Optional<List<Order>> findDistinctByCustomer_ChatIdAndOrderStatus_IdAndAddresIsNotNull(Long customerId, Long statusId);
    Optional<List<Order>> findDistinctByCustomer_ChatIdAndOrderStatus_IdAndPhoneIsNotNull(Long customerId, Long statusId);
}
