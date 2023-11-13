package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Buff;
import com.shaurmalay.bot.model.Cart;
import com.shaurmalay.bot.model.GoodInCart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodInCartDao extends CrudRepository<GoodInCart,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM GoodInCart g WHERE g.id = :goodInCartId AND :buff MEMBER OF g.buffs")
    void removeBuffFromGoodInCart(@Param("goodInCartId") Long goodInCartId, @Param("buff") Buff buff);

    Optional<List<GoodInCart>> findAllByCart(Cart cart);
    void deleteAllByCart_Id(Long cartId);
    void deleteById(Long id);
    Optional<GoodInCart> findById(Long id);
}
