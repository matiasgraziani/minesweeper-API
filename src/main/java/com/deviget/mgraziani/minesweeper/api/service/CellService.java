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

    private final List<MineStatus> CAN_BE_FLAGGED = Arrays.asList(MineStatus.Hided, MineStatus.QuestionFlag, MineStatus.RedFlag, MineStatus.Flagged);

    @Transactional
    public Game flagCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Cell cell = game.generateCellMap().get(horizontal + "-" + vertical);
            this.changeCellStatus(cell, MineStatus.Flagged);
            return game;
        }
        return null;
    }

    @Transactional
    public Game questionCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Cell cell = game.generateCellMap().get(horizontal + "-" + vertical);
            this.changeCellStatus(cell, MineStatus.QuestionFlag);
            return game;
        }
        return null;
    }

    @Transactional
    public Game redFlagCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Cell cell = game.generateCellMap().get(horizontal + "-" + vertical);
            this.changeCellStatus(cell, MineStatus.RedFlag);
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

    public Game clickCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Cell cell = game.generateCellMap().get(horizontal + "-" + vertical);
            cell.setStatus(MineStatus.Empty);
            return game;
        }
        return null;
    }
}
