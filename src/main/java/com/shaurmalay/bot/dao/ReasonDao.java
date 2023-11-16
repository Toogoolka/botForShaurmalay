package com.shaurmalay.bot.dao;

import com.shaurmalay.bot.model.Reason;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReasonDao extends CrudRepository<Reason, Long> {
    Optional<Reason> findByCallback(String callback);
}
