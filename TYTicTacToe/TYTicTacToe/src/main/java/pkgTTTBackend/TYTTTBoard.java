package pkgTTTBackend;

import java.util.Scanner;

public class TYTTTBoard {
    private final char[][] tttBoard;           // The 3x3 Tic-Tac-Toe board
    private final Scanner scan;                // Scanner for user input
    private final TYMachinePlayer machinePlayer;  // Reference to the AI player
    private int totalValidEntries;             // Number of filled cells

    // Constructor to initialize board, machine player, and scanner
    public TYTTTBoard() {
        tttBoard = new char[TYSPOT.ROW][TYSPOT.COL];
        scan = new Scanner(System.in);
        machinePlayer = new TYMachinePlayer(this);
        clearBoard();  // Start with an empty board
    }

    // Public getter for the board
    public char[][] getBoard() {
        return tttBoard;
    }

    // Method to clear and reset the board
    public void clearBoard() {
        totalValidEntries = 0;
        for (int i = 0; i < TYSPOT.ROW; i++) {
            for (int j = 0; j < TYSPOT.COL; j++) {
                tttBoard[i][j] = TYSPOT.default_char;
            }
        }
    }

    // Method to print the board
    public void printBoard() {
        for (int i = 0; i < TYSPOT.ROW; i++) {
            for (int j = 0; j < TYSPOT.COL; j++) {
                System.out.print(tttBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to check if the board is full
    public boolean isBoardFull() {
        return totalValidEntries >= TYSPOT.ROW * TYSPOT.COL;
    }

    // Method to check if a specific player has won
    public boolean checkWinner(char playerChar) {
        // Check rows
        for (int i = 0; i < TYSPOT.ROW; i++) {
            if (tttBoard[i][0] == playerChar && tttBoard[i][1] == playerChar && tttBoard[i][2] == playerChar) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < TYSPOT.COL; j++) {
            if (tttBoard[0][j] == playerChar && tttBoard[1][j] == playerChar && tttBoard[2][j] == playerChar) {
                return true;
            }
        }

        // Check diagonals
        if (tttBoard[0][0] == playerChar && tttBoard[1][1] == playerChar && tttBoard[2][2] == playerChar) {
            return true;
        }
        if (tttBoard[0][2] == playerChar && tttBoard[1][1] == playerChar && tttBoard[2][0] == playerChar) {
            return true;
        }

        return false;
    }

    // Method to prompt the player and machine to play
    public int play() {
        TYIOManager.initPrompt();
        TYIOManager.printBoard(this);
        System.out.println();

        while (!isBoardFull()) {
            TYIOManager.rowColPrompt();
            int[] coords = TYIOManager.readIntegerInput(2);
            if (coords == null) {
                TYIOManager.invalidEntryMessage();
                continue;
            }

            int row = coords[0];
            int col = coords[1];

            // Quit condition
            if (row == -1 || col == -1) {
                TYIOManager.quitGameMessage();
                return TYSPOT.GAME_QUIT;
            }

            if (!isValidMove(row, col)) {
                TYIOManager.cellNotFreeMessage(row, col);
                continue;
            }

            updateBoard(row, col, TYSPOT.player_char);
            totalValidEntries++;


            int status = machinePlayer.isGameOver();
            if (status != TYSPOT.GAME_INCOMPLETE) {
                return conclude(status);
            }

            TYMachinePlayer.play();  // Machine makes a move
            totalValidEntries++;
            TYIOManager.printBoard(this);
            System.out.println();

            status = machinePlayer.isGameOver();
            if (status != TYSPOT.GAME_INCOMPLETE) {
                return conclude(status);
            }
        }

        return conclude(TYSPOT.GAME_DRAW);
    }

    // Update the board with the specified character
    public void updateBoard(int row, int col, char c) {
        tttBoard[row][col] = c;
    }

    // Overloaded updateBoard for machine player
    public void updateBoard(int row, int col) {
        updateBoard(row, col, TYSPOT.machine_char);
    }

    // Method to validate move
    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < TYSPOT.ROW &&
                col >= 0 && col < TYSPOT.COL &&
                tttBoard[row][col] == TYSPOT.default_char;
    }

    // Conclude game with appropriate message
    private int conclude(int status) {
        switch (status) {
            case TYSPOT.GAME_PLAYER -> TYIOManager.playerWinMessage();
            case TYSPOT.GAME_MACHINE -> TYIOManager.machineWinMessage();
            case TYSPOT.GAME_DRAW -> TYIOManager.gameDrawMessage();
        }
        return status;
    }

    // Prompt again after a game ends
    public void playAgainMessage() {
        TYIOManager.playAgainMessage();
        String response = scan.nextLine();
        if (response.equalsIgnoreCase("q")) {

            TYIOManager.initPrompt();
            TYIOManager.printBoard(this);
            System.out.println();
            TYIOManager.quitGameMessage();
            System.exit(0);
        }
    }
}
