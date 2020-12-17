package com.deviget.mgraziani.minesweeper.api.domain;

import com.deviget.mgraziani.minesweeper.api.exception.InvalidParamsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.deviget.mgraziani.minesweeper.api.TestDefaults.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameTest {

    @Test
    public void testCreateGameSuccess() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );

        assertEquals("Test", game.getPlayer().getName());
        assertEquals(DEFAULT_HORIZONTAL_SIZE, game.getHorizontalSize());
        assertEquals(DEFAULT_VERTICAL_SIZE, game.getVerticalSize());
        assertEquals(DEFAULT_MINES_NUM, game.getMines());
        assertNotNull(game.getStart());
        assertNull(game.getEnd());

        assertEquals(DEFAULT_HORIZONTAL_SIZE*DEFAULT_VERTICAL_SIZE, game.getCells().size());

        Integer mines = 0;
        for (Cell cell:game.getCells()) {
            if(cell.getMine())
                mines++;
        }
        assertEquals(DEFAULT_MINES_NUM, mines);
    }

    @Test
    public void testCreateGameEmpty() {
        assertThrows(InvalidParamsException.class, ()-> {
            Game game = new Game(
                    null,
                    null,
                    null,
                    null
            );
        });
    }

    @Test
    public void testGetProximityMines() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                1
        );
        //Remove all mines
        game.getCells().stream().forEach(cell -> cell.setMine(Boolean.FALSE));

        Cell cell = game.findCell(1,1).get();
        cell.setMine(Boolean.TRUE);

        cell = game.findCell(2,2).get();
        cell.setMine(Boolean.TRUE);

        cell = game.findCell(1,3).get();
        cell.setMine(Boolean.TRUE);

        cell = game.findCell(2,1).get();
        cell.setMine(Boolean.TRUE);

        // This should not count
        cell = game.findCell(1,4).get();
        cell.setMine(Boolean.TRUE);

        int mines = game.getProximityMines(1, 2);

        assertEquals(4, mines);
    }

    @Test
    public void testGetProximityMinesEmpty() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                1
        );
        //Remove all mines
        game.getCells().stream().forEach(cell -> cell.setMine(Boolean.FALSE));
        int mines = game.getProximityMines(1, 2);

        assertEquals(0, mines);
    }

    @Test
    public void testFindCell() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );

        assertTrue(game.findCell(2,2).isPresent());
    }

    @Test
    public void testFindCellNotFound() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );

        assertFalse(game.findCell(5,7).isPresent());
    }

}
