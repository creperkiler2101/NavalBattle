package engine.ui;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Entry extends UIBase {
    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {
        super.update();

        //System.out.println(isSelected());
    }

    @Override
    public void keyPress(KeyEvent event) {
        System.out.println(this.isSelected());
        if (!this.isSelected())
            return;

        if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE && !getText().equals(""))
            setText(getText().substring(0, getText().length() - 1));

        if (isAllowedChar(event.getKeyChar())) {
            String sym = "" + event.getKeyChar();
            boolean isUpperCase = false;
            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK))
                isUpperCase = true;
            if (event.isShiftDown())
                isUpperCase = !isUpperCase;

            if (isUpperCase)
                sym = sym.toUpperCase();

            setText(getText() + sym);
        }
    }

    @Override
    public void keyUp(KeyEvent event) {
        if (!this.isSelected())
            return;
    }

    private boolean isAllowedChar(char sym) {
        if (font == null)
            return false;
        return font.characters.get(sym) != null;
    }
}
