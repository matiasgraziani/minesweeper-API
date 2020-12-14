package com.deviget.mgraziani.minesweeper.api.integration;

import com.deviget.mgraziani.minesweeper.api.domain.Cell;
import com.deviget.mgraziani.minesweeper.api.domain.Game;
import com.deviget.mgraziani.minesweeper.api.domain.MineStatus;
import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import com.deviget.mgraziani.minesweeper.api.util.BaseTest;

import java.util.Optional;

import static com.deviget.mgraziani.minesweeper.api.service.GameService.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameIntegrationTest extends BaseTest {

    public void createDefaultPlayer() throws Exception {
        Player player = new Player();
        player.setName("Matias");
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated());
    }

    @Test
    /**
     * POST /game should create a new user game with default values
     */
    public void testCreate() throws Exception {
        createDefaultPlayer();
        ResultActions result = mockMvc.perform(put("/game")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        String content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);
    }

    @Test
    /**
     * GET /game should return an existing game for the user
     */
    public void testGetGame() throws Exception {
        createDefaultPlayer();
        mockMvc.perform(put("/game")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        ResultActions result = mockMvc.perform(get("/game")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        String content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);
    }

    private void checkDefaultGame(String content) throws JsonProcessingException {
        JavaType type = objectMapper.getTypeFactory().constructType(Game.class);
        Game game = objectMapper.readValue(content, type);

        assertNotNull(game);
        // Player
        Player player = game.getPlayer();
        assertNotNull(player);
        assertEquals(1L, player.getId().longValue());
        assertEquals("Matias", player.getName());
        // Game
        assertEquals(DEFAULT_HORIZONTAL_SIZE, game.getHorizontalSize());
        assertEquals(DEFAULT_VERTICAL_SIZE, game.getVerticalSize());
        assertEquals(DEFAULT_MINES_NUM, game.getMines());
        assertEquals(16, game.getCells().size());
        // Cells
        for (Cell cell:game.getCells()) {
        }
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
                assertEquals(MineStatus.None, cell.getStatus());
                if(cell.getMine()){
                    count++;
                }
            }
        }
        assertEquals(count, game.getMines().intValue());

    }
}
