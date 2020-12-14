package com.deviget.mgraziani.minesweeper.api.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    /**
     * getRandomMinePositions should return a set of 4 random values between 1 and 16
     */
    public void testGetRandomMinePositions(){
        Set<Integer> positions = gameService.getRandomMinePositions();
        assertEquals(4, positions.size());
        for (Integer pos:positions) {
            assertTrue(pos > 0);
            assertTrue(pos < 17);
        }
    }

    @Test
    /**
     * createCells should set mines on the correct cells
     */
    public void testMinesSetCorrectlyOnGame(){

    }
}
