package engine.ui;

import engine.base.Component;
import engine.base.components.SpriteRenderer;
import engine.core.Input;

import java.awt.event.MouseEvent;

public class UI extends Component {
    private SpriteRenderer sr;
    public SpriteRenderer getSpriteRenderer() {
        return sr;
    }

    private boolean hover = false;
    public boolean isHovered() {
        return hover;
    }

    private boolean selected = false;

    @Override
    protected void start() {
        sr = getGameObject().addComponent(SpriteRenderer.class);
    }

    @Override
    protected void update() {
        if (!hover && Input.isButtonDown(MouseEvent.BUTTON1))
            selected = false;
    }

    @Override
    protected void mouseEnter() {
        hover = true;
    }

    @Override
    protected void mouseMove() {

    }

    @Override
    protected void mouseExit() {
        hover = false;
    }

    @Override
    protected void mouseDown(int button) {
        if (button == MouseEvent.BUTTON1)
            selected = true;
    }
    @Override
    protected void mousePress(int button) { }
    @Override
    protected void mouseUp(int button) { }
}
