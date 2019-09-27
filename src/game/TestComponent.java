package game;

import engine.base.Component;
import engine.core.Input;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import engine.core.Time;

import java.awt.event.KeyEvent;

public class TestComponent extends Component {
    SpriteRenderer sr;

    @Override
    public void start() {
        sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("LeftHand");
        delayedExecute(this::goTop, 5 + Time.startupDelay);
    }

    @Override
    public void update() {
        if (Input.isKeyDown(KeyEvent.VK_E))
            System.out.println("down");
        if (Input.isKeyPress(KeyEvent.VK_E))
            System.out.println("press");
        if (Input.isKeyUp(KeyEvent.VK_E))
            System.out.println("up");
        getGameObject().getTransform().getLocalPosition().x += 1 * Time.getDeltaTime();
    }

    public void goTop() {
        getGameObject().getTransform().getLocalPosition().y += 100;
        delayedExecute(this::goTop, 5);
    }
}
