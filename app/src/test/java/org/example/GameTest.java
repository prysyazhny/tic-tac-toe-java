package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private boolean invokeCheckWin(Game game) throws Exception {
        Method m = Game.class.getDeclaredMethod("checkWin");
        m.setAccessible(true);
        return (boolean) m.invoke(game);
    }

    private void setBoard(Game game, String[][] board) throws Exception {
        Field f = Game.class.getDeclaredField("board");
        f.setAccessible(true);
        f.set(game, board);
    }

    @Test
    void emptyBoardHasNoWinner() throws Exception {
        Game g = new Game();
        assertFalse(invokeCheckWin(g));
    }

    @Test
    void rowWinIsDetected() throws Exception {
        Game g = new Game();
        setBoard(g, new String[][]{
                {"X", "X", "X"},
                {"4", "5", "6"},
                {"7", "8", "9"}
        });
        assertTrue(invokeCheckWin(g));
    }

    @Test
    void columnWinIsDetected() throws Exception {
        Game g = new Game();
        setBoard(g, new String[][]{
                {"O", "2", "3"},
                {"O", "5", "6"},
                {"O", "8", "9"}
        });
        assertTrue(invokeCheckWin(g));
    }

    @Test
    void diagonalWinIsDetected() throws Exception {
        Game g = new Game();
        setBoard(g, new String[][]{
                {"X", "2", "3"},
                {"4", "X", "6"},
                {"7", "8", "X"}
        });
        assertTrue(invokeCheckWin(g));
    }

    @Test
    void noWinOnMixedBoard() throws Exception {
        Game g = new Game();
        setBoard(g, new String[][]{
                {"X", "O", "X"},
                {"O", "X", "O"},
                {"O", "X", "O"}
        });
        assertFalse(invokeCheckWin(g));
    }

    @Test
    void playGameSetsWinnerToX() {
        String moves = String.join("\n", "1", "4", "2", "5", "3") + "\n";
        System.setIn(new ByteArrayInputStream(moves.getBytes(StandardCharsets.UTF_8)));

        Game g = new Game();
        g.setCurrentPlayer("X");  // X should start
        g.playGame();

        assertEquals("X", g.getWinner(), "X should be the winner after taking 1,2,3");
    }
}
