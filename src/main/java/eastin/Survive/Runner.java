package eastin.Survive;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Runner {
    public long window;
    public static Runner runner;

    public static void main(String[] args) {

        runner = new Runner();

        World.createWorld();

        try {
            runner.initGLFW();
            runner.initGL();
            runner.loop();
            glfwFreeCallbacks(runner.window);
            glfwDestroyWindow(runner.window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(GameState.WIDTH, GameState.HEIGHT, GameState.NAME, NULL, NULL);

        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            } else{
                //pass key and action to barriers' movement method
                World.world.handleInput(key, action);
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (vidmode.width() - GameState.WIDTH) / 2,
                (vidmode.height() - GameState.HEIGHT) / 2
        );

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void initGL(){
        GL.createCapabilities();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, GameState.WIDTH, 0, GameState.HEIGHT, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_DEPTH_TEST);
    }


    private void loop() {
        GL.createCapabilities();
        glClearColor(.2f,.8f,0f, 0f);
        int frames = 0;
        long lastFrameRateDisplay = System.currentTimeMillis();
        long swapTimer = 0;
        long swapStart;
        while (!glfwWindowShouldClose(window)) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            World.world.update();

            glfwPollEvents();

            while(World.world.paused) {
                glfwPollEvents();
            }

            World.world.render();

            swapStart = System.currentTimeMillis();
            glfwSwapBuffers(window); // swap the color buffers
            swapTimer += (System.currentTimeMillis() - swapStart);

            frames++;
            if(System.currentTimeMillis() - lastFrameRateDisplay > 5000) {
                System.out.println("frame rate: " + (double)(1000*frames) / ((double)((System.currentTimeMillis()) - (lastFrameRateDisplay))));
                lastFrameRateDisplay = System.currentTimeMillis();
                frames = 0;

                System.out.println("swap: " + swapTimer);

                swapTimer = 0;
            }
        }
    }
}