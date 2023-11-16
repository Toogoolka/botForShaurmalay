package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class DelPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private int price;
    private String callBack;
}
