package pkgTTTBackend;

public class TYMachinePlayer {
    private static TYTTTBoard tttBoard;

    // Constructor to initialize the board reference
    protected TYMachinePlayer(TYTTTBoard board) {
        TYMachinePlayer.tttBoard = board;
    }

    // Method to check if the game is over
    protected int isGameOver() {
        if (tttBoard.isBoardFull()) {
            return TYSPOT.GAME_DRAW;
        }
        if (tttBoard.checkWinner(TYSPOT.machine_char)) {
            return TYSPOT.GAME_MACHINE;
        }
        if (tttBoard.checkWinner(TYSPOT.player_char)) {
            return TYSPOT.GAME_PLAYER;
        }
        return TYSPOT.GAME_INCOMPLETE;
    }

    // Public method called to make the machine's move
    public static void play() {
        // Try to win first
        if (playToConclude(TYSPOT.machine_char)) return;
        // Try to block the opponent
        if (playToConclude(TYSPOT.player_char)) return;
        // Else, follow the optimal path
        playOptimalPath();
    }

    // Tries to win or block based on the character passed
    private static boolean playToConclude(char c) {
        char[][] board = tttBoard.getBoard();

        // Check rows
        for (int row = 0; row < 3; row++) {
            int count = 0, emptyCol = -1;
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == c) count++;
                else if (board[row][col] == TYSPOT.default_char) emptyCol = col;
            }
            if (count == 2 && emptyCol != -1) {
                tttBoard.updateBoard(row, emptyCol);
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            int count = 0, emptyRow = -1;
            for (int row = 0; row < 3; row++) {
                if (board[row][col] == c) count++;
                else if (board[row][col] == TYSPOT.default_char) emptyRow = row;
            }
            if (count == 2 && emptyRow != -1) {
                tttBoard.updateBoard(emptyRow, col);
                return true;
            }
        }

        // Check diagonals
        int diagCount = 0, diagEmpty = -1;
        for (int i = 0; i < 3; i++) {
            if (board[i][i] == c) diagCount++;
            else if (board[i][i] == TYSPOT.default_char) diagEmpty = i;
        }
        if (diagCount == 2 && diagEmpty != -1) {
            tttBoard.updateBoard(diagEmpty, diagEmpty);
            return true;
        }

        // Check reverse diagonal
        int revDiagCount = 0, revDiagEmpty = -1;
        for (int i = 0; i < 3; i++) {
            if (board[i][2 - i] == c) revDiagCount++;
            else if (board[i][2 - i] == TYSPOT.default_char) revDiagEmpty = i;
        }
        if (revDiagCount == 2 && revDiagEmpty != -1) {
            tttBoard.updateBoard(revDiagEmpty, 2 - revDiagEmpty);
            return true;
        }

        return false; // No winning or blocking move found
    }

    // Method to follow the optimal path for the machine
    private static void playOptimalPath() {
        char[][] board = tttBoard.getBoard();

        // Try to take the center if available
        if (board[1][1] == TYSPOT.default_char) {
            tttBoard.updateBoard(1, 1);
            return;
        }

        // Try to take one of the corners (2, 2), (0, 0), (0, 2), (2, 0) in that order
        int[][] cornerMoves = {{2, 2}, {0, 0}, {0, 2}, {2, 2}};
        for (int[] move : cornerMoves) {
            int row = move[0], col = move[1];
            if (board[row][col] == TYSPOT.default_char) {
                tttBoard.updateBoard(row, col);
                return;
            }
        }

        // Try to take the sides (1, 0), (1, 2), (0, 1), (2, 1)
        int[][] sideMoves = {{1, 0}, {1, 2}, {0, 1}, {2, 1}};
        for (int[] move : sideMoves) {
            int row = move[0], col = move[1];
            if (board[row][col] == TYSPOT.default_char) {
                tttBoard.updateBoard(row, col);
                return;
            }
        }

        // If no optimal spot is found, pick a random one
        playRandomPick();
    }

    // Method to play a random available spot
    private static void playRandomPick() {
        int row, col;
        do {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        } while (tttBoard.getBoard()[row][col] != TYSPOT.default_char);
        tttBoard.updateBoard(row, col);
    }
}
