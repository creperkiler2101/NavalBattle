package engine.core;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

import javax.swing.*;
import java.util.Calendar;

public class Application {
    protected static Application current;
    protected long lastTime = System.nanoTime();

    protected Calendar lastDate = Calendar.getInstance();
    protected int frames = 0;

    public static Application getCurrent() {
        return current;
    }

    protected GL2 gl2;
    public GL2 getGL2() {
        return gl2;
    }

    protected Window window;

    public Application() {
        current = this;

        EngineInitializer initializer = new EngineInitializer();
        GLContext context = initializer.InitOpenGL();

        window = new Window();

        setTitle("Hello world!");
        setSize(640, 500);
        setPosition(100, 100);

        window.setContext(context);
    }

    float rot = 0;
    float x =0;

    protected void update(GLAutoDrawable glad) {
        this.gl2 = glad.getGL().getGL2();

        gl2.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );

        x += 0.2f;
        float y = 25;

        frames++;
        Calendar currentCal = Calendar.getInstance();
        if (currentCal.get(Calendar.SECOND) > lastDate.get(Calendar.SECOND)) {
            Time.fps = frames;
            frames = 0;
            lastDate = currentCal;
        }

        long current = System.nanoTime();
        Time.deltaTime = (current - lastTime) * 0.000000001f;
        lastTime = current;

        /*Texture tex = Resources.getSprite("LeftHand");

        tex.enable(gl2);
        tex.bind(gl2);

        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,GL2. GL_CLAMP_TO_EDGE);

        gl2.glPushMatrix();
        gl2.glMatrixMode(GL2.GL_TEXTURE);
        gl2.glLoadIdentity();

        gl2.glTranslatef(0.5f,0.5f,0.0f);
        //gl2.glScalef(1f, 1 / 0.5f, 1f);
        gl2.glRotatef(rot,0.0f,0.0f,1.0f);
        gl2.glTranslatef(-0.5f,-0.5f,0.0f);



        gl2.glBegin(GL2.GL_QUADS);

        TextureCoords texcoords = tex.getImageTexCoords();

        gl2.glVertex2f(x, y);
        gl2.glTexCoord2f(texcoords.left(), texcoords.bottom());

        gl2.glVertex2f(x, y + tex.getImageHeight() * (0.5f));
        gl2.glTexCoord2f(texcoords.left(), texcoords.top());

        gl2.glVertex2f(x + tex.getImageWidth() * (0.5f), y + tex.getImageHeight() * (0.5f));
        gl2.glTexCoord2f(texcoords.right(), texcoords.top());

        gl2.glVertex2f(x + tex.getImageWidth() * (0.5f), y);
        gl2.glTexCoord2f(texcoords.right(), texcoords.bottom());

        gl2.glEnd();

        gl2.glPopMatrix();
        gl2.glFlush();

        tex.disable(gl2);
        rot += 16f * Time.deltaTime;


        //for (int i =0; i < 10000; i++) {System.out.println(i + " " + Time.getFps());}

        System.out.println(Time.getFps());
        glad.swapBuffers();*/
    }

    public void loadResources(String path) {
        Resources.load(path);
    }

    public void onGLInitialized() { }

    public void setSize(int width, int height) {
        window.setSize(width, height);
    }
    public void setPosition(int x, int y) {
        window.setLocation(x, y);
    }
    public void setTitle(String title) {
        window.setTitle(title);
    }
    public void show() {
        window.setVisible(true);
    }
    public void hide() {
        window.setVisible(false);
    }
    public void goFullscreen() {
        hide();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(true);
        show();
    }
    public void goWindowed() {
        hide();
        window.setExtendedState(JFrame.NORMAL);
        window.setUndecorated(false);
        show();
    }
}
