package engine.ui;

import engine.base.Vector3;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Entry extends UIBase {
    public String placeholder = "";

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
    protected void renderText(Vector3 pos) {
        if (font == null)
            return;
        if (fontColor == null)
            fontColor = new Color(0, 0, 0);

        Vector3 fullPosition = new Vector3(getTextOffset().x + pos.x, getTextOffset().y + pos.y);

        String toDraw = getText();
        if (isPassword)
            toDraw = "*".repeat(getText().length());

        if (toDraw.length() > 0)
            font.drawString(toDraw, fullPosition, fontScale, fontColor, fontSpacing);
        else
            font.drawString(placeholder, fullPosition, fontScale, fontColor, fontSpacing);
    }

    @Override
    public void keyPress(KeyEvent event) {
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
