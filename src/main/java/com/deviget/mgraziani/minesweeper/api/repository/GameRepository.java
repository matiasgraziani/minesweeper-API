package com.deviget.mgraziani.minesweeper.api.repository;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long>{

}
