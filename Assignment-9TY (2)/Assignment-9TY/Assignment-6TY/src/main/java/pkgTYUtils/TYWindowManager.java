package pkgTYUtils;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;

public class TYWindowManager {

    private static TYWindowManager instance;
    private final long window;
    private final int winWidth;
    private final int winHeight;

    private TYWindowManager(int width, int height, int x, int y) {
        this.winWidth = width;
        this.winHeight = height;

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(width, height, "Game of Life", 0, 0);
        glfwSetWindowPos(window, x, y);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    public static TYWindowManager get(int width, int height, int x, int y) {
        if (instance == null) {
            instance = new TYWindowManager(width, height, x, y);
        }
        return instance;
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public boolean isWindowOpen() {
        return !glfwWindowShouldClose(window);
    }

    public void destroyGlfwWindow() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public int getWinWidth() {
        return winWidth;
    }

    public int getWinHeight() {
        return winHeight;
    }

    public long getWindowHandle() {
        return window;
    }
}
