package com.shaurmalay.bot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "reasons")
public class Reason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String callback;

    public Reason(String name) {
        this.name = name;
    }

    public Reason() {
    }
}
