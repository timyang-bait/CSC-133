// Tim Yang
// Shankar Swamy
// CSC 133
// 3/1/25
// Assignment4 CSC-133-05
// Instructions Create a tick-tac-toe board as shown in the video provided.

package pkgAssignment_4Driver;
import static pkgTTTBackend.TYSPOT.*;
public class Driver {
    public static void main(String[] args) {
        pkgTTTBackend.TYTTTBoard my_board = new pkgTTTBackend.TYTTTBoard();

        int retVal = GAME_INCOMPLETE;
        while (retVal == GAME_INCOMPLETE) {
            retVal = my_board.play();
            if (retVal != GAME_QUIT) {
                retVal = GAME_INCOMPLETE;
                my_board.clearBoard();
                my_board.playAgainMessage();
            } // if (retVal != GAME_QUIT)
        } // while (retVal == pkgTTTBackend.SlSPOT.GAME_CONTINUE)
    } // public static void main(String[] args)
} // public class Driver



