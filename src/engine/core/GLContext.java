package engine.core;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

class GLContext extends GLCanvas {
    protected static GLContext current;

    public GLContext(GLCapabilities capabilities) {
        super(capabilities);
        current = this;

        this.setAutoSwapBufferMode(false);
        this.addGLEventListener(new GLContextListener());
    }
}
