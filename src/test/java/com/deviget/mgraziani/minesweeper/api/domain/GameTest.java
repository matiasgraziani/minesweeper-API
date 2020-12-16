package com.deviget.mgraziani.minesweeper.api.domain;

import com.deviget.mgraziani.minesweeper.api.exception.InvalidParamsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static com.deviget.mgraziani.minesweeper.api.service.GameService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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

        assertEquals(16, game.getCells().size());

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
    public void testGenerateCellMap() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );
        Map<String,Cell> map = game.generateCellMap();
        assertEquals(game.getCells().size(), map.keySet().size());
        Integer mines = 0;
        for (Cell cell:map.values()) {
            if(cell.getMine())
                mines++;
        }
        assertEquals(DEFAULT_MINES_NUM, mines);
    }
}
