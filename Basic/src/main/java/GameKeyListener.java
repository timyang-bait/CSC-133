import CSC133snsTY.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {
    private Player player;

    public GameKeyListener(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_UP) {
            player.jump();
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        // Optional: Implement if needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Optional: Implement if needed
    }
}