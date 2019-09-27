package engine.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
    }
}
