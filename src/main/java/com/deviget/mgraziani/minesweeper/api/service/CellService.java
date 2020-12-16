package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CellService {

    @Autowired
    private GameService gameService;

    @Transactional
    public Game flagCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = gameService.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Cell cell = game.generateCellMap().get(horizontal + "-" + vertical);
            cell.setStatus(MineStatus.Flagged);
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
            cell.setStatus(MineStatus.QuestionFlag);
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
            cell.setStatus(MineStatus.RedFlag);
            return game;
        }
        return null;
    }

}
