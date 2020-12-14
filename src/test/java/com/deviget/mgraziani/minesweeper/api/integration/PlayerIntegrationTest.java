package com.deviget.mgraziani.minesweeper.api.integration;

import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.deviget.mgraziani.minesweeper.api.util.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import static org.junit.Assert.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlayerIntegrationTest extends BaseTest {

    @Test
    /**
     * POST /player should create a new user with posted values
     */
    public void testCreateUser() throws Exception {
        Player player = new Player();
        player.setName("Matias");
        ResultActions result = mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated());
        String content = result.andReturn().getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"name\":\"Matias\"}", content);
    }

    @Test
    /**
     * POST /player should return Bad Request(400) if params are not send
     */
    public void testCreateUserBadRequest() throws Exception {
        mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    /**
     * GET /player/{id} should return a player
     */
    public void testGetUser() throws Exception {
        Player player = new Player();
        player.setName("Matias");
        ResultActions result = mockMvc.perform(post("/player")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated());
        ResultActions result2 = mockMvc.perform(get("/player/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        String content = result2.andReturn().getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"name\":\"Matias\"}", content);
    }

    @Test
    /**
     * GET /player/{id} should return not found (404) if user doesn't exist
     */
    public void testGetUserFail() throws Exception {
        mockMvc.perform(get("/player/32124")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

}
