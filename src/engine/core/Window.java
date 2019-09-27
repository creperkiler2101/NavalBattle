package engine.core;

import engine.base.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

final class Window extends JFrame {
    protected GLContext context;

    protected Window() { }

    protected void setContext(GLContext context) {
        this.context = context;
        this.getContentPane().add(context, BorderLayout.CENTER);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Input.onKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Input.onKeyUp(e);
            }
        });
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Input.onButtonPress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Input.onButtonUp(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Input.mousePosition = new Vector3(e.getX(), e.getY(), 0);;
                Application.getCurrent().getCurrentScene().mouseMove();
            }
        };
        context.addMouseListener(ma);
        context.addMouseMotionListener(ma);
        context.addMouseWheelListener(ma);
    }
}
