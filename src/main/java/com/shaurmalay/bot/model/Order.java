package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amountItems;
    private String addres;
    private String positions;
    private int orderSum;
    private LocalDateTime orderedAt;
    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "chat_id")
    private User customer;

}
