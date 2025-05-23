package CSC133snsTY;

import java.awt.*;
import java.awt.event.*;

public class Player {
    private int x, y;
    private int width = 50, height = 50;
    private int dx = 0;
    private boolean left = false, right = false;
    float a,b ; // Position
    float velocityA = 0;
    final float gravity = 0.5f;
    final float jumpStrength = 10f;
    boolean onGround = true;
    final float groundLevel = 416f;


    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void updateJump() {
        if (!onGround) {
            velocityA += gravity;
        }
        y += velocityA;

        // Simple ground collision

        if (y >= groundLevel) {
            y = (int) groundLevel;
            velocityA = 0;
            onGround = true;
        }
    }

    public void jump() {
        if (onGround) {
            velocityA = jumpStrength;
            onGround = false;
        }
    }

    public void update() {
        if (left) dx = -5;
        else if (right) dx = 5;
        else dx = 0;

        x += dx;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) left = true;
        if (code == KeyEvent.VK_RIGHT) right = true;
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) left = false;
        if (code == KeyEvent.VK_RIGHT) right = false;
    }
}
