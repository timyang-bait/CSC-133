package pkgBackendTY;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class TYWindowManager {
    private static TYWindowManager my_window; // Singleton static instance of WindowManger
    private static long glfwWindow; // glfw Window handle
    private static int win_width, win_height; // Dimensions of the window
    private static GLFWFramebufferSizeCallback resizeCallback; // callback to resize window


    private TYWindowManager(int width, int height) {  // Constructor to initialize window
        this.win_width = width;
        this.win_height = height;
        initGlfwWindow();
    }

    public static TYWindowManager get(int width, int height) { // Static method to return single instance of WindowManager
        if (my_window == null) {
            my_window = new TYWindowManager(width, height); // creates new instance given width and height
        }
        return my_window; // return single instance
    }

    public static TYWindowManager get(int width, int height, int orgX, int orgY) { // overload static to set and return
        get(width, height);                                                        // windows position
        setWinWidth(orgX, orgY);
        return my_window;
    }


    public void destroyGlfwWindow() { // method to destory window
        glfwDestroyWindow(glfwWindow);
        if (resizeCallback != null) {
            resizeCallback.free();
        }
    }
    // static private, protected method to set windows width and height
    protected static void setWinWidth(int width, int height) {
        if (my_window != null && my_window.glfwWindow != NULL) {

        }
    }

    private static GLFWFramebufferSizeCallback resizeWindow =
            new GLFWFramebufferSizeCallback(){
                @Override
                public void invoke(long window, int width, int height){
                    glViewport(0,0,width, height);
                }
            };
    public void enableResizeWindowCallback() { //  Set framebuffer size callback if not set yet
        glfwSetFramebufferSizeCallback(glfwWindow, resizeWindow);
    }

    public void swapBuffers() { // method to swap window buffers
        glfwSwapBuffers(glfwWindow);
    }

    public void updateContextToThis() { //Updates the context of openGL
        glfwMakeContextCurrent(glfwWindow);
    }

    public static TYWindowManager get() { // Static method to get single instance of
        return my_window;                 // of Window Manager
    }
    public boolean isGlfwWindowClosed() { // checks if window is closed
        return glfwWindowShouldClose(glfwWindow);
    }
    private void initGlfwWindow() { // private method to initialize glfw window
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(win_width, win_height, "TYWindow", NULL, NULL);
        if (glfwWindow == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(glfwWindow);
        GL.createCapabilities();
        glfwShowWindow(glfwWindow);

        // Initialize the resize callback
        resizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                win_width = width;
                win_height = height;
                glViewport(0, 0, width, height); // Adjust OpenGL viewport
            }
        };

        // Set the resize callback for the window
        glfwSetFramebufferSizeCallback(glfwWindow, resizeCallback);
    }
}
