package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<GoodInCart> goodIncarts;

    public void addGoodInCart(GoodInCart goodInCart) {
        if(goodIncarts == null)
            goodIncarts = new ArrayList<>();
        goodIncarts.add(goodInCart);
    }
}
