package com.deviget.mgraziani.minesweeper.api.repository;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface GameRepository extends JpaRepository<Game, Long>{
    Optional<Game> findByPlayerAndActiveTrue(Player player);
    Set<Game> findAllByPlayer(Player player);
}
