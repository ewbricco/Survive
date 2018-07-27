package eastin.Survive;

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
    public static Enemies enemies;

    public static void main(String[] args) {
        mc = new MainCharacter();
        barriers = new Barriers();
        enemies = new Enemies();

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
        int frames = 0;
        long lastFrameRateDisplay = System.currentTimeMillis();
        long barriersTimer = 0;
        long enemiesTimer = 0;
        long mcTimer = 0;
        long barriersStart;
        long enemiesStart;
        long mcStart;
        long floatTimer = 0;
        long floatStart;
        long clearTimer = 0;
        long clearStart;
        long swapTimer = 0;
        long swapStart;
        while (!glfwWindowShouldClose(window)) {

            clearStart = System.currentTimeMillis();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            clearTimer += (System.currentTimeMillis() - clearStart);

            floatStart = System.currentTimeMillis();
            glfwPollEvents();
            floatTimer += (System.currentTimeMillis() - floatStart);

            barriersStart = System.currentTimeMillis();
            barriers.update(mc.getCenter());
            barriers.render();
            barriersTimer += (System.currentTimeMillis() - barriersStart);

            enemiesStart = System.currentTimeMillis();
            enemies.update(mc.getCenter());
            enemies.render();
            enemiesTimer += (System.currentTimeMillis() - enemiesStart);

            mcStart = System.currentTimeMillis();
            mc.render();
            mc.update();
            mcTimer += (System.currentTimeMillis() - mcStart);

            swapStart = System.currentTimeMillis();
            glfwSwapBuffers(window); // swap the color buffers
            swapTimer += (System.currentTimeMillis() - swapStart);

            frames++;
            if(System.currentTimeMillis() - lastFrameRateDisplay > 5000) {
                System.out.println("frame rate: " + (double)(1000*frames) / ((double)((System.currentTimeMillis()) - (lastFrameRateDisplay))));
                lastFrameRateDisplay = System.currentTimeMillis();
                frames = 0;

                System.out.println("barriers: " + barriersTimer);
                System.out.println("enemies: " + enemiesTimer);
                System.out.println("mc: " + mcTimer);
                System.out.println("float: " + floatTimer);
                System.out.println("clear: " + clearTimer);
                System.out.println("swap: " + swapTimer);

                barriersTimer = 0;
                enemiesTimer = 0;
                mcTimer = 0;
                floatTimer = 0;
                clearTimer = 0;
                swapTimer = 0;
            }
        }
    }
}