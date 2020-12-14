package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> create(){
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
