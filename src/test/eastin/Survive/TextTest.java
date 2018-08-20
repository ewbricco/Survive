package eastin.Survive;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.newdawn.slick.TrueTypeFont;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by ebricco on 8/13/18.
 */
public class TextTest {

    public static long window;
    public static TrueTypeFont font;


    public static void main(String[] args) {


        try {
            //Font awtFont = new Font("Arial", Font.PLAIN, 24);
            //font = new TrueTypeFont(awtFont, false);

            initGLFW();
            initGL();
            loop();
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } catch(Exception e) {
            System.out.println(e.toString());
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
        //glDisable(GL_DEPTH_TEST);
        glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private static void loop() {
        GL.createCapabilities();
        glClearColor(.2f,.8f,0f, 0f);

        while (!glfwWindowShouldClose(window)) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwPollEvents();

            System.out.println("sup");

            font.drawString(50, 50, "Platform");

            System.out.println("hi");

            glfwSwapBuffers(window); // swap the color buffers

        }
    }


}
