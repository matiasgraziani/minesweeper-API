package com.deviget.mgraziani.minesweeper.api.dto;

public class RequestCellDTO {
    private Integer horizontal;
    private Integer vertical;

    public Integer getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Integer horizontal) {
        this.horizontal = horizontal;
    }

    public Integer getVertical() {
        return vertical;
    }

    public void setVertical(Integer vertical) {
        this.vertical = vertical;
    }
}
