package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PutMapping(value="/{userId}", produces = "application/json")
    public ResponseEntity<Game> create(@PathVariable("userId") Long userId) throws Exception {
        return new ResponseEntity<Game>(gameService.create(userId), HttpStatus.CREATED);
    }

    @GetMapping(value="/{userId}", produces = "application/json")
    public ResponseEntity<Game> get(@PathVariable("userId") Long userId){
        Optional<Game> gameOptional = gameService.get(userId);
        if (gameOptional.isPresent()) {
            return new ResponseEntity<Game>(gameOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Game>> list(){
        return new ResponseEntity<List<Game>>(gameService.list(), HttpStatus.OK);
    }

}
