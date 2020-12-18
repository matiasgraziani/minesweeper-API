package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.deviget.mgraziani.minesweeper.api.TestDefaults.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService service;

    @Test
    /**
     * checkFinishGame should don't change things if end hasn't finish
     */
    public void testCheckFinishGameContinue() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );

        service.checkFinishGame(game);
        assertNull(game.getEnd());
        assertNotEquals(0,
                game.getCells().stream().filter(cell -> cell.getStatus().equals(MineStatus.Hided)).count());
    }

    @Test
    /**
     * checkFinishGame should adjust all cells to show the value/mine it has ans set the end time
     */
    public void testCheckFinishGameEnd() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );
        //We do this to only leave Hide the ones with mines
        game.getCells().stream().filter(cell -> !cell.getMine())
                .forEach(cell -> cell.setStatus(MineStatus.Empty));

        service.checkFinishGame(game);
        assertNotNull(game.getEnd());
        assertEquals(0,
                game.getCells().stream().filter(cell -> cell.getStatus().equals(MineStatus.Hided)).count());
        assertEquals(DEFAULT_MINES_NUM.longValue(),
                game.getCells().stream().filter(cell -> cell.getStatus().equals(MineStatus.Flagged)).count());
    }

    @Test
    /**
     * checkFinishGame should adjust all cells to show the value/mine it has
     */
    public void testCheckFinishGameLostGame() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );
        //We do this to only leave Hide the ones with mines
        game.getCells().stream().filter(cell -> !cell.getMine())
                .forEach(cell -> cell.setStatus(MineStatus.Empty));
        game.getCells().stream().filter(cell -> cell.getMine())
                .forEach(cell -> cell.setStatus(MineStatus.Flagged));
        game.getCells().stream().filter(cell -> cell.getMine()).findFirst().get().setStatus(MineStatus.Exploited);

        service.checkFinishGame(game);
        assertNotNull(game.getEnd());
        assertEquals(0,
                game.getCells().stream().filter(cell -> cell.getStatus().equals(MineStatus.Hided)).count());
        assertEquals(DEFAULT_MINES_NUM.longValue() -1,
                game.getCells().stream().filter(cell -> cell.getStatus().equals(MineStatus.Flagged)).count());
    }
}
