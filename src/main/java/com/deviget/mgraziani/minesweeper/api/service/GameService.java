package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GameService {

    public static final Integer DEFAULT_MINES_NUM = 10;
    public static final Integer DEFAULT_HORIZONTAL_SIZE = 8;
    public static final Integer DEFAULT_VERTICAL_SIZE = 8;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerService playerService;

    public Game create(Long userId) throws Exception {
        return create(userId, DEFAULT_HORIZONTAL_SIZE, DEFAULT_VERTICAL_SIZE, DEFAULT_MINES_NUM);
    }

    public Game create(Long userId, Integer horizontalSize, Integer verticalSize, Integer mines) throws Exception {
        Player player = playerService.findById(userId).get();

        //First I disable any previous game
        Optional<Game> gameOptional = this.getCurrent(player);
        if(gameOptional.isPresent()){
            Game game = gameOptional.get();
            game.setActive(Boolean.FALSE);
            gameRepository.save(game);
        }

        Game game = new Game(
                player,
                horizontalSize,
                verticalSize,
                mines
        );
        gameRepository.save(game);
        return game;
    }

    public Set<Game> list(Long playerId) {
        Player player = playerService.findById(playerId).get();
        return this.list(player);
    }

    public Set<Game> list(Player player) {
        return gameRepository.findAllByPlayer(player);
    }

    public Optional<Game> getCurrent(Long playerId) {
        Player player = playerService.findById(playerId).get();
        return this.getCurrent(player);
    }

    public Optional<Game> getCurrent(Player player) {
        return gameRepository.findByPlayerAndActiveTrue(player);
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
        game.setActive(Boolean.FALSE);
    }

    public List<Game> listAllGames() {
        return gameRepository.findAll();
    }

    @Transactional
    public Game switchGame(Long userId, Long gameId) {
        Set<Game> games = list(userId);
        games.forEach(game -> game.setActive(Boolean.FALSE));
        Optional<Game> gameOptional = games.stream()
                .filter(game -> game.getId().equals(gameId))
                .filter(game -> game.getEnd() == null)
                .findFirst();
        Game game = null;
        if(gameOptional.isPresent()){
            game = gameOptional.get();
            game.setActive(Boolean.TRUE);
        }
        return game;
    }
}
