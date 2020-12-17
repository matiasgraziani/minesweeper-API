package com.deviget.mgraziani.minesweeper.api.integration;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.dto.RequestCellDTO;
import com.deviget.mgraziani.minesweeper.api.integration.util.BaseIntegrationTest;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.deviget.mgraziani.minesweeper.api.TestDefaults.DEFAULT_PLAYER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
    }

}
