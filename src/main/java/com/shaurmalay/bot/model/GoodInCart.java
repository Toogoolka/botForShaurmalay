package com.shaurmalay.bot.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "good_in_cart")
public class GoodInCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goodCallbacck;
    @ManyToMany
    @JoinTable(name = "buffsForGood",
            joinColumns = @JoinColumn(name = "goodInCartId"),
            inverseJoinColumns = @JoinColumn(name = "buffId"))
    private List<Buff> buffs;
    @ManyToOne
    @JoinColumn(name = "cartId", referencedColumnName = "id")
    private Cart cart;

    public boolean addBuff(Buff buff) {
        if(buffs == null)
            buffs = new ArrayList<>();
        if (buffs.size() >= 3) {
            return false;
        }
        buffs.add(buff);
        return true;
    }

    public void deleteAllBuffs() {
        if(buffs == null)
            buffs = new ArrayList<>();
        buffs.clear();
    }

    public void deleteLastBuff() {
        if(buffs == null || buffs.isEmpty()) {
            buffs = new ArrayList<>();
            return;
        }
        buffs.remove(buffs.get(buffs.size() -1));
    }
}
