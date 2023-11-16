package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * @author Vladislav Tugulev
 * @Date 25.10.2023
 */
@Entity
@Table(name = "status")
@Data
public class StatusUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    @ManyToMany(mappedBy = "statuses")
    private List<User> users;

}
