package com.shaurmalay.bot.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Vladislav Tugulev
 * @Date 25.10.2023
 */
@Data
@Component
public class Cart {
    private List<Good> goods = new ArrayList<>();
    private int sum = 0;
    private int counterPositions = 0;

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
    public void incrementCounter() {
        counterPositions++;
    }
}
