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

    private String[][] getBoard(Game game) throws Exception {
        Field f = Game.class.getDeclaredField("board");
        f.setAccessible(true);
        return (String[][]) f.get(game);
    }

    private void invokeComputerMove(Game game) throws Exception {
        Method m = Game.class.getDeclaredMethod("computerMove");
        m.setAccessible(true);
        m.invoke(game);
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

    @Test
    void computerFirstMoveIsCorner() throws Exception {
        Game g = new Game(true, false); // X is computer
        g.setCurrentPlayer("X");

        invokeComputerMove(g);

        String[][] board = getBoard(g);

        int cornerCount = 0;
        if ("X".equals(board[0][0])) cornerCount++;
        if ("X".equals(board[0][2])) cornerCount++;
        if ("X".equals(board[2][0])) cornerCount++;
        if ("X".equals(board[2][2])) cornerCount++;

        assertEquals(1, cornerCount, "Computer should take exactly one corner on the first move.");
    }

    @Test
    void computerSecondMoveTakesCenterIfAvailable() throws Exception {
        Game g = new Game(false, true); // O is computer

        // One move already played (X in top-left), center is free
        setBoard(g, new String[][]{
                {"X", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"}
        });

        g.setCurrentPlayer("O"); // computer's turn, totalMoves == 1

        invokeComputerMove(g);

        String[][] board = getBoard(g);

        assertEquals("O", board[1][1],
                "On the second move, the computer should take the center if it is free.");
    }

    @Test
    void computerTakesWinningMoveWhenAvailable() throws Exception {
        Game g = new Game(true, false); // X is computer

        // X can win by taking (0,2)
        setBoard(g, new String[][]{
                {"X", "X", "3"},
                {"4", "O", "6"},
                {"7", "8", "9"}
        });

        g.setCurrentPlayer("X");

        invokeComputerMove(g);

        String[][] board = getBoard(g);

        assertEquals("X", board[0][2],
                "Computer should complete the row to win when possible.");
    }

    @Test
    void computerBlocksOpponentWinningMove() throws Exception {
        Game g = new Game(true, false); // X is computer, O is human

        // O is about to win on the top row; X must block at (0,2)
        setBoard(g, new String[][]{
                {"O", "O", "3"},
                {"4", "X", "6"},
                {"7", "8", "9"}
        });

        g.setCurrentPlayer("X");

        invokeComputerMove(g);

        String[][] board = getBoard(g);

        assertEquals("X", board[0][2],
                "Computer should block the opponent's winning move.");
    }

    @Test
    void invalidInputIsRejectedAndGameStillPlays() {
        // "a" (invalid), "10" (out of range), then valid winning sequence for X: 1,4,2,5,3
        String moves = String.join("\n", "a", "10", "1", "4", "2", "5", "3") + "\n";
        System.setIn(new ByteArrayInputStream(moves.getBytes(StandardCharsets.UTF_8)));

        Game g = new Game();
        g.setCurrentPlayer("X");
        g.playGame();

        assertEquals("X", g.getWinner(),
                "Game should ignore invalid inputs and still let X win.");
    }
}
