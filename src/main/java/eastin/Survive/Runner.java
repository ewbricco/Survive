package eastin.Survive;

import eastin.Survive.sound.Sound;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static org.lwjgl.system.MemoryUtil.*;


public class Runner {
    public static long window;

    public static void main(String[] args) {
        World.createWorld();

        run(World.world);
    }

    public static void run(World world) {
        run(world, new ArrayList<>(), () -> false);
    }

    public static void run(World world, List<DoIf> doifs, EndIf endCondition) {
        try {
            //Sound.initializeAbilityToPlaySound();
            initGLFW(world);
            initGL();
            loop(world, doifs, endCondition);
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
            world.destroy();
            Sound.shutdown();
        }
    }



    public static void initGLFW(World world) {
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
                world.handleInput(key, action);
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

    public static void initGL(){
        GL.createCapabilities();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, GameState.WIDTH, 0, GameState.HEIGHT, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_DEPTH_TEST);
    }

    public static void loop(World world, List<DoIf> doifs, EndIf endCondition) {
        GL.createCapabilities();
        glClearColor(.2f,.8f,0f, 0f);
        int frames = 0;
        long lastFrameRateDisplay = System.currentTimeMillis();
        long swapTimer = 0;
        long swapStart;
        while (!glfwWindowShouldClose(window) && !endCondition.test()) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            updateWorld(world, doifs);

            glfwPollEvents();

            while(world.paused) {
                glfwPollEvents();
            }

            world.render();

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

    private static void updateWorld(World world, List<DoIf> doifs) {
        world.update();

        doifs.forEach(doIf -> doIf.attempt());
    }

    public static void simulateLoop(World world, List<DoIf> doifs, EndIf endCondition) {
        while (!endCondition.test()) {

            long start = System.currentTimeMillis();

            updateWorld(world, doifs);

            long toSleep = GameState.MILLISPERFRAME - (System.currentTimeMillis() - start);
            toSleep = Math.max(0, toSleep);

            try {
                Thread.sleep(toSleep);
            } catch(InterruptedException e) {
                System.out.println(e.toString());
            }
        }
    }

    public static class RunEveryNth implements DoIf {
        private int n;
        private int i;
        private Runnable action;

        public RunEveryNth(int n, Runnable action) {
            this.n = n;
            this.action = action;
            i=1;
        }

        public void attempt() {
            if(i%n == 0) {
                action.run();
            }
            i++;
        }
    }

    public interface DoIf {
        void attempt();
    }

    public interface EndIf {
        boolean test();
    }
}