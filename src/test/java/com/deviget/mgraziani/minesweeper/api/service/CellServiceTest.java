package com.deviget.mgraziani.minesweeper.api.service;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.deviget.mgraziani.minesweeper.api.TestDefaults.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CellServiceTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private CellService service;

    private Game defaultGame() throws Exception {
        return new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );
    }

    private Game mockGetGame(Game game){
        given(this.gameService.get()).willReturn(Optional.of(game));
        return game;
    }

    private Game mockGetGame() throws Exception {
        return this.mockGetGame(defaultGame());
    }

    @Test
    public void testFlagCell() throws Exception {
        mockGetGame();
        Game game = service.flagCell(2, 3);
        Cell cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Flagged, cell.getStatus());
    }

    @Test
    public void testFlagCellInverted() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.Flagged);
        mockGetGame(game);

        game = service.flagCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Hided, cell.getStatus());
    }

    @Test
    public void testFlagCellWithQuestion() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.QuestionFlag);
        mockGetGame(game);

        game = service.flagCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Flagged, cell.getStatus());
    }

    @Test
    public void testFlagCellWithRedFlag() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.RedFlag);
        mockGetGame(game);

        game = service.flagCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Flagged, cell.getStatus());
    }

    @Test
    public void testQuestionCell() throws Exception {
        mockGetGame();
        Game game = service.questionCell(2, 3);
        Cell cell = game.findCell(2,3).get();
        assertEquals(MineStatus.QuestionFlag, cell.getStatus());
    }

    @Test
    public void testQuestionCellInverted() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.QuestionFlag);
        mockGetGame(game);

        game = service.questionCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Hided, cell.getStatus());
    }

    @Test
    public void testQuestionCellWithFlagged() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.Flagged);
        mockGetGame(game);

        game = service.questionCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.QuestionFlag, cell.getStatus());
    }

    @Test
    public void testQuestionCellWithRedFlag() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.RedFlag);
        mockGetGame(game);

        game = service.questionCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.QuestionFlag, cell.getStatus());
    }

    @Test
    public void testRedFlagCell() throws Exception {
        mockGetGame();
        Game game = service.redFlagCell(2, 3);
        Cell cell = game.findCell(2,3).get();
        assertEquals(MineStatus.RedFlag, cell.getStatus());
    }

    @Test
    public void testRedFlagCellInverted() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.RedFlag);
        mockGetGame(game);

        game = service.redFlagCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Hided, cell.getStatus());
    }

    @Test
    public void testRedFlagCellWithFlagged() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.Flagged);
        mockGetGame(game);

        game = service.redFlagCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.RedFlag, cell.getStatus());
    }

    @Test
    public void testRedFlagCellWithQuestion() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.QuestionFlag);
        mockGetGame(game);

        game = service.redFlagCell(2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.RedFlag, cell.getStatus());
    }

    @Test
    public void testClickCellValue() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setMine(Boolean.TRUE);
        cell = game.findCell(2,2).get();
        cell.setMine(Boolean.FALSE);
        mockGetGame(game);

        game = service.clickCell(2,2);
        cell = game.findCell(2,2).get();
        assertEquals(MineStatus.Value, cell.getStatus());
        assertNotEquals(0, cell.getAdjacentMines().intValue());
    }

    @Test
    public void testClickCellMine() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setMine(Boolean.FALSE);
        cell = game.findCell(2,2).get();
        cell.setMine(Boolean.TRUE);
        mockGetGame(game);

        game = service.clickCell(2,2);
        cell = game.findCell(2,2).get();
        assertEquals(MineStatus.Exploited, cell.getStatus());

        //Check game has finish
        assertNotNull(game.getEnd());
    }

    @Test
    public void testClickCellEmpty() throws Exception {
        //TODO
    }
}