package com.deviget.mgraziani.minesweeper.api.domain;

import javax.persistence.*;

@Entity
@Table(name = "player")
public class Player {

    public Player() {}

    public Player(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
