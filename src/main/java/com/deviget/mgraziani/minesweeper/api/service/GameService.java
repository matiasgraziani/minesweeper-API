package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.repository.GameRepository;
import com.deviget.mgraziani.minesweeper.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GameService {

    public static final Long DEFAULT_PLAYER = 1L;
    public static final Integer DEFAULT_MINES_NUM = 10;
    public static final Integer DEFAULT_HORIZONTAL_SIZE = 8;
    public static final Integer DEFAULT_VERTICAL_SIZE = 8;

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

    public void checkFinishGame(Game game){
        //We check if we need to end the game
        if(game.getEnd() == null){
            long notChecked = game.getCells().stream()
                    .filter(cell -> !cell.getStatus().equals(MineStatus.Empty))
                    .filter(cell -> !cell.getStatus().equals(MineStatus.Value))
                    .count();
            if(notChecked == game.getMines().longValue()){
                game.setEnd(LocalDateTime.now());
            }
        }
        //If ended we flag the mines that haven't been flagged
        if(game.getEnd() != null){
            game.getCells().stream()
                    .filter(cell -> !cell.getStatus().equals(MineStatus.Empty))
                    .filter(cell -> !cell.getStatus().equals(MineStatus.Value))
                    .filter(cell -> !cell.getStatus().equals(MineStatus.Exploited))
                    .forEach(cell -> cell.setStatus(MineStatus.Flagged));
        }
    }
}
