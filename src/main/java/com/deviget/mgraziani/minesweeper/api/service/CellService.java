package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Optional<Cell> cellOptional = game.findCell(horizontal, vertical);
            if(cellOptional.isPresent() && CAN_BE_FLAGGED.contains(cellOptional.get().getStatus())){
                Cell cell = cellOptional.get();
                if(cell.getMine()){
                    //FIXME GAME OVER
                }else{
                    int mineCount = game.getProximityMines(cell.getHorizontal(), cell.getVertical());
                    if(mineCount != 0){
                        cell.setStatus(MineStatus.Value);
                        cell.setAdjacentMines(mineCount);
                    }else{
                        // TODO propagate to other cells
                    }
                }
                return game;
            }
        }
        return null;
    }
}
