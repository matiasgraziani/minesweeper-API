package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CellService {

    @Autowired
    private GameService gameService;

    private final List<MineStatus> CAN_BE_FLAGGED = Arrays.asList(MineStatus.Hided, MineStatus.QuestionFlag,
            MineStatus.RedFlag, MineStatus.Flagged);

    @Transactional
    public Game flagCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Optional<Cell> cell = game.findCell(horizontal, vertical);
            if(cell.isPresent()){
                this.changeCellStatus(cell.get(), MineStatus.Flagged);
            }
            return game;
        }
        return null;
    }

    @Transactional
    public Game questionCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Optional<Cell> cell = game.findCell(horizontal, vertical);
            if(cell.isPresent())
                this.changeCellStatus(cell.get(), MineStatus.QuestionFlag);
            return game;
        }
        return null;
    }

    @Transactional
    public Game redFlagCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Optional<Cell> cell = game.findCell(horizontal, vertical);
            if(cell.isPresent())
                this.changeCellStatus(cell.get(), MineStatus.RedFlag);
            return game;
        }
        return null;
    }

    private void changeCellStatus(Cell cell, MineStatus mineStatus){
        if(CAN_BE_FLAGGED.contains(cell.getStatus()) && !cell.getStatus().equals(mineStatus)){
            cell.setStatus(mineStatus);
        }else if(cell.getStatus().equals(mineStatus)){
            cell.setStatus(MineStatus.Hided);
        }
    }

    @Transactional
    public Game clickCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        Game game = null;
        if(gameOptional.isPresent()){
            game = gameOptional.get();
            Optional<Cell> cellOptional = game.findCell(horizontal, vertical);
            if(cellOptional.isPresent() && CAN_BE_FLAGGED.contains(cellOptional.get().getStatus())){
                Cell cell = cellOptional.get();
                internalCellClick(game, cell, Boolean.TRUE);
            }
            gameService.checkFinishGame(game);
        }
        return game;
    }

    private void internalCellClick(Game game, Cell cell, Boolean original){
        //First check if we haven't select a mine
        if(cell.getMine()){
            // If not original call or flagged we end the game
            if(original && !cell.getStatus().equals(MineStatus.Flagged)){
                game.setEnd(LocalDateTime.now());
                List<Cell> cellList = game.getCells().stream()
                        .filter(c-> c.getStatus().equals(MineStatus.Hided))
                        .collect(Collectors.toList());
                cell.setStatus(MineStatus.Exploited);
                cellList.forEach(cell1 -> internalCellClick(game, cell1, Boolean.FALSE));
            }else{
                //We flag the mines we check once the game is ended
                if(game.getEnd() == null){
                    cell.setStatus(MineStatus.Flagged);
                }
            }
        }else{
            // If not a mine we check if is Hided
            if(cell.getStatus().equals(MineStatus.Hided)){
                int mineCount = game.getProximityMines(cell.getHorizontal(), cell.getVertical());
                // If has mines around we show how many
                if(mineCount != 0){
                    cell.setStatus(MineStatus.Value);
                    cell.setAdjacentMines(mineCount);
                }else{
                    // If doesn't have mines around we check on the mines next to it in a recursive way
                    List<Cell> cellList = game.getCells().stream().filter(c ->
                            c.getHorizontal() >= cell.getHorizontal() - 1 &&
                                    c.getHorizontal() <= cell.getHorizontal() + 1 &&
                                    c.getVertical() >= cell.getVertical() - 1 &&
                                    c.getVertical() <= cell.getVertical() + 1)
                            .filter(c -> c.getId() != cell.getId())
                            .filter(c -> c.getStatus().equals(MineStatus.Hided))
                            .collect(Collectors.toList());
                    cell.setStatus(MineStatus.Empty);
                    cellList.forEach(cell1 -> internalCellClick(game, cell1, Boolean.FALSE));
                }
            }
        }
    }
}
