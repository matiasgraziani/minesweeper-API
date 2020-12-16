package com.deviget.mgraziani.minesweeper.api.domain;

import com.deviget.mgraziani.minesweeper.api.exception.InvalidParamsException;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Entity
public class Game {

    public Game(){}

    public Game(Player player, Integer horizontalSize, Integer verticalSize, Integer mines) throws Exception{
        if(player == null||horizontalSize == null||horizontalSize < 1||
                verticalSize == null||verticalSize < 1||mines == null||mines < 1||
                mines > horizontalSize*verticalSize
        ){
            throw new InvalidParamsException("Not valid parameters");
        }

        this.player = player;
        this.horizontalSize = horizontalSize;
        this.verticalSize = verticalSize;
        this.mines = mines;
        Set<Integer> positions = getRandomMinePositions(horizontalSize, verticalSize, mines);
        this.createCells(positions);
    }

    private void createCells(Set<Integer> positions){
        int count = 1;
        for (int h = 1; h <= this.getHorizontalSize(); h++) {
            for (int v = 1; v <= this.getVerticalSize(); v++) {
                Cell cell = new Cell();
                cell.setHorizontal(h);
                cell.setVertical(v);
                cell.setStatus(MineStatus.Hided);
                if(positions.contains(count)){
                    cell.setMine(Boolean.TRUE);
                }else{
                    cell.setMine(Boolean.FALSE);
                }
                count++;
                this.getCells().add(cell);
            }
        }
    }

    private Set<Integer> getRandomMinePositions(Integer verticalSize, Integer horizontalSize, Integer mines){
        Set<Integer> positions = new HashSet<>();
        Integer max = verticalSize * horizontalSize;
        while(positions.size() < mines) {
            positions.add(ThreadLocalRandom.current().nextInt(1, max+1));
        }
        return positions;
    }

    @Transient
    private Map cellMap = new HashMap<String, Cell>();

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

    public Map<String, Cell> generateCellMap(){
        if (cellMap.isEmpty()){
            for (Cell cell:this.getCells()) {
                cellMap.put(cell.getHorizontal() + "-" + cell.getVertical(), cell);
            }
        }
        return cellMap;
    }

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
