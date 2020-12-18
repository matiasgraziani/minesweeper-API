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

    private Game diagonalGame() throws Exception {
        Game game = new Game(
                new Player("Test"),
                DEFAULT_HORIZONTAL_SIZE,
                DEFAULT_VERTICAL_SIZE,
                DEFAULT_MINES_NUM
        );
        for (Cell cell:game.getCells()) {
            if(cell.getHorizontal().equals(cell.getVertical())){
                cell.setMine(Boolean.TRUE);
            }else{
                cell.setMine(Boolean.FALSE);
            }
        }
        return game;
    }

    private Game mockGetGame(Game game){
        given(this.gameService.get(DEFAULT_PLAYER)).willReturn(Optional.of(game));
        return game;
    }

    private Game mockGetGame() throws Exception {
        return this.mockGetGame(defaultGame());
    }

    @Test
    /**
     * flagCell should change cell status to flagged
     */
    public void testFlagCell() throws Exception {
        mockGetGame();
        Game game = service.flagCell(DEFAULT_PLAYER, 2, 3);
        Cell cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Flagged, cell.getStatus());
    }

    @Test
    /**
     * flagCell should change cell status to Hided if already is flagged
     */
    public void testFlagCellInverted() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.Flagged);
        mockGetGame(game);

        game = service.flagCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Hided, cell.getStatus());
    }

    @Test
    /**
     * flagCell should change cell status to flagged if Question
     */
    public void testFlagCellWithQuestion() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.QuestionFlag);
        mockGetGame(game);

        game = service.flagCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Flagged, cell.getStatus());
    }

    @Test
    /**
     * flagCell should change cell status to flagged if Red Flagged
     */
    public void testFlagCellWithRedFlag() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.RedFlag);
        mockGetGame(game);

        game = service.flagCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Flagged, cell.getStatus());
    }

    @Test
    /**
     * questionCell should change cell status to question
     */
    public void testQuestionCell() throws Exception {
        mockGetGame();
        Game game = service.questionCell(DEFAULT_PLAYER, 2, 3);
        Cell cell = game.findCell(2,3).get();
        assertEquals(MineStatus.QuestionFlag, cell.getStatus());
    }

    @Test
    /**
     * questionCell should change cell status to hided if question
     */
    public void testQuestionCellInverted() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.QuestionFlag);
        mockGetGame(game);

        game = service.questionCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Hided, cell.getStatus());
    }

    @Test
    /**
     * questionCell should change cell status to question if flagged
     */
    public void testQuestionCellWithFlagged() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.Flagged);
        mockGetGame(game);

        game = service.questionCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.QuestionFlag, cell.getStatus());
    }

    @Test
    /**
     * questionCell should change cell status to question if red flagged
     */
    public void testQuestionCellWithRedFlag() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.RedFlag);
        mockGetGame(game);

        game = service.questionCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.QuestionFlag, cell.getStatus());
    }

    @Test
    /**
     * redFlagCell should change cell status to red flagged
     */
    public void testRedFlagCell() throws Exception {
        mockGetGame();
        Game game = service.redFlagCell(DEFAULT_PLAYER, 2, 3);
        Cell cell = game.findCell(2,3).get();
        assertEquals(MineStatus.RedFlag, cell.getStatus());
    }

    @Test
    /**
     * redFlagCell should change cell status to hided if red flagged
     */
    public void testRedFlagCellInverted() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.RedFlag);
        mockGetGame(game);

        game = service.redFlagCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.Hided, cell.getStatus());
    }

    @Test
    /**
     * redFlagCell should change cell status to red flagged if flagged
     */
    public void testRedFlagCellWithFlagged() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.Flagged);
        mockGetGame(game);

        game = service.redFlagCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.RedFlag, cell.getStatus());
    }

    @Test
    /**
     * redFlagCell should change cell status to red flagged if question
     */
    public void testRedFlagCellWithQuestion() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setStatus(MineStatus.QuestionFlag);
        mockGetGame(game);

        game = service.redFlagCell(DEFAULT_PLAYER, 2, 3);
        cell = game.findCell(2,3).get();
        assertEquals(MineStatus.RedFlag, cell.getStatus());
    }

    @Test
    /**
     * clickCell should change cell status to valued if cell has mine next to it
     */
    public void testClickCellValue() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setMine(Boolean.TRUE);
        cell = game.findCell(2,2).get();
        cell.setMine(Boolean.FALSE);
        mockGetGame(game);

        game = service.clickCell(DEFAULT_PLAYER, 2,2);
        cell = game.findCell(2,2).get();
        assertEquals(MineStatus.Value, cell.getStatus());
        assertNotEquals(0, cell.getAdjacentMines().intValue());
    }

    @Test
    /**
     * clickCell should change cell status to exploited and end game if has mine
     */
    public void testClickCellMine() throws Exception {
        Game game = defaultGame();
        Cell cell = game.findCell(2,3).get();
        cell.setMine(Boolean.FALSE);
        cell = game.findCell(2,2).get();
        cell.setMine(Boolean.TRUE);
        mockGetGame(game);

        game = service.clickCell(DEFAULT_PLAYER, 2,2);
        cell = game.findCell(2,2).get();
        assertEquals(MineStatus.Exploited, cell.getStatus());

        //Check game has finish
        assertNotNull(game.getEnd());
    }

    @Test
    /**
     * clickCell should change cell status to empty and check next mines in recursive way
     */
    public void testClickCellEmptyCell() throws Exception {
        mockGetGame(diagonalGame());
        Game game = service.clickCell(DEFAULT_PLAYER, 4,1);
        assertEquals(DEFAULT_HORIZONTAL_SIZE*DEFAULT_VERTICAL_SIZE, game.getCells().size());
        for (Cell cell:game.getCells()) {
            if(cell.getHorizontal().equals(4) && cell.getVertical().equals(1)){
                assertEquals(MineStatus.Empty, cell.getStatus());
            }
            else if(cell.getHorizontal().equals(3) && cell.getVertical().equals(1)){
                assertEquals(MineStatus.Value, cell.getStatus());
                assertEquals(1, cell.getAdjacentMines().intValue());
            }
            else if(cell.getHorizontal().equals(3) && cell.getVertical().equals(2)){
                assertEquals(MineStatus.Value, cell.getStatus());
                assertEquals(2, cell.getAdjacentMines().intValue());
            }
            else if(cell.getHorizontal().equals(4) && cell.getVertical().equals(2)){
                assertEquals(MineStatus.Value, cell.getStatus());
                assertEquals(1, cell.getAdjacentMines().intValue());
            }
            else if(cell.getHorizontal().equals(5) && cell.getVertical().equals(2)){
                assertEquals(MineStatus.Empty, cell.getStatus());
            }
            else if(cell.getHorizontal().equals(5) && cell.getVertical().equals(1)){
                assertEquals(MineStatus.Empty, cell.getStatus());
            } else {
                cell.getStatus().equals(MineStatus.Hided);
            }
        }
    }

    @Test
    /**
     * flagCell should not do nothing if cell doesn't exist
     */
    public void testFlagCellInvalid() throws Exception {
        mockGetGame();
        Game game = service.flagCell(DEFAULT_PLAYER, -1, -1);
        assertNotNull(game);
        assertFalse(game.findCell(-1, -1).isPresent());
        assertFalse(
                game.getCells().stream()
                        .filter(cell -> cell.getStatus().equals(MineStatus.Flagged))
                        .findFirst()
                        .isPresent()
        );
    }

    @Test
    /**
     * redFlagCell should not do nothing if cell doesn't exist
     */
    public void testRedFlagCellInvalid() throws Exception {
        mockGetGame();
        Game game = service.redFlagCell(DEFAULT_PLAYER, -1, -1);
        assertNotNull(game);
        assertFalse(game.findCell(-1, -1).isPresent());
        assertFalse(
                game.getCells().stream()
                        .filter(cell -> cell.getStatus().equals(MineStatus.RedFlag))
                        .findFirst()
                        .isPresent()
        );
    }

    @Test
    /**
     * questionCell should not do nothing if cell doesn't exist
     */
    public void testQuestionCellInvalid() throws Exception {
        mockGetGame();
        Game game = service.questionCell(DEFAULT_PLAYER, -1, -1);
        assertNotNull(game);
        assertFalse(game.findCell(-1, -1).isPresent());
        assertFalse(
                game.getCells().stream()
                        .filter(cell -> cell.getStatus().equals(MineStatus.QuestionFlag))
                        .findFirst()
                        .isPresent()
        );
    }

    @Test
    /**
     * clickCell should not do nothing if cell doesn't exist
     */
    public void testClickCellInvalid() throws Exception {
        mockGetGame();
        Game game = service.clickCell(DEFAULT_PLAYER, -1, -1);
        assertNotNull(game);
        assertFalse(game.findCell(-1, -1).isPresent());
        assertFalse(
                game.getCells().stream()
                        .filter(cell -> !cell.getStatus().equals(MineStatus.Hided))
                        .findFirst()
                        .isPresent()
        );
    }

}
