package com.shaurmalay.bot.dao.impl;

import com.shaurmalay.bot.model.Order;
import com.shaurmalay.bot.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;


    public List<Order> findAllByCustomerIDAndStatusId(User customer, Long statusId) {
        String hql = "FROM Order o JOIN o.orderStatus s WHERE o.customer = :customer AND s.id = :statusId";

        List<Order> result = entityManager.createQuery(hql)
                .setParameter("customer", customer)
                .setParameter("statusId", statusId)
                .getResultList();
        return result;
    }
}
