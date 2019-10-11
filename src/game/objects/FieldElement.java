package game.objects;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import game.objects.controllers.GameController;

import java.awt.*;

public class FieldElement extends Component {
    public int state;
    public int x;
    public int y;
    public boolean isCurrent = false;

    public SpriteRenderer overlaySprite;
    public SpriteRenderer overlayHitSprite;
    public GameController game;

    public Texture square = Resources.getSprite("square");

    @Override
    public void start() {
        overlaySprite = getGameObject().addComponent(SpriteRenderer.class);
        overlayHitSprite = getGameObject().addComponent(SpriteRenderer.class);

        overlaySprite.sprite = square;
        overlayHitSprite.sprite = square;
        overlaySprite.color = new Color(0, 0, 0, 0);
        overlayHitSprite.color = new Color(0, 0, 0, 0);
    }

    @Override
    public void update() {
        if (state == 1) {
            overlayHitSprite.color = new Color(255, 0, 0, 150);
        }
    }

    @Override
    protected void mouseEnter() {
        overlaySprite.color = new Color(255, 255, 255, 150);
    }

    @Override
    protected void mouseExit() {
        overlaySprite.color = new Color(255, 255, 255, 0);
    }

    @Override
    protected void mouseUp(int button) {
        if (state == 0 && button == 1 && !isCurrent && game.turn) {
            state = 1;
            //Send server about shot
        }
    }
}
