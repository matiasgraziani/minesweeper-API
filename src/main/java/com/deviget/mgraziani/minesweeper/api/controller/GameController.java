package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.dto.InitialGameDTO;
import com.deviget.mgraziani.minesweeper.api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PutMapping(value="/{userId}", produces = "application/json")
    public ResponseEntity<Game> create(@PathVariable("userId") Long userId) throws Exception {
        return new ResponseEntity<Game>(gameService.create(userId), HttpStatus.CREATED);
    }

    @PostMapping(value="/{userId}", produces = "application/json")
    public ResponseEntity<Game> create(
            @PathVariable("userId") Long userId,
            @RequestBody InitialGameDTO gameDTO
    ) throws Exception {
        return new ResponseEntity<Game>(
                gameService.create(
                        userId,
                        gameDTO.getHorizontalSize(),
                        gameDTO.getVerticalSize(),
                        gameDTO.getMines()
                ),
                HttpStatus.CREATED);
    }

    @GetMapping(value="/{userId}", produces = "application/json")
    public ResponseEntity<Set<Game>> get(@PathVariable("userId") Long userId){
        return new ResponseEntity<Set<Game>>(gameService.list(userId), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Game>> listAll(){
        return new ResponseEntity<List<Game>>(gameService.listAllGames(), HttpStatus.OK);
    }

    @PutMapping(value="/switch/{userId}/{gameId}", produces = "application/json")
    public ResponseEntity<Game> switchGame(
            @PathVariable("userId") Long userId, @PathVariable("gameId") Long gameId) throws Exception {
        Game game = gameService.switchGame(userId, gameId);
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }
}
