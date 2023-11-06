package com.shaurmalay.bot.model;


import jakarta.persistence.*;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Entity
@Table(name = "goods")
@Data
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    @OneToMany(mappedBy = "good",fetch = FetchType.EAGER)
    private List<Buff> buffList;
    @ManyToOne
    @JoinColumn(name = "typeId", referencedColumnName = "id")
    private GoodTypes type;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private String emoji;
    private String callBack;

    @Override
    public String toString() {
        String s = name;
        if (buffList != null) {
            s = name + " (" +
                    buffList +
                    ")";
        }
        return s;
    }
    public void addToBuffList(Buff buff) {
        if (buffList == null) {
            buffList = new ArrayList<>();
            buffList.add(buff);
            return;
        }
        buffList.add(buff);
    }
}
