package engine.core;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import engine.base.Camera;
import engine.base.GameObject;
import engine.base.components.SpriteRenderer;

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
        Camera.setActiveCamera(new Camera());

        setTitle("Hello world!");
        setSize(640, 500);
        setPosition(100, 100);

        r = gm.addComponent(SpriteRenderer.class);
        window.setContext(context);
    }

    GameObject gm = new GameObject();
    SpriteRenderer r;

    protected void update(GLAutoDrawable glad) {
        this.gl2 = glad.getGL().getGL2();

        gl2.glEnable(GL2.GL_BLEND);
        gl2.glEnable(GL2.GL_TEXTURE_2D);
        gl2.glEnable(GL2.GL_DEPTH_TEST);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl2.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );

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

        r.sprite = Resources.getSprite("LeftHand");
        //gm.getTransform().getPosition().x++;
        gm.getTransform().getPosition().x = 350;
        gm.getTransform().getPosition().y = 350;
        Camera.getActiveCamera().getTransform().getPosition().x += 1f;
        //gm.getTransform().getRotation().x++;
        //gm.getTransform().scale.x += 0.01f;
        //gm.getTransform().scale.y += 0.01f;
        gm.update();

        glad.swapBuffers();
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
        //hide();
       // window.setUndecorated(true);
        hide();
        window.dispose();
        window.setUndecorated(true);
        show();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public void goWindowed() {
        hide();
        window.dispose();
        window.setUndecorated(false);
        show();
        window.setExtendedState(JFrame.NORMAL);
    }
}
