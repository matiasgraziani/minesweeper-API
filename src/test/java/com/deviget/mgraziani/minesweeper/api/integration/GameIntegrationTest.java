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
import com.deviget.mgraziani.minesweeper.api.integration.util.BaseIntegrationTest;

import java.util.List;
import java.util.Optional;

import static com.deviget.mgraziani.minesweeper.api.TestDefaults.DEFAULT_PLAYER;
import static com.deviget.mgraziani.minesweeper.api.service.GameService.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameIntegrationTest extends BaseIntegrationTest {

    @Test
    /**
     * POST /game should create a new user game with default values
     */
    public void testCreate() throws Exception {
        createDefaultPlayer();
        ResultActions result = mockMvc.perform(put("/game/"+DEFAULT_PLAYER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        String content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);
    }

    @Test
    /**
     * POST /game should create a new user game with default values and invalidate previous ones
     */
    public void testCreateMultipleTimes() throws Exception {
        createDefaultPlayer();
        ResultActions result = mockMvc.perform(put("/game/"+DEFAULT_PLAYER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        String content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);

        result = mockMvc.perform(put("/game/"+DEFAULT_PLAYER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);

        result = mockMvc.perform(put("/game/"+DEFAULT_PLAYER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);

        result = mockMvc.perform(get("/game")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        content = result.andReturn().getResponse().getContentAsString();

        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, Game.class);
        List<Game> arrayOfGame = objectMapper.readValue(content, type);
        assertEquals(3, arrayOfGame.size());
        assertEquals(1, arrayOfGame.stream().filter(game -> game.getActive()).count());
    }

    @Test
    /**
     * POST /game should create a new user game with default values and invalidate previous ones
     */
    public void testCreateMultipleGameWithDifferentPlayers() throws Exception {
        //Player 1
        createDefaultPlayer();
        ResultActions result = mockMvc.perform(put("/game/"+DEFAULT_PLAYER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        String content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);

        //Player 2
        createDefaultPlayer("Player 2");
        result = mockMvc.perform(put("/game/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content, "Player 2");

        result = mockMvc.perform(get("/game")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        content = result.andReturn().getResponse().getContentAsString();

        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, Game.class);
        List<Game> arrayOfGame = objectMapper.readValue(content, type);

        //Check are only 2 games and both are active
        assertEquals(2, arrayOfGame.size());
        assertEquals(2, arrayOfGame.stream().filter(game -> game.getActive()).count());

        //Check one is for player 1
        assertEquals(
                1,
                arrayOfGame.stream()
                        .filter(game -> game.getPlayer().getName().equals("Player 1") && game.getActive())
                        .count()
        );
        //Check one is for player 2
        assertEquals(
                1,
                arrayOfGame.stream()
                        .filter(game -> game.getPlayer().getName().equals("Player 2") && game.getActive())
                        .count()
        );
    }

    @Test
    /**
     * GET /game should return an existing game for the user
     */
    public void testGetGame() throws Exception {
        createDefaultPlayer();
        mockMvc.perform(put("/game/"+DEFAULT_PLAYER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        ResultActions result = mockMvc.perform(get("/game/"+DEFAULT_PLAYER)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        String content = result.andReturn().getResponse().getContentAsString();
        checkDefaultGame(content);
    }

    private void checkDefaultGame(String content) throws JsonProcessingException {
        checkDefaultGame(content, "Player 1");
    }

    private void checkDefaultGame(String content, String playerName) throws JsonProcessingException {
        JavaType type = objectMapper.getTypeFactory().constructType(Game.class);
        Game game = objectMapper.readValue(content, type);

        assertNotNull(game);
        // Player
        Player player = game.getPlayer();
        assertNotNull(player);
        assertEquals(playerName, player.getName());
        // Game
        assertEquals(DEFAULT_HORIZONTAL_SIZE, game.getHorizontalSize());
        assertEquals(DEFAULT_VERTICAL_SIZE, game.getVerticalSize());
        assertEquals(DEFAULT_MINES_NUM, game.getMines());
        assertEquals(0, game.getCells().size());
        assertNull(game.getStart());
        assertNull(game.getEnd());
    }
}
