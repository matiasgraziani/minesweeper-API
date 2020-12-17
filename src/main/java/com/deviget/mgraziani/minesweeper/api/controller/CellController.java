package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.dto.RequestCellDTO;
import com.deviget.mgraziani.minesweeper.api.service.CellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cell")
public class CellController {

    @Autowired
    private CellService cellService;

    @PostMapping(value = "flag",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> flagCell(@RequestBody RequestCellDTO cell){
        Game game = cellService.flagCell(cell.getUserId(), cell.getHorizontal(), cell.getVertical());
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }

    @PostMapping(value = "question",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> questionCell(@RequestBody RequestCellDTO cell){
        Game game = cellService.questionCell(cell.getUserId(), cell.getHorizontal(), cell.getVertical());
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }

    @PostMapping(value = "red-flag",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> redFlagCell(@RequestBody RequestCellDTO cell){
        Game game = cellService.redFlagCell(cell.getUserId(), cell.getHorizontal(), cell.getVertical());
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }

    @PostMapping(value = "click",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Game> clickCell(@RequestBody RequestCellDTO cell){
        Game game = cellService.clickCell(cell.getUserId(), cell.getHorizontal(), cell.getVertical());
        if(game == null){
            return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Game>(game, HttpStatus.OK);
        }
    }
}
