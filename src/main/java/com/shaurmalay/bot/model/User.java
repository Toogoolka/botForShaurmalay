package com.shaurmalay.bot.model;

import jakarta.persistence.*;

import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;


import java.sql.Timestamp;
import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 24.10.2023
 */
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    @ManyToMany
    @JoinTable(name = "status_users",
    joinColumns = @JoinColumn(name = "userChatId"),
    inverseJoinColumns = @JoinColumn(name = "statusId"))
    private List<StatusUser> statuses;
    @OneToMany(mappedBy = "customer")
    private List<Order> orderList;
    private Timestamp registeredAt;
}
