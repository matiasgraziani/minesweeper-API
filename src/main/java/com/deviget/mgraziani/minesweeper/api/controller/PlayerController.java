package com.deviget.mgraziani.minesweeper.api.controller;

import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    public PlayerRepository playerRepository;

    @GetMapping(produces = "application/json", value="/{id}")
    public ResponseEntity<Player> get(@PathVariable("id") Long id){
        if (playerRepository.findById(id).isPresent()) {
            return new ResponseEntity<Player>(playerRepository.findById(id).get(), HttpStatus.OK);
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
        playerRepository.save(player);
        return new ResponseEntity<Player>(player, HttpStatus.CREATED);
    }
}
