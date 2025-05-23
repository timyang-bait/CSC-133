package pkgTYRenderEngine;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import pkgCSC133.TYGoLArray;
import pkgTYUtils.TYWindowManager;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class TYRenderer {

    private static final int FPV = 2; // Floats per vertex: x, y

    private final TYWindowManager myWM;
    private final TYGeometryManager geomMgr;
    private int vao, vbo;
    private int shaderProgram;
    private int vpMatLocation;

    public TYRenderer(TYWindowManager wm, TYGeometryManager gm) {
        this.myWM = wm;
        this.geomMgr = gm;
        initOpenGL();
    }

    private void initOpenGL() {
        GL11.glClearColor(0.0f, 0.0f, 1.0f, 1.0f); // Blue background
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        shaderProgram = createShaderProgram();
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        GL20.glVertexAttribPointer(0, FPV, GL11.GL_FLOAT, false, FPV * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void run() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL20.glUseProgram(shaderProgram);

        // Build and upload the VP matrix
        Matrix4f vpMatrix = new Matrix4f().setOrtho(
                0, myWM.getWinWidth(),
                0, myWM.getWinHeight(),
                -1f, 1f
        );
        FloatBuffer vpBuffer = BufferUtils.createFloatBuffer(16);
        vpMatrix.get(vpBuffer);
        GL20.glUniformMatrix4fv(vpMatLocation, false, vpBuffer);

        // Generate live cell geometry
        float[] vertices = new float[geomMgr.getNumRows() * geomMgr.getNumCols() * 12];
        geomMgr.generateTilesVertices((TYGoLArray) geomMgr.getMyPPArray(), vertices);

        // Upload vertex data
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Draw the vertices
        GL30.glBindVertexArray(vao);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / FPV);
        GL30.glBindVertexArray(0);

        myWM.swapBuffers();
        glfwPollEvents();
    }

    private int createShaderProgram() {
        String vertexShaderSource = """
                #version 330 core
                layout(location = 0) in vec2 aPos;
                uniform mat4 vpMatrix;
                void main() {
                    gl_Position = vpMatrix * vec4(aPos, 0.0, 1.0);
                }
                """;

        String fragmentShaderSource = """
                #version 330 core
                out vec4 FragColor;
                void main() {
                    FragColor = vec4(0.65, .45, 0.15, 1.0); //
                }
                """;

        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShader, vertexShaderSource);
        GL20.glCompileShader(vertexShader);
        checkShaderCompile(vertexShader, "VERTEX");

        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShader, fragmentShaderSource);
        GL20.glCompileShader(fragmentShader);
        checkShaderCompile(fragmentShader, "FRAGMENT");

        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
        GL20.glLinkProgram(program);
        checkProgramLink(program);

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        vpMatLocation = GL20.glGetUniformLocation(program, "vpMatrix");

        return program;
    }

    private void checkShaderCompile(int shader, String type) {
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException(type + " SHADER COMPILE ERROR:\n" + GL20.glGetShaderInfoLog(shader));
        }
    }

    private void checkProgramLink(int program) {
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("PROGRAM LINK ERROR:\n" + GL20.glGetProgramInfoLog(program));
        }
    }
}
