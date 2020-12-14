package com.deviget.mgraziani.minesweeper.api.domain;

import javax.persistence.*;

@Entity
public class Cell {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Integer vertical;

    private Integer horizontal;

    private Boolean mine;

    private MineStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVertical() {
        return vertical;
    }

    public void setVertical(Integer vertical) {
        this.vertical = vertical;
    }

    public Integer getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Integer horizontal) {
        this.horizontal = horizontal;
    }

    public Boolean getMine() {
        return mine;
    }

    public void setMine(Boolean mine) {
        this.mine = mine;
    }

    public MineStatus getStatus() {
        return status;
    }

    public void setStatus(MineStatus status) {
        this.status = status;
    }

}
