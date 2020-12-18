package com.deviget.mgraziani.minesweeper.api.integration;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.dto.RequestCellDTO;
import com.deviget.mgraziani.minesweeper.api.integration.util.BaseIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.deviget.mgraziani.minesweeper.api.TestDefaults.DEFAULT_PLAYER;
import static com.deviget.mgraziani.minesweeper.api.service.GameService.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CellIntegrationTest extends BaseIntegrationTest {
    @Test
    /**
     * GET /cell/flag should return the game board with the cell flagged
     */
    public void testCellFlagged() throws Exception {
        createDefaultPlayer();
        createDefaultGame();
        RequestCellDTO cell = new RequestCellDTO();
        cell.setHorizontal(1);
        cell.setVertical(2);
        cell.setUserId(DEFAULT_PLAYER);
        ResultActions result = mockMvc.perform(post("/cell/flag")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(cell)))
                .andExpect(status().isOk());

        String content = result.andReturn().getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructType(Game.class);
        Game game = objectMapper.readValue(content, type);

        Cell response_cell = game.findCell(1,2).get();
        assertEquals(response_cell.getStatus(), MineStatus.Flagged);

        response_cell = game.findCell(2,1).get();
        assertNotEquals(response_cell.getStatus(), MineStatus.Flagged);

        response_cell = game.findCell(1,1).get();
        assertNotEquals(response_cell.getStatus(), MineStatus.Flagged);

        //Check after the click/flag the game was started correctly
        checkDefaultGameStarted(game);
    }

    @Test
    /**
     * GET /cell/question should return the game board with the cell flagged question
     */
    public void testCellQuestion() throws Exception {
        createDefaultPlayer();
        createDefaultGame();
        RequestCellDTO cell = new RequestCellDTO();
        cell.setHorizontal(1);
        cell.setVertical(2);
        cell.setUserId(DEFAULT_PLAYER);
        ResultActions result = mockMvc.perform(post("/cell/question")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(cell)))
                .andExpect(status().isOk());

        String content = result.andReturn().getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructType(Game.class);
        Game game = objectMapper.readValue(content, type);

        Cell response_cell = game.findCell(1,2).get();
        assertEquals(response_cell.getStatus(), MineStatus.QuestionFlag);

        response_cell = game.findCell(2,1).get();
        assertNotEquals(response_cell.getStatus(), MineStatus.QuestionFlag);

        response_cell = game.findCell(1,1).get();
        assertNotEquals(response_cell.getStatus(), MineStatus.QuestionFlag);

        //Check after the click/flag the game was started correctly
        checkDefaultGameStarted(game);
    }

    @Test
    /**
     * GET /cell/red-flag should return the game board with the cell flagged red
     */
    public void testCellRedFlagged() throws Exception {
        createDefaultPlayer();
        createDefaultGame();
        RequestCellDTO cell = new RequestCellDTO();
        cell.setHorizontal(1);
        cell.setVertical(2);
        cell.setUserId(DEFAULT_PLAYER);
        ResultActions result = mockMvc.perform(post("/cell/red-flag")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(cell)))
                .andExpect(status().isOk());


        String content = result.andReturn().getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructType(Game.class);
        Game game = objectMapper.readValue(content, type);

        Cell response_cell = game.findCell(1,2).get();
        assertEquals(response_cell.getStatus(), MineStatus.RedFlag);

        response_cell = game.findCell(2,1).get();
        assertNotEquals(response_cell.getStatus(), MineStatus.RedFlag);

        response_cell = game.findCell(1,1).get();
        assertNotEquals(response_cell.getStatus(), MineStatus.RedFlag);

        //Check after the click/flag the game was started correctly
        checkDefaultGameStarted(game);
    }

    private void checkDefaultGameStarted(Game game) throws JsonProcessingException {
        assertNotNull(game);
        // Player
        Player player = game.getPlayer();
        assertNotNull(player);
        assertEquals("Player 1", player.getName());
        // Game
        assertEquals(DEFAULT_HORIZONTAL_SIZE, game.getHorizontalSize());
        assertEquals(DEFAULT_VERTICAL_SIZE, game.getVerticalSize());
        assertEquals(DEFAULT_MINES_NUM, game.getMines());
        assertEquals(DEFAULT_VERTICAL_SIZE*DEFAULT_HORIZONTAL_SIZE, game.getCells().size());
        assertNotNull(game.getStart());
        assertNull(game.getEnd());

        // Cells
        int count = 0;
        for (int h = 1; h <= DEFAULT_HORIZONTAL_SIZE; h++) {
            for (int v = 1; v <= DEFAULT_VERTICAL_SIZE; v++) {
                int finalV = v;
                int finalH = h;
                Optional<Cell> cellOptional = game.getCells().stream()
                        .filter(c ->
                                c.getVertical() == finalV && c.getHorizontal() == finalH)
                        .findFirst();
                assertTrue(cellOptional.isPresent());
                Cell cell = cellOptional.get();
                if(cell.getMine()){
                    count++;
                }
            }
        }
        assertEquals(count, game.getMines().intValue());
    }
}
