/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.stb;

import org.lwjgl.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.*;

/** STB Easy Font demo. */
public final class EasyFont extends FontDemo {

    private static final int BASE_HEIGHT = 1000000;

    private EasyFont(String filePath) {
        super(BASE_HEIGHT, filePath);
    }

    public static void main(String[] args) {
        String filePath;
        if (args.length == 0) {
            System.out.println("Use 'ant demo -Dclass=org.lwjgl.demo.stb.EasyFont -Dargs=<path>' to load a different text file (must be UTF8-encoded).\n");
            filePath = "README.md";
        } else {
            filePath = args[0];
        }

        new EasyFont(filePath).run("STB Easy Font Demo");
    }

    @Override
    protected void loop() {
        String text = "Ammo: 57";
        ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);

        int quads = stb_easy_font_print(500, 500, text, null, charBuffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        glClearColor(.2f, .8f, 0f, 0f); // BG color
        glColor3f(0f, 0f, 0f); // Text color

        while (!glfwWindowShouldClose(getWindow())) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            System.out.println(getScale());

            float scaleFactor = 1.0f + getScale() * 0.25f;

            glPushMatrix();
            // Zoom
            glScalef(scaleFactor, scaleFactor, 1f);
            // Scroll

            System.out.println(getLineOffset() * getFontHeight());
            glTranslatef(4.0f, 4.0f - getLineOffset() * getFontHeight(), 0f);

            glDrawArrays(GL_QUADS, 0, quads * 4);

            glPopMatrix();

            glfwSwapBuffers(getWindow());
        }

        glDisableClientState(GL_VERTEX_ARRAY);
    }

}