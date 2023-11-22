package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amountItems;
    private String addres;
    private String phone;
    private String positions;
    private int orderSum;
    private LocalDateTime orderedAt;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statusId", referencedColumnName = "id")
    private OrderStatus orderStatus;
    private Long paymentId;
    private String linkToPay;
    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "chat_id")
    private User customer;

}
