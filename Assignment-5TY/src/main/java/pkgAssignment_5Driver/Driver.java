// Tim Yang
// Shankar Swamy
// CSC 133
// 3/8/25
// Assignment5 CSC-133-05
// Instructions: Use assignment 1 to help with WindowManager class file
// And make it work with the driver file from assignment 5

package pkgAssignment_5Driver;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import pkgBackendTY.TYWindowManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Driver {

    //long window;
    static int WIN_WIDTH = 1800, WIN_HEIGHT = 1200, WIN_POS_X = 30, WIN_POX_Y = 90;
    private static final int OGL_MATRIX_SIZE = 16;
    // call glCreateProgram() here - we have no gl-context here
    int shader_program;
    Matrix4f viewProjMatrix = new Matrix4f();
    FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
    int vpMatLocation = 0, renderColorLocation = 0;
    public static void main(String[] args) {
        TYWindowManager myWM = TYWindowManager.get(WIN_WIDTH, WIN_HEIGHT, WIN_POS_X, WIN_POX_Y);
        myWM = TYWindowManager.get();
        myWM = TYWindowManager.get(2*WIN_WIDTH, 2*WIN_HEIGHT, WIN_POS_X, WIN_POX_Y);
        new Driver().render(myWM);
    } // public static void main(String[] args)

    void render(TYWindowManager myWindowManager) {
        myWindowManager.updateContextToThis();
        renderLoop(myWindowManager);
        myWindowManager.destroyGlfwWindow();
    } // void render()

    void renderLoop(TYWindowManager myWindowManager) {
        glfwPollEvents();
        initOpenGL();
        renderObjects(myWindowManager);
        /* Process window messages in the main thread */
        while (!myWindowManager.isGlfwWindowClosed()) {
            glfwWaitEvents();
        }
    } // void renderLoop()
    public void enableResizeWindowCallback() {

    }
    void initOpenGL() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(0, 0, WIN_WIDTH, WIN_HEIGHT);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        this.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(0.6f, 0.7f, 0.1f, 1.0f);" +
                        "}");
        glCompileShader(fs);
        glAttachShader(shader_program, fs);
        glLinkProgram(shader_program);
        glUseProgram(shader_program);
        vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");
        return;
    } // void initOpenGL()

    void renderObjects(TYWindowManager myWM) {
        while (!myWM.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            int vbo = glGenBuffers();
            int ibo = glGenBuffers();
            float[] vertices = {-20f, -20f, 20f, -20f, 20f, 20f, -20f, 20f};
            int[] indices = {0, 1, 2, 0, 2, 3};
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.
                    createFloatBuffer(vertices.length).
                    put(vertices).flip(), GL_STATIC_DRAW);
            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.
                    createIntBuffer(indices.length).
                    put(indices).flip(), GL_STATIC_DRAW);
            glVertexPointer(2, GL_FLOAT, 0, 0L);
            viewProjMatrix.setOrtho(-100, 100, -100, 100, 0, 10);
            glUniformMatrix4fv(vpMatLocation, false,
                    viewProjMatrix.get(myFloatBuffer));
            glUniform3f(renderColorLocation, 1.0f, 0.498f, 0.153f);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            final int VTD = 6; // need to process 6 Vertices To Draw 2 triangles
            glDrawElements(GL_TRIANGLES, VTD, GL_UNSIGNED_INT, 0L);
            myWM.swapBuffers();
        }
    } // renderObjects
}  //  public class Driver

