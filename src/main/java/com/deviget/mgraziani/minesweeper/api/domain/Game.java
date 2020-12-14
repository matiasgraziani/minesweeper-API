package com.deviget.mgraziani.minesweeper.api.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="player_id", nullable=false)
    private Player player;

    @Column(name = "vertical_size")
    private Integer verticalSize;

    @Column(name = "horizontal_size")
    private Integer horizontalSize;

    private Integer mines;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="game_id")
    private Set<Cell> cells = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVerticalSize() {
        return verticalSize;
    }

    public void setVerticalSize(Integer verticalSize) {
        this.verticalSize = verticalSize;
    }

    public Integer getHorizontalSize() {
        return horizontalSize;
    }

    public void setHorizontalSize(Integer horizontalSize) {
        this.horizontalSize = horizontalSize;
    }

    public Integer getMines() {
        return mines;
    }

    public void setMines(Integer mines) {
        this.mines = mines;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Cell> getCells() {
        return cells;
    }

    public void setCells(Set<Cell> cells) {
        this.cells = cells;
    }
}
