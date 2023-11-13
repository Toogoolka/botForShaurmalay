package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



/**
 * @author Vladislav Tugulev
 * @Date 05.11.2023
 */
@Repository
public interface CartDao extends CrudRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
