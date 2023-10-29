package com.shaurmalay.bot.model;


import jakarta.persistence.*;
import lombok.Data;


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
    @Transient
    private List<Buff> buffList;
    @ManyToOne
    @JoinColumn(name = "typeId", referencedColumnName = "id")
    private GoodTypes type;
    private String emoji;
}
