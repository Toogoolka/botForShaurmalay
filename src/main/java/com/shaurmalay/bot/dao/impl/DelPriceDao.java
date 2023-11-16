package com.shaurmalay.bot.dao.impl;

import com.shaurmalay.bot.model.DelPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DelPriceDao extends CrudRepository<DelPrice,Long> {
    Optional<DelPrice> findByCallBack(String callback);

}
