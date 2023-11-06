package com.shaurmalay.bot.dao.impl;

import com.shaurmalay.bot.dao.CartDao;
import com.shaurmalay.bot.model.Cart;
import com.shaurmalay.bot.model.Good;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Vladislav Tugulev
 * @Date 05.11.2023
 */
@Repository
public class CartDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Good> getGoodsByCartId(Long cartId) {
        String hql = "SELECT c.goods From Cart as c WHERE c.id = :cartId";
        Optional<List<Good>> goods = Optional.ofNullable(entityManager.createQuery(hql)
                .setParameter("cartId", cartId).getResultList());

        if (goods.isEmpty()) {
            return new ArrayList<>();
        }
        return goods.get();
    }

    public int getSumInCart(Long id) {
        String hql = "SELECT SUM(g.price) + SUM(b.price) as total FROM Cart c " +
                "JOIN c.goods g " +
                "JOIN g.buffList b " +
                "WHERE c.id = :cartId " +
                "GROUP BY c.id";
        BigDecimal sum = (BigDecimal) entityManager.createQuery(hql).setParameter("cartId", id).getSingleResult();
        return sum.intValue();
    }
}
