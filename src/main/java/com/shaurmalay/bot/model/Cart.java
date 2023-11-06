package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Vladislav Tugulev
 * @Date 25.10.2023
 */
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
    public void addToCart(Good good) {
        goods.add(good);
    }

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
    public void deleteFromCart(Good good) {
        goods.remove(good);
    }

}
