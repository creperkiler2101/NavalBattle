package engine.core;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

class GLContextListener implements GLEventListener {
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        System.out.println("GL Context initializing");

        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        Application.current.gl2 = glAutoDrawable.getGL().getGL2();

        gl2.glEnable(GL2.GL_BLEND);
        gl2.glClearColor(69f / 255f,138f / 255f, 247f / 255f, 255f / 255f);

        System.out.println("OK");
        Application.getCurrent().onGLInitialized();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.out.print("GL Context disposed");
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        Application.getCurrent().update(glAutoDrawable);
        //display(glAutoDrawable);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();

        gl2.glMatrixMode( GL2.GL_PROJECTION );
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, width, 0.0f, height );

        gl2.glMatrixMode( GL2.GL_MODELVIEW );
        gl2.glLoadIdentity();

        gl2.glViewport( 0, 0, width, height );
    }
}
