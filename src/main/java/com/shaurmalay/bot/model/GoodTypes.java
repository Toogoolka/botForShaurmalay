package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Entity
@Table(name = "good_types")
@Data
public class GoodTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String typeName;
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Good> goods;
}
