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
    @Column
    private int sum;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Good> goods;
    @Column(name = "user_id")
    private Long userId;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<GoodInCart> goodIncarts;

    public int calculateSum() {

        sum = goods.stream()
                .mapToInt(good -> good.getPrice())
                .sum();
        sum += goods.stream()
                .filter(good -> good.getBuffList() != null && !good.getBuffList().isEmpty())
                .flatMap(good -> good.getBuffList().stream())
                .mapToInt(Buff::getPrice)
                .sum();
        return sum;
    }
    public void addGoodInCart(GoodInCart goodInCart) {
        if(goodIncarts == null)
            goodIncarts = new ArrayList<>();
        goodIncarts.add(goodInCart);
    }
    public void deleteGoodInCart(GoodInCart goodInCart) {
        goodIncarts.remove(goodInCart);
    }

}
