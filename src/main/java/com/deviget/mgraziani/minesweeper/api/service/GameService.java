package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.repository.GameRepository;
import com.deviget.mgraziani.minesweeper.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GameService {

    public static final Long DEFAULT_PLAYER = 1L;
    public static final Integer DEFAULT_MINES_NUM = 4;
    public static final Integer DEFAULT_HORIZONTAL_SIZE = 4;
    public static final Integer DEFAULT_VERTICAL_SIZE = 4;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Game create() throws Exception {
        Player player = playerRepository.findById(DEFAULT_PLAYER).get();
        Game game = new Game(
                player,
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );
        gameRepository.save(game);
        return game;
    }

    public Optional<Game> get() {
        return gameRepository.findById(DEFAULT_PLAYER);
    }

    @Transactional
    public Game flagCell(Integer horizontal, Integer vertical) {
        Optional<Game> gameOptional = this.get();
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
        Optional<Game> gameOptional = this.get();
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
        Optional<Game> gameOptional = this.get();
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            Cell cell = game.generateCellMap().get(horizontal + "-" + vertical);
            cell.setStatus(MineStatus.RedFlag);
            return game;
        }
        return null;
    }

}
