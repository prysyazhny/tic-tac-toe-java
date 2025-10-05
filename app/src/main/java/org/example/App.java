package org.example;

public class App {

  public String getGreeting() {
    return "\nWelcome to Tic-Tac-Toe!\n";
  }

  public static void main(String[] args) {

    boolean playGame = true;
    System.out.println(new App().getGreeting());

    while (playGame) {
      Game game = new Game();
      game.playGame();

      java.util.Scanner scanner = new java.util.Scanner(System.in);
      System.out.println("\nDo you want to play again? (y/n): ");
      String response = scanner.nextLine();

      if (!response.equalsIgnoreCase("y")) {
        playGame = false;
        System.out.println("\nGoodbye!\n");
      } 
    }
  }
}
