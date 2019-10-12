package game.objects;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import game.connection.Client;
import game.objects.controllers.GameController;

import java.awt.*;
import java.util.Currency;

public class FieldElement extends Component {
    public int state;
    public int x;
    public int y;
    public boolean isCurrent = false;

    public SpriteRenderer overlaySprite;
    public SpriteRenderer overlayHitSprite;
    public GameController game;

    public Texture square = Resources.getSprite("white");

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
            overlayHitSprite.color = new Color(255, 255, 255, 100);
        }
        else if (state == 2) {
            overlayHitSprite.color = new Color(255, 0, 0, 100);
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
            game.turn = false;
            Client.current.sendMessage("shot;" + Client.current.loggedAs + ";" + x + ";" + y);
        }
    }
}
