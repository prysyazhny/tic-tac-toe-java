package org.example;

public class Game {

    private String[][] board = {
        {"1", "2", "3"},
        {"4", "5", "6"},
        {"7", "8", "9"},
    };

    private String currentPlayer = "X";
    private String winner = "";

    private void displayBoard(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                System.out.print("   " + board[i][j] + "");
            }
            System.out.println("\n");
        }
    }

    private boolean checkWin(){
        // probably covers all cases
        if (board[0][0].equals(board[0][1]) && board[0][1].equals(board[0][2])) return true;
        if (board[1][0].equals(board[1][1]) && board[1][1].equals(board[1][2])) return true;
        if (board[2][0].equals(board[2][1]) && board[2][1].equals(board[2][2])) return true;
        if (board[0][0].equals(board[1][0]) && board[1][0].equals(board[2][0])) return true;
        if (board[0][1].equals(board[1][1]) && board[1][1].equals(board[2][1])) return true;
        if (board[0][2].equals(board[1][2]) && board[1][2].equals(board[2][2])) return true;
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) return true;
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) return true;
        return false;
    }

    public void setCurrentPlayer(String player) {
        this.currentPlayer = player;
    }

    public String getWinner() {
        return winner;
    }

    public void playGame(){
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int moves = 0;
        while (true){
            displayBoard();
            System.out.println("Player " + currentPlayer + ", enter your move (1-9): ");
            String moveInput = scanner.nextLine();

            // make sure input is valid
            if (!moveInput.matches("[1-9]")){ 
                System.out.println("Invalid input. Try again.");
                continue;
            }
            int move = Integer.parseInt(moveInput);
            if (move < 1 || move > 9){
                System.out.println("Invalid move. Try again.");
                continue;
            }
            int row = (move - 1) / 3;
            int col = (move - 1) % 3;
            if (board[row][col].equals("X") || board[row][col].equals("O")){
                System.out.println("Cell already taken. Try again.");
                continue;
            }
            board[row][col] = currentPlayer;
            moves++;

            if (checkWin()){
                displayBoard();
                System.out.println("Player " + currentPlayer + " wins!");
                winner = currentPlayer;  
                break;
            }
            if (moves == 9){
                displayBoard();
                System.out.println("It's a draw!");
                winner = "D";          
                break;
            }
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
    }
}
