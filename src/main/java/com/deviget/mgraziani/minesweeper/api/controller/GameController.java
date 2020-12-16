package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.dto.RequestCellDTO;
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

    @PostMapping(value = "cell/flag",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> flagCell(@RequestBody RequestCellDTO cell){
        Game game = gameService.flagCell(cell.getHorizontal(), cell.getVertical());
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }

    @PostMapping(value = "cell/question",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> questionCell(@RequestBody RequestCellDTO cell){
        Game game = gameService.questionCell(cell.getHorizontal(), cell.getVertical());
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }


    @PostMapping(value = "cell/red-flag",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> redFlagCell(@RequestBody RequestCellDTO cell){
        Game game = gameService.redFlagCell(cell.getHorizontal(), cell.getVertical());
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }
}
