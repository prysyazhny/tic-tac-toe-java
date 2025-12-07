package org.example;

public class Game {

    private String[][] board;
    private String currentPlayer = "X";
    private String winner = "";

    private boolean xIsComputer;
    private boolean oIsComputer;

    public Game(boolean xIsComputer, boolean oIsComputer) {
        this.xIsComputer = xIsComputer;
        this.oIsComputer = oIsComputer;
        resetBoard();
    }

    public Game() {
        this(false, false);
    }

    private void resetBoard() {
        this.board = new String[][] {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"},
        };
    }

    private void displayBoard(){
        System.out.println();
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                System.out.print("   " + board[i][j]);
            }
            System.out.println("\n");
        }
    }

    private boolean checkWin(){
        // rows
        if (board[0][0].equals(board[0][1]) && board[0][1].equals(board[0][2])) return true;
        if (board[1][0].equals(board[1][1]) && board[1][1].equals(board[1][2])) return true;
        if (board[2][0].equals(board[2][1]) && board[2][1].equals(board[2][2])) return true;
        // cols
        if (board[0][0].equals(board[1][0]) && board[1][0].equals(board[2][0])) return true;
        if (board[0][1].equals(board[1][1]) && board[1][1].equals(board[2][1])) return true;
        if (board[0][2].equals(board[1][2]) && board[1][2].equals(board[2][2])) return true;
        // diagonals
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) return true;
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) return true;
        return false;
    }

    private boolean isComputerTurn() {
        if ("X".equals(currentPlayer)) {
            return xIsComputer;
        } else {
            return oIsComputer;
        }
    }

    private boolean isCellTaken(int row, int col) {
        return "X".equals(board[row][col]) || "O".equals(board[row][col]);
    }

    private int countMoves() {
        int count = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (isCellTaken(i, j)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void setCurrentPlayer(String player) {
        this.currentPlayer = player;
    }

    public String getWinner() {
        return winner;
    }

    public void playGame(){
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        playGame(scanner);
    }

    public void playGame(java.util.Scanner scanner){
        while (true){
            displayBoard();
            boolean computerTurn = isComputerTurn();

            if (computerTurn) {
                System.out.println("Computer (" + currentPlayer + ") is making a move...");
                computerMove();
            } else {
                int move = askPlayerForMove(scanner);
                int row = (move - 1) / 3;
                int col = (move - 1) % 3;
                board[row][col] = currentPlayer;
            }

            if (checkWin()){
                displayBoard();
                if (computerTurn) {
                    System.out.println("Computer (" + currentPlayer + ") wins!");
                } else {
                    System.out.println("Player " + currentPlayer + " wins!");
                }
                winner = currentPlayer;  
                break;
            }

            if (countMoves() == 9){
                displayBoard();
                System.out.println("It's a draw!");
                winner = "D";          
                break;
            }

            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
    }

    private int askPlayerForMove(java.util.Scanner scanner) {
        while (true) {
            System.out.print("Player " + currentPlayer + ", enter your move (1-9): ");
            String moveInput = scanner.nextLine().trim();

            if (!moveInput.matches("[1-9]")){ 
                System.out.println("Invalid input. Try again.");
                continue;
            }

            int move = Integer.parseInt(moveInput);
            int row = (move - 1) / 3;
            int col = (move - 1) % 3;

            if (isCellTaken(row, col)){
                System.out.println("Cell already taken. Try again.");
                continue;
            }

            return move;
        }
    }

    private void computerMove() {
        int totalMoves = countMoves();

        if (totalMoves == 0) {
            int[][] corners = {
                {0, 0},
                {0, 2},
                {2, 0},
                {2, 2}
            };
            java.util.Random rand = new java.util.Random();
            while (true) {
                int[] choice = corners[rand.nextInt(corners.length)];
                int r = choice[0];
                int c = choice[1];
                if (!isCellTaken(r, c)) {
                    board[r][c] = currentPlayer;
                    return;
                }
            }
        }

        if (totalMoves == 1 && !isCellTaken(1, 1)) {
            board[1][1] = currentPlayer;
            return;
        }

        if (tryWinningMove()) {
            return;
        }

        if (tryBlockingMove()) {
            return;
        }

        randomMove();
    }

    private boolean tryWinningMove() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (!isCellTaken(i, j)) {
                    String original = board[i][j];
                    board[i][j] = currentPlayer;
                    boolean win = checkWin();
                    if (win) {
                        return true;
                    }
                    board[i][j] = original;
                }
            }
        }
        return false;
    }

    private boolean tryBlockingMove() {
        String opponent = currentPlayer.equals("X") ? "O" : "X";

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (!isCellTaken(i, j)) {
                    String original = board[i][j];
                    board[i][j] = opponent;
                    boolean opponentWouldWin = checkWin();
                    board[i][j] = original;

                    if (opponentWouldWin) {
                        board[i][j] = currentPlayer;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void randomMove() {
        java.util.Random rand = new java.util.Random();
        int emptyCount = 0;

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (!isCellTaken(i, j)) {
                    emptyCount++;
                }
            }
        }

        if (emptyCount == 0) {
            return; 
        }

        int choiceIndex = rand.nextInt(emptyCount);
        int currentIndex = 0;

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (!isCellTaken(i, j)) {
                    if (currentIndex == choiceIndex) {
                        board[i][j] = currentPlayer;
                        return;
                    }
                    currentIndex++;
                }
            }
        }
    }
}
