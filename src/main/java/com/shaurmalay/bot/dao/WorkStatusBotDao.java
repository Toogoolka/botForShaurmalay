package com.shaurmalay.bot.dao;


import com.shaurmalay.bot.model.WorkStatusBot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkStatusBotDao extends CrudRepository<WorkStatusBot,Long> {
}
