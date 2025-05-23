package pkgAssignment_1Driver;

public class Driver {
    public static void main(String[] args) {
        pkgTTTBackend.TyTTTBoard my_board = new pkgTTTBackend.TyTTTBoard();

        my_board.printBoard();
        my_board.play();

        System.out.println("Thank you for playing! Come back to waste more of your time!");
    }
}  // public class Driver



