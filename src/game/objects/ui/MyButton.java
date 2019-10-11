package game.objects.ui;

import engine.core.Input;
import engine.core.Resources;
import engine.ui.Button;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MyButton extends Button {
    public boolean isActive = true;

    @Override
    public void start() { }

    @Override
    public void update() {
        super.update();

        if (isPressed())
            sprite = Resources.getSprite("buttonPress");
        else if (isHovered())
            sprite = Resources.getSprite("buttonHover");
        else
            sprite = Resources.getSprite("buttonNormal");
    }

    @Override
    public void mouseEnter() {
        super.mouseEnter();
    }

    @Override
    public void mouseMove() {

    }

    @Override
    public void mouseExit() {
        super.mouseExit();
    }

    @Override
    public void mouseDown(int button) {
        super.mouseDown(button);
    }

    @Override
    public void mousePress(int button) { }

    @Override
    public void mouseUp(int button) {
        super.mouseUp(button);
    }

    @Override
    public void keyPress(KeyEvent event) { }

    @Override
    public void keyUp(KeyEvent event) { }
}
