package pkgTTTBackend;

import java.util.Scanner;

public class TyTTTBoard {
    private final char[][] board;
    private char playerP;
    private boolean endGame;
    private final Scanner scan;

    // Use Constructor to create board and player
    public TyTTTBoard() {
        board = new char[3][3];
        scan = new Scanner(System.in);
        playerP = 'X';
        endGame = false;
        initBoard();
    }

    // Method to initialize or reset board.
    private void initBoard() {
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                board[i][k] = '-';
            }
        }
    }

    public void printBoard() {
        System.out.println("Current board on play:");
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                System.out.print(board[i][k]);
                if (k < 2) System.out.print(" ");
            }
            System.out.println();
        }
    }

    // Game Start
    public void play() {

        while (!endGame) {

            String input = getInput();
            //Quit the game if q is pressed
            if (input.equalsIgnoreCase("q")) {
                endGame = true;
                System.out.println("You have quit the game. Game over!");
                break;
            } else {
                int row = Character.getNumericValue(input.charAt(0));
                int col = Character.getNumericValue(input.charAt(1));
                if (moveValid(row, col)) {
                    makeValidMove(row, col);
                    checkWin();
                    switchPlayer();
                } else {
                    System.out.println("Invalid move. Please try again.");
                }
                printBoard();
            }
        }
    }

    private String getInput() {
        System.out.print("Enter row and column 0-2 or q to quit: ");
        return scan.nextLine();
    }

    private boolean moveValid(int row, int col) {
        return row >= 0 && col >= 0 && row < 3 && col < 3 && board[row][col] == '-';
    }

    //Method to make the next move for the player
    private void makeValidMove(int row, int col) {
        board[row][col] = playerP;
    }

    // Method used to switch player
    private void switchPlayer() {
        if (playerP == 'X') {
            playerP = 'O';
        } else {
            playerP = 'X';
        }
    }

    // Method to check who wins.
    private void checkWin() {
        if (checkRows() || checkColumns() || checkDiagonals()) {
            System.out.println("Player " + playerP + " won!");
            endGame = true;
        }
    }

    // Check columns for win
    private boolean checkColumns() {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == playerP && board[1][i] == playerP && board[2][i] == playerP) {
                return true;
            }
        }
        return false;
    }

    // Check diagonals for win
    private boolean checkDiagonals() {
        if (board[0][0] == playerP && board[1][1] == playerP && board[2][2] == playerP) {
            return true;
        }
        return board[0][2] == playerP && board[1][1] == playerP && board[2][0] == playerP;
    }

    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == playerP && board[i][1] == playerP && board[i][2] == playerP) {
                return true;
            }
        }
return false;
    }
}


