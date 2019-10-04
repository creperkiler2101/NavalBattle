package engine.core;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

class GLContext extends GLCanvas {
    protected FPSAnimator animator;

    public GLContext(GLCapabilities capabilities) {
        super(capabilities);
        this.setAutoSwapBufferMode(false);
        this.addGLEventListener(new GLContextListener());


        animator = new FPSAnimator(60);
        animator.add(this);
        animator.start();
    }
}
