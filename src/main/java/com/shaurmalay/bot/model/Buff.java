package com.shaurmalay.bot.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Entity
@Table(name = "ingredients")
@Data
public class Buff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    private String callback;
    @ManyToOne
    @JoinColumn(name = "good_id")
    private Good good;
    @Override
    public String toString() {
        return name;
    }
    @ManyToMany(mappedBy = "buffs")
    List<GoodInCart> goodIncarts;
}
