package com.shaurmalay.bot.dao.impl;

import com.shaurmalay.bot.model.Good;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 25.10.2023
 */
@Repository
public class GoodDaoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Good> getAllShaurmas() {
        String hql = "FROM Good as g JOIN g.type as gt WHERE gt.typeName = :name";
        Query query = entityManager.createQuery(hql).setParameter("name", "Shaurma");

        List<Good> shaurmas = query.getResultList();
        return shaurmas.isEmpty() ? null : shaurmas;
    }
}
