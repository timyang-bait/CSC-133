package pkgTYUtils;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class TYWindowManager {
    private static TYWindowManager my_window; // Singleton instance
    private static long glfwWindow; // GLFW Window handle
    private static int win_width, win_height; // Window dimensions

    // Framebuffer resize callback
    private static GLFWFramebufferSizeCallback resizeCallback;


    private TYWindowManager(int width, int height, int xPos, int yPos) {  // Constructor to initialize window
        win_width = width;
        win_height = height;
        initGlfwWindow(xPos, yPos);  // Pass position to window initialization
    }

    // Updated get method to take window position as parameters
    public static TYWindowManager get(int width, int height, int xPos, int yPos) {
        if (my_window == null) {
            my_window = new TYWindowManager(width, height, xPos, yPos);
        }
        return my_window;
    }
    public static TYWindowManager get() { // method to return the single instance of WindowManager
        return my_window;
    }


    public boolean isGlfwWindowClosed() {     // Method to check if the window should be closed
        return glfwWindowShouldClose(glfwWindow);
    }

    // Method to swap the buffers (for rendering)
    public void swapBuffers() {
        glfwSwapBuffers(glfwWindow);
    }

    // Private method to initialize the GLFW window and its context
    private void initGlfwWindow(int xPos, int yPos) {
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Make the window invisible initially
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Allow window resizing

        // Create the window
        glfwWindow = glfwCreateWindow(win_width, win_height, "CSC 133 Assignment-6", NULL, NULL);
        if (glfwWindow == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Set window position
        glfwSetWindowPos(glfwWindow, xPos, yPos);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(glfwWindow);

        // Initialize OpenGL capabilities
        GL.createCapabilities();

        // Initialize the resize callback
        resizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                win_width = width;
                win_height = height;
                glViewport(0, 0, width, height); // Adjust OpenGL viewport when resized
            }
        };

        // Set the resize callback
        glfwSetFramebufferSizeCallback(glfwWindow, resizeCallback);
    }


    public void destroyGlfwWindow() { // Method to destroy the GLFW window
        glfwDestroyWindow(glfwWindow);
        if (resizeCallback != null) {
            resizeCallback.free();
        }
    }




    public void updateContextToThis() {     // Method to update the context to this window for OpenGL
        glfwMakeContextCurrent(glfwWindow);
    }


    public int getWinWidth() {// Method to get the width of the window
        return win_width;
    }


    public int getWinHeight() {    // Method to get the height of the window
        return win_height;
    }


    public static void setWinSize(int width, int height) {  // Method to set the window's size
        if (my_window != null && glfwWindow != NULL) {
            win_width = width;
            win_height = height;
            glViewport(0, 0, width, height); // Adjust OpenGL viewport
        }
    }
}
