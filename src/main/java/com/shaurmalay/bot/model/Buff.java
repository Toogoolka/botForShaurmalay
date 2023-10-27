package com.shaurmalay.bot.model;


import jakarta.persistence.*;
import lombok.Data;


/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Entity
@Table(name = "ingridients")
@Data
public class Buff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    private String callbackData;

    @Override
    public String toString() {
        return name;
    }
}
