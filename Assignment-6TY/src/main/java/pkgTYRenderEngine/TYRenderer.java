package pkgTYRenderEngine;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import pkgTYUtils.TYWindowManager;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class TYRenderer {

    // Static constants for UML, more included
    private static final int VPT = 4;  // Vertices per Tile
    private static final int IPV = 6;  // Indices per Tile for the 2 triangles
    private static final int FPV = 2; // Floats per Vertex x and y (x,y)
    private static final int EPT = IPV;  // Elements per Tile = Indices per Tile

    // Private Fields for UML
    private int NUM_ROWS, NUM_COLS;
    private int shader_program;
    private int vao, vbo, ebo;
    private final TYWindowManager myWM;
    private Matrix4f viewProjMatrix;
    private int vpMatLocation, renderColorLocation;
    private int myFloatBufferSize; // Used for buffer size

    // Constructor
    public TYRenderer(TYWindowManager wm) {
        this.myWM = wm;
        initOpenGL();
    }

    // Initialize OpenGL settings, create shaders, buffers, etc.
    private void initOpenGL() {
        // Set background color
        GL11.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        // Enable depth test for proper rendering
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // Create the shader program for rendering tiles
        shader_program = createShaderProgram();

        // Create OpenGL resources for the grid
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo = GL30.glGenBuffers();
        ebo = GL30.glGenBuffers();

        // Setup vertex attribute pointers
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        GL30.glVertexAttribPointer(0, FPV, GL11.GL_FLOAT, false, FPV * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(0);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }

    // Method to handle the rendering of the grid of tiles
    public void render(final int OFFSET, final int PADDING, final int SIZE, final int numRows, final int numCols) {
        this.NUM_ROWS = numRows;
        this.NUM_COLS = numCols;
        // Edge padding, Tile size, and Offset

        // Create an matrix based on matrix size Must be set like this or error comes out
        viewProjMatrix = new Matrix4f();
        viewProjMatrix.setOrtho(0, myWM.getWinWidth(), 0, myWM.getWinHeight(), -1.0f, 1.0f);

        // Get uniform variable locations in the shader program
        vpMatLocation = GL20.glGetUniformLocation(shader_program, "viewProjMatrix");
        renderColorLocation = GL20.glGetUniformLocation(shader_program, "renderColor");

        // Create vertices and indices for the grid of tiles
        float[] vertices = generateTilesVertices(OFFSET, PADDING, SIZE, NUM_ROWS, NUM_COLS);
        int[] indices = generateTileIndices(NUM_ROWS, NUM_COLS);

        // Load the vertex data to the GPU
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        // Storing vertex data
        FloatBuffer myFloatBuffer = MemoryStack.stackMallocFloat(vertices.length);
        myFloatBuffer.put(vertices).flip();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, myFloatBuffer, GL30.GL_STATIC_DRAW);

        // Load into the index data to the GPU
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
        IntBuffer indexBuffer = MemoryStack.stackMallocInt(indices.length);
        indexBuffer.put(indices).flip();
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL30.GL_STATIC_DRAW);

        // Start render loop
        renderLoop();
    }

    // The main render loop: continually render and update the scene
    private void renderLoop() {
        while (!myWM.isGlfwWindowClosed()) {
            // Render the grid of tiles
            renderObjects();

            // Swap the OpenGL buffers
            myWM.swapBuffers();
            // Poll for GLFW events
            glfwPollEvents();

            // Set the view-projection matrix in the shader
            GL20.glUniformMatrix4fv(vpMatLocation, false, viewProjMatrix.get(new float[16]));

            // Set the color uniform
            GL20.glUniform4f(renderColorLocation, 1.0f, 0.498f, 0.153f, 0.0f);
            // Clear the screen before rendering each frame
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            // Update the projection matrix in case the window size changes
            viewProjMatrix.setOrtho(0, myWM.getWinWidth(), 0, myWM.getWinHeight(), -1.0f, 1.0f);

            // Use the shader program
            GL20.glUseProgram(shader_program);


        }
    }



    // Create the vertices for the grid of tiles
    private float[] generateTilesVertices(final int OFFSET, final int PADDING, final int SIZE, final int rowTiles, final int colTiles) {
        float[] vertices = new float[rowTiles * colTiles * VPT * FPV];

        for (int row = 0; row < rowTiles; row++) {
            for (int col = 0; col < colTiles; col++) {
                int myIndx = (row * colTiles + col) * VPT * FPV;

                float xmin = OFFSET + col * (SIZE + PADDING);
                float ymin = myWM.getWinHeight() - OFFSET - row * (SIZE + PADDING) - SIZE;

                vertices[myIndx] = xmin;                         // bottom-left x
                vertices[myIndx + 1] = ymin;                     // bottom-left y
                vertices[myIndx + 2] = xmin + SIZE;              // bottom-right x
                vertices[myIndx + 3] = ymin;                     // bottom-right y
                vertices[myIndx + 4] = xmin + SIZE;              // top-right x
                vertices[myIndx + 5] = ymin + SIZE;             // top-right y
                vertices[myIndx + 6] = xmin;                     // top-left x
                vertices[myIndx + 7] = ymin + SIZE;             // top-left y
            }
        }
        return vertices;
    }

    // Generate the indices for each tile's vertices
    private int[] generateTileIndices(final int rowTiles, final int colTiles) {
        int[] indices = new int[rowTiles * colTiles * EPT];

        for (int row = 0; row < rowTiles; row++) {
            for (int col = 0; col < colTiles; col++) {
                int tileNum = row * colTiles + col;
                int startIndex = tileNum * EPT;
                int startIV = tileNum * VPT;

                indices[startIndex] = startIV;
                indices[startIndex + 1] = startIV + 1;
                indices[startIndex + 2] = startIV + 2;
                indices[startIndex + 3] = startIV;
                indices[startIndex + 4] = startIV + 2;
                indices[startIndex + 5] = startIV + 3;
            }
        }

        return indices;
    }
    // Render all the objects or tiles for window screen
    private void renderObjects() {
        GL30.glBindVertexArray(vao);  // Bind the VAO for the grid
        GL11.glDrawElements(GL11.GL_TRIANGLES, NUM_ROWS * NUM_COLS * EPT, GL11.GL_UNSIGNED_INT, 0);
    }
    // Create the shader program for vertex and fragment
    private int createShaderProgram() {
        // Shader code (vertex and fragment shaders) got help on this part.
        String vertexShaderCode = """
        #version 330 core
        layout(location = 0) in vec2 position;
        uniform mat4 viewProjMatrix;
        void main() {
            gl_Position = viewProjMatrix * vec4(position, 0.0, 1.0);
        }
        """;
        String fragmentShaderCode = """
        #version 330 core
        out vec4 FragColor;
        uniform vec4 renderColor;
        void main() {
            FragColor = renderColor;
        }
        """;

        // Compile shaders
        int vertexShader = compileShader(vertexShaderCode, GL20.GL_VERTEX_SHADER);
        int fragmentShader = compileShader(fragmentShaderCode, GL20.GL_FRAGMENT_SHADER);

        // Link shaders has to stay in this order to be used correctly
        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
        GL20.glLinkProgram(program);

        // Clean up shaders
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        return program;
    }

    // Compile shader
    private int compileShader(String shaderCode, int shaderType) {
        int shader = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shader, shaderCode);
        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            String infoLog = GL20.glGetShaderInfoLog(shader);
            throw new RuntimeException("Shader Compilation failed: " + infoLog);
        }

        return shader;
    }
}
