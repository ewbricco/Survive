package Survive;

import Survive.Utils.GameCoordinate;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Runner {
    public static long window;
    public static MainCharacter mc;
    public static Barriers barriers;
    //public static Enemies enemies;

    public static void main(String[] args) {
        mc = new MainCharacter();
        barriers = new Barriers();
        //enemies = new Enemies();

        try {
            initGLFW();
            initGL();
            loop();
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private static void initGLFW() {
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
            }
            else{
                //pass key and action to barriers' movement method
                mc.checkInput(key, action);
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

    private static void initGL(){
        GL.createCapabilities();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, GameState.WIDTH,0, GameState.HEIGHT, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_DEPTH_TEST);
    }


    private static void loop() {
        GL.createCapabilities();
        glClearColor(.2f,.8f,0f, 0f);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwPollEvents();
            barriers.update(mc.getCoords());
            barriers.render(mc.getAreaQuad());
            //enemies.update(mc.getX(), mc.getY());
            //enemies.render();
            mc.render();
            mc.move();
            glfwSwapBuffers(window); // swap the color buffers
        }
    }
}