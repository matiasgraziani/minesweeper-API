package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PutMapping(produces = "application/json")
    public ResponseEntity<Game> create() throws Exception {
        return new ResponseEntity<Game>(gameService.create(), HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Game> get(){
        Optional<Game> gameOptional = gameService.get();
        if (gameOptional.isPresent()) {
            return new ResponseEntity<Game>(gameOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }
    }

}
