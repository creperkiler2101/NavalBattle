package engine.core;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

class GLContextListener implements GLEventListener {
    private FPSAnimator animator;
    private boolean initalized = false;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        System.out.println("GL Context initializing");

        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        Application.current.gl2 = glAutoDrawable.getGL().getGL2();


        gl2.glDisable(GL2.GL_LIGHTING);
        gl2.glEnable(GL2.GL_TEXTURE_2D);
        gl2.glEnable(GL2.GL_POINT_SMOOTH);
        gl2.glEnable(GL2.GL_COLOR_MATERIAL);

        gl2.glDepthMask(false);
        gl2.glEnable(GL2.GL_BLEND);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        //gl2.glEnable(GL2.GL_DEPTH_TEST);
        //gl2.glDepthFunc(GL2.GL_LEQUAL);
        //gl2.glEnable(GL2.GL_DEPTH_TEST);

        gl2.glClearColor(69f / 255f,138f / 255f, 247f / 255f, 255f / 255f);

        System.out.println("OK");

        animator = new FPSAnimator(60);
        animator.add(GLContext.current);
        animator.start();

        Application.getCurrent().resourceLoad();
        if (!initalized)
            Application.getCurrent().onGLInitialized();
        initalized = true;
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        animator.remove(GLContext.current);
        System.out.print("GL Context disposed");
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        try {
            Application.getCurrent().update(glAutoDrawable);
        }
        catch (Exception ex) { }
        //display(glAutoDrawable);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();

        gl2.glMatrixMode( GL2.GL_PROJECTION );
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, 1920f, 0.0f, 1080f);

        gl2.glMatrixMode( GL2.GL_MODELVIEW );
        gl2.glLoadIdentity();

        gl2.glViewport( 0, 0, width, height );
    }
}
