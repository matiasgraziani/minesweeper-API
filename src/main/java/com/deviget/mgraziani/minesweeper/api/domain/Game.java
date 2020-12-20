package com.deviget.mgraziani.minesweeper.api.domain;

import com.deviget.mgraziani.minesweeper.api.exception.InvalidParamsException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "game")
public class Game {

    public static Integer MAX_SIZE = 20;

    public Game(){}

    public Game(Player player, Integer horizontalSize, Integer verticalSize, Integer mines) throws Exception{
        if(player == null||horizontalSize == null||horizontalSize < 1||
                verticalSize == null||verticalSize < 1||mines == null||mines < 1||
                mines > horizontalSize*verticalSize || horizontalSize > MAX_SIZE || verticalSize > 20
        ){
            throw new InvalidParamsException("Not valid parameters");
        }

        this.player = player;
        this.horizontalSize = horizontalSize;
        this.verticalSize = verticalSize;
        this.mines = mines;
    }

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

    private LocalDateTime start;

    private LocalDateTime end;

    private Boolean active = Boolean.TRUE;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="game_id")
    private Set<Cell> cells = new HashSet<>();

    public Optional<Cell> findCell(Integer horizontalSize, Integer verticalSize){
        return this.getCells().stream().filter(cell ->
                cell.getHorizontal().equals(horizontalSize) &&
                        cell.getVertical().equals(verticalSize))
                .findFirst();
    }

    public Integer getProximityMines(Integer horizontalSize, Integer verticalSize){
        long mineCount = this.getCells().stream().filter(cell ->
                cell.getHorizontal() >= horizontalSize - 1 &&
                        cell.getHorizontal() <= horizontalSize + 1 &&
                        cell.getVertical() >= verticalSize - 1 &&
                        cell.getVertical() <= verticalSize + 1)
                .filter(cell -> cell.getMine())
                .count();
        return (int)mineCount;
    }

    private Set<Integer> getRandomMinePositions(Integer horizontalSize, Integer verticalSize,
                                                Integer mines, Integer ignorePosition){
        Set<Integer> positions = new HashSet<>();
        Integer max = verticalSize * horizontalSize;
        while(positions.size() < mines) {
            Integer position = ThreadLocalRandom.current().nextInt(1, max+1);
            if(!position.equals(ignorePosition)){
                positions.add(position);
            }
        }
        return positions;
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

    public void start(Integer horizontal, Integer vertical){
        if(this.start == null){
            Integer originalPosition = getCurrentPosition(horizontal, vertical);
            Set<Integer> positions = getRandomMinePositions(this.horizontalSize, this.verticalSize, mines, originalPosition);
            this.createCells(positions);
            this.start = LocalDateTime.now();
        }
    }

    public Integer getCurrentPosition(Integer horizontal, Integer vertical){
        Integer fullColumn = (horizontal - 1) * this.verticalSize;
        Integer partialColumn = vertical;
        return fullColumn + partialColumn;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
