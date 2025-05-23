package pkgTTTBackend;

import java.util.Scanner;

public class TYIOManager {
    // Scanner for user input (Aggregation relationship)
    private static final Scanner myScanner = new Scanner(System.in);

    // Private constructor to prevent instantiation
    private TYIOManager() {}

    // Method to initialize the game with a prompt
    public static void initPrompt() {
        System.out.println("Welcome to your unproductive time of the day! \n");
    }

    // Method to display the board using TYTTTBoard
    public static void printBoard(TYTTTBoard board) {
        board.printBoard();
    }

    // Method to prompt user to enter row and column numbers
    public static void rowColPrompt() {
        System.out.println("Enter row and column numbers (space separated):");
    }

    // Method to display a message when the chosen cell is not available
    public static void cellNotFreeMessage(int row, int col) {
        System.out.println("Cell [" + row + "][" + col + "] is not available. Please try again.");
    }


    // Method to notify that the player has won
    public static void playerWinMessage() {
        System.out.println("Congratulations! You have won the game!");
    }

    // Method to notify that the machine has won
    public static void machineWinMessage() {
        System.out.println("The machine has won! Better luck next time.");
    }
    // Method to read an array of integer inputs from the user
    public static int[] readIntegerInput(int size) {
        int[] inputArray = new int[size];

        String input = myScanner.nextLine();
        String[] parts = input.split(" ");

        if (parts.length == size) {
            try {
                for (int i = 0; i < size; i++) {
                    inputArray[i] = Integer.parseInt(parts[i]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter valid integers.");
                return null;
            }
        } else {
            System.out.println("Invalid number of inputs! Please enter exactly " + size + " integers.");
            return null;
        }

        return inputArray;
    }

    // Method to display an error message when there is an issue with the play
    public static void errorInPlayMessage() {
        System.out.println("Error in making a move. Please try again.");
    }
    // Method to display a message when the game is a draw
    public static void gameDrawMessage() {
        System.out.println("It's a draw! No winner this time.");
    }

    // Method to prompt the user to play again
    public static void playAgainMessage() {
        System.out.println("Beginning another round; type q to quit!");
    }
    // Method to display an error message for invalid entries
    public static void invalidEntryMessage() {
        System.out.println("Invalid entry. Please try again.");
    }

    // Method to display a message when the game is quit
    public static void quitGameMessage() {
        System.out.println("Good bye - game over, come again ASAP and waste more time!");
    }


}
