package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    public PlayerRepository playerRepository;

    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }


    public Player save(Player player) {
        return playerRepository.save(player);
    }
}
