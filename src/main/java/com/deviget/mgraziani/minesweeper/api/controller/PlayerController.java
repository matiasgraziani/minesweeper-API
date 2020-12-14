package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    public PlayerService playerService;

    @GetMapping(produces = "application/json", value="/{id}")
    public ResponseEntity<Player> get(@PathVariable("id") Long id){
        Optional<Player> playerOptional = playerService.findById(id);
        if (playerOptional.isPresent()) {
            return new ResponseEntity<Player>(playerOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = "application/json")
    public ResponseEntity<Player> create(@RequestBody Player player){
        if(player == null){
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        player.setId(null);
        playerService.save(player);
        return new ResponseEntity<Player>(player, HttpStatus.CREATED);
    }
}
