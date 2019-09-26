package game;

import engine.base.Component;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import engine.core.Time;

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
        getGameObject().getTransform().getLocalPosition().x += 1 * Time.getDeltaTime();
    }

    public void goTop() {
        getGameObject().getTransform().getLocalPosition().y += 100;
        delayedExecute(this::goTop, 5);
    }
}
