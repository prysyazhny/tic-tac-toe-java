package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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

    Scanner scanner = new Scanner(System.in);

    System.out.println(new App().getGreeting());

    while (playGame) {

      int gameMode = 0;
      while (true) {
        System.out.println("What kind of game would you like to play?\n");
        System.out.println("1. Human vs. Human");
        System.out.println("2. Human vs. Computer");
        System.out.println("3. Computer vs. Human\n");
        System.out.print("What is your selection? ");

        String input = scanner.nextLine().trim();
        try {
          gameMode = Integer.parseInt(input);
          if (gameMode >= 1 && gameMode <= 3) {
            break;
          }
        } catch (NumberFormatException e) {
        }
        System.out.println("That is not a valid selection! Try again.\n");
      }

      boolean xIsComputer = false;
      boolean oIsComputer = false;

      if (gameMode == 1) {
        System.out.println("\nGreat! Human vs. Human.\n");
      } else if (gameMode == 2) {
        xIsComputer = false;
        oIsComputer = true;
        System.out.println("\nGreat! You will go first and the computer will be O.\n");
      } else if (gameMode == 3) {
        xIsComputer = true;
        oIsComputer = false;
        System.out.println("\nGreat! The computer will go first and will be X.\n");
      }

      Game game = new Game(xIsComputer, oIsComputer);

      if (!xIsComputer && !oIsComputer) {
        game.setCurrentPlayer(nextStartingPlayer);
        System.out.println("Player " + nextStartingPlayer + " will start.\n");
      } else {
        game.setCurrentPlayer("X");
      }

      game.playGame(scanner);

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

      System.out.print("Do you want to play again? (y/n): ");
      String response = scanner.nextLine().trim();

      if (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
        playGame = false;
      } 
    }

    try (PrintWriter out = new PrintWriter(new FileWriter("game_log.txt"))) {
      out.println("---- Tic-Tac-Toe Game Log ----");
      out.println("Games played : " + gamesPlayed);
      out.println("Player X wins: " + xWins);
      out.println("Player O wins: " + oWins);
      out.println("Ties         : " + ties);
      out.println("------------------------------");
    } catch (IOException e) {
      System.out.println("Could not write game log: " + e.getMessage());
    }

    System.out.println("\nGoodbye!\n");
    scanner.close();
  }
}
