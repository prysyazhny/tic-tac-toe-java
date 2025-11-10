package org.example;

public class App {

  public String getGreeting() {
    return "\nWelcome to Tic-Tac-Toe!\n";
  }

  public static void main(String[] args) {

    boolean playGame = true;

    int xWins = 0;
    int oWins = 0;
    int ties = 0;
    int gamesPlayed = 0;

    String nextStartingPlayer = "X";

    System.out.println(new App().getGreeting());

    while (playGame) {
      Game game = new Game();

      game.setCurrentPlayer(nextStartingPlayer);

      game.playGame();

      String result = game.getWinner();
      gamesPlayed++;

      if ("X".equals(result)) {
        xWins++;
        nextStartingPlayer = "O";
      } else if ("O".equals(result)) {
        oWins++;
        nextStartingPlayer = "X";
      } else { 
        ties++;
      }

      System.out.println("\n----- Game Statistics -----");
      System.out.println("Games played : " + gamesPlayed);
      System.out.println("Player X wins: " + xWins);
      System.out.println("Player O wins: " + oWins);
      System.out.println("Ties         : " + ties);
      System.out.println("---------------------------\n");

      java.util.Scanner scanner = new java.util.Scanner(System.in);
      System.out.println("\nDo you want to play again? (y/n): ");
      String response = scanner.nextLine();

      if (!response.equalsIgnoreCase("y")) {
        playGame = false;
      } 
    }

    try (java.io.PrintWriter out =
             new java.io.PrintWriter(new java.io.FileWriter("game_log.txt"))) {
      out.println("---- Tic-Tac-Toe Game Log ----");
      out.println("Games played : " + gamesPlayed);
      out.println("Player X wins: " + xWins);
      out.println("Player O wins: " + oWins);
      out.println("Ties         : " + ties);
      out.println("------------------------------");
    } catch (java.io.IOException e) {
      System.out.println("Could not write game log: " + e.getMessage());
    }

    System.out.println("\nGoodbye!\n");
  }
}
