package com.deviget.mgraziani.minesweeper.api;

import com.deviget.mgraziani.minesweeper.api.domain.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

import static org.junit.Assert.assertEquals;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinesweeperApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class PlayerTest {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateUser() throws Exception {
        Player player = new Player();
        player.setName("Matias");
        ResultActions result = mockMvc.perform(post("/player").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated());
        String content = result.andReturn().getResponse().getContentAsString();
        assertEquals(content, "{\"id\":2,\"name\":\"Matias\"}");
    }

    @Test
    public void testGetUser() throws Exception {
        Player player = new Player();
        player.setName("Matias");
        ResultActions result = mockMvc.perform(post("/player").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated());
        ResultActions result2 = mockMvc.perform(get("/player/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        String content = result2.andReturn().getResponse().getContentAsString();
        assertEquals(content, "{\"id\":1,\"name\":\"Matias\"}");
    }

}
